(ns jsoup.soup-test
  (:use clojure.test (jsoup soup))
  (:import java.util.LinkedHashMap))

(deftest connect-options-test
  (testing "Jsoup connection options"
  (let [conn (-connect POST "http://127.0.0.1" 
                       :user-agent "CoCo/1.0" 
                       :follow-redirects true
                       :auth (basic-auth "night" "password")
                       :cookies {:user "night" :other "value"})]
  (is (instance? LinkedHashMap (.. (-connect GET "http://127.0.0.1") (request) (headers))))
  (is (= "CoCo/1.0" (get (.. conn (request) (headers)) "User-Agent")))
  (is (= "night" (get (.. conn (request) (cookies)) "user")))
  (is (= POST (.. conn (request) (method)))))))

(deftest auth-header-test
 (testing "Basic auth header"
 (is (= {"Authorization" "Basic bmlnaHQ6dmVyeS1sMG5nUF9hc3N3M3JkLg=="} (basic-auth "night" "very-l0ngP_assw3rd.")))))

(deftest slurp-parse-test
 (testing "Parse file"
 (let [document (slurp-parse "test/resources/test-content.html" :encoding "UTF-8" :base-uri "http://base")]
 (is (not= nil? document))
 (is (= "http://base/../../../overview-summary.html" (.attr (second (select document "a[href]")) "abs:href")))
 (is (= "NavBarFont1" (first (attr-seq (select (select document "td") "font") "class")))))))

(deftest html-sample
  (testing "Html sample"
  (let [html "<p>An <a href='http://example.com/'><b>example</b></a> link.</p>"
        doc (parse html) 
        link (first (select doc "a"))]
   (is (= ["An example link." "http://example.com/" "example"]
          [(.. doc (body) (text)) (.attr link "href") (.text link)]))
   (is (= "<a href=\"http://example.com/\"><b>example</b></a>" (.outerHtml link))))))

(comment 
  ;; Tests to be mocked
  
(deftest post-test
  (is (= "" (post! "http://posttestserver.com/post.php"
                   "body"
                   :user-agent "CoCo/1.0"
                   :data {:verio "one" :tasty "2" :random "uioi"}))))

(deftest get-test
 (is (= 22 (count (attr-seq (get! "http://google.com" "a[href]" :user-agent "CoCo/1.0") "abs:href")))))
  
(deftest select-test
  (testing "Jsoup Connection Options"
  (let [conn (-connect GET "http://google.com")]
  (is (= "http://www.google.es/imghp?hl=es&tab=wi" (.attr (first (select conn "a[href]")) "abs:href")))
  (is (= "http://www.google.es/imghp?hl=es&tab=wi" (.attr (first (get! "http://google.com" "a[href]")) "abs:href"))))))

)
