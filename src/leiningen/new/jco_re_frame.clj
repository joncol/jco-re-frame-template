(ns leiningen.new.jco-re-frame
  (:require [clojure.string :as str]
            [leiningen.core.main :as main]
            [leiningen.new.templates
             :refer [->files name-to-path renderer sanitize-ns]]))

(def render (renderer "jco-re-frame"))

(def valid-opts ["+cider" "+less" "+sass" "+spec" "+test"])

(defn cider? [opts]
  (some #{"+cider"} opts))

(defn less? [opts]
  (some #{"+less"} opts))

(defn sass? [opts]
  (some #{"+sass"} opts))

(defn spec? [opts]
  (some #{"+spec"} opts))

(defn test? [opts]
  (some #{"+test"} opts))

(defn java-version>=9? []
  (not (clojure.string/starts-with?
        (System/getProperty "java.specification.version") "1.")))

(defn- conflicting-opts-msg [x y]
  (str "Only one of the options " x " and " y " can be used"))

(defn validate-opts [opts]
  (let [invalid-opts (remove (set valid-opts) opts)]
    (cond
      (seq invalid-opts) (str "Invalid option"
                              (when (< 1 (count invalid-opts)) "s") ": "
                              (str/join " " invalid-opts)
                              "\nSupported options: "
                              (str/join " " valid-opts))
      (and (less? opts) (sass? opts)) (conflicting-opts-msg "+less" "+sass")
      (and (spec? opts) (test? opts)) (conflicting-opts-msg "+spec" "+test"))))

(defn- template-data
  [name opts]
  {:name               name
   :project-ns         (sanitize-ns name)
   :sanitized          (name-to-path name)
   :if-cider?          (fn [x] (if (cider? opts) x ""))
   :if-less?           (fn [x] (if (less? opts) x ""))
   :if-sass?           (fn [x] (if (sass? opts) x ""))
   :if-spec?           (fn [x] (if (spec? opts) x ""))
   :if-test?           (fn [x] (if (test? opts) x ""))
   :if-java-version-9? (fn [x] (if (java-version>=9?) x ""))})

(defn- template-files [data]
  ["project.clj" (render "project.clj" data)])

(defn jco-re-frame
  "Leiningen template to generate re-frame projects."
  [name & opts]
  (if-let [error (validate-opts opts)]
    (println error)
    (let [data (template-data name opts)]
      (main/info "Generating fresh 'lein new' re-frame (jco style) project.")
      (->files data (template-files data)))))
