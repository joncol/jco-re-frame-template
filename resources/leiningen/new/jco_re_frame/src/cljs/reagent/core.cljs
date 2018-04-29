{{^re-frame?}}
(ns {{project-ns}}.core
  (:require [reagent.core :as r :refer [atom]]
            [secretary.core :as secretary :include-macros true]
            [accountant.core :as accountant]))

(defn home-page []
  [:div [:h2 "Welcome to {{name}}"]
   [:div [:a {:href "/about"} "go to about page"]]])

(defn about-page []
  [:div [:h2 "About {{name}}"]
   [:div [:a {:href "/"} "go to the home page"]]])

(defonce page (atom #'home-page))

(defn current-page []
  [:div [@page]])

(secretary/defroute "/" []
  (reset! page #'home-page))

(secretary/defroute "/about" []
  (reset! page #'about-page))

(defn mount-root []
  (r/render [current-page] (.getElementById js/document "app")))

(defn init! []
  (accountant/configure-navigation!
   {:nav-handler
    (fn [path]
      (secretary/dispatch! path))
    :path-exists?
    (fn [path]
      (secretary/locate-route path))})
  (accountant/dispatch-current!)
  (mount-root))
{{/re-frame?}}
{{#re-frame?}}
(ns {{project-ns}}.core
  (:require [reagent.core :as r]
            [re-frame.core :as rf]
            [{{project-ns}}.events :as events]
            [{{project-ns}}.routes :as routes]
            [{{project-ns}}.views :as views]
            [{{project-ns}}.config :as config]))

(defn dev-setup []
  (when config/debug?
    (enable-console-print!)
    (println "dev mode")))

(defn mount-root []
  (rf/clear-subscription-cache!)
  (r/render [views/main-panel] (.getElementById js/document "app")))

(defn ^:export init! []
  (routes/app-routes)
  (rf/dispatch-sync [::events/initialize-db])
  (dev-setup)
  (mount-root))
{{/re-frame?}}
