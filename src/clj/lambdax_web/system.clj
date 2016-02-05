(ns lambdax-web.system
  (:require [lambdax-web.server :as webserver]
            [lambdax-web.scheduler :as scheduler]
            [lambdax-web.events :as events]
            [com.stuartsierra.component :as component]))

(defn dev-system [{:keys [web-port] :as config}]
  (component/system-map
   :app-state (atom {:events []})
   :scheduler (component/using
               (scheduler/new-scheduler #(swap! % assoc :events (events/last-3-events))
                                        5000)
               [:app-state])
   :webserver (component/using
               (webserver/dev-server web-port)
               [:app-state])))

(defn prod-system [{:keys [web-port] :as config}]
  (component/system-map
   :app-state (atom {:events []})
   :scheduler (component/using
               (scheduler/new-scheduler #(swap! % assoc :events (events/last-3-events)) 3600000)
               [:app-state])
   :webserver (component/using
               (webserver/prod-server web-port)
               [:app-state])))
