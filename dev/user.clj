(ns user
  (:require
   [cheshire.core :as json]
   [clj-http.client :as http]
   [clojure.repl :refer :all]
   [clojure.tools.namespace.repl :as tn :refer [refresh]]
   [mount.core :as mount]
   [speaker-notes.config :as config]
   [speaker-notes.core]))


(tn/set-refresh-dirs "dev" "src")

(defn start []
  (mount/start))

(defn stop []
  (mount/stop))

(defn restart []
  (stop)
  (refresh :after 'user/start))

(comment

 (start)
 (stop)
 (restart)


 (def res (time
           (let [body-str (json/generate-string {:notes (config/load-resource "speaker_notes/notes_samples/bicycle_revolution.txt")})]
             (http/post "http://localhost:3000/suggest-correction"
                        {:content-type     :json
                         :body             body-str
                         :throw-exceptions false}))))
 res

 (-> res
     :body
     (json/parse-string true)))
