(ns closeness.core-test
  (:require [clojure.test :refer :all]
            [closeness.core :refer :all]))

; graph example taken from:
; https://groups.google.com/forum/#!topic/clojure/LYHVoxsjmn8
(def G
  (create-graph
    {[0 1] 1, ;distances
     [0 3] 1,
     [1 2] 1,
     [2 0] 1,
     [3 0] 1,
     [3 4] 1,
     [3 6] 1,
     [4 5] 1,
     [5 3] 1,
     [6 3] 1,
     [6 7] 1,
     [7 8] 1,
     [8 6] 1}))

(deftest floyd-warshall-test
  (is (= (floyd-warshall G) {[2 1] 2, [3 2] 3, [4 3] 2, [5 4] 2, [6 5] 3, [7 6] 2, [8 7] 2, [1 0] 2, [2 2] 3, [3 3] 2, [4 4] 3, [5 5] 3, [6 6] 2, [7 7] 3, [8 8] 3, [0 0] 2, [1 1] 3, [2 3] 2, [3 4] 1, [4 5] 1, [5 6] 2, [6 7] 1, [7 8] 1, [0 1] 1, [1 2] 1, [2 4] 3, [3 5] 2, [4 6] 3, [5 7] 3, [6 8] 2, [0 2] 2, [1 3] 3, [2 5] 4, [3 6] 1, [4 7] 4, [5 8] 4, [0 3] 1, [1 4] 4, [2 6] 3, [3 7] 2, [4 8] 5, [0 4] 2, [1 5] 5, [2 7] 4, [3 8] 3, [0 5] 3, [1 6] 4, [2 8] 5, [0 6] 2, [1 7] 5, [0 7] 3, [1 8] 6, [0 8] 4, [8 0] 3, [7 0] 4, [8 1] 4, [6 0] 2, [7 1] 5, [8 2] 5, [5 0] 2, [6 1] 3, [7 2] 6, [8 3] 2, [4 0] 3, [5 1] 3, [6 2] 4, [7 3] 3, [8 4] 3, [3 0] 1, [4 1] 4, [5 2] 4, [6 3] 1, [7 4] 4, [8 5] 4, [3 1] 2, [4 2] 5, [5 3] 1, [6 4] 2, [7 5] 5, [8 6] 1, [2 0] 1})))

(deftest discover-vertices-test
  (is (= (discover-vertices {[0 1] 1, [0 3] 1, [1 2] 1, [2 0] 1, [3 0] 1, [3 4] 1, [3 6] 1, [4 5] 1, [5 3] 1, [6 3] 1, [6 7] 1, [7 8] 1, [8 6] 1}) [0 1 2 3 4 5 6 7 8])))

(deftest get-all-paths-from-vertice-test
  (is (= (get-all-paths-from-vertice 1 {[0 1] 1, [0 2] 1, [1 1] 1}) {[0 1] 1, [1 1] 1})))

(deftest sum-all-weights-from-paths-test
  (is (= (sum-all-weights-from-paths {[0 1] 1, [1 1] 1}) 2)))

(deftest sum-of-shortest-paths-test
  (is (= (sum-of-shortest-paths {[0 1] 1, [0 3] 1, [1 2] 1, [2 0] 1, [3 0] 1, [3 4] 1, [3 6] 1, [4 5] 1, [5 3] 1, [6 3] 1, [6 7] 1, [7 8] 1, [8 6] 1})
      [{:vertice 3, :sum-of-distances 17}
       {:vertice 0, :sum-of-distances 20}
       {:vertice 6, :sum-of-distances 20}
       {:vertice 4, :sum-of-distances 24}
       {:vertice 1, :sum-of-distances 27}
       {:vertice 7, :sum-of-distances 27}
       {:vertice 5, :sum-of-distances 30}
       {:vertice 2, :sum-of-distances 33}
       {:vertice 8, :sum-of-distances 33}])))

(deftest sort-vertices-test
  (is (= (sort-vertices
           [{:vertice 1, :sum-of-distances 4}
            {:vertice 2, :sum-of-distances 2}
            {:vertice 3, :sum-of-distances 7}
            {:vertice 4, :sum-of-distances 10}
            {:vertice 5, :sum-of-distances 1}
            {:vertice 6, :sum-of-distances 8}])
           [{:sum-of-distances 1, :vertice 5}
            {:sum-of-distances 2, :vertice 2}
            {:sum-of-distances 4, :vertice 1}
            {:sum-of-distances 7, :vertice 3}
            {:sum-of-distances 8, :vertice 6}
            {:sum-of-distances 10, :vertice 4}])))

(deftest parse-file-test
  (is (= (first (parse-file)) [[2 1] 1])))

(deftest example-from-file-test
  (is (= (sum-of-shortest-paths (parse-file (clojure.java.io/resource "edges")))
         [{:vertice 44, :sum-of-distances 200}
          {:vertice 88, :sum-of-distances 201}
          {:vertice 33, :sum-of-distances 202}
          {:vertice 51, :sum-of-distances 203}
          {:vertice 57, :sum-of-distances 205}
          {:vertice 1, :sum-of-distances 207}
          {:vertice 76, :sum-of-distances 207}
          {:vertice 5, :sum-of-distances 208}
          {:vertice 20, :sum-of-distances 209}
          {:vertice 28, :sum-of-distances 209}
          {:vertice 82, :sum-of-distances 209}
          {:vertice 98, :sum-of-distances 209}
          {:vertice 9, :sum-of-distances 210}
          {:vertice 92, :sum-of-distances 210}
          {:vertice 4, :sum-of-distances 211}
          {:vertice 69, :sum-of-distances 211}
          {:vertice 35, :sum-of-distances 212}
          {:vertice 62, :sum-of-distances 212}
          {:vertice 73, :sum-of-distances 212}
          {:vertice 99, :sum-of-distances 212}
          {:vertice 3, :sum-of-distances 213}
          {:vertice 8, :sum-of-distances 213}
          {:vertice 37, :sum-of-distances 213}
          {:vertice 74, :sum-of-distances 213}
          {:vertice 65, :sum-of-distances 214}
          {:vertice 95, :sum-of-distances 214}
          {:vertice 55, :sum-of-distances 215}
          {:vertice 97, :sum-of-distances 215}
          {:vertice 29, :sum-of-distances 216}
          {:vertice 89, :sum-of-distances 216}
          {:vertice 12, :sum-of-distances 217}
          {:vertice 23, :sum-of-distances 217}
          {:vertice 67, :sum-of-distances 217}
          {:vertice 78, :sum-of-distances 217}
          {:vertice 90, :sum-of-distances 217}
          {:vertice 31, :sum-of-distances 219}
          {:vertice 41, :sum-of-distances 219}
          {:vertice 58, :sum-of-distances 219}
          {:vertice 64, :sum-of-distances 219}
          {:vertice 75, :sum-of-distances 219}
          {:vertice 15, :sum-of-distances 220}
          {:vertice 22, :sum-of-distances 220}
          {:vertice 26, :sum-of-distances 220}
          {:vertice 36, :sum-of-distances 220}
          {:vertice 45, :sum-of-distances 220}
          {:vertice 48, :sum-of-distances 220}
          {:vertice 63, :sum-of-distances 220}
          {:vertice 38, :sum-of-distances 221}
          {:vertice 66, :sum-of-distances 222}
          {:vertice 13, :sum-of-distances 223}
          {:vertice 47, :sum-of-distances 223}
          {:vertice 53, :sum-of-distances 223}
          {:vertice 86, :sum-of-distances 223}
          {:vertice 0, :sum-of-distances 224}
          {:vertice 40, :sum-of-distances 224}
          {:vertice 54, :sum-of-distances 224}
          {:vertice 93, :sum-of-distances 224}
          {:vertice 34, :sum-of-distances 225}
          {:vertice 2, :sum-of-distances 226}
          {:vertice 17, :sum-of-distances 227}
          {:vertice 50, :sum-of-distances 227}
          {:vertice 10, :sum-of-distances 228}
          {:vertice 24, :sum-of-distances 228}
          {:vertice 39, :sum-of-distances 228}
          {:vertice 84, :sum-of-distances 228}
          {:vertice 18, :sum-of-distances 229}
          {:vertice 42, :sum-of-distances 229}
          {:vertice 52, :sum-of-distances 229}
          {:vertice 16, :sum-of-distances 230}
          {:vertice 27, :sum-of-distances 230}
          {:vertice 46, :sum-of-distances 230}
          {:vertice 59, :sum-of-distances 230}
          {:vertice 81, :sum-of-distances 230}
          {:vertice 14, :sum-of-distances 231}
          {:vertice 49, :sum-of-distances 233}
          {:vertice 7, :sum-of-distances 234}
          {:vertice 77, :sum-of-distances 234}
          {:vertice 79, :sum-of-distances 234}
          {:vertice 43, :sum-of-distances 235}
          {:vertice 70, :sum-of-distances 235}
          {:vertice 80, :sum-of-distances 238}
          {:vertice 83, :sum-of-distances 238}
          {:vertice 91, :sum-of-distances 239}
          {:vertice 60, :sum-of-distances 240}
          {:vertice 72, :sum-of-distances 240}
          {:vertice 85, :sum-of-distances 241}
          {:vertice 56, :sum-of-distances 242}
          {:vertice 68, :sum-of-distances 243}
          {:vertice 6, :sum-of-distances 244}
          {:vertice 11, :sum-of-distances 244}
          {:vertice 87, :sum-of-distances 244}
          {:vertice 32, :sum-of-distances 246}
          {:vertice 30, :sum-of-distances 248}
          {:vertice 96, :sum-of-distances 248}
          {:vertice 94, :sum-of-distances 252}
          {:vertice 61, :sum-of-distances 253}
          {:vertice 21, :sum-of-distances 257}
          {:vertice 71, :sum-of-distances 262}
          {:vertice 25, :sum-of-distances 263}
          {:vertice 19, :sum-of-distances 270}])))
