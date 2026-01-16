(ns user
  (:require
   [ring.adapter.jetty :as jetty]
   [speaker-notes.handler :as handler]
   [clojure.repl :refer :all]
   [clojure.tools.namespace.repl :as tn :refer [refresh refresh-all]]
   [reloaded.repl :as repl :refer [init clear start stop go reset reset-all system]]))


(tn/set-refresh-dirs "dev" "src")

(defonce ^:private server (atom nil))

(defn start-server
  ([] (start-server 3000))
  ([port]
   (when @server
     (println "Server already running on port" port)
     @server)
   (reset! server (jetty/run-jetty #'handler/app {:port port :join? false}))
   (println "Started server on" (str "http://localhost:" port))
   @server))

(defn stop-server []
  (when-let [s @server]
    (.stop s)
    (reset! server nil)
    (println "Stopped server"))
  :stopped)

(defn restart-server []
  (stop-server)
  (Thread/sleep 500)
  (start-server))

(defn reset-server []
  ;; Stop, reload changed namespaces, then start again
  (stop)
  (tn/refresh :after 'user/start-server))


(comment

 (start-server)

 (stop-server)
 (restart-server)

 (reset-server))
