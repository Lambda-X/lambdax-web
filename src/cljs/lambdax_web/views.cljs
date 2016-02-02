(ns lambdax-web.views
  (:require [om.dom :as dom]
            [om.next :as om :refer-macros [defui]]))

(defn render-main-section []
  (dom/section #js {:id "main" :className "full-width"}
               (dom/img #js {:src "img/greg.svg"
                             :alt "Greg the programmer"})
               (dom/img #js {:src "img/over-greg.svg"
                             :alt "over"
                             :className "hover-image"})))

(defn render-slogan-section []
  (dom/section #js {:id "slogan" :className "dark-theme slogan"}
               (dom/div #js {:className "page-width"}
                        (dom/a #js {:href "index.html"
                                    :title "Our awesome  website"
                                    :className "inline-block logo"}
                               (dom/img #js {:src "img/lambdax-short.svg"
                                             :alt "Lambdax"}))
                        (dom/div #js {:className "inline-block slogan"}
                                 (dom/h1 nil "We do functional programming")
                                 (dom/p nil "We are the first Clojure centring software house.")
                                 (dom/p nil "Functional experts dedicated to clojure and cojure script.")))))

(defn render-footer-section []
  (dom/section #js {:id "contact" :className "dark-theme contact"}
               (dom/div #js {:className "page-width"}
                        (dom/h1 #js {:className "page-title"}
                                "Contact us")
                        (dom/div #js {:className "rp-container three-part"}
                                 (dom/div #js {:className "inline-block"})
                                 (dom/div #js {:className "inline-block"}
                                          (dom/h2 nil "We are waiting for your message!")
                                          (dom/p nil "You can also catch us up via our social accounts! Observe us to stay in touch.")
                                          (dom/div #js {:className "socials"}
                                                   (dom/a #js {:href "http://facebook.com" :title "Facebook" :rel "nofollow"}
                                                          (dom/i #js {:className "fa fa-facebook"}))
                                                   (dom/a #js {:href "http://github.com" :title "Github" :rel "nofollow"}
                                                          (dom/i #js {:className "fa fa-github-alt"}))
                                                   (dom/a #js {:href "http://twitter.com":title "Twitter" :rel "nofollow"}
                                                          (dom/i #js {:className "fa fa-twitter"}))))
                                 (dom/div #js {:className "inline-block"}
                                          (dom/input #js {:type "text" :name "name" :placeholder "your name"})
                                          (dom/input #js {:type "email" :name "email" :placeholder "your email"})
                                          (dom/textarea #js {:type "text" :name "content" :defaultValue "What's up?"})
                                          (dom/input #js {:type "submit" :value "send message" :className "btn"})
                                          (dom/div #js {:className "clearfix"})))
                        (dom/img #js {:src "img/greg-contact.svg" :alt "Greg"}))))

(defui RenderAboutUs
  static om/IQuery
  (query [this]
         [:title :img :content])
  Object
  (render [this]
          (let [{:keys [title img content]} (om/props this)]
            (dom/div (clj->js {:className "inline-block"})
                     (dom/img (clj->js {:src (:src img) :alt (:alt img)}))
                     (dom/h3 nil title)
                     (if (= (:type content) :text)
                       (dom/p nil (:text content))
                       (dom/button (clj->js {:className (:className content)})
                                   (:text content)))))))

(def render-about-us (om/factory RenderAboutUs))


(defui Technologies
  static om/IQuery
  (query [this]
         [:name :img])
  Object
  (render [this]
          (let [{:keys [name img]} (om/props this)]
            (dom/div (clj->js {:className "inline-block"})
                     (dom/img (clj->js {:src (:src img) :alt (:alt img)}))
                     (dom/h2 nil name)))))

(def render-technologies (om/factory Technologies))

(defui Projects
  static om/IQuery
  (query [this]
         [:title :tags :img])
  Object
  (render [this]
          (let [{:keys [title tags img]} (om/props this)]
            (dom/div (clj->js {:className "inline-block"})
                     (dom/img (clj->js {:src (:src img) :alt (:alt img)}))
                     (dom/p nil title)
                     (dom/p #js {:className "tags"}
                            tags)))))

(def render-projects (om/factory Projects))

(defui Team
  static om/IQuery
  (query [this]
         [:name :title :img])
  Object
  (render [this]
          (let [{:keys [name title img]} (om/props this)]
            (dom/div #js {:className "inline-block"}
                     (dom/div #js {:className "image"}
                              (dom/img (clj->js {:src (:src img) :alt (:alt img)}))
                              (dom/div #js {:className "egg animated"}
                                       (dom/p nil title)))
                     (dom/h3 nil name)))))

(def render-team (om/factory Team))

(defui News
  static om/IQuery
  (query [this]
         [:title :text :month :day :type :author :img :link])
  Object
  (render [this]
          (let [{:keys [title text month day type author img link]} (om/props this)]
            (dom/div #js {:className "news-box"}
                     (dom/img (clj->js {:src (:src img) :alt (:alt img) :className "inline-block"}))
                     (dom/div #js {:className "inline-block text"}
                              (dom/p #js {:className "top"}
                                     (dom/span #js {:className "type"}
                                               type)
                                     (dom/span #js {:className "author"}
                                               author))
                              (dom/h3 nil title)
                              (dom/p nil text)
                              (dom/a (clj->js {:href (:href link) :title (:title link)})
                                     (:text link)))
                     (dom/span #js {:className "calendar-card"}
                               (dom/strong nil day)
                               month)))))

(def render-news (om/factory News))

(defui RenderSection
  static om/IQuery
  (query [this]
         [:name :title :parts :theme :content])
  Object
  (render [this]
          (let [{:keys [name title parts theme content]} (om/props this)]
            (dom/section (clj->js {:id name :className (str theme " " name)})
                         (dom/div #js {:className "page-width"}
                                  (dom/h1 #js {:className "page-title"}
                                          title)
                                  (dom/div (clj->js {:className
                                                     (if parts
                                                       "rp-container three-part"
                                                       "rp-container")})
                                           (cond (= name "about-us") (map render-about-us content)
                                                 (= name "technologies") (map render-technologies content)
                                                 (= name "projects") (map render-projects content)
                                                 (= name "team") (map render-team content)
                                                 (= name "news") (map render-news content))))))))

(def render-section (om/factory RenderSection))

(defui RootView
  static om/IQuery
  (query [this]
         [:sections])
  Object
  (render [this]
          (let [{:keys [sections]} (om/props this)]
            (dom/div #js {:id "main-div"}
                     (render-main-section)
                     (render-slogan-section)
                     (map render-section sections)
                     (render-footer-section)))))
