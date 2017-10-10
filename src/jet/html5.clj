(ns jet.html5
  (:require [clojure.string :as str]
            [hiccup.core :refer [html]]
            [jet :refer [Widget]]
            jet.core
            [jet.css :as css]
            [jet.page :refer [Page]]
            [schema.core :as s]))

(defn ^:private link-script*
  [self attrs url]
  (update self :head #(conj % (html [:script (assoc (jet.core/<-attrs attrs)
                                                    :src url)]))))

(defn ^:private link-style*
  [self attrs url]
  (let [attrs' (merge {:rel "stylesheet" :type "text/css"} attrs)]
    (update self :head #(conj % (html [:link (assoc (jet.core/<-attrs attrs')
                                                    :href url)])))))

(defn ^:private embed-script*
  [self attrs text]
  (update self :body #(conj % (html [:script (jet.core/<-attrs attrs) text]))))

(s/defrecord Html5Page [charset :- s/Str
                        language :- s/Str
                        title :- (s/maybe s/Str)
                        head :- [s/Str]
                        body :- [s/Str]]
  Widget
  (to-head [self html]
    (update self :head #(conj % html)))
  (to-body [self html]
    (-> self
        (update :body #(conj % html))))
  (set-title [self title]
    (assoc self :title title))
  (embed-style [self text]
    (update self :head #(conj % (html [:style text]))))
  (embed-style [self attrs text]
    (update self :head #(conj % (html [:style (jet.core/<-attrs attrs) text]))))
  (link-style [self url]
    (link-style* self {} url))
  (link-style [self attrs url]
    (link-style* self attrs url))
  (embed-script [self text]
    (embed-script* self {} text))
  (embed-script [self attrs text]
    (embed-script* self attrs text))
  (link-script [self url]
    (link-script* self {} url))
  (link-script [self attrs url]
    (link-script* self attrs url))

  Page
  (render-html [self]
    (let [head' (concat [(html [:meta {:charset charset}])
                         (html [:title {} title])]
                        head)]
      (str "<!doctype html>
<html lang=\"" language "\">"
           (html [:head {} (str/join head')])
           (html [:body {} (str/join body)])
           "</html>"))))

(defn page
  [encoding language]
  (Html5Page. encoding language nil [] []))
