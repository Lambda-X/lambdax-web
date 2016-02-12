(ns lambdax-web.server
  (:require [org.httpkit.server :as httpkit]
            [lambdax-web.handler :as handler]
            [com.stuartsierra.component :as component]))

;; Server

(defrecord WebServer [;; params
                      port build access-domain pre-middleware post-middleware
                      ;; state
                      app shut-down]
  component/Lifecycle
  (start [component]
    (if shut-down
      component
      (do (println "Starting webserver on port" port)
          (let [handler (handler/new-handler pre-middleware
                                             post-middleware
                                             access-domain
                                             (:state app))
                server (httpkit/run-server handler {:port port})]
            (assoc component :shut-down server)))))
  (stop [component]
    (when-let [shutdown-fn (:shut-down component)]
      (println "Shutting webserver")
      (shutdown-fn))
    (dissoc component :shut-down)))

(defn new-server [port build access-domain pre-mw post-mw]
  (WebServer. port build access-domain pre-mw post-mw nil nil))
