(ns gemini-sample
  (:require [mount.core :as mount]
            [speaker-notes.config :as config]
            [speaker-notes.corrections :as corrections]
            [speaker-notes.llm.api :as llm-api]
            [speaker-notes.llm.core :refer [llm-client]]
            [speaker-notes.llm.providers.gemini :as gemini]))


(comment

 (require '[speaker-notes.config :as config])
 (config/env "VERTEX_AI_API_KEY")


 (def client (gemini/make-example-gemini-vertexai {}))

 (def res (time
           (llm-api/run-inference
            client
            "You are a conscientious research analyst"
            "Please find me the board members of the Microsoft company. Return a JSON list with objects where each object is a
            board member's information: name, role, etc.")))

 res
 (keys (:input-cached-tokens res))

 (def uo (:usage-opts res))
 (some-> uo
         (.orElse nil)
         (.promptTokenCount)
         (.orElse nil))


 ;;;; Test the speaker notes checker
 (mount/start #'speaker-notes.llm.core/llm-client)
 (corrections/suggest-correction llm-client (config/load-resource "speaker_notes/notes_samples/bicycle_revolution.txt"))
 )
