# Closoup

Clojurized access for [Jsoup](http://jsoup.org/).

## Usage

Getting some links of a web page:

```clojure
(use 'jsoup.soup)

($ (get! "http://google.com" :user-agent "CoCo/1.0") ;; get request with options
   td "a[href]" ;; Jsoup selectors
   (attr "abs:href")) ;; attribute selector
```
A post with basic authentication:

```clojure
($ (post! "http://127.0.0.1"  
        :user-agent "CoCo/1.0" 
        :follow-redirects true
        :auth (basic-auth "night" "password")
        :cookies {:user "night" :other "value"}
        :data {:param "one" :another "2"}) ;; post options & data
 td a) ;; Jsoup selectors
```

Parsing a local file:

```clojure
($ (slurp! "test-content.html" :encoding "UTF-8" :base-uri "http://base") "a[href]")
```

EOF
