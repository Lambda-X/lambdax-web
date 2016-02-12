(ns lambdax-web.app
  (:require [com.stuartsierra.component :as component]))

(defrecord App [name version state]
  component/Lifecycle
  (start [component]
         (if state
           component
           (assoc component :state (atom {:events []}))))
  (stop [component]
        (dissoc component :state)))

(defn new-app [name version]
  (App. name version nil))
