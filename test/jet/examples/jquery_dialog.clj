(ns jet.examples.jquery-dialog
  (:require [clojure.test :refer :all]
            [hiccup.core :refer [html]]
            [jet :refer :all]
            jet.page
            jet.html5
            [jet.test :refer :all]))

(def expected "
<!doctype html>
<html lang=\"en\">
<head>
<meta charset=\"utf-8\">
<title>jQuery UI Dialog - Default functionality</title>
<meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">
<link rel=\"stylesheet\" href=\"//code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css\">
<link rel=\"stylesheet\" href=\"/resources/demos/style.css\">
<script src=\"https://code.jquery.com/jquery-1.12.4.js\"></script>
<script src=\"https://code.jquery.com/ui/1.12.1/jquery-ui.js\"></script>
<script>
$( function() {
  $( \"#dialog\" ).dialog();
} );
</script>
</head>
<body>
<div id=\"dialog\" title=\"Basic dialog\">
<p>This is the default dialog which is useful for displaying information. The dialog window can be moved, resized and closed with the 'x' icon.</p>
</div>
</body>
</html>")

(defn dialog
  [widget]
  (-> widget
      (set-title "jQuery UI Dialog - Default functionality")
      (to-head (html [:meta {:name "viewport" :content "width=device-width, initial-scale=1"}]))
      (to-head (html [:link {:rel "stylesheet" :href "//code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css"}]))
      (to-head (html [:link {:rel "stylesheet" :href "/resources/demos/style.css"}]))
      (link-script "https://code.jquery.com/jquery-1.12.4.js")
      (link-script "https://code.jquery.com/ui/1.12.1/jquery-ui.js")
      (to-head (html [:script "$( function() {
  $( \"#dialog\" ).dialog();
} );"]))
      (to-body
        (html
          [:div {:id "dialog"
                 :title "Basic dialog"}
           [:p "This is the default dialog which is useful for displaying information. The dialog window can be moved, resized and closed with the 'x' icon."]]))))

(deftest main
  []
  (let [actual (-> (jet.html5/page "utf-8" "en")
                   dialog                       
                   jet.page/render-html)]
    (is (equals? expected actual))))
