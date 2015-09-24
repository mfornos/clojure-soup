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
 (let [document (slurp! "test/resources/test-content.html" :encoding "UTF-8" :base-uri "http://base")]
 (is (not= nil? document))
 (is (= {"http://base/../overview-summary.html" "Overview"}
        (second ($ document "a[href]" (map (fn [e] {(.attr  e "abs:href") (.text e)}))))))
 (is (= "NavBarFont1" (first ($ document "td" "font" (attr "class"))))))))

(deftest html-sample
  (testing "Html sample"
  (let [html "<p>An <a href='http://example.com/'><b>example</b></a> link.</p>"
        doc (parse html)
        link (first ($ doc a))]
   (is (= "<b>example</b>" (.outerHtml ($ link b))))
   (is (= ["An example link." "http://example.com/" "example"]
          [(.. doc (body) (text)) (.attr link "href") (.text link)]))
   (is (= "<a href=\"http://example.com/\"><b>example</b></a>" (.outerHtml link))))))

(deftest more-html-test
  (let [html "<p>An <a href='http://example.com/'><b>example</b></a> link.</p>"
        doc (parse html)
        link (first ($ doc a))]
  (is (= "http://example.com/" (.. (first ($ doc "p" "a")) (attr "href"))))
  (is (= (list "An example link.") ($ doc "p" (text))))))

(comment
  ;; Tests to be mocked

(deftest post-test
  (is (= "" (post! "http://posttestserver.com/post.php"
                   :user-agent "CoCo/1.0"
                   :data {:verio "one" :tasty "2" :random "uioi"}))))

(deftest get-test
 (is (= 22 (count ($ (get! "http://google.com" :user-agent "CoCo/1.0") "a[href]" (map (fn [e] (.attr  e "abs:href")))))))
 (is (= 2 (count ($ (get! "http://google.com") "td" "a[href]"
   (map (fn [e] {(.attr  e "href") (.text e)})))))))

)
