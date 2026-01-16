(ns speaker-notes.llm.providers.core
  (:require [cheshire.core :as json]
            [clojure.string :as str])
  (:import (com.fasterxml.jackson.core JsonParseException)
           (com.fasterxml.jackson.databind JsonMappingException)))

(def ^:private fenced-json-re
  #"(?s)```json\s*(.*?)\s*```")

(defn- extract-json-string-from-response [content]
  (let [matches (map second (re-seq fenced-json-re content))]
    (cond
      (seq matches)
      (last matches)

      (and (str/starts-with? content "{") (str/ends-with? content "}"))
      content

      (and (str/starts-with? content "[") (str/ends-with? content "]"))
      content

      :else
      (throw (ex-info "Extracted content is not valid JSON format"
                      {:raw-content content})))))

(defn extract-response
  "
  - If ```json ... ``` blocks exist, parse the *last* one.
  - Else if content looks like a bare object/array, parse it.
  - Else return nil (or throw if you prefer).
  - Returns Clojure.

  Optional opts:
  - :keywordize? (default false) -> pass true to keywordize keys."
  ([content] (extract-response content {:keywordize? true}))
  ([content {:keys [keywordize?] :or {keywordize? true}}]
   (when (some? content)
     (let [json-str (extract-json-string-from-response content)]
       (try
         (json/parse-string json-str keywordize?)
         (catch JsonParseException e
           (throw (ex-info (str "Error decoding JSON: " (.getMessage e))
                           {:raw-content content, :original-error e}
                           e)))
         (catch JsonMappingException e
           (throw (ex-info (str "Error decoding JSON: " (.getMessage e))
                           {:raw-content content, :original-error e}
                           e))))))))
