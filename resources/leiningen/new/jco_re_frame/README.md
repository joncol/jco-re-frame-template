# {{name}}

This is the {{name}} project.

## Development Mode

To start the Figwheel compiler, navigate to the project folder and run the
following command in the terminal:
```
lein figwheel
```
Figwheel will automatically push cljs changes to the browser. The server will be
available at [http://localhost:3449](http://localhost:3449) once Figwheel starts
up.

Figwheel also starts `nREPL` using the value of the `:nrepl-port` in the
`:figwheel` config found in `project.clj`. By default the port is set to `7002`.

The figwheel server can have unexpected behaviors in some situations such as
when using websockets. In this case it's recommended to run a standalone
instance of a web server as follows:
```
lein do clean, run
```

The application will now be available at
[http://localhost:3000](http://localhost:3000).

{{#sass?}}
### Style Compilation

To compile [sass](https://github.com/tuhlmann/lein-sass) sources and then watch
for changes and recompile until interrupted, run:
```
lein sass auto
```
{{/sass?}}

### Optional Development Tools

Start the browser REPL:

```
$ lein repl
```
The Jetty server can be started by running:

```clojure
(start-server)
```
and stopped by running:
```clojure
(stop-server)
```

{{#test-or-spec?}}
## Running the Tests
{{/test-or-spec?}}
{{#test?}}
To run
[cljs.test](https://github.com/clojure/clojurescript/blob/master/src/main/cljs/cljs/test.cljs)
tests, use:
```
lein doo
```
{{/test?}}
{{#spec?}}

To run [speclj](https://github.com/slagyr/speclj) tests, use:
```
lein cljsbuild test
```
{{/spec?}}
{{#test-or-spec?}}

For installation instructions of PhantomJS, please see
[this](http://phantomjs.org/download.html).
{{/test-or-spec?}}

## Building for Release

```
lein do clean, uberjar
```
