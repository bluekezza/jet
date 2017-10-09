(ns jet.page
  (:require [clojure.string :as str]
            [jet :refer :all]
            [hiccup.core :refer [html]]
            [hiccup.page]
            [schema.core :as s]))

(defprotocol Page
  (render-html [self] "html"))
