(ns lambdax-web.events
  (:require [twitter.oauth :refer [make-oauth-creds]]
            [twitter.api.restful :refer [statuses-user-timeline]]
            [clj-time.core :as t]
            [clj-time.coerce :as c]
            [clojure.string :as string]
            [feedparser-clj.core :refer :all])
  (:import [java.text SimpleDateFormat]))

;;;;;;;;;;;;;;;;;;;;;;;;
;;;;; Event record ;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;

(defrecord Event [author title text date type link img])

;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;; Twitter feeds ;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;

(def TwitterDateFormat (SimpleDateFormat. "EEE MMM d HH:mm:ss Z yyyy"))

(def api-key "xSQw3UZy93DromQMPTkkMNgwT")

(def api-secret "ABTdn4qhSUKm80x4d9Q0wT1wrm2HUlLcEOFH3Fr0dOgcsyiGFK")

(def acess-token "4849942733-Bhz6FEO4qjw7s16kroRiO924KoLqtbHY0PeSORF")

(def acess-token-secret "d9HNMN1sVqJPmo97Y9NAdYzQVGKD3p5zsjgkxvEZsyM5q")

(def my-creds (make-oauth-creds api-key
                                api-secret
                                acess-token
                                acess-token-secret))

(def tweet-keys-to-select [:text :created_at :screen-name :entities])

(defn get-user-screen [user]
  (statuses-user-timeline :oauth-creds my-creds :params {:screen-name user}))

(defn last-tweets [number-of-tweets user-name]
  (->> user-name
       get-user-screen
       :body
       (take number-of-tweets)
       (map
        #(let [{:keys [text created_at screen-name entities]}
               (select-keys % tweet-keys-to-select)]
           (->Event (str "@" user-name)
                    "TWITTER NEWS!"
                    (string/join " " (-> text (string/split #" ") butlast))
                    (.parse TwitterDateFormat created_at)
                    :tweet
                    (-> entities :media first :url)
                    {:src "img/news.png" :alt "news"})))))


;;;;;;;;;;;;;;;;;;;;;;
;;;;;; RSS feeds ;;;;;
;;;;;;;;;;;;;;;;;;;;;;

(defn rss-feeds
  "Getting all rss feeds from blog"
  [rss-url]
  (parse-feed rss-url))

(def rss-keys-to-select [:authors :link :title :published-date :contents :description])

(defn last-statuses [number-of-statuses rss-url]
  (->> rss-url
       rss-feeds
       :entries
       (take number-of-statuses)
       (map
        #(let [{:keys [authors title link contents published-date description]}
               (select-keys % rss-keys-to-select)]
           (->Event "The LambdaX Team"
                    ;;(:name (first authors))
                    title
                    (-> description
                        :value
                        (string/split #"<p>")
                        second
                        (string/replace #"</p>" "")) 
                    ;;(:value (first contents))
                    published-date
                    :blog-post
                    link
                    {:src "img/news.png"
                     :alt "news"})))))


;;;;;;;;;;;;;;;;;;;;;
;;;;; Endpoints ;;;;;
;;;;;;;;;;;;;;;;;;;;;

(def twitter-user "scalac_io")

(def blog-rss "http://lambdax-blog-devel.scalac.io/blog/feed.xml")

;;;;;;;;;;;;;;;;;;;;;;;
;;;;; Last events ;;;;;
;;;;;;;;;;;;;;;;;;;;;;;

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
  (let [last-blog-post (last-statuses 1 blog-rss)]
    (->> (if (older-than-month? last-blog-post)
           (last-tweets 3 twitter-user)
           (concat (last-tweets 2 twitter-user) last-blog-post))
         (sort-by :date))))