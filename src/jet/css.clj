(ns jet.css
  (:import [com.yahoo.platform.yui.compressor CssCompressor]
           [java.io StringReader StringWriter]))

(defn compress
  [^String style]
  (let [compressor (CssCompressor. (StringReader. style))
        writer (StringWriter.)]
    (.compress compressor writer 0)
    (.toString writer)))
