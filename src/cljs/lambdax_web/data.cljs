(ns lambdax-web.data)

(def about-us [{:title "We believe in our values"
                :img {:src "img/icon-we.svg"
                      :alt "Our culture"}
                :content {:type :text
                          :text "Our community is based on experienced peoplewith a lot of knowledge. We strongly believe, that teamwork and some other stuff will help us come to better solutions and create more effective products."}}
               {:title "We do functional programming"
                :img {:src "img/icon-fp.svg"
                      :alt "Our culture"}
                :content {:type :text
                          :text "Our communitoy is based on experienced people with a lot of knowledage. We strongly believe, that teamwork and some other stuff will help us come to better solutions and create more effective products."}}
               {:title "We are ready to help you"
                :img {:src "img/icon-contact.svg"
                      :alt "Our culture"}
                :content {:type :button
                          :text "contact us"}}])

(def technologies [{:name "ClojureScript"
                    :img {:src "img/cljs.svg"
                          :alt "Clojure"}}
                   {:name "Clojure"
                    :img {:src "img/clojure.svg"
                          :alt "Clojure"}}
                   {:name "ClojureScript"
                    :img {:src "img/cljs.svg"
                          :alt "Clojure"}}])

(def projects [{:title "Replumb is a plumbing library for your self-hosted
              ClojureScript Read-Eval-Print-Loops."
                :tags "clojure, lib, functional programing"
                :img {:src "img/replumb.svg"
                      :alt "replumb"}}
               {:title "Live demo of replumb
              created with clojure script."
                :tags "clojurescript, website, live demo"
                :img {:src "img/replumb.svg"
                      :alt "Replumb"}}])

(def team [{:name "Andrea"
            :title "Hello there!"
            :img {:src "img/boy.svg"
                  :alt "Andrea"}}
           {:name "Dajana"
            :title "Hello there!"
            :img {:src "img/girl.svg"
                  :alt "Dajana"}}
           {:name "Tomek"
            :title "Hello there!"
            :img {:src "img/boy.svg"
                  :alt "Tomek"}}])

(def news [{:title "How awesome Clojure is!"
            :text "Some random tekst goes here. Some random tekst
                   goes here. Some random tekst goes here. Some
                   random tekst goes here. Some random tekst goes here."
            :date #inst "2016-02-03T15:08:47.000-00:00"
            :type "BLOG POST"
            :author "Andrea.Richiardi"
            :img {:src "img/news.png"
                  :alt "news"}
            :link "#"}
           {:title "How awesome Clojure is!"
            :text "Some random tekst goes here. Some random tekst
                   goes here. Some random tekst goes here. Some
                   random tekst goes here. Some random tekst goes here."
            :date #inst "2016-02-03T15:08:47.000-00:00"
            :type "BLOG POST"
            :author "Andrea.Richiardi"
            :img {:src "img/news.png"
                  :alt "news"}
            :link "#"}
           {:title "How awesome Clojure is!"
            :text "Some random tekst goes here. Some random tekst
                   goes here. Some random tekst goes here. Some
                   random tekst goes here. Some random tekst goes here."
            :date #inst "2016-02-03T15:08:47.000-00:00"
            :type "BLOG POST"
            :author "Andrea.Richiardi"
            :img {:src "img/news.png"
                  :alt "news"}
            :link "#"}])

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
