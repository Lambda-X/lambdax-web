(ns lambdax-web.core
  (:require [lambdax-web.twitter-feed :as tf]
            [lambdax-web.rss-blog :as rss]
            [com.stuartsierra.component :as component]
            [overtone.at-at :refer :all]
            [clj-time.core :as t]
            [clj-time.format :as f]
            [clj-time.coerce :as c]))

(def twitter-user "scalac_io")

(def blog-rss "http://blog.scalac.io/feeds/index.xml")

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

;; SCHEDULER

(def my-pool (mk-pool))

(defn run-scheduler! [time-in-ms]
  (every time-in-ms #(last-3-events) my-pool))

(defn stop-and-reset-scheduler! []
  (stop-and-reset-pool! my-pool))

(defn kill-scheduler! []
  (stop-and-reset-pool! my-pool :strategy :kill))
