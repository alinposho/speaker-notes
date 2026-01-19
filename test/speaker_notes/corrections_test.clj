(ns speaker-notes.corrections-test
  (:require [clojure.test :refer :all]
            [mount.core :as mount]
            [speaker-notes.config :as config]
            [speaker-notes.corrections :as corrections]
            [speaker-notes.llm.core :refer [llm-client]]))

(defn setup-llm-client [f]
  (mount/start #'speaker-notes.llm.core/llm-client)
  (f)
  (mount/stop #'speaker-notes.llm.core/llm-client))

(use-fixtures :each setup-llm-client)

(deftest suggest-correction-test
  (testing "suggest-correction should ask the LLM to inspect the text and return improvement suggestions"
    (let [;; When
          result (corrections/suggest-correction
                  llm-client
                  (config/load-resource "speaker_notes/notes_samples/bicycle_revolution.txt"))

          maybe-issue (some-> result
                              :issues
                              first)]

      ;; Then
      (is (some? (:issues result)))
      (is (< 0 (count (:issues result))))
      (when maybe-issue
        (is (= #{:fragment :description :suggestion} (set(keys maybe-issue))))))))
