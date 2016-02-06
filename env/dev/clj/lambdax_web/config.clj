(ns lambdax-web.config)

(def defaults
  {:name "LamdbaX Website Backend (DEV)"
   :fetch-interval 10000
   :build :dev
   :port 3000
   :feeds #{{:name :lambdax-blog
             :type :rss
             :url "http://lambdax-blog-devel.scalac.io/blog/feed.xml"}
            {:name :lamdbax-twitter
             :type :twitter
             :user "scalac_io"}}})
