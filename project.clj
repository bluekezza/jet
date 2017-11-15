(defproject jet "0.1.0"
  :description "Jet Html page builder"
  :min-lein-version "2.0.0"
  :license {:name "MIT License"
            :url "https://opensource.org/licenses/MIT"
            :year 2017
            :key "mit"}
  :dependencies
  [[org.clojure/clojure "1.8.0"]
   [com.yahoo.platform.yui/yuicompressor "2.4.8"]
   [clj-stable-pprint "0.0.3"]
   [hiccup "1.0.5"]
   [prismatic/schema "1.1.6"]]
  :profiles {:dev {:dependencies [[tempfile "0.2.0"]]}}
  :deploy-repositories [["clojars" {:sign-releases false}]])
