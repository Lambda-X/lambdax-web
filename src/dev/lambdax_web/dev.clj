(ns lambdax-web.dev
  (:require [clojure.pprint :refer [pprint]]
            [clojure.repl :refer :all]
            [clojure.tools.namespace.repl :refer [refresh refresh-all]]
            [com.stuartsierra.component :as component]
            [lambdax-web.system :as sys]))

(def system nil)

(defn ppsys
  "Pretty prints the system var"
  []
  (if-not (nil? system)
    (pprint system)
    (println "System not initialized yet")))

(defn init []
  (->> (sys/make-config)
       (sys/new-system)
       (constantly)
       (alter-var-root #'system)))

(defn start []
  (alter-var-root #'system component/start))

(defn stop []
  (alter-var-root #'system #(some-> % component/stop)))

(defn go []
  (init)
  (start))

(defn reset []
  (stop)
  (refresh :after 'lambdax-web.dev/go))
