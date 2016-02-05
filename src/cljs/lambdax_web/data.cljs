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
                          :alt "Clojure"}}
                   ])

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
            :month "JUN"
            :day "30"
            :type "BLOG POST"
            :author "Andrea.Richiardi"
            :img {:src "img/news.png"
                  :alt "news"}
            :link {:href "#"
                   :title "more"
                   :text "read more"}}
           {:title "How awesome Clojure is!"
            :text "Some random tekst goes here. Some random tekst
                   goes here. Some random tekst goes here. Some
                   random tekst goes here. Some random tekst goes here."
            :month "JUN"
            :day "30"
            :type "BLOG POST"
            :author "Andrea.Richiardi"
            :img {:src "img/news.png"
                  :alt "news"}
            :link {:href "#"
                   :title "more"
                   :text "read-more"}}
           {:title "How awesome Clojure is!"
            :text "Some random tekst goes here. Some random tekst
                   goes here. Some random tekst goes here. Some
                   random tekst goes here. Some random tekst goes here."
            :month "JUN"
            :day "30"
            :type "BLOG POST"
            :author "Andrea.Richiardi"
            :img {:src "img/news.png"
                  :alt "news"}
            :link {:href "#"
                   :title "more"
                   :text "read-more"}}])

(def sections [{:name "about-us"
                :title "About us"
                :parts true
                :theme "light-theme"
                :content about-us}
               {:name "technologies"
                :title "We are best at"
                :parts true
                :theme "dark-theme"
                :content technologies}
               {:name "projects"
                :title "Our recent projects"
                :parts false
                :theme "light-theme"
                :content projects}
               {:name "team"
                :title "Team"
                :parts true
                :theme "dark-theme"
                :content team}
               {:name "news"
                :title "Latest news"
                :parts false
                :theme "light-theme"
                :content news}])


(def application-state {:twitter-status [{:created_at "date-time"
                                          :text "some text"
                                          :retweet_count "0"
                                          :favourite-count "0"}
                                         {:created_at "date-time"
                                          :text "some text"
                                          :retweet_count "0"
                                          :favourite-count "0"}]
                        :sections sections
                        :about-us about-us
                        :technologies technologies
                        :projects projects
                        :team team
                        :news news})
