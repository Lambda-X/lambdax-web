(ns lambdax-web.core
  (:require [om.next :as om]
            [goog.dom :as gdom]
            [lambdax-web.data :as data]
            [lambdax-web.views :as views]))

(defn read [{:keys [state]} k _]
  {:value (get @state k)})

(def parser (om/parser {:read read}))

(def reconciler
  (om/reconciler
   {:state data/application-state
    :parser parser}))

(om/add-root! reconciler views/RootView (gdom/getElement "app"))
