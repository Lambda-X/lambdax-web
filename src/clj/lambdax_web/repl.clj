(ns lambdax-web.repl
  (:require [com.stuartsierra.component :as component]
            [clojure.tools.nrepl.server :refer [start-server stop-server]]
            [cider.nrepl :refer (cider-nrepl-handler)]))

(defrecord CiderReplServer [port build handler server]
  component/Lifecycle
  (start [component]
         (let [server-map (start-server :port (or port 0)
                                        :handler cider-nrepl-handler)]
           (println "Repl server on port" (:port server-map))
           (assoc component :server server-map)))
  (stop [component]
        (when server
          (do (println "Shutting repl server")
              (stop-server server))
          component)))

(defn new-repl-server [port build]
  (map->CiderReplServer {:port port :build build}))
