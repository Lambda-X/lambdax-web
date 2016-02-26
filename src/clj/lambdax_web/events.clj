(ns lambdax-web.events
  (:require [twitter.oauth :refer [make-oauth-creds]]
            [twitter.api.restful :refer [statuses-user-timeline]]
            [clj-time.core :as t]
            [clj-time.coerce :as c]
            [clojure.string :as string]
            [lambdax-web.config :as config]
            [clj-rome.reader :as rome])
  (:import [java.text SimpleDateFormat]
           [java.util Locale]))

;;;;;;;;;;;;;;;;;;;;;;;;
;;;;; Event record ;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;

(defrecord Event [author title text date type link img])

;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;; Twitter feeds ;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;

(def TwitterDateFormat (SimpleDateFormat. "EE MMM dd HH:mm:ss z yyyy" Locale/ENGLISH))

(def my-creds (let [twitter-feed (get-in config/defaults [:feeds :lambdax-twitter])]
                (make-oauth-creds (:api-key twitter-feed)
                                  (:api-secret twitter-feed)
                                  (:access-token twitter-feed)
                                  (:access-token-secret twitter-feed))))

(def tweet-keys-to-select [:text :created_at :screen-name])

(defn get-user-screen [user]
  (statuses-user-timeline :oauth-creds my-creds :params {:screen-name user}))

(defn last-tweets [number-of-tweets user-name]
  (->> user-name
       get-user-screen
       :body
       (take number-of-tweets)
       (map
        #(let [{:keys [text created_at screen-name]}
               (select-keys % tweet-keys-to-select)]
           (->Event (str "@" user-name)
                    "TWITTER NEWS!"
                    (string/join " " (-> text (string/split #" ") butlast))
                    (.parse TwitterDateFormat created_at)
                    :tweet
                    (-> text (string/split #" ") last)
                    {:src "img/news.png" :alt "news"})))))


;;;;;;;;;;;;;;;;;;;;;;
;;;;;; RSS feeds ;;;;;
;;;;;;;;;;;;;;;;;;;;;;

(defn rss-feeds
  "Getting all rss feeds from blog"
  [rss-url]
  (rome/build-feed rss-url))

(def rss-keys-to-select [:description :title :author :published-date :link])

(defn last-statuses [number-of-statuses rss-url]
  (println "Fetching and parsing" rss-url)
  (->> rss-url
       rss-feeds
       :entries
       (take number-of-statuses)
       (map
        #(let [{:keys [description title author published-date link]}
               (select-keys % rss-keys-to-select)]
           (->Event author
                    title
                    (:value description)
                    published-date
                    :blog-post
                    link
                    {:src "img/news.png"
                     :alt "news"})))))

;;;;;;;;;;;;;;;;;;;;;;;
;;;;; Last events ;;;;;
;;;;;;;;;;;;;;;;;;;;;;;

(defn older-than-3-months? [blog-post]
  "Is blog post older than month?"
  (-> blog-post
      :date
      c/from-date
      (t/interval (t/now))
      t/in-months
      (> 2)))

(defn last-3-events []
  (let [lambdax-blog-rss (get-in config/defaults [:feeds :lambdax-blog :url])
        lambdax-twitter-user (get-in config/defaults [:feeds :lambdax-twitter :user])
        last-blog-post (last-statuses 1 lambdax-blog-rss)]
    (->> (if (older-than-3-months? last-blog-post)
           (last-tweets 3 lambdax-twitter-user)
           (concat (last-tweets 2 lambdax-twitter-user) last-blog-post))
         (sort-by :date #(compare %2 %1)))))
