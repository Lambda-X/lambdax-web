(ns lambdax-web.core
  (:require [com.stuartsierra.component :as component]
            [lambdax-web.system :as sys])
  (:gen-class))

(def system nil)

(defn init []
  (->> (sys/make-config)
       (sys/new-system)
       (constantly)
       (alter-var-root #'system)))

(defn start []
  (alter-var-root #'system component/start))

(defn stop []
  (alter-var-root #'system #(some-> % component/stop)))

(defn -main [& args]
  (.addShutdownHook (Runtime/getRuntime) (Thread. (comp shutdown-agents stop)))
  (init)
  (start))
