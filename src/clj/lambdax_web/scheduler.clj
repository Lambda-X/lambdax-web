(ns lambdax-web.scheduler
  (:require [overtone.at-at :as scheduler]
            [com.stuartsierra.component :as component]
            [lambdax-web.events :as events]))

;; Scheduler

(def fetch-events!
  "A function that fetches events and assoc them to the app-atom,
  typically the global state, under the :events key. It is side
  effectful."
  (fn [app-atom]
    (swap! app-atom assoc :events (events/last-3-events))))

(defrecord Scheduler [scheduled-fn interval pool app]
  component/Lifecycle
  (start [component]
         (if pool
           component
           (let [pool (scheduler/mk-pool)]
             (scheduler/every interval (partial scheduled-fn (:state app)) pool)
             (assoc component :pool pool))))
  (stop [component]
        (when-let [pool (:pool component)]
          (scheduler/stop-and-reset-pool! pool :strategy :kill))
        (dissoc component :pool)))

(defn new-scheduler [scheduled-fn interval]
  (Scheduler. scheduled-fn interval nil nil))
