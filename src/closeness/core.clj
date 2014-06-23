(ns closeness.core)
(use 'clojure.java.io)
(use 'clojure.pprint)

; I could have used bellman-ford(BF) to get the shortest path of all vertices,
; but as BF is O(E*V), running to all vertices would give the complexity of
; O(E*V^2).

; So instead, I've used floyd-warshall which gives me the shortest path of all
; vertices with O(V^3) complexity.

; I did implement it 100% myself, but got a couple of ideas from others:
; - using Double/POSITIVE_INFINITY instead of a big value like I did in college.
; - a list comprehension to help me with the loop
; - learned to destruct(very similar to guard clauses, like I'm used to in
; erlang/elixir)
(defn floyd-warshall [{:keys [nodes distances]}]
  (let [possible-paths (for [k nodes i nodes j nodes] [k i j])]
    (loop [pp possible-paths dist distances prev {}]
      (let [path (first pp)]
        (if (= path nil)
          dist
          (let [[k i j] path]
            (let [computed-path (+ (dist [i k] Double/POSITIVE_INFINITY)
                                   (dist [k j] Double/POSITIVE_INFINITY))]
              (if (< computed-path (dist [i j] Double/POSITIVE_INFINITY))
                (recur (rest pp) (assoc dist [i j] computed-path) (assoc prev [i j] k))
                (recur (rest pp) dist prev)))))))))

; Discovers the vertices given a hashmap with the distances and it's weight
(defn discover-vertices [distances]
  (vec (sort (distinct (flatten
          (map (fn [distance] (first distance) ) distances))))))

; Gets all paths from a vertice of a hashmap with paths
(defn get-all-paths-from-vertice [vertice paths]
  (select-keys paths (for [[k v] paths :when (= (last k) vertice)] k)))

; Sum the weights for some paths
(defn sum-all-weights-from-paths [paths]
  (reduce + (map (fn [path]
         (last path)) paths)))

; Creates a graph with the given distances and weights in the form of:
; {[node1 node2] w}
; where node1 and node2 are the vertices and w is the weight of the edge
(defn create-graph [distances]
  {:nodes (discover-vertices distances) :distances distances})

; Sort the vertices based on their sum of distances
(defn sort-vertices [vertices]
  (vec (sort-by (fn[v] (get v :sum-of-distances)) vertices)))

; Computes the sum of the shortest paths for each vertice ordered by it's
; distance asc
(defn sum-of-shortest-paths [distances]
  (let [shortest-paths (floyd-warshall (create-graph distances))]
    (sort-vertices
      (map
        (fn [vertice]
          { :vertice vertice :sum-of-distances
           (sum-all-weights-from-paths
             (get-all-paths-from-vertice vertice shortest-paths))})
        (discover-vertices distances)))))

; Read file
(defn read-file [file]
  (with-open [rdr (clojure.java.io/reader file)]
    (doall (line-seq rdr))))
(defn parse-line[line] (vec (map (fn[l] (read-string l)) (clojure.string/split line #"\s+")))) 
; Creates a graph with the given distances and weights in the form of:
; {[node1 node2] w}
; where node1 and node2 are the vertices and w is the weight of the edge
; from a file read
(defn parse-file [file]
  (reduce
    (fn [edges line]
      (let [edge (parse-line line)]
        (assoc edges edge 1)))
    {}
    (read-file file)))

(defn -main [& args]
  (pprint (sum-of-shortest-paths (parse-file(clojure.java.io/resource "edges")))))
