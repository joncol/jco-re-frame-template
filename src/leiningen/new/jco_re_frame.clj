(ns leiningen.new.jco-re-frame
  (:require [clojure.string :as str]
            [leiningen.core.main :as main]
            [leiningen.new.templates
             :refer [->files name-to-path renderer sanitize-ns]]))

(def render (renderer "jco-re-frame"))

(def valid-opts ["+cider" "+less" "+sass" "+spec" "+test" "+re-frame"])

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

(defn re-frame? [opts]
  (some #{"+re-frame"} opts))

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
  {:name            name
   :project-ns      (sanitize-ns name)
   :sanitized       (name-to-path name)
   :cider?          (cider? opts)
   :less?           (less? opts)
   :sass?           (sass? opts)
   :less-or-sass?   (or (less? opts) (sass? opts))
   :spec?           (spec? opts)
   :test?           (test? opts)
   :spec-or-test?   (or (spec? opts) (test? opts))
   :re-frame?       (re-frame? opts)
   :java-version-9? (java-version>=9?)})

(defn- template-files [data opts]
  (let [files [["project.clj" (render "project.clj" data)]
               ["src/clj/{{sanitized}}/handler.clj" (render "src/clj/reagent/handler.clj" data)]
               ["src/clj/{{sanitized}}/server.clj" (render "src/clj/reagent/server.clj" data)]
               ["env/prod/clj/{{sanitized}}/middleware.clj" (render "env/prod/clj/reagent/middleware.clj" data)]
               ["env/dev/clj/{{sanitized}}/middleware.clj" (render "env/dev/clj/reagent/middleware.clj" data)]
               ["env/dev/clj/{{sanitized}}/repl.clj" (render "env/dev/clj/reagent/repl.clj" data)]
               ["src/cljs/{{sanitized}}/core.cljs" (render "src/cljs/reagent/core.cljs" data)]
               ["src/cljc/{{sanitized}}/util.cljc" (render "src/cljc/reagent/util.cljc" data)]
               ["env/dev/cljs/{{sanitized}}/dev.cljs" (render "env/dev/cljs/reagent/dev.cljs" data)]
               ["env/prod/cljs/{{sanitized}}/prod.cljs" (render "env/prod/cljs/reagent/prod.cljs" data)]
               ["LICENSE" (render "LICENSE" data)]
               ["README.md" (render "README.md" data)]
               [".gitignore" (render "gitignore" data)]
               ["resources/log4j.properties" (render "resources/log4j.properties" data)]]
        files (if (test? opts)
                (conj files
                      ["test/cljs/{{sanitized}}/core_test.cljs" (render "test/cljs/reagent/core_test.cljs" data)]
                      ["test/cljs/{{sanitized}}/doo_runner.cljs" (render "runners/doo_runner.cljs" data)])
                files)
        files (if (spec? opts)
                (conj files
                      ["spec/cljs/{{sanitized}}/core_test.cljs" (render "spec/cljs/reagent/core_spec.cljs" data)]
                      ["spec/vendor/console-polyfill.js" (render "vendor/console-polyfill.js" data)]
                      ["spec/vendor/es5-sham.js" (render "vendor/es5-sham.js" data)]
                      ["spec/vendor/es5-shim.js" (render "vendor/es5-shim.js" data)]
                      ["runners/speclj" (render "runners/speclj" data)])
                files)
        files (if (less? opts)
                (conj files
                      ["src/less/site.main.less" (render "src/less/site.main.less" data)]
                      ["src/less/profile.less" (render "src/less/profile.less" data)]
                      )
                files)
        files (if (sass? opts)
                (conj files
                      ["src/sass/site.scss" (render "src/sass/site.scss" data)]
                      ["src/sass/_profile.scss" (render "src/sass/_profile.scss" data)])
                files)
        files (if (re-frame? opts)
                (conj files
                      ["src/cljs/{{sanitized}}/config.cljs" (render "src/cljs/reagent/config.cljs" data)]
                      ["src/cljs/{{sanitized}}/db.cljs" (render "src/cljs/reagent/db.cljs" data)]
                      ["src/cljs/{{sanitized}}/events.cljs" (render "src/cljs/reagent/events.cljs" data)]
                      ["src/cljs/{{sanitized}}/routes.cljs" (render "src/cljs/reagent/routes.cljs" data)]
                      ["src/cljs/{{sanitized}}/subs.cljs" (render "src/cljs/reagent/subs.cljs" data)]
                      ["src/cljs/{{sanitized}}/views.cljs" (render "src/cljs/reagent/views.cljs" data)])
                files)]
    files))

(defn jco-re-frame
  "Leiningen template to generate re-frame projects."
  [name & opts]
  (if-let [error (validate-opts opts)]
    (println error)
    (let [data (template-data name opts)]
      (main/info "Generating fresh 'lein new' re-frame (jco style) project.")
      (apply ->files data (template-files data opts)))))
