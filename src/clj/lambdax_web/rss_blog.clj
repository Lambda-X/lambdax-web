(ns lambdax-web.rss-blog
  (require [feedparser-clj.core :refer :all]
           [lambdax-web.event-record :refer :all]))

(defn rss-feeds
  "Getting all rss feeds from blog"
  [rss-url]
  (parse-feed rss-url))

(def keys-to-select [:authors :link :title :published-date :contents])

(defn last-statuses [number-of-statuses rss-url]
  (->> rss-url
       rss-feeds
       :entries
       (take number-of-statuses)
       (map
        #(let [{:keys [authors title link contents published-date]}
               (select-keys % keys-to-select)]
           (->Event (:name (first authors))
                    title
                    (:value (first contents))
                    published-date
                    "BLOG POST"
                    link
                    {:src "img/news.png"
                     :alt "news"})))))
