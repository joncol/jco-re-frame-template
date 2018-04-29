(ns ^:figwheel-no-load {{project-ns}}.dev
  (:require [devtools.core :as devtools]
            [{{project-ns}}.core :as core]))

(devtools/install!)

(enable-console-print!)

(core/init!)
