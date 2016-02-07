(ns lambdax-web.core
  (:require [com.stuartsierra.component :as component]
            [lambdax-web.system :as sys])
  (:gen-class))

(def system nil)

(defn init []
  (let [config (sys/make-config)]
    (println "[Initializing]" (:name config) (when (:version config)
                                               (str "- " (:version config))))
    (->> config
         (sys/new-system)
         (constantly)
         (alter-var-root #'system))))

(defn start []
  (do (println "Starting system...")
      (alter-var-root #'system component/start)))

(defn stop []
  (do (println "Stopping system...")
      (alter-var-root #'system #(some-> % component/stop))))

(defn -main [& args]
  (.addShutdownHook (Runtime/getRuntime) (Thread. #(do (stop) (shutdown-agents))))
  (init)
  (start))
