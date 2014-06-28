(ns closeness.core)
(use 'clojure.java.io)
(use 'clojure.pprint)

; I could have used bellman-ford(BF) to get the shortest path of all vertices,
; but as BF is O(E*V), running to all vertices would give the complexity of
; O(E*V^2).

; So instead, I've used floyd-warshall which gives me the shortest path of all
; vertices with O(V^3) complexity.

; I did implement it 100% myself, but got a couple of ideas from other impls:
; - using Double/POSITIVE_INFINITY instead of a big value like I did in college.
; - a list comprehension to help me with the loop
; - learned to destruct(very similar to guard clauses, like I'm used to in
; erlang/elixir)
(defn floyd-warshall [{:keys [vertices edges]}]
  (let [possible-paths (for [k vertices i vertices j vertices] [k i j])]
    (loop [pp possible-paths eds edges prev {}]
      (let [path (first pp)]
        (if (= path nil)
          eds
          (let [[k i j] path]
            (let [computed-path (+ (eds [i k] Double/POSITIVE_INFINITY)
                                   (eds [k j] Double/POSITIVE_INFINITY))]
              (if (< computed-path (eds [i j] Double/POSITIVE_INFINITY))
                (recur (rest pp) (assoc eds [i j] computed-path) (assoc prev [i j] k))
                (recur (rest pp) eds prev)))))))))

; Discovers the vertices given a hashmap with the edges and it's weight
(defn discover-vertices [edges]
  (vec (sort (distinct (flatten
          (map first edges))))))

; Gets all paths from a vertice of a hashmap with paths
(defn get-all-paths-from-vertice [vertice paths]
  (select-keys paths (for [[k v] paths :when (= (last k) vertice)] k)))

; Sum the weights for some paths
(defn sum-all-weights-from-paths [paths]
  (reduce + (map last paths)))

; Creates a graph with the given edges and weights in the form of:
; {[node1 node2] w}
; where node1 and node2 are the vertices and w is the weight of the edge
(defn create-graph [edges]
  {:vertices (discover-vertices edges) :edges edges})

; Sort the vertices based on their sum of edges
(defn sort-vertices [vertices]
  (vec (sort-by (fn[v] (get v :sum-of-paths)) vertices)))

; Computes the sum of the shortest paths for each vertice ordered by it's
; path asc
(defn sum-of-shortest-paths [edges]
  (let [shortest-paths (floyd-warshall (create-graph edges))]
    (sort-vertices
      (map
        (fn [vertice]
          { :vertice vertice :sum-of-paths
           (sum-all-weights-from-paths
             (get-all-paths-from-vertice vertice shortest-paths))})
        (discover-vertices edges)))))

; Read file
(defn read-file [file]
  (with-open [rdr (clojure.java.io/reader file)]
    (doall (line-seq rdr))))

(defn parse-line[line]
  (vec (map read-string (clojure.string/split line #"\s+")))) 

; Creates a graph with the given edges and weights in the form of:
; {[node1 node2] w}
; where node1 and node2 are the vertices and w is the weight of the edge
; from a file read
(defn parse-file [file]
  (reduce
    (fn [edges line]
      (let [edge (parse-line line)]
        (assoc edges edge 1))) ; assigning weights
    {}
    (read-file file)))

(defn -main [& args]
  (pprint (sum-of-shortest-paths (parse-file(clojure.java.io/resource "edges")))))
