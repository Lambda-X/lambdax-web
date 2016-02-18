(ns lambdax-web.config)

(def defaults
  {:name "LambdaX Website (DEV)"
   :production? false
   :events {:interval 600000 ;; milliseconds
            :url "http://backend.lambdax.io/devel/events"}
   :contact-form {:email "sales@scalac.io"}})
