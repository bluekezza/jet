(ns jet.amp
  (:require [clojure.string :as str]
            [jet :refer :all]
            [jet.core]
            [jet.css :as css]
            [jet.page :refer :all]
            [hiccup.core :refer [html]]
            [hiccup.page]
            [schema.core :as s]))

(def extensions
  {"amp-ad" "https://cdn.ampproject.org/v0/amp-ad-0.1.js"
   "amp-analytics" "https://cdn.ampproject.org/v0/amp-analytics-0.1.js"
   "amp-carousel" "https://cdn.ampproject.org/v0/amp-carousel-0.1.js"
   "amp-lightbox" "https://cdn.ampproject.org/v0/amp-lightbox-0.1.js"
   "amp-iframe" "https://cdn.ampproject.org/v0/amp-iframe-0.1.js"
   "amp-sticky-ad" "https://cdn.ampproject.org/v0/amp-sticky-ad-1.0.js"})

(defn parse-for-extensions
  "parses an html snippet for any custom amp elements in order to inject the extension script"
  [widget html extensions]
  (let [used-tags (reduce (fn [acc tag]
                            (let [pattern (str "<" tag)]
                              (if (re-find (re-pattern pattern) html)
                                (conj acc tag)
                                acc)))
                          []
                          (keys extensions))
        widget (reduce (fn [acc tag]
                         (let [url (get extensions tag)]
                           (link-script acc {:async "", :custom-element tag} url)))
                       widget
                       used-tags)]
    widget))

(s/defrecord AmpPage [charset :- s/Str
                      title :- (s/maybe s/Str)
                      head :- [s/Str]
                      scripts :- [s/Str]
                      styles :- [s/Str]
                      body :- [s/Str]]
  Widget
  (to-head [self html]
    (update self :head #(conj % html)))
  (to-body [self html]
    (-> self
        (parse-for-extensions html extensions)
        (update :body #(conj % html))))
  (set-title [self title]
    (assoc self :title title))
  (embed-style [self text]
    (update self :styles #(conj % text)))
  (embed-style [self attrs text]
    (update self :head #(conj % (html [:style (jet.core/<-attrs attrs) text]))))
  (link-style [self attrs url]
    self)
  (embed-script [self attrs text]
    self)
  (link-script [self url]
    (link-script self {} url))
  (link-script [self attrs url]
    (update self :scripts #(conj % (html [:script (assoc (jet.core/<-attrs attrs)
                                                         :src url)]))))

  Page
  (render-html [self]
    (let [styles' (->> styles
                       str/join
                       css/compress)
          head' (concat [(html [:meta {:charset charset}])
                         (html [:title {} title])]
                        scripts
                        head
                        [(html [:style {:amp-custom ""} styles'])])]
      (str "<!doctype html>
<html ⚡ lang=\"en\">"
           (html [:head {} (str/join head')])
           (html [:body {} (str/join body)])
           "</html>"))))

(defn ^:private boilerplate
  [widget]
  (-> widget
      (link-script :async "https://cdn.ampproject.org/v0.js")
      (to-head (html [:meta {:name "viewport"
                            :content "width=device-width,minimum-scale=1"}]))
      (embed-style :amp-boilerplate "body{-webkit-animation:-amp-start 8s steps(1,end) 0s 1 normal both;-moz-animation:-amp-start 8s steps(1,end) 0s 1 normal both;-ms-animation:-amp-start 8s steps(1,end) 0s 1 normal both;animation:-amp-start 8s steps(1,end) 0s 1 normal both}@-webkit-keyframes -amp-start{from{visibility:hidden}to{visibility:visible}}@-moz-keyframes -amp-start{from{visibility:hidden}to{visibility:visible}}@-ms-keyframes -amp-start{from{visibility:hidden}to{visibility:visible}}@-o-keyframes -amp-start{from{visibility:hidden}to{visibility:visible}}@keyframes -amp-start{from{visibility:hidden}to{visibility:visible}}")
      (to-head (html [:noscript {}
                     [:style {:amp-boilerplate ""} "body{-webkit-animation:none;-moz-animation:none;-ms-animation:none;animation:none}"]]))))

(defn- default
  []
  (AmpPage. "utf-8" nil [] [] [] []))

(def page
  (-> (default)
      (boilerplate)))
