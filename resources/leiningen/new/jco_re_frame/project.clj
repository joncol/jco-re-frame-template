(defproject {{name}} "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[compojure "1.6.1"]
                 [hiccup "1.0.5"]
                 [org.clojure/clojure "1.9.0"]
                 [org.clojure/clojurescript "1.10.238" :scope "provided"]
                 [reagent "0.8.0"]
                 [reagent-utils "0.3.1"]
                 [ring "1.6.3"]
                 [ring-server "0.5.0"]
                 [ring/ring-defaults "0.3.1"]
                 [secretary "1.2.3"]
                 [venantius/accountant "0.2.4"
                  :exclusions [org.clojure/tools.reader]]
                 [yogthos/config "1.1.1"]]
  :plugins [[lein-asset-minifier "0.2.7" :exclusions [org.clojure/clojure]]
            [lein-cljsbuild "1.1.7"]
            [lein-environ "1.1.0"]]
  :ring {:handler      {{project-ns}}.handler/app
         :uberwar-name "{{name}}.war"}
  :min-lein-version "2.5.0"
  :uberjar-name "{{name}}.jar"
  :main {{project-ns}}.server
  {{#if-java-version-9?}}
  :jvm-opts ["--add-modules" "java.xml.bind"]
  {{/if-java-version-9?}}
  :clean-targets ^{:protect false}
  [:target-path
   [:cljsbuild :builds :app :compiler :output-dir]
   [:cljsbuild :builds :app :compiler :output-to]]

  :source-paths ["src/clj" "src/cljc"]
  {{#if-spec?}}
  :test-paths ["spec/clj"]
  {{/if-spec?}}
  :resource-paths ["resources" "target/cljsbuild"]

  :minify-assets
  {:assets
   {"resources/public/css/site.min.css" "resources/public/css/site.css"}}

  :cljsbuild
  {:builds {:min
            {:source-paths ["src/cljs" "src/cljc" "env/prod/cljs"]
             :compiler
             {:output-to     "target/cljsbuild/public/js/app.js"
              :output-dir    "target/cljsbuild/public/js"
              :source-map    "target/cljsbuild/public/js/app.js.map"
              :optimizations :advanced
              :pretty-print  false}}
            :app
            {:source-paths ["src/cljs" "src/cljc" "env/dev/cljs"]
             :figwheel     {:on-jsload "{{project-ns}}.core/mount-root"}
             :compiler
             {:main          "{{name}}.dev"
              :asset-path    "/js/out"
              :output-to     "target/cljsbuild/public/js/app.js"
              :output-dir    "target/cljsbuild/public/js/out"
              :source-map    true
              :optimizations :none
              :pretty-print  true}}
            {{#if-test?}}
            :test
            {:source-paths ["src/cljs" "src/cljc" "test/cljs"]
             :compiler {:main          {{project-ns}}.doo-runner
                        :asset-path    "/js/out"
                        :output-to     "target/test.js"
                        :output-dir    "target/cljstest/public/js/out"
                        :optimizations :whitespace
                        :pretty-print  true}}{{/if-test?}}{{#if-spec?}}
            :test
            {:source-paths ["src/cljs" "src/cljc" "spec/cljs"]
             :compiler     {:output-to     "target/test.js"
                            :optimizations :whitespace
                            :pretty-print  true}}{{/if-spec?}}}{{#if-spec?}}
   :test-commands {"unit" ["phantomjs" "runners/speclj" "target/test.js"]}{{/if-spec?}}}
  {{#if-test?}}
  :doo {:build "test"
        :alias {:default [:chrome]}}
  {{/if-test?}}
  {{#if-spec?}}
  :doo {:build "test"}
  {{/if-spec?}}

  :figwheel
  {:http-server-root "public"
   :server-port      3449
   :nrepl-port       7002
   :nrepl-middleware ["cemerick.piggieback/wrap-cljs-repl"
                      {{#if-cider?}}
                      "cider.nrepl/cider-middleware"
                      "refactor-nrepl.middleware/wrap-refactor"{{/if-cider?}}]
   :css-dirs         ["resources/public/css"]
   :ring-handler     {{project-ns}}.handler/app}

  {{#if-less?}}
  :less {:source-paths ["src/less"]
         :target-path  "resources/public/css"}
  {{/if-less?}}
  {{#if-sass?}}
  :sass {:source-paths ["src/sass"]
         :target-path  "resources/public/css"}
  {{/if-sass?}}

  :profiles {:dev {:repl-options {:init-ns {{project-ns}}.repl
                                  :nrepl-middleware [cemerick.piggieback/wrap-cljs-repl]}

                   :dependencies [[binaryage/devtools "0.9.10"]
                                  [ring/ring-mock "0.3.2"]
                                  [ring/ring-devel "1.6.3"]
                                  [prone "1.5.2"]
                                  [figwheel-sidecar "0.5.15"]
                                  [org.clojure/tools.nrepl "0.2.13"]
                                  [com.cemerick/piggieback "0.2.2"]
                                  {{#if-spec?}}
                                  [speclj "3.3.2"]
                                  {{/if-spec?}}
                                  [pjstadig/humane-test-output "0.8.3"]{{#if-less?}}
                                  ;; To silence warnings from less4clj dependecies about missing logger implementation
                                  [org.slf4j/slf4j-nop "1.7.25"]{{/if-less?}}{{#if-sass?}}
                                  ;; To silence warnings from sass4clj dependecies about missing logger implementation
                                  [org.slf4j/slf4j-nop "1.7.25"]{{/if-sass?}}]

                   :source-paths ["env/dev/clj"]
                   :plugins [[lein-figwheel "0.5.15"]
                             {{#if-test?}}
                             [lein-doo "0.1.10"]
                             {{/if-test?}}
                             {{#if-spec?}}
                             [speclj "3.3.2"]
                             {{/if-spec?}}
                             {{#if-cider?}}
                             [cider/cider-nrepl "0.15.1"]
                             [org.clojure/tools.namespace "0.3.0-alpha4"
                              :exclusions [org.clojure/tools.reader]]
                             [refactor-nrepl "2.3.1"
                              :exclusions [org.clojure/clojure]]{{/if-cider?}}{{#if-less?}}
                             [deraen/lein-less4j "0.6.2"]{{/if-less?}}{{#if-sass?}}
                             [deraen/lein-sass4clj "0.3.1"]{{/if-sass?}}]

                   :injections [(require 'pjstadig.humane-test-output)
                                (pjstadig.humane-test-output/activate!)]

                   :env {:dev true}}

             :uberjar {:hooks [minify-assets.plugin/hooks]
                       :source-paths ["env/prod/clj"]
                       :prep-tasks ["compile" ["cljsbuild" "once" "min"]]
                       :env {:production true}
                       :aot :all
                       :omit-source true}})
