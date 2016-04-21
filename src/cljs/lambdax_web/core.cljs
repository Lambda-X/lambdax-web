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
             (let [response (.getResponse this)]
               (cb (if (s/blank? response)
                     []
                     (t/read (t/reader :json) response))))))
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
(let [config config/defaults
      url (get-in config [:events :url])
      interval (get-in config [:events :interval])
      cb (partial merge-events reconciler)]
  (transit-get url cb)
  (js/setInterval #(transit-get url cb) interval))
