(ns lambdax-web.rss-blog
  (require [feedparser-clj.core :refer :all]))

(defn rss-feeds
  "Getting all rss feeds from blog"
  [rss-url]
  (parse-feed rss-url))

(def keys-to-select [:authors :link :title :updated-date :contents])

(defn last-statuses [number-of-statuses rss-url]
  (->> rss-url
       rss-feeds
       :entries
       (take number-of-statuses)
       (map
        #(let [{:keys [authors title link contents updated-date]}
               (select-keys % keys-to-select)]
           {:author (:name (first authors))
            :title title
            :text (:value (first contents))
            :date updated-date
            :type "BLOG POST"
            :link link}))))
