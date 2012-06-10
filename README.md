# Soup

Clojurized access for [Jsoup](http://jsoup.org/).

## Usage

Getting all the links of a web page:

```clojure
user=> (use 'jsoup.soup)
nil
user=> ($ (get! "http://google.com" :user-agent "CoCo/1.0") "a[href]" (attr "abs:href"))
("http://www.google..." ...)
```
A post with basic authentication:

```clojure
($ (post! "http://127.0.0.1" 
       :user-agent "CoCo/1.0" 
       :follow-redirects true
       :auth (basic-auth "night" "password")
       :cookies {:user "night" :other "value"}
       :data {:param "one" :another "2"}) td a)
```

Parsing a local file:

```clojure
($ (slurp! "test-content.html" :encoding "UTF-8" :base-uri "http://base") "a[href]")
```

EOF
