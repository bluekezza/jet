(ns jet.core)

(defn <-attrs
  [attrs]
  (if (keyword? attrs)
    (assoc {} attrs "")
    attrs))
