(ns {{project-ns}}.core-spec
  (:require-macros [speclj.core :refer [describe it should should= should-not]])
  (:require [speclj.core]
            [reagent.core :as r :refer [atom]]
            [{{project-ns}}.core :as core]))

(def is-client (not (nil? (try (.-document js/window)
                              (catch js/Object e nil)))))

(defn add-test-div [name]
  (let [doc  js/document
        body (.-body js/document)
        div  (.createElement doc "div")]
    (.appendChild body div)
    div))

(defn with-mounted-component [comp f]
  (when is-client
    (let [div (add-test-div "_testreagent")]
      (let [comp (r/render-component comp div #(f comp div))]
        (r/unmount-component-at-node div)
        (r/flush)
        (.removeChild (.-body js/document) div)))))

(defn found-in [re div]
  (let [res (.-innerHTML div)]
    (if (re-find re res)
      true
      (do (println "Not found: " res)
          false))))

(describe "test home"
  (it "contains 'Welcome to' in home page"
      (with-mounted-component (core/home-page)
        (fn [c div]
          (should (found-in #"Welcome to" div))))))
