(ns speaker-notes.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
            [speaker-notes.llm.core :refer [llm-client]]
            [speaker-notes.corrections :as corrections]))

(defroutes app-routes
  (GET "/" [] "Hello World")

  (POST "/suggest-correction" [notes _context]
    (corrections/suggest-correction llm-client notes))
  (route/not-found "Not Found"))

(def app
  (wrap-defaults app-routes site-defaults))
