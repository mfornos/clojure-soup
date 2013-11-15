# Clojure Soup

Clojurized access for [Jsoup](http://jsoup.org/).

[![clojars version](https://clojars.org/clj-soup/clojure-soup/latest-version.svg)](https://clojars.org/clj-soup/clojure-soup)

## Leiningen

```
[clj-soup/clojure-soup "0.1.0"]
```

## Maven
```xml
<dependency>
  <groupId>clj-soup</groupId>
  <artifactId>clojure-soup</artifactId>
  <version>${clojure-soup.version}</version>
</dependency>
```


## Examples

Get some links of a web page:

```clojure
(use 'jsoup.soup)

($ (get! "http://google.com" :user-agent "CoCo/1.0") ;; get request with options
   td "a[href]" ;; Jsoup selectors
   (attr "abs:href")) ;; attribute selector
```

Get all Emoji names concatenated by single bars from 'emoji-cheat-sheet.com':

```clojure
($ (get! "http://www.emoji-cheat-sheet.com/") 
   "li div:has(span.emoji)" (text) 
   (map #(clojure.string/replace % ":" "")) 
   (clojure.string/join "|")) 
```

Post with basic authentication:

```clojure
($ (post! "http://127.0.0.1"  
        :user-agent "CoCo/1.0" 
        :follow-redirects true
        :auth (basic-auth "night" "password")
        :cookies {:user "night" :other "value"}
        :data {:param "one" :another "2"}) ;; post options & data
 td a) ;; Jsoup selectors
```

Parse a local file:

```clojure
($ (slurp! "test-content.html" :encoding "UTF-8" :base-uri "http://base") "a[href]")
```

EOF
