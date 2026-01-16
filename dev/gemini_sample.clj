(ns gemini-sample
  (:require [speaker-notes.llm.api :as llm-api]
            [speaker-notes.llm.providers.gemini :as gemini]))


(comment

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
         (.orElse nil)))
