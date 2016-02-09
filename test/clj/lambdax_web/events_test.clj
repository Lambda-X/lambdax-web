(ns lambdax-web.events-test
  (:require [clojure.test :refer [deftest is]]
            [lambdax-web.events :as events]))

(comment
  (deftest twitter-feeds
    (is (contains? (first (events/last-statuses 1 "scalac_io")) :text))))
