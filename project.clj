(defproject speaker-notes "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :min-lein-version "2.0.0"
  :dependencies [[org.clojure/clojure "1.11.4"]
                 [cheshire "6.1.0"]
                 [compojure "1.7.2"]
                 [ring/ring-defaults "0.7.0"]]
  :plugins [[lein-ring "0.12.6"]]
  :ring {:handler speaker-notes.handler/app}
  :profiles
  {:dev {:dependencies [[javax.servlet/servlet-api "2.5"]
                        [org.clojure/tools.namespace "1.5.1"]
                        [ring/ring-devel "1.10.0"]
                        [ring/ring-jetty-adapter "1.10.0"]
                        [ring/ring-mock "0.6.2"]
                        [reloaded.repl "0.2.4"]]
         :resource-paths ["resources" "test-resources"]
         :source-paths   ["src" "dev"]
         :test-paths     ["test"]}})
