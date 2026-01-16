(ns speaker-notes.config
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
