(ns speaker-notes.llm.core
  (:require [mount.core :as mount]
            [speaker-notes.llm.providers.gemini :as gemini]))

(mount/defstate llm-client
                :start (gemini/make-example-gemini-vertexai {}))
