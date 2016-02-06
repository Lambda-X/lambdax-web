(ns lambdax-web.config)

(def defaults
  {:name "LamdbaX Website Backend"
   :fetch-interval 360000
   :build :prod
   :port 3001
   :feeds #{{:name :lambdax-blog
             :type :rss
             :url "http://lambdax-blog-devel.scalac.io/blog/feed.xml"}
            {:name :lamdbax-twitter
             :type :twitter
             :user "scalac_io"}}})
