(ns jet)

(defprotocol Widget
  "provides a Widget abstraction"
  (set-title [self title] "Set the page title")
  (to-head [self html] "Adds html to the head") 
  (to-body [self html] "Add html to the body")
  (embed-style [self text] [self attrs text] "Embeds a style tag in the head")
  (link-style [self url] [self attrs url] "Adds a reference, via a <link> tag, to an external stylesheet")
  (embed-script [self text] [self attrs text] "Adds javascript text")
  (link-script [self url] [self attrs url] "Add a link with attributes to a remote javascript"))
