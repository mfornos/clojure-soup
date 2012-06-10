(ns ^{:doc "Clojurized access for Jsoup."} 
  jsoup.soup
  (:use clojure.walk)
  (:import (org.jsoup Jsoup Connection Connection$Method)
           (org.jsoup.select Elements)
           (org.jsoup.nodes Element Document)
           org.apache.commons.codec.binary.Base64))

(def POST Connection$Method/POST) 
(def GET Connection$Method/GET) 

(defn -configure[^Connection conn opts]
  "Configures the connection."
  (let [opts       (apply hash-map opts)
        auth       (get opts :auth)
        user-agent (get opts :user-agent)
        cookies    (get opts :cookies)
        data       (get opts :data)
        method     (get opts :method)
        referrer   (get opts :referrer)
        timeout    (get opts :timeout)
        url        (get opts :url)
        headers    (get opts :headers)
        follow-redirects    (get opts :follow-redirects)
        ignore-content-type (get opts :ignore-content-type)
        ignore-http-errors  (get opts :ignore-http-errors)]
  (if-not (nil? user-agent) (.userAgent conn user-agent))
  (if-not (nil? cookies) (.cookies conn (stringify-keys cookies)))
  (if-not (nil? data) (.data conn (stringify-keys data)))
  (if-not (nil? method) (.method conn method))
  (if-not (nil? referrer) (.referrer conn referrer))
  (if-not (nil? timeout) (.timeout conn timeout))
  (if-not (nil? url) (.referrer conn url))
  (if-not (nil? auth) (doseq [[k v] auth] (.header conn k v)))
  (if-not (nil? follow-redirects) (.followRedirects conn follow-redirects))
  (if-not (nil? ignore-content-type) (.ignoreContentType conn ignore-content-type))
  (if-not (nil? ignore-http-errors) (.ignoreHttpErrors conn ignore-http-errors))
  (if-not (nil? headers) (doseq [[k v] headers] (.header conn k v))))
  conn)   

(defn -connect[^Connection$Method method url & opts]
  "Creates a connection configured by the given opts."
  (let [connection (.method (org.jsoup.Jsoup/connect url) method)]
  (if-not (nil? opts) (-configure connection opts))
  connection)) 

(defmulti parse
    "Parses a File, Inputstream or String and returns a Document instance."
 	  (fn [x & opts] (string? x)))

(defmethod parse true [content & opts]
  (let [opts     (apply hash-map opts)
        base-uri (get opts :base-uri)]
  (if (nil? base-uri) (org.jsoup.Jsoup/parse content) (org.jsoup.Jsoup/parse content base-uri))))

(defmethod parse false [file & opts]
  (let [opts      (apply hash-map opts)
        base-uri  (get opts :base-uri)
        encoding  (get opts :encoding "UTF-8")]
  (if (nil? base-uri) (org.jsoup.Jsoup/parse file encoding) (org.jsoup.Jsoup/parse file encoding base-uri))))

(defn slurp-parse [location & opts]
  (let [content (apply slurp location opts)]
  (if(nil? opts) (parse content) (apply parse content opts))))

(defn select [selector doc] (.select doc selector))

(defn basic-auth [username password]
  "Creates a basic authorization header."
  {"Authorization" (str "Basic " (String.  (Base64/encodeBase64 (.getBytes (str username ":" password)))))})

;; See apricot-soup @github ;)
(defmacro $ [doc & body]
  (let [exprs (map #(if (string? %1) `(select ~%1)
                      (if (symbol? %1)
                        `(select ~(str %1))
                         (if (keyword? %1)
                           `(select ~(str "#"(name %1)))
                            %1))) body)]
     `(->> ~doc ~@exprs)))

(defn exec [connection] (.. connection (execute) (parse)))

(defn get![uri & opts]
  (exec (apply -connect GET uri opts)))

(defn post![uri & opts]
  (exec (apply -connect POST uri opts)))

;; TODO add some macros for filters and maps
(defn attrs [elements selector]
  (map #(.attr % selector) elements))

