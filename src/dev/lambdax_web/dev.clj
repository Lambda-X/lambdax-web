(ns lambdax-web.dev
  (:require [clojure.pprint :refer [pprint]]
            [clojure.repl :refer :all]
            [clojure.tools.namespace.repl :refer [refresh refresh-all]]
            [com.stuartsierra.component :as component]
            [lambdax-web.system :as app]))

(def system nil)

(defn init []
  (alter-var-root #'system (constantly (app/dev-system {:web-port 3000}))))

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
