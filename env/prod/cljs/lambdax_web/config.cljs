(ns lambdax-web.config)

(def defaults
  {:name "LambdaX Website"
   :production? true
   :events {:interval 3600000 ;; in milliseconds
            :url "http://backend.lambdax.io/prod/events"}
   :contact-form {:email "sales@scalac.io"}})
