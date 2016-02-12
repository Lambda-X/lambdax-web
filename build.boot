(def +version+ "0.1.0-SNAPSHOT")

(set-env!
 :dependencies '[;; Boot deps
                 [adzerk/boot-cljs            "1.7.228-1" :scope "test"]
                 [pandeiro/boot-http          "0.7.1-SNAPSHOT" :scope "test"]
                 [adzerk/boot-reload          "0.4.4" :scope "test"]

                 ;; Repl
                 [adzerk/boot-cljs-repl       "0.3.0"]
                 [com.cemerick/piggieback     "0.2.1"  :scope "test"]
                 [weasel                      "0.7.0"  :scope "test"]
                 [org.clojure/tools.nrepl     "0.2.12" :scope "test"]

                 ;; Tests
                 [crisptrutski/boot-cljs-test "0.2.2-SNAPSHOT"  :scope "test"]
                 [adzerk/boot-test            "1.0.7"      :scope "test"]

                 ;; App deps
                 [org.clojure/clojure         "1.7.0"]
                 [org.clojure/clojurescript   "1.7.228"]
                 [org.clojure/core.async      "0.2.371" :scope "test"]
                 [org.omcljs/om               "1.0.0-alpha28"]
                 [twitter-api                 "0.7.8"]
                 [com.cognitect/transit-cljs  "0.8.237"]
                 [com.andrewmcveigh/cljs-time "0.3.14"]])

(require '[adzerk.boot-cljs             :refer [cljs]]
         '[adzerk.boot-reload           :refer [reload]]
         '[adzerk.boot-test             :as boot-test]
         '[pandeiro.boot-http           :refer [serve]]
         '[crisptrutski.boot-cljs-test  :refer [test-cljs]]
         '[adzerk.boot-cljs-repl        :refer [cljs-repl start-repl]])

(task-options! pom {:project 'lambdax-web
                    :version +version+
                    :url "https://github.com/ScalaConsultants/lambdax-web"
                    :description "The LambdaX official website project"
                    :license {"Eclipse Public License" "http://www.eclipse.org/legal/epl-v10.html"}})

(def dev-options
  {:optimization :none
   :source-map-timestamp true})

(def prod-options
  {:closure-defines {"goog.DEBUG" false}
   :optimize-constants true
   :static-fns true
   :elide-asserts true
   :pretty-print false
   :source-map-timestamp true
   :parallel-build true})

(deftask dev-build []
  (set-env! :source-paths #{"src/cljs" "env/dev/cljs"}
            :resource-paths #{"resources/public"})
  (comp (cljs :optimizations :simple
              :compiler-options dev-options
              :source-map true)
        (sift :include #{#"\.out"} :invert true)
        (target)))

(deftask prod-build []
  (set-env! :source-paths #{"src/cljs" "env/prod/cljs"}
            :resource-paths #{"resources/public"})
  (comp (cljs :optimizations :advanced
              :compiler-options prod-options
              :source-map true)
        (sift :include #{#"\.out"} :invert true)
        (target)))

(deftask cider
  "Add CIDER support:
   https://github.com/boot-clj/boot/wiki/Cider-REPL"
  []
  (require 'boot.repl)
  (swap! @(resolve 'boot.repl/*default-dependencies*)
         concat '[[cider/cider-nrepl "0.11.0-SNAPSHOT"]
                  [venantius/ultra "0.4.0"]
                  [org.clojure/tools.nrepl "0.2.12"]
                  [refactor-nrepl "2.0.0-SNAPSHOT"]])
  (swap! @(resolve 'boot.repl/*default-middleware*)
         concat '[refactor-nrepl.middleware/wrap-refactor
                  cider.nrepl/cider-middleware])
  identity)

(deftask dev
  "Start the dev env..."
  []
  (set-env! :source-paths #{"src/cljs" "env/dev/cljs"}
            :resource-paths #{"resources/public"})
  (comp (serve)
        (watch)
        (cljs-repl)
        (reload)
        (cljs :compiler-options dev-options)))
