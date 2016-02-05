(ns lambdax-web.core
  (:require [om.next :as om]
            [om.next.protocols :as om-p]
            [cognitect.transit :as t]
            [goog.dom :as gdom]
            [lambdax-web.data :as data]
            [lambdax-web.parser :as p]
            [lambdax-web.views :as views])
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
                    (when-let [response-string (.getResponseText this)]
                      (cb (t/read (t/reader :json) response-string)))))
         "GET"))

(def reconciler
  (om/reconciler
   {:state data/application-state
    :parser p/parser}))

(om/add-root! reconciler views/RootView (gdom/getElement "app"))

(transit-get "/events" (partial merge-events reconciler))

(js/setInterval
 #(transit-get "/events" (partial merge-events reconciler))
 5000)
