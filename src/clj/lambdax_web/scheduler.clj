(ns lambdax-web.scheduler
  (:require [overtone.at-at :as scheduler]
            [com.stuartsierra.component :as component]))

;; Scheduler

(defrecord Scheduler [scheduled-fn schedule-interval pool app-state]
  component/Lifecycle
  (start [component]
    (if pool
      component
      (let [pool (scheduler/mk-pool)]
        (scheduler/every schedule-interval (partial scheduled-fn app-state) pool)
        (assoc component :pool pool))))
  (stop [component]
    (when-let [pool (:pool component)]
      (scheduler/stop-and-reset-pool! pool :strategy :kill))
    (dissoc component :pool)))

(defn new-scheduler [scheduled-fn schedule-interval]
  (Scheduler. scheduled-fn schedule-interval nil nil))
