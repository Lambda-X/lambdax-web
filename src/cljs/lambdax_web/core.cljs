(ns lambdax-web.core
  (:require [om.next :as om]
            [goog.dom :as gdom]
            [lambdax-web.data :as data]
            [lambdax-web.parser :as p]
            [lambdax-web.views :as views]))

(enable-console-print!)

(def reconciler
  (om/reconciler
   {:state data/application-state
    :parser p/parser}))

(om/add-root! reconciler views/RootView (gdom/getElement "app"))
