(ns user
  (:require
   [clojure.repl :refer :all]
   [clojure.tools.namespace.repl :as tn :refer [refresh]]
   [mount.core :as mount]
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
 (restart))
