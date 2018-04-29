(ns leiningen.new.jco-re-frame
  (:require [clojure.string :as str]
            [leiningen.core.main :as main]
            [leiningen.new.templates :refer [->files name-to-path renderer]]))

(def render (renderer "jco-re-frame"))

(def valid-opts ["+cider" "+sass"])

(defn cider? [opts]
  (some #{"+cider"} opts))

(defn sass? [opts]
  (some #{"+sass"} opts))

(defn validate-opts [opts]
  (let [invalid-opts (remove (set valid-opts) opts)]
    (cond
      (seq invalid-opts) (str "Invalid options: " (str/join " " invalid-opts)
                              "\nSupported options: "
                              (str/join " " valid-opts)))))

(defn jco-re-frame
  "Leiningen template to generate re-frame projects."
  [name]
  (let [data {:name name
              :sanitized (name-to-path name)}]
    (main/info "Generating fresh 'lein new' re-frame (jco style) project.")
    (->files data
             ["src/{{sanitized}}/foo.clj" (render "foo.clj" data)])))
