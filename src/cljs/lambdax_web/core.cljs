(ns lambdax-web.core
  (:require [clojure.string :as s]
            [om.next :as om]
            [om.next.protocols :as om-p]
            [cognitect.transit :as t]
            [goog.dom :as gdom]
            [lambdax-web.data :as data]
            [lambdax-web.parser :as p]
            [lambdax-web.views :as views]
            [lambdax-web.config :as config])
  (:import [goog.net XhrIo]))

(enable-console-print!)

(defn merge-events [reconciler new-events]
  (swap! (-> reconciler :config :state)
         assoc-in [:section/by-name :news :content] new-events)
  (om-p/queue! reconciler [:sections]))

(defn transit-get [url cb]
  (.send XhrIo url
         (fn [e]
           (this-as this
                    (let [response-string (.getResponseText this)]
                      (if (s/blank? response-string)
                        (.log js/console "Empty result")
                        (cb (t/read (t/reader :json) response-string))))))
         "GET"))

(defn map->form-str [data]
  (->> data
       (map (fn [[k v]]
              (str (name k) "=" v)))
       (s/join "&")))

(defn formspree-send [mail data]
  (.send XhrIo (str "//formspree.io/" mail)
         (fn [e])
         "POST" (map->form-str data)
         #js {"Accept" "application/json"}))

(defn send [{:keys [form]} cb]
  (let [params (-> form om/query->ast :children first :params)]
    (formspree-send (get-in config/defaults [:contact-form :email]) params)))

(def reconciler
  (om/reconciler
   {:state data/application-state
    :parser p/parser
    :remotes [:form]
    :send send}))

(om/add-root! reconciler views/RootView (gdom/getElement "app"))

;; AR - TODO let's improve this please
(transit-get (get-in config/defaults [:events :url]) (partial merge-events reconciler))

(js/setInterval #(transit-get (get-in config/defaults [:events :url]) (partial merge-events reconciler))
                (get-in config/defaults [:events :interval]))
