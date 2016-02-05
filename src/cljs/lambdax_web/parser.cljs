(ns lambdax-web.parser
  (:require [om.next :as om]))

(defmulti read om/dispatch)

(defmethod read :sections
  [{:keys [state query]} k _]
  (let [st @state
        refs (get st k)]
    {:value (mapv (partial get-in st) refs)}))

(defmethod read :default
  [{:keys [state]} k _]
  {:value (get @state k)})

(def parser (om/parser {:read read}))
