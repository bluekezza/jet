(ns jet)

(defprotocol Widget
  "provides a Widget abstraction"
  (to-head [self html] "Adds html to the head") 
  (to-body [self html] "Add html to the body")
  (set-title [self title] "Set the page title")
  (embed-style [self text] [self attrs text] "Embeds a style tag in the head")
  (link-style [self attrs url] "Adds a reference, via a <link> tag, to an external stylesheet")
  (embed-script [self attrs text] "Adds javascript text")
  (link-script [self url] [self attrs url] "Add a link with attributes to a remote javascript"))
