# Soup

Clojurized access for [Jsoup](http://jsoup.org/).

## Usage

Getting all the links of web page:

```clojure
user=> (use 'jsoup.soup)
nil
user=> (attr-seq (get! "http://google.com" "a[href]" :user-agent "CoCo/1.0") "abs:href")
("http://www.google.es/imghp?hl=es&tab=wi" ...)

```
A post with basic authentication:

```clojure
(post! "http://127.0.0.1"
       "a[href]" ; <- Jsoup select expression 
       :user-agent "CoCo/1.0" 
       :follow-redirects true
       :auth (basic-auth "night" "password")
       :cookies {:user "night" :other "value"}
       :data {:param "one" :another "2"})
```

Parsing a local file:

```clojure
(select 
  (slurp-parse "test-content.html" :encoding "UTF-8" :base-uri "http://base") "a[href]")
```

EOF
