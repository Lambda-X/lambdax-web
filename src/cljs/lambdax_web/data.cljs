(ns lambdax-web.data)

(def about-us [{:title "We believe in our values"
                :img {:src "img/icon-we.svg"
                      :alt "Our culture"}
                :content {:type :text
                          :text "Passion for functional programming and partnership with clients are the main foundation of our service. We deliver lean, stable code and we focus on the customer experience."}}
               {:title "We do functional programming"
                :img {:src "img/icon-fp.svg"
                      :alt "Our culture"}
                :content {:type :text
                          :text "Clojure embraces functional programming, provides excellent built-in concurrency support and targets several platforms, most notably the JVM and JavaScript (Node.js included). The result is robust, practical, and fast. Your business drives."}}
               {:title "We are ready to help you"
                :img {:src "img/icon-contact.svg"
                      :alt "Our culture"}
                :content {:type :button
                          :text "contact us"
                          :onclick "#contact"}}])

(def technologies [{:name "ClojureScript"
                    :img {:src "img/cljs.svg"
                          :alt "Clojure"}}
                   {:name "Clojure"
                    :img {:src "img/clojure.svg"
                          :alt "Clojurescript"}}])

(def projects [{:title "Replumb is a plumbing library for your self-hosted
              ClojureScript Read-Eval-Print-Loops."
                :tags "clojure, repl, functional programing"
                :img {:src "img/replumb.svg"
                      :alt "replumb"}
                :url "https://github.com/Lambda-X/replumb"}
               {:title "Clojurescript.io - A live Repl, featuring replumb."
                :tags "clojurescript, website, react"
                :img {:src "img/clojurescript_io.png"
                      :alt "clojurescript.io"}
                :url "http://www.clojurescript.io"}])

(def team [{:name "Andrea"
            :title "Hello there!"
            :img {:src "img/boy.svg"
                  :alt "Andrea"}
            :imgreal {:src "img/andrea.png"
                              :alt "Andrea"}}
           {:name "Dajana"
            :title "Hello there!"
            :img {:src "img/girl.svg"
                  :alt "Dajana"}
            :imgreal {:src "img/dajana.png"
                              :alt "Dajana"}}
           {:name "Tomek"
            :title "Hello there!"
            :img {:src "img/boy.svg"
                  :alt "Tomek"}
              :imgreal {:src "img/tomek.png"
                                            :alt "Tomek"}}])

(def sections [{:section-name :about-us
                :title "About us"
                :parts true
                :content about-us}
               {:section-name :technologies
                :title "We are best at"
                :parts true
                :content technologies}
               {:section-name :projects
                :title "Our recent projects"
                :parts false
                :content projects}
               {:section-name :team
                :title "Team"
                :parts true
                :content team}
               {:section-name :news
                :title "Latest news"
                :parts false
                :content []}])

(def application-state {:sections sections
                        :message-sent? false})
