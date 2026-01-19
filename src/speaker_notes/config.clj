(ns speaker-notes.config
  (:require [clojure.java.io :as io])
  (:import (io.github.cdimascio.dotenv Dotenv)))

(defonce dotenv
         (-> (Dotenv/configure)
             (.ignoreIfMissing)
             (.load)))

(defn env
  "Read from .env if present, otherwise fall back to real env vars.
   (env \"DB_URL\")"
  [k]
  (or (.get dotenv k) (System/getenv k)))


(defn load-resource [filename]
  (if-let [res (io/resource filename)]
    (slurp res)
    (throw (Exception. (str "File not found: " filename)))))
