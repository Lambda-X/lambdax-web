(ns lambdax-web.config)

(def defaults
  {:name "LambdaX Website"
   :production? true
   :events {:url "http://backend.lambdax.io/prod/events"
            :interval 180000}
   :contact-form {:email "sales@scalac.io"}})
