(ns lambdax-web.config
  (:require [prone.middleware :as prone]
            lambdax-web.handler))

(def defaults
  {:name "LamdbaX Website Backend (DEV)"
   :fetch-interval 4000
   :build :dev
   :nrepl-port 0 ;; auto select
   :port 3000
   :access-domain "*"
   :feeds {:lambdax-blog {:name "LambdaX Blog RSS"
                          :type :rss
                          :url "http://de139ge9we8jv.cloudfront.net/blog/feed.xml"}
           :lambdax-twitter {:name "LambdaX Twitter Timeline"
                             :type :twitter
                             :user "scalac_io"
                             :api-key "xSQw3UZy93DromQMPTkkMNgwT"
                             :api-secret "ABTdn4qhSUKm80x4d9Q0wT1wrm2HUlLcEOFH3Fr0dOgcsyiGFK"
                             :access-token "4849942733-Bhz6FEO4qjw7s16kroRiO924KoLqtbHY0PeSORF"
                             :access-token-secret "d9HNMN1sVqJPmo97Y9NAdYzQVGKD3p5zsjgkxvEZsyM5q"}}
   ;; this middlewares will preceed the main one
   :pre-middleware lambdax-web.handler/wrap-pass-through
   ;; this middlewares will follow the main one
   :post-middleware (fn [handler]
                      (-> handler
                          (prone/wrap-exceptions {:skip-prone? (fn [req]
                                                                 (contains? (:headers req) "postman-token"))})
                          lambdax-web.handler/dev-post-mw))})
