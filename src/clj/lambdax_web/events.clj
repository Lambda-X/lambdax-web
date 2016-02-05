(ns lambdax-web.events
  (:require [lambdax-web.twitter-feed :as tf]
            [lambdax-web.rss-blog :as rss]
            [clj-time.core :as t]
            [clj-time.coerce :as c]))

(def twitter-user "scalac_io")

(def blog-rss "http://lambdax-blog-devel.scalac.io/blog/feed.xml")

;; Last events

(defn older-than-month? [blog-post]
  "Is blog post older than month?"
  (-> blog-post
      first
      :date
      c/from-date
      (t/interval (t/now))
      t/in-months
      (> 0)))

(defn last-3-events []
  (let [last-blog-post (rss/last-statuses 1 blog-rss)]
    (->> (if (older-than-month? last-blog-post)
           (tf/last-tweets 3 twitter-user)
           (concat (tf/last-tweets 2 twitter-user) last-blog-post))
         (sort-by :created_at))))
