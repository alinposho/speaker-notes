(defproject speaker-notes "0.1.0-SNAPSHOT"
  :min-lein-version "2.0.0"
  :dependencies [[org.clojure/clojure "1.12.4"]
                 [org.clojure/tools.logging "1.3.1"]
                 [cheshire "6.1.0"]
                 [ch.qos.logback/logback-classic "1.5.18"]
                 [clj-http "3.12.2"]
                 [commons-io/commons-io "2.18.0"]
                 [compojure "1.7.2"]
                 [com.google.genai/google-genai "1.35.0"]
                 [io.github.cdimascio/dotenv-java "3.0.2"]
                 [mount "0.1.18"]
                 [org.slf4j/slf4j-api "2.0.17"]
                 [ring/ring-defaults "0.7.0"]
                 [ring/ring-jetty-adapter "1.10.0"]
                 [ring/ring-json "0.5.1"]
                 [selmer "1.12.70"]]
  :plugins [[lein-ring "0.12.6"]]
  :ring {:handler speaker-notes.handler/app}
  :main speaker-notes.core
  :profiles
  {:dev {:dependencies [[javax.servlet/servlet-api "2.5"]
                        [org.clojure/tools.namespace "1.5.1"]
                        [ring/ring-devel "1.10.0"]
                        [ring/ring-mock "0.6.2"]
                        [reloaded.repl "0.2.4"]]
         :resource-paths ["resources" "test-resources"]
         :source-paths   ["src" "dev"]
         :test-paths     ["test"]}
   :uberjar {:aot :all}})
