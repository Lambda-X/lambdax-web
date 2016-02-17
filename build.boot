;;;;;;;;;;;;;;;;;;;;;;
;;;  Dependencies  ;;;
;;;;;;;;;;;;;;;;;;;;;;

(def common-deps '[;; Boot deps
                   [adzerk/boot-cljs            "1.7.228-1" :scope "test"]
                   [pandeiro/boot-http          "0.7.1-SNAPSHOT" :scope "test"]
                   [adzerk/boot-reload          "0.4.4" :scope "test"]
                   [degree9/boot-semver         "1.2.4" :scope "test"]
                   ;; Repl
                   [adzerk/boot-cljs-repl       "0.3.0"]
                   [com.cemerick/piggieback     "0.2.1"  :scope "test"]
                   [weasel                      "0.7.0"  :scope "test"]
                   [org.clojure/tools.nrepl     "0.2.12" :scope "test"]])

(def frontend-deps '[[org.clojure/clojure         "1.7.0" :scope "provided"]
                     [org.clojure/clojurescript   "1.7.228" :scope "provided"]
                     [org.clojure/core.async      "0.2.371" :scope "test"]
                     [org.omcljs/om               "1.0.0-alpha28"  :scope "provided"]
                     [twitter-api                 "0.7.8" :scope "provided"]
                     [com.cognitect/transit-cljs  "0.8.237" :scope "provided"]
                     [com.andrewmcveigh/cljs-time "0.3.14" :scope "provided"]])

(def backend-dev-deps '[[prone "1.0.1"]
                        [ring/ring-mock "0.3.0"]
                        [ring/ring-devel "1.4.0"]
                        [org.clojure/tools.namespace "0.2.11"]
                        [org.clojure/tools.trace "0.7.9"]
                        ;; [pjstadig/humane-test-output "0.7.1"]
                        [mvxcvi/puget "1.0.0"]])

(def backend-deps '[[org.clojure/clojure "1.7.0"]
                    [twitter-api "0.7.8"]
                    [com.stuartsierra/component "0.3.1"]
                    [overtone/at-at "1.2.0"]
                    [clj-time "0.11.0"]
                    [com.cognitect/transit-clj "0.8.285"]
                    [http-kit "2.1.18"]
                    [ring/ring-core "1.4.0" :exclusions [ring/ring-jetty-adapter]]
                    [bidi "1.21.1"]
                    [environ "1.0.2"]
                    [org.clojure/tools.nrepl "0.2.12"]
                    [cider/cider-nrepl "0.11.0-SNAPSHOT"]
                    [clj-rome "0.4.0"]
                    [boot-environ "1.0.2"]])

(def deps
  (case (get-env :flavor)
    "frontend" (vec (distinct (concat common-deps frontend-deps)))
    "backend" (vec (distinct (concat common-deps backend-deps backend-dev-deps)))
    (do (boot.util/warn "You need to specify a flavor with -e flavor=frontend|backend for build and dev task to work\n")
        common-deps)))

(set-env! :dependencies deps)

(require '[adzerk.boot-cljs      :refer [cljs]]
         '[adzerk.boot-reload    :refer [reload]]
         '[pandeiro.boot-http    :refer [serve]]
         '[adzerk.boot-cljs-repl :refer [cljs-repl start-repl]]
         '[boot-semver.core      :refer :all])

(def +version+ (get-version))

(task-options! pom {:project 'lambdax-web
                    :version +version+
                    :url "https://github.com/ScalaConsultants/lambdax-web"
                    :description "The LambdaX official website project"
                    :license {"Eclipse Public License" "http://www.eclipse.org/legal/epl-v10.html"}})

;;;;;;;;;;;;;;;;;;;;;;
;;;    Options     ;;;
;;;;;;;;;;;;;;;;;;;;;;

(defmulti options
  "Return the correct option map for the build, dispatching on identity"
  identity)

(defmethod options [:frontend :dev] [selection]
  {:env {:source-paths #{"src/cljs" "env/dev/cljs"}
         :resource-paths #{"resources/public"}}
   :build-task (comp (cljs :optimizations :none
                           :source-map true
                           :compiler-options {:source-map-timestamp true})
                     (target))
   :dev-task (comp (serve)
                   (watch)
                   (cljs-repl)
                   (reload)
                   (cljs :optimizations :none
                         :source-map true
                         :compiler-options {:source-map-timestamp true}))})

(defmethod options [:frontend :prod] [selection]
  {:env {:source-paths #{"src/cljs" "env/prod/cljs"}
         :resource-paths #{"resources/public"}}
   :build-task (comp (cljs :optimizations :advanced
                           :source-map true
                           :compiler-options {:closure-defines {"goog.DEBUG" false}
                                              :optimize-constants true
                                              :static-fns true
                                              :elide-asserts true
                                              :pretty-print false
                                              :source-map-timestamp true
                                              :parallel-build true})
                     ;; AR - main.out is needed for source maps!
                     ;; (sift :include #{#"\.out"} :invert true)
                     (target))
   :dev-task identity})

(def backend-build-task
  (comp (aot :all true)
        (uber)
        (jar :main 'lambdax-web.core
             :file "lambdax-web-standalone.jar")
        (target)))

(defmethod options [:backend :dev] [selection]
  {:env {:source-paths #{"src/clj" "env/dev/clj" "src/dev"}}
   :build-task backend-build-task
   :dev-task (repl :init-ns 'lambdax-web.dev)})

(defmethod options [:backend :prod] [selection]
  {:env {:source-paths #{"src/clj" "env/prod/clj"}
         :dependencies (vec (concat common-deps backend-deps))}
   :build-task backend-build-task
   :dev-task identity})

;;;;;;;;;;;;;;;;;;;;;;
;;;  Entry points  ;;;
;;;;;;;;;;;;;;;;;;;;;;

(defn selection
  [build]
  (mapv keyword [(get-env :flavor) build]))

(deftask build
  "Build task, based on the input flavor (specified with -e
  flavor=frontend|backend) AND the input type prod|devel."
  [t type VAL kw "The build type, either :production or :devel."]

  (apply boot.util/info "Building %1$s with %2$s build type...\n" (selection type))

  (let [options (options (selection type))]
    (apply set-env! (reduce #(into %2 %1) [] (:env options)))
    ;; (clojure.pprint/pprint (:dependencies (get-env)))
    (:build-task options)))

(deftask dev
  []
  (let [sel (selection :dev)]
    (apply boot.util/info "Development interactive mode for %s...\n" sel)

    (let [options (options sel)]
      (apply merge-env! (reduce #(into %2 %1) [] (:env options)))
      (:dev-task options))))

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
