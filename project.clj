(defproject lambdax-web "0.1.0-SNAPSHOT"
  :description "The LambdaX official website"
  :url "https://github.com/ScalaConsultants/lambdax-web"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}

  :dependencies [[org.clojure/clojure "1.7.0"]
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
                 [clj-rome "0.4.0"]]
  :plugins [[lein-environ "1.0.2"]]

  :source-paths ["src/clj" "test/clj"]
  :min-lein-version "2.0.0"
  :uberjar-name "lambdax-web-standalone.jar"
  :jvm-opts ^:replace ["-XX:+TieredCompilation" "-XX:TieredStopAtLevel=1" "-Xverify:none"]
  :resource-paths ["resources"]

  :main ^:skip-aot lambdax-web.core

  :profiles
  {:uberjar {:omit-source true
             :aot :all
             :source-paths ["env/prod/clj"]}
   :dev [:project/dev :profiles/dev]
   :project/dev {:dependencies [[prone "1.0.1"]
                                [ring/ring-mock "0.3.0"]
                                [ring/ring-devel "1.4.0"]
                                [org.clojure/tools.namespace "0.2.11"]
                                [org.clojure/tools.trace "0.7.9"]
                                ;; [pjstadig/humane-test-output "0.7.1"]
                                [mvxcvi/puget "1.0.0"]]
                 :plugins [[lein-figwheel "0.5.0-6" :exclusions [cider/cider-nrepl]]]
                 :source-paths ["src/dev" "env/dev/clj"]
                 :repl-options {:init-ns lambdax-web.dev}}
   :profiles/dev {}}

  :clean-targets ^{:protect false} [:target-path]

  :aliases {"bg-repl" ["trampoline" "repl" ":headless" ">" "repl.out " "2>" "repl.err" "&"]})
