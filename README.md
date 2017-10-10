# Jet

A library to manage generating Html from modular widgets without the limitations of templating frameworks.

## Target audience

Developers who have become frustrated with building server-generated web sites using mustache templates or hiccup.

## Summary

* Jet is a library for server-generated Html pages
* It relies on a widget protocol to encapsulate all facets of a UI component including:
** html
** css
** javascript
** metadata
* Widgets can be written in isolation then re-used for different page specs such as:
** html4
** html5
** [AMP](https://www.ampproject.org/)
* Why do we need it?
** existing approaches such as templates, when used to manage the page, cause the elements of a widget to fragment across the code base, such that:
*** Html is added as a mustache template file invoked by a parent mustache template
*** Css styles are added as a separate css file linked by the mustache template
*** Javascript is either linked to or written in the head of the parent mustache template
*** Business logic manages the controller and view-model to populate the mustache template
* Fragmentation increases the complexity of managing medium to large-scale web sites.
* Jet reduces complexity by encapsulating all widget code behind a single abstraction, the [Jet.widget]() protocol.

## Inspiration

`Jet` is a clone of the Widget pattern as implemented in the super-awesome [Yesod Web Framework|https://www.yesodweb.com/book/widgets]

## Usage

### Initialization

The main entry-point for constructing a Jet page looks like this:

``` clojure
(ns my-app.core
  (:require [jet :refer :all]
            jet.page
            jet.html5))

(defn process
  []
  (-> (jet.html5/page "utf-8" "en") ;; initialize the jet engine for html5
      my-widget                     ;; invoke our custom widget(s)
      jet.page/render-html)))       ;; render as Html
```

the `jet.page` namespace is only required at this high level. From here on its widgets all the way down.

### Widgets

Our `my-widget` function is our first example of a widget.

A widget is merely a function that takes as its first parameter a Jet.Widget record.
Typically widgets will take additional parameters but here we're keeping it simple.

Our widget can now start generating Html, CSS, Javascript or metadata.

#### Delegation

We can compose widgets from other widgets here for example we describe a page by delegating to header, content and footer widgets.

``` clojure
(:require jet)

(defn my-widget
  "this widget delegates html generation to other widgets"
  [widget]
  (-> widget
      header
      content
      footer))
```

#### Generating Content

To generate content we invoke methods on the Widget protocol.

``` clojure
(:require [hiccup.core :refer [html]])

(defn header
  [widget]
  (-> widget
      ;; requests a certain title for the page
      (set-title "our widget tutorial")
      ;; requires a link element to the head
      (to-head (html [:link {:rel "canonical", :href "http://localhost/my/tutorial.html"}]))
      ;; requires this embedded css style
      (embed-style "h1 {
  font-size: 20px;
}")
      ;; requires this html element as its body
      (to-body (html [:h1 "A very interesting article"]))
      ;; requires this external javascript file
      (link-script {:type "text/javascript"} "https://code.jquery.com/jquery-3.2.1.slim.min.js")))
```

As you can see a widget can describe everything that it requires to be generated.

Here is the widget protocol in its entirety:

``` clojure

| method            | args                | description                                                   |
| ----------------- |:-------------------:| -------------------------------------------------------------:|
| to-head           | [html]              | Add a html element to the head                                |
| to-body           | [html]              | Add a html element to the body                                |
| set-title         | [text]              | Set the page title                                            |
| embed-style       | [text] [attrs text] | Add a style tag in the head                                   |
| link-style        | [url] [attrs url]   | Adds a reference, via a <link> tag, to an external stylesheet |
| embed-script      | [attrs text]        | Adds javascript text                                          |
| link-script       | [url]               | Add a link to a remote javascript file                        |

```

## Contributing

### Building

The development environment is configured using [nix|https://nixos.org/nix/].

Simply install `nix` for your operating system and from within a shell run:

``` sh
nix-shell --pure
```

`nix` will read the `shell.nix` file and load the required dependencies.
