(ns speaker-notes.llm.api)


(defprotocol LLM
  (provider-name [this])
  (get-model-id [this])
  (run-inference [this system-prompt user-prompt]))
