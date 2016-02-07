(ns lambdax-web.config
  (:require [prone.middleware :as prone]
            lambdax-web.handler))

(def defaults
  {:name "LamdbaX Website Backend (DEV)"
   :fetch-interval 10000
   :build :dev
   :nrepl-port 0 ;; auto select
   :port 3000
   :feeds #{{:name :lambdax-blog
             :type :rss
             :url "http://lambdax-blog-devel.scalac.io/blog/feed.xml"}
            {:name :lamdbax-twitter
             :type :twitter
             :user "scalac_io"}}
   ;; this middlewares will preceed the main one
   :pre-middleware lambdax-web.handler/wrap-pass-through
   ;; this middlewares will follow the main one
   :post-middleware (fn [handler]
                      (-> handler
                          (prone/wrap-exceptions {:skip-prone? (fn [req]
                                                                 (contains? (:headers req) "postman-token"))})
                          lambdax-web.handler/dev-post-mw))})
