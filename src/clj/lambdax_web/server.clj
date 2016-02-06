(ns lambdax-web.server
  (:require [org.httpkit.server :as httpkit]
            [bidi.bidi :as bidi]
            [ring.util.response :refer [resource-response]]
            [ring.middleware.resource :refer [wrap-resource]]
            [ring.middleware.content-type :refer [wrap-content-type]]
            [ring.middleware.not-modified :refer [wrap-not-modified]]
            [lambdax-web.util :as util]
            [com.stuartsierra.component :as component]
            [prone.middleware :as prone]))

(defn index [_]
  (assoc (resource-response "index.html" {:root "public"})
         :headers {"Content-Type" "text/html"}))

(defn wrap-transit-response [handler]
  (fn [req]
    {:status 200
     :headers {"Content-Type" "application/transit+json"}
     :body (-> req handler util/write-transit)}))

(defn events [{:keys [app-state]}]
  (let [events (:events @app-state)]
    (or (seq events) "No events available")))

(def routes
  [""  {"/" :index
        "/events"
        {:get {[""] :events}}}])

(def match->handler
  {:index index
   :events (wrap-transit-response events)})

(defn route-handler [{:keys [uri request-method] :as req}]
  (let [match (bidi/match-route routes uri :request-method request-method)]
    (if-let [handler (match->handler (:handler match))]
      ;; if handler is found call it on request
      (handler req)
      ;; if not, echo the request back as an response
      req)))

(defn wrap-state [handler app-state]
  (fn [req]
    (handler (assoc req :app-state app-state))))

(defn prod-handler [app-state]
  (-> route-handler
      (wrap-state app-state)))

(defn dev-handler [app-state]
  (fn [req]
    ((-> (prod-handler app-state)
         (wrap-resource "public")
         (prone/wrap-exceptions
          {:skip-prone? (fn [req] (contains? (:headers req) "postman-token"))})
         wrap-content-type
         wrap-not-modified)
     req)))

;; Server

(defrecord WebServer [port build app shut-down]
  component/Lifecycle
  (start [component]
         (if shut-down
           component
           (do (println "Starting webserver on port" port)
               (let [handler (case build
                               :dev (dev-handler (:state app))
                               :prod (prod-handler (:state app)))
                     server (httpkit/run-server handler {:port port})]
                 (assoc component :shut-down server)))))
  (stop [component]
        (when-let [shutdown-fn (:shut-down component)]
          (println "Shutting webserver")
          (shutdown-fn))
        (dissoc component :shut-down)))

(defn new-server [port build]
  (WebServer. port build nil nil))
