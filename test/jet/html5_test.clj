(ns jet.html5-test
  (:require [clojure.java.shell :refer [sh]]
            [clojure.string :as str]
            [clojure.test :refer :all]
            [hiccup.core :refer [html]]
            [jet :refer :all]
            jet.page
            jet.html5
            [tempfile.core :refer [tempfile with-tempfile]]))

(defn <-sh
  [result]
  (if (> (:exit result) 0)
    (throw (ex-info (:err result) result))
    (:out result)))

(defn tidy
  [txt]
  (<-sh (sh "tidy" "-" "--sort-attributes" "alpha" :in txt)))

(defn diff
  [lhs rhs]
  (with-tempfile [lhs-file (tempfile lhs)
                  rhs-file (tempfile rhs)]
    (let [result (<-sh (sh "diff" (.getPath lhs-file) (.getPath rhs-file)))]
      (= "" result))))

(defn equals?
  [lhs rhs]
  (true? (diff (tidy lhs) (tidy rhs))))

(def empty-html-doc "<!doctype html>
<html lang=\"en\">
<head>
<meta charset=\"utf-8\">
<title></title>
</head>
<body>
</body>
</html>")

(deftest empty-test
  (let [actual (-> (jet.html5/page "utf-8" "en") 
                   jet.page/render-html)]
    (is (equals? empty-html-doc actual))))

(deftest set-title-test
  (let [title "~title!"
        actual (-> (jet.html5/page "utf-8" "en") 
                   (set-title title)
                   jet.page/render-html)
        expected (str/replace empty-html-doc (re-pattern (html [:title])) (html [:title title]))]
    (is (equals? expected actual))))
