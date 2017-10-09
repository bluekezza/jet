(ns jet.test
  (:require [clojure.java.shell :refer [sh]]
            [clojure.string :as str]
            [clojure.test :refer :all]
            [tempfile.core :refer [tempfile with-tempfile]]))

(defn <-sh
  "handle the sh results"
  [result]
  (if (> (:exit result) 0)
    (do
      (binding [*out* *err*]
        (println (:err result)))
      (throw (ex-info (:err result) result)))
    (:out result)))

(defn tidy
  "tidy the html string"
  [txt]
  (<-sh (sh "tidy" "-" "--sort-attributes" "alpha" :in txt)))

(defn diff*
  "perform a diff of two string values"
  [lhs rhs]
  (with-tempfile [lhs-file (tempfile lhs)
                  rhs-file (tempfile rhs)]
    (sh "diff" (.getPath lhs-file) (.getPath rhs-file))))

(defn diff
  "diff 2 string values"
  [lhs rhs]
  (let [result (diff* lhs rhs)]
    (= "" (<-sh result))))

(defn equals?
  "are 2 html string equals"
  [lhs rhs]
  (true? (diff (tidy lhs) (tidy rhs))))



