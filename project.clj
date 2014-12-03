(defproject clj-soup/clojure-soup "0.1.2"
  :description "Clojurized access for Jsoup."
  :url "https://github.com/mfornos/clojure-soup"
  :license {:name "Eclipse Public License - v 1.0"
            :url "http://www.eclipse.org/legal/epl-v10.html"
            :distribution :repo
            :comments "same as Clojure"}
  :dependencies [
        [org.clojure/clojure "1.6.0"]
        [commons-codec "1.10"]
        [org.jsoup/jsoup "1.8.1"]
  ]
  :plugins [
        [lein-eclipse "1.0.0"]
  ]
  
  ;;
  ;:java-source-path "src/main/java"
  ;:source-path "src/main/clojure"
  ;:test-path "src/test/clojure"
  ;:main ...
  ;:aot [_]
  ;:manifest {"Class-Path" "lib/clojure-1.4.0.jar"}
)
