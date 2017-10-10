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

(deftest to-head-test
  (let [element (html [:meta {:name "viewport" :content "width=device-width, initial-scale=1.0"}])
        actual (-> (jet.html5/page "utf-8" "en") 
                   (to-head element)
                   jet.page/render-html)
        expected (str/replace empty-html-doc #"</head>" (str element "</head>"))]
    (is (equals? expected actual))))

(deftest to-body-test
  (let [element (html [:a {:href "http://xkcd.com"} "xkcd"])
        actual (-> (jet.html5/page "utf-8" "en") 
                   (to-body element)
                   jet.page/render-html)
        expected (str/replace empty-html-doc #"<body>" (str "<body>" element))]
    (is (equals? expected actual))))

(deftest embed-style-test
  (let [css "body { border: 1px; }"
        actual (-> (jet.html5/page "utf-8" "en") 
                   (embed-style css)
                   jet.page/render-html)
        expected (str/replace empty-html-doc #"</head>" (str (html [:style css]) "</head>"))]
    (is (equals? expected actual))))

(deftest embed-style-attrs-test
  (let [css "body { border: 1px; }"
        attrs {:class "loggable"}
        actual (-> (jet.html5/page "utf-8" "en") 
                   (embed-style attrs css)
                   jet.page/render-html)
        expected (str/replace empty-html-doc #"</head>" (str (html [:style attrs css]) "</head>"))]
    (is (equals? expected actual))))

(deftest link-style-test
  (let [actual (-> (jet.html5/page "utf-8" "en") 
                   (link-style "theme.css")
                   jet.page/render-html)
        element (html [:link {:rel "stylesheet" :type "text/css" :href "theme.css"}])
        expected (str/replace empty-html-doc #"</head>" (str element "</head>"))]
    (is (equals? expected actual))))

(deftest link-style-attrs-test
  (let [actual (-> (jet.html5/page "utf-8" "en") 
                   (link-style {:media "print"} "theme.css")
                   jet.page/render-html)
        element (html [:link {:rel "stylesheet" :type "text/css" :media "print" :href "theme.css"}])
        expected (str/replace empty-html-doc #"</head>" (str element "</head>"))]
    (is (equals? expected actual))))

(def javascript-code "(function (){
window.alert(\"Hello, world!\");
})")

(deftest embed-script-test
  (let [actual (-> (jet.html5/page "utf-8" "en") 
                   (embed-script javascript-code )
                   jet.page/render-html)
        element (html [:script javascript-code])
        expected (str/replace empty-html-doc #"<body>" (str "<body>" element))]
    (is (equals? expected actual))))

(deftest embed-script-attrs-test
  (let [actual (-> (jet.html5/page "utf-8" "en") 
                   (embed-script {:type "text/javascript"} javascript-code)
                   jet.page/render-html)
        element (html [:script {:type "text/javascript"} javascript-code])
        expected (str/replace empty-html-doc #"<body>" (str "<body>" element))]
    (is (equals? expected actual))))
