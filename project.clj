(defproject lambdax-web "0.1.0-SNAPSHOT"
  :description "The LambdaX official website"
  :url "https://github.com/ScalaConsultants/lambdax-web"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}

  :dependencies [[org.clojure/clojure "1.7.0"]
                 [org.clojure/clojurescript "1.7.170"]
                 [org.clojure/core.async "0.2.371" :scope "test"]
                 [org.omcljs/om "1.0.0-alpha28"]
                 [figwheel-sidecar "0.5.0-SNAPSHOT" :scope "test"]
                 [twitter-api "0.7.8"]
                 [com.stuartsierra/component "0.3.1"]
                 [overtone/at-at "1.2.0"]
                 [org.clojars.scsibug/feedparser-clj "0.4.0"]
                 [clj-time "0.11.0"]
                 [com.cognitect/transit-clj "0.8.285"]
                 [com.cognitect/transit-cljs "0.8.237"]
                 [com.andrewmcveigh/cljs-time "0.4.0"]
                 [http-kit "2.1.18"]
                 [ring/ring-core "1.4.0" :exclusions [ring/ring-jetty-adapter]]
                 [bidi "1.21.1"]
                 [environ "1.0.2"]]

  :plugins [[lein-cljsbuild "1.1.1"]
            [lein-figwheel "0.5.0-3"]
            [lein-environ "1.0.2"]]

  :source-paths ["src/clj" "src/cljs" "test/clj" "test/cljs"]
  :min-lein-version "2.0.0"
  :uberjar-name "lambdax-web.jar"
  :jvm-opts ["-server"]
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
                 :source-paths ["src/dev" "env/dev/clj"]
                 :resource-paths ^:replace ["dev-resources"]
                 :repl-options {:init-ns lambdax-web.dev}}
   :profiles/dev {}}


  :clean-targets ^{:protect false} ["resources/public/js/compiled" "target"]

  :cljsbuild
  {:builds
   [{:id "dev"
     :source-paths ["src/cljs"]

     :figwheel {:on-jsload "lambdax-web.core/on-js-reload"}

     :compiler {:main lambdax-web.core
                :asset-path "js/compiled/out"
                :output-to "resources/public/js/compiled/lambdax_web.js"
                :output-dir "resources/public/js/compiled/out"
                :source-map-timestamp true}}
    ;; This next build is an compressed minified build for
    ;; production. You can build this with:
    ;; lein cljsbuild once min
    {:id "min"
     :source-paths ["src/cljs"]
     :compiler {:output-to "resources/public/js/compiled/lambdax_web.js"
                :main lambdax-web.core
                :optimizations :advanced
                :pretty-print false}}]}

  :figwheel {;; :http-server-root "public" ;; default and assumes "resources"
             ;; :server-port 3449 ;; default
             ;; :server-ip "127.0.0.1"

             :css-dirs ["resources/public/css"] ;; watch and update CSS

             ;; Start an nREPL server into the running figwheel process
             ;; :nrepl-port 7888

             ;; Server Ring Handler (optional)
             ;; if you want to embed a ring handler into the figwheel http-kit
             ;; server, this is for simple ring servers, if this
             ;; doesn't work for you just run your own server :)
             ;; :ring-handler hello_world.server/handler

             ;; To be able to open files in your editor from the heads up display
             ;; you will need to put a script on your path.
             ;; that script will have to take a file path and a line number
             ;; ie. in  ~/bin/myfile-opener
             ;; #! /bin/sh
             ;; emacsclient -n +$2 $1
             ;;
             ;; :open-file-command "myfile-opener"

             ;; if you want to disable the REPL
             ;; :repl false

             ;; to configure a different figwheel logfile path
             ;; :server-logfile "tmp/logs/figwheel-logfile.log"
             }
  :aliases {"bg-repl" ["trampoline" "repl" ":headless" "> repl.out 2> repl.err < /dev/null &"]})
