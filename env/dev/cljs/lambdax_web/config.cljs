(ns lambdax-web.config)

(def defaults
  {:name "LambdaX Website (DEV)"
   :production? false
   :events {:url "http://backend.lambdax.io/devel/events"
            :interval 5000}
   :contact-form {:email "sales@scalac.io"}})
