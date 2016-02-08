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

(defmulti mutate (fn [_ k _] k))

(defmethod mutate 'message/send-message!
  [{:keys [state]} _ {:keys [name email message]}]
  {:action (fn [] (swap! state update-in [:message-sent?] not))
   :form true})

(def parser (om/parser {:read read :mutate mutate}))
