(ns speaker-notes.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.middleware.defaults :refer [site-defaults wrap-defaults]]
            [ring.middleware.json :refer [wrap-json-body wrap-json-response]]
            [ring.util.response :refer [response]]
            [speaker-notes.corrections :as corrections]
            [speaker-notes.llm.core :refer [llm-client]]))

(defroutes app-routes
  (GET "/" [] "Hello World")

  (POST "/suggest-correction" {:keys [body] :as _request}
    (let [result (corrections/suggest-correction llm-client (:notes body))]
      (response result)))
  (route/not-found "Not Found"))

(def app
  (-> app-routes
      #_(wrap-defaults site-defaults)
      (wrap-json-response)
      (wrap-json-body {:keywords? true})))
