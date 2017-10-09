(ns jet.html5-test
  (:require [clojure.java.shell :refer [sh]]
            [clojure.string :as str]
            [clojure.test :refer :all]
            [hiccup.core :refer [html]]
            [jet :refer :all]
            jet.page
            jet.html5
            [jet.test :refer :all]))

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
