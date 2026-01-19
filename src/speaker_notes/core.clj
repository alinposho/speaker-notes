(ns speaker-notes.core
  (:require [mount.core :refer [defstate]]
            [ring.adapter.jetty :as jetty]
            [speaker-notes.handler :as handler]
            [speaker-notes.config :as config]
            [clojure.tools.logging :as log])
  (:gen-class))

(defstate server
  :start (let [port (Integer/parseInt (or (config/env "PORT") "3000"))]
           (log/info "Starting server on port" port)
           (jetty/run-jetty handler/app {:port port :join? false}))
  :stop (.stop server))

(defn -main [& _args]
  (log/info "Starting application")
  (mount.core/start)
  (log/info "Application started"))
