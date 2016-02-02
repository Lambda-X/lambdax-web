(ns lambdax-web.twitter-feed
  (:require [twitter.oauth :refer [make-oauth-creds]]
            [twitter.api.restful :refer [statuses-user-timeline]])
  (:import [java.text SimpleDateFormat]))

(def TwitterDateFormat (SimpleDateFormat. "EEE MMM d HH:mm:ss Z yyyy"))

(def api-key "xSQw3UZy93DromQMPTkkMNgwT")

(def api-secret "ABTdn4qhSUKm80x4d9Q0wT1wrm2HUlLcEOFH3Fr0dOgcsyiGFK")

(def acess-token "4849942733-Bhz6FEO4qjw7s16kroRiO924KoLqtbHY0PeSORF")

(def acess-token-secret "d9HNMN1sVqJPmo97Y9NAdYzQVGKD3p5zsjgkxvEZsyM5q")

(def my-creds (make-oauth-creds api-key
                                api-secret
                                acess-token
                                acess-token-secret))

(def keys-to-select [:text :created_at :screen-name :user])

(defn get-user-screen [user]
  (statuses-user-timeline :oauth-creds my-creds :params {:screen-name user}))

(defn last-tweets [number-of-tweets user-name]
  (->> user-name
       get-user-screen
       :body
       (take number-of-tweets)
       (map
        #(let [{:keys [text created_at screen-name user]}
               (select-keys % keys-to-select)]
           {:author (str "@" user-name)
            :title "TWITTER NEWS!"
            :text text
            :date (.parse TwitterDateFormat created_at)
            :type "TWEET"
            :link (:url user)}))))
