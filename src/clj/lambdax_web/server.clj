(ns lambdax-web.server
  (:require [org.httpkit.server :as httpkit]
            [bidi.bidi :as bidi]
            [ring.util.response :refer [resource-response]]
            [ring.middleware.resource :refer [wrap-resource]]
            [ring.middleware.content-type :refer [wrap-content-type]]
            [ring.middleware.not-modified :refer [wrap-not-modified]]
            [lambdax-web.util :as util]
            [com.stuartsierra.component :as component]))

(defn index [_]
  (assoc (resource-response "index.html" {:root "public"})
         :headers {"Content-Type" "text/html"}))

(defn wrap-transit-response [handler]
  (fn [req]
    {:status 200
     :headers {"Content-Type" "application/transit+json"}
     :body (-> req handler util/write-transit)}))

(defn feeds [{:keys [app-state]}]
  (let [feeds (:feeds @app-state)]
    (or (seq feeds) "No feeds available")))

(def routes
  [""  {"/" :index
        "/feeds"
        {:get {[""] :feeds}}}])

(def match->handler
  {:index index
   :feeds (wrap-transit-response feeds)})

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
         wrap-content-type
         wrap-not-modified)
     req)))

;; Server

(defrecord WebServer [port handler app-state shut-down]
  component/Lifecycle
  (start [component]
    (if shut-down
      component
      (let [request-handler (handler app-state)
            server (httpkit/run-server request-handler {:port port})]
        (assoc component :shut-down server))))
  (stop [component]
    (when-let [shutdown-fn (:shut-down component)]
      (shutdown-fn))
    (dissoc component :shut-down)))

(defn dev-server [web-port]
  (WebServer. 3000 dev-handler nil nil))

(defn prod-server [web-port]
  (WebServer. 3000 prod-handler nil nil))
