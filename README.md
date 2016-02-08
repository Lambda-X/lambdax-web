# The LambdaX Website 

This is the project for the website of LambdaX, both client and server. It has
been completely written in Clojure(Script) and employs the following libs,
whose authors we will always be grateful to:

* [component](https://github.com/componentjs/component)
* [twitter-api](https://github.com/adamwynne/twitter-api)
* [overtone At-at](https://github.com/overtone/at-at)
* [environ](https://github.com/weavejester/environ)
* [transit](https://github.com/cognitect/transit-format)
* [clj-time](https://github.com/seancorfield/clj-time)
* [httpkit](http://www.http-kit.org/)
* [bidi](https://github.com/juxt/bidi)
* [cider-nrepl](https://github.com/clojure-emacs/cider-nrepl)
* [om.next](https://github.com/omcljs/om)
* ...and more

## BACK END

### Deploy

In order to generate the artifact, execute `lein uberjar`.

This will create `target/lambdax-web-standalone.jar` that can be launched in a script:

```

# export LAMBDAX_WEB_PORT=3000              # default for Prod is 3001
# export LAMBDAX_WEB_NREPL_PORT=45555       # default is 0, aka auto selection
# export LAMBDAX_WEB_FETCH_EVERY=           # default 360000 (in seconds)

export LAMBDAX_WEB_VERSION=0.1.0-SNAPSHOT # not shown unless specified with this variable
java -jar target/lambdax-web-standalone.jar
```

Note that the `config.clj` in the project for prod is under "env/prod/clj".

### Develop

The classic `lein repl` works but you can also use `nohup lein bg-repl` (logs to repl.err/out).

Note that the `lambdax-web.config` namespace in `env/dev/clj` contains the
default configuration and can be further customized using the above environment
variables.

## FRONT END

### Deploy

To create a production build run:

`lein cljsbuild once min`

And open your browser in `resources/public/index.html`. You will not
get live reloading, nor a REPL.

### Develop

To get an interactive development environment run:

`lein figwheel`

and open your browser at [localhost:3449](http://localhost:3449/).
This will auto compile and send all changes to the browser without the
need to reload. After the compilation process is complete, you will
get a Browser Connected REPL. An easy way to try it is:

`(js/alert "Am I connected?")`

and you should see an alert in the browser window.

## License

Copyright © 2016

Distributed under the Eclipse Public License either version 1.0 or (at your option) any later version.
