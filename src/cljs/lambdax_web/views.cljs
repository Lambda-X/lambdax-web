(ns lambdax-web.views
  (:require [om.dom :as dom]
            [om.next :as om :refer-macros [defui]]
            [clojure.string :refer [join split upper-case]]
            [cljs-time.core :as t]
            [cljs-time.coerce :as c]))

(def ^:private number->month
  {1 "JAN"
   2 "FEB"
   3 "MAR"
   4 "APR"
   5 "MAY"
   6 "JUNE"
   7 "JULY"
   8 "AUG"
   9 "SEPT"
   10 "OCT"
   11 "NOV"
   12 "DEC"})

(defn render-main-section []
  (dom/section #js {:id "main" :className "full-width"}
               (dom/object #js {:data "img/greg.svg"
                             :id "greg-svg"
                             :alt "Greg the programmer"
                             :type "image/svg+xml"})
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
                                 (dom/h1 nil "Putting (defn) back into programming")
                                 (dom/p nil "We are a Clojure-centric software house.")
                                 (dom/p nil "Functional experts dedicated to Clojure and ClojureScript.")))))

(defui AboutUs
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
                       (dom/a (clj->js {:href (:onclick content)})
                        (dom/button (clj->js {:className (-> content
                                                             :text
                                                             (split #"\s+")
                                                             (as-> content-text
                                                                 (->> content-text
                                                                      (join "-")
                                                                      (str "btn "))))})
                                    (:text content))))))))

(def render-about-us (om/factory AboutUs {:keyfn :title}))

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

(def render-technologies (om/factory Technologies {:keyfn :name}))

(defui Projects
  static om/IQuery
  (query [this]
         [:title :tags :img :url])
  Object
  (render [this]
          (let [{:keys [title tags img url]} (om/props this)]
            (dom/div (clj->js {:className "inline-block"})
                     (dom/a (clj->js {:href url})
                      (dom/img (clj->js {:src (:src img) :alt (:alt img)})))
                     (dom/p nil title)
                     (dom/p #js {:className "tags"}
                            tags)))))

(def render-projects (om/factory Projects {:keyfn :title}))

(defui Team
  static om/IQuery
  (query [this]
         [:name :title :img])
  Object
  (render [this]
          (let [{:keys [name title img imgreal]} (om/props this)]
            (dom/div #js {:className "inline-block"}
                     (dom/div #js {:className "image"}
                              (dom/img (clj->js {:src (:src img) :alt (:alt img)}))
                              (dom/div #js {:className "egg animated"}
                                       (dom/img (clj->js {:src (:src imgreal) :alt (:alt imgreal)}))))
                     (dom/h3 nil name)))))

(def render-team (om/factory Team {:keyfn :name}))

(defui News
  static om/IQuery
  (query [this]
         [:title :text :date :type :author :img :link])
  Object
  (render [this]
          (let [{:keys [title text date type author img link]} (om/props this)
                date (c/from-date date)]
            (dom/div #js {:className "news-box"}
                     (dom/img (clj->js {:src (:src img) :alt (:alt img) :className "inline-block"}))
                     (dom/div #js {:className "inline-block text"}
                              (dom/p #js {:className "top"}
                                     (dom/span #js {:className "type"}
                                               (join " " (-> type
                                                             name
                                                             upper-case
                                                             (split #"-"))))
                                     " by "
                                     (dom/span #js {:className "author"}
                                               author))
                              (dom/h3 nil title)
                              (dom/p nil text)
                              (dom/a (clj->js {:href link :title "more"})
                                     "read-more"))
                     (dom/span #js {:className "calendar-card"}
                               (dom/strong nil (t/day date))
                               (->> date
                                    t/month
                                    (get number->month)))))))

(def render-news (om/factory News {:keyfn :link}))

(def ^:private section->content
  {:about-us render-about-us
   :technologies render-technologies
   :projects render-projects
   :team render-team
   :news render-news})

(defui Section
  static om/Ident
  (ident [this {:keys [section-name]}]
         [:section/by-name section-name])
  static om/IQuery
  (query [this]
         (zipmap [:about-us :technologies :projects :team :news]
                 (map #(conj [:section-name :title :parts :theme] {:content %})
                      [(om/get-query AboutUs)
                       (om/get-query Technologies)
                       (om/get-query Projects)
                       (om/get-query Team)
                       (om/get-query News)])))
  Object
  (render [this]
          (let [{:keys [section-name title parts theme content]} (om/props this)]
            (dom/section (clj->js {:id (name section-name) :className (str theme " " (name section-name))})
                         (dom/div #js {:className "page-width"}
                                  (dom/h1 #js {:className "page-title"}
                                          title)
                                  (dom/div (clj->js {:className
                                                     (if parts
                                                       "rp-container three-part"
                                                       "rp-container")})
                                           (map (get section->content section-name) content)))))))

(def render-section (om/factory Section {:keyfn (comp name :section-name)}))

(defn render-footer-section [contact-form]
  (dom/section #js {:id "contact" :className "dark-theme contact"}
               (dom/div #js {:className "page-width"}
                        (dom/h1 #js {:className "page-title"}
                                "Contact us")
                        (dom/div #js {:className "rp-container three-part"}
                                 (dom/div #js {:className "inline-block"})
                                 (dom/div #js {:className "inline-block"}
                                          (dom/h2 nil "Connect with us on")
                                          ;;(dom/p nil "You can also catch us up via our social accounts! Observe us to stay in touch.")
                                          (dom/div #js {:className "socials"}
                                                   (dom/a #js {:href "http://facebook.com" :title "Facebook" :rel "nofollow"}
                                                          (dom/i #js {:className "fa fa-facebook"}))
                                                   (dom/a #js {:href "https://github.com/Lambda-X" :title "Github" :rel "nofollow"}
                                                          (dom/i #js {:className "fa fa-github-alt"}))
                                                   (dom/a #js {:href "https://mobile.twitter.com/lambdax_io":title "Twitter" :rel "nofollow"}
                                                          (dom/i #js {:className "fa fa-twitter"}))))
                                 (dom/div #js {:className "inline-block"}
                                          contact-form)
                                 (dom/img #js {:src "img/greg-contact.svg" :alt "Greg"})))))

(defui ContactForm
  Object
  (initLocalState [this]
                  {:name "" :email "" :message ""})
  (render [this]
          (dom/form #js {:onSubmit (fn [event]
                                     (.preventDefault event)
                                     ((:submit-message (om/props this))
                                      {:name (:name (om/get-state this))
                                       :email (:email (om/get-state this))
                                       :message (:message (om/get-state this))})
                                     (om/update-state! this assoc :name "" :email "" :message ""))}
                    (dom/input #js {:type "text"
                                    :name "name"
                                    :placeholder "your name"
                                    :value (:name (om/get-state this))
                                    :onChange (fn [event]
                                                (om/update-state! this assoc :name (.. event -target -value)))})
                    (dom/input #js {:type "email"
                                    :name "email"
                                    :placeholder "your email"
                                    :pattern "[^ @]*@[^ @]*"
                                    :required "required"
                                    :value (:email (om/get-state this))
                                    :onChange (fn [event]
                                                (om/update-state! this assoc :email (.. event -target -value)))})
                    (dom/textarea #js {:type "text"
                                       :name "content"
                                       :placeholder "Leave your message"
                                       :defaultValue "What's up?"
                                       :value (:message (om/get-state this))
                                       :onChange (fn [event]
                                                   (om/update-state! this assoc :message (.. event -target -value)))})
                    (dom/input #js {:type "submit"
                                    :value "send message"
                                    :className "btn"})
                    (dom/div #js {:className "clearfix"}))))

(def render-contact-form (om/factory ContactForm))


(defui RootView
  static om/IQuery
  (query [this]
         [{:sections (om/get-query Section)} :message-sent?])
  Object
  (render [this]
          (let [{:keys [sections message-sent?]} (om/props this)]
            (dom/div #js {:id "main-div"}
                     (render-main-section)
                     (render-slogan-section)
                     (map #(render-section (assoc %1 :theme %2))
                          sections
                          (cycle '("light-theme" "dark-theme")))
                     (if message-sent?
                       (render-footer-section
                            (dom/div #js {:className "message"}
                                (dom/h2 nil "Thank You")
                                (dom/p nil "for getting in touch!")))
                       (render-footer-section (render-contact-form
                                               {:submit-message (fn [new-message]
                                                                  (om/transact! this `[(message/send-message! ~new-message)]))})))))))
