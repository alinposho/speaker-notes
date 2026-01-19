(ns speaker-notes.corrections
  "Provides speaker notes corrections"
  (:require [clojure.tools.logging :as log]
            [selmer.parser :as selmer]
            [speaker-notes.config :as config]
            [speaker-notes.llm.api :as llm]))


(def meeting-notes-system-prompt
  (config/load-resource "llm/prompt/correct_speaker_notes_system_prompt.txt"))

(def meeting-notes-user-prompt-template
  (config/load-resource "llm/prompt/correct_speaker_notes_user_prompt.txt"))

(defn- user-template [speaker-notes]
  (selmer/render meeting-notes-user-prompt-template {:notes speaker-notes}))


(defn suggest-correction [llm-client speaker-notes]
  (let [system-prompt meeting-notes-system-prompt
        user-prompt (user-template speaker-notes)
        inference-result (llm/run-inference llm-client system-prompt user-prompt)]
    (log/info (str "Suggested corrections expended tokens"
                   (select-keys inference-result #{:input-tokens :input-cached-tokens :output-tokens})))
    (:result inference-result)))


(comment

 (user-template "Some speaker notes")
 )
