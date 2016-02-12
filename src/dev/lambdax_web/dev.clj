(ns lambdax-web.dev
  (:require [clojure.pprint :as pprint :refer [pprint]]
            [clojure.tools.namespace.repl :refer [refresh]]
            [lambdax-web.core :as core]))

(defn ppsys
  "Pretty prints the system var"
  []
  (if-not (nil? core/system)
    (pprint core/system)
    (println "System not initialized yet")))

(defn go []
  (core/init)
  (core/start))

(defn reset []
  (core/stop)
  (refresh :after 'lambdax-web.dev/go))
