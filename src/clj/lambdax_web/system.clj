(ns lambdax-web.system
  (:require [lambdax-web.app :as app]
            [lambdax-web.server :as webserver]
            [lambdax-web.scheduler :as scheduler]
            [lambdax-web.events :as events]
            [lambdax-web.config :as config]
            [lambdax-web.repl :as repl]
            [com.stuartsierra.component :as component]
            [environ.core :refer [env]]
            [clojure.edn :as edn]
            [clojure.java.io :as io])
  (:import java.util.Properties))

(defn fetch-version!
  "Collects the version from version.properties, returns the config with
  added :version or passes it through if some error happened."
  [config]
  ;; AR - A bit messy, cause it's Java!
  (if-let [version (-> (doto (Properties.)
                         ;; side effect
                         (.load (try
                                  (some->> (if (= :prod (:build config))
                                             (io/resource "version.properties")
                                             (io/file "version.properties"))
                                           (io/input-stream))
                                  (catch java.io.FileNotFoundException ex
                                    (println "Handled exception -" (.getMessage ex))))))
                       (.getProperty "VERSION"))]
    (assoc config :version version)
    config))

(defn make-config
  "Creates a default configuration map"
  []
  (-> config/defaults
      (fetch-version!)
      (merge (into {} [(some->> (:lambdax-web-version env) (hash-map :version))
                       (some->> (:lambdax-web-port env) (Integer/parseInt) (hash-map :port))
                       (some->> (:lambdax-web-nrepl-port env) (Integer/parseInt) (hash-map :nrepl-port))
                       (some->> (:lambdax-web-fetch-every env) (Integer/parseInt) (hash-map :fetch-interval))]))))

(defn new-system
  [config-map]
  (component/system-map
   :app (app/new-app (:name config-map)
                     (:version config-map))
   :scheduler (component/using
                (scheduler/new-scheduler scheduler/fetch-events!
                                         (:fetch-interval config-map))
                [:app])
   :webserver (component/using
                (webserver/new-server (:port config-map)
                                      (:build config-map)
                                      (or (env :access-domain)
                                          (:access-domain config-map))
                                      (:pre-middleware config-map)
                                      (:post-middleware config-map))
                [:app])
   :repl (repl/new-repl-server (:nrepl-port config-map)
                               (:build config-map))))
