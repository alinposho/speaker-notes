(ns gemini-sample
  (:require [speaker-notes.llm.providers.gemini :as gemini]
            [speaker-notes.llm.api :as llm-api]))


(comment

 (def client (gemini/make-example-gemini-vertexai {}))

 (llm-api/run-inference
  client
  "You are a conscientious research analyst"
  "Please find me the board members of the Microsoft company. Return a JSON list with objects where each object is a
  board member's information: name, role, etc.")


 )
