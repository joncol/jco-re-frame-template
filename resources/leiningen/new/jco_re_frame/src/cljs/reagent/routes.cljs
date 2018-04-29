(ns {{project-ns}}.routes
  (:require [goog.events :as gevents]
            [goog.history.EventType :as EventType]
            [re-frame.core :as rf]
            [secretary.core :as secretary]
            [{{project-ns}}.events :as events])
  (:require-macros [secretary.core :refer [defroute]])
  (:import goog.History))

(defn hook-browser-navigation! []
  (doto (History.)
    (gevents/listen
     EventType/NAVIGATE
     (fn [event]
       (secretary/dispatch! (.-token event))))
    (.setEnabled true)))

(defn app-routes []
  (secretary/set-config! :prefix "#")
  (defroute "/" []
    (rf/dispatch [::events/set-active-panel :home-panel]))

  (defroute "/about" []
    (rf/dispatch [::events/set-active-panel :about-panel]))

  (hook-browser-navigation!))
