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

{{#if-less-or-sass?}}
### Style Compilation
{{/if-less-or-sass?}}
{{#if-less?}}
To compile [less](https://github.com/Deraen/less4clj) sources and then watch for
changes and recompile until interrupted, run:
```
lein less4j auto
```
{{/if-less?}}
{{#if-sass?}}
To compile [sass](https://github.com/Deraen/sass4clj) sources and then watch for
changes and recompile until interrupted, run:
```
lein sass4clj auto
```
{{/if-sass?}}

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

{{#if-test-or-spec?}}
## Running the Tests
{{/if-test-or-spec?}}
{{#if-test?}}
To run
[cljs.test](https://github.com/clojure/clojurescript/blob/master/src/main/cljs/cljs/test.cljs)
tests, use:
```
lein doo
```
{{/if-test?}}
{{#if-spec?}}

To run [speclj](https://github.com/slagyr/speclj) tests, use:
```
lein cljsbuild test
```
{{/if-spec?}}
{{#if-test-or-spec?}}

For installation instructions of PhantomJS, please see
[this](http://phantomjs.org/download.html).
{{/if-test-or-spec?}}

## Building for Release

```
lein do clean, uberjar
```
