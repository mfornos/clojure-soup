(ns ^{:doc "Clojurized access for Jsoup."} 
  jsoup.soup
  (:use clojure.walk)
  (:import (org.jsoup Jsoup Connection Connection$Method)
           org.apache.commons.codec.binary.Base64))

(def POST Connection$Method/POST) 
(def GET Connection$Method/GET) 

(def ^:private params 
  ["user-agent" "cookies", "data", "method", "referrer", "timeout", 
   "url", "follow-redirects", "ignore-content-type", "ignore-http-errors",
   "max-body-size"])

(defn- to-fn-name [name] 
  (let [words (clojure.string/split name #"[\s_-]+")] 
    (apply str (first words) (map #(clojure.string/capitalize %) (rest words))))) 

(defn- invoke [instance method & args] 
  (clojure.lang.Reflector/invokeInstanceMethod instance method (to-array args))) 

(defn -configure [^Connection connection opts]
  "Configures the connection."
  (let [opts       (apply hash-map opts)
        auth       (get opts :auth)
        headers    (get opts :headers)]
  (doseq [v params]
    (let [var  (get opts (keyword v))
          vars (if (map? var) (stringify-keys var) var)]
    (if-not (nil? var) (invoke connection (to-fn-name v) vars))))
  (if-not (nil? auth) (doseq [[k v] auth] (.header connection k v)))
  (if-not (nil? headers) (doseq [[k v] headers] (.header connection k v)))) connection)

(defn -connect [^Connection$Method method url & opts]
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

(defn select [selector doc] (.select doc selector))

(defn basic-auth [username password]
  "Creates a basic authorization header."
  {"Authorization" (str "Basic " (String.  (Base64/encodeBase64 (.getBytes (str username ":" password)))))})

(defn exec [^Connection connection] (.. connection (execute) (parse)))

(defn get![uri & opts]
  (exec (apply -connect GET uri opts)))

(defn post![uri & opts]
  (exec (apply -connect POST uri opts)))

(defn slurp! [location & opts]
  (let [content (apply slurp location opts)]
  (if (nil? opts) (parse content) (apply parse content opts))))

(defmacro $ [doc & forms]
  (let [exprs# (map #(if (string? %) `(select ~%)
                      (if (symbol? %) `(select ~(str %))
                         (if (keyword? %) `(select ~(str "#"(name %)))
                            %))) forms)]
     `(->> ~doc ~@exprs#))) ;; See apricot-soup @github ;)

(defn text [elements] (map #(.text %) elements))

(defn attr [selector elements] (map #(.attr % selector) elements))

