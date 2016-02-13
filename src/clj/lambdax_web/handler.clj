(ns lambdax-web.handler
  (:require [bidi.bidi :as bidi]
            [ring.util.response :refer [resource-response]]
            [ring.middleware.resource :refer [wrap-resource]]
            [ring.middleware.content-type :refer [wrap-content-type]]
            [ring.middleware.not-modified :refer [wrap-not-modified]]
            [lambdax-web.util :as util]))

(defn index [_]
  (assoc (resource-response "index.html" {:root "public"})
         :headers {"Content-Type" "text/html"}))

(defn wrap-transit-response [handler]
  (fn [req]
    {:status 200
     :headers {"Content-Type" "application/transit+json;charset=utf-8"}
     :body (-> req handler util/write-transit)}))

(defn wrap-cors [handler domain]
  (fn [req]
    (-> req
        handler
        (assoc-in [:headers "Access-Control-Allow-Origin"] domain))))

(defn events [{:keys [app-state]}]
  (let [events (:events @app-state)]
    (or (seq events) "No events available")))

(def routes
  [""  {"/" :index
        "/events"
        {:get {[""] :events}}}])

(defn get-match->handler [access-domain]
  {:index index
   :events (-> events
               wrap-transit-response
               (wrap-cors access-domain))})

(defn route-handler [match->handler {:keys [uri request-method] :as req}]
  (let [match (bidi/match-route routes uri :request-method request-method)]
    (if-let [handler (match->handler (:handler match))]
      ;; if handler is found call it on request
      (handler req)
      ;; if not, echo the request back as an response
      req)))

(defn wrap-state [handler app-state]
  (fn [req]
    (handler (assoc req :app-state app-state))))

(defn dev-post-mw
  [handler]
  (-> handler
      (wrap-resource "public")
      (wrap-content-type)
      (wrap-not-modified)))

(defn wrap-pass-through
  "Middleware that does nothing, just calls the next handler with the
  request."
  [handler]
  (fn [request]
    (handler request)))

(defn new-handler
  "Create the main app handler.

  In the middleware chain, pre-middleware will preceed the main handler,
  whereas post-middleware will follow."
  [pre-middleware post-middleware access-domain app-state]
  (-> (post-middleware (partial route-handler
                                (get-match->handler access-domain)))
      (wrap-state app-state)
      pre-middleware))
