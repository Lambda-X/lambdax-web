(ns lambdax-web.twitter-feed-test
  (:require [clojure.test :refer [deftest is]]
            [lambdax-web.twitter-feed :as tf]))

(comment
  (deftest twitter-feeds
    (is (contains? (first (tf/last-statuses 1 "scalac_io")) :text))))
