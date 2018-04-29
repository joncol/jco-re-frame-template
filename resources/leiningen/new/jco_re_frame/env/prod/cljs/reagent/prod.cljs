(ns {{project-ns}}.prod
  (:require [{{project-ns}}.core :as core]))

;; Ignore println statements in prod.
(set! *print-fn* (fn [& _]))

(core/init!)
