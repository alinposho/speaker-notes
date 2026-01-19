(ns speaker-notes.llm.providers.gemini
  "Wrapper/Helper code for the GCP Gemini Vertex AI API LLM models"
  (:require [clojure.tools.logging :as log]
            [speaker-notes.config :as config]
            [speaker-notes.llm.api :as api]
            [speaker-notes.llm.providers.core :as providers.core])
  (:import (com.google.genai Client)
           (com.google.genai.types ThinkingConfig Tool)
           (com.google.genai.types GenerateContentConfig GenerateContentResponseUsageMetadata GoogleSearch HttpOptions ThinkingConfig Tool)))


(def gemini-3-pro-preview "gemini-3-pro-preview")

(def reasoning-effort->thinking-budget
  {:low    {gemini-3-pro-preview 16384}
   :medium {gemini-3-pro-preview 24576}
   :high   {gemini-3-pro-preview 32768}})

(defn- get-thinking-budget ^long [^String model-name reasoning-effort]
  (long (get-in reasoning-effort->thinking-budget [reasoning-effort model-name] 0)))

(defn- run-inference-unsafe [wrapper system-prompt user-prompt]
  (let [{:keys [client model-id max-output-tokens temperature tool ^ThinkingConfig thinking-config]} wrapper
        prompt (str system-prompt "\n" user-prompt)
        cfg-builder (-> (GenerateContentConfig/builder)
                        (.temperature (float temperature))
                        (.maxOutputTokens (int max-output-tokens))
                        (.thinkingConfig thinking-config)
                        (.responseMimeType "text/plain"))
        cfg (cond-> cfg-builder
                    tool (.tools (into-array Tool [tool]))
                    :always (.build))
        resp (.generateContent (.-models client) ^String model-id ^String prompt ^GenerateContentConfig cfg)
        result (providers.core/extract-response (.text resp))

        usage-opt (.usageMetadata resp)
        ^GenerateContentResponseUsageMetadata usage (.orElse usage-opt nil)

        input-tokens (some-> usage (.promptTokenCount) (.orElse nil))
        input-cached-tokens (some-> usage (.cachedContentTokenCount) (.orElse nil))
        output-tokens (some-> usage (.candidatesTokenCount) (.orElse nil))]

    (when-not result
      (log/error (format "JSON parsing failed for model %s. Raw content: %s..."
                         model-id
                         (subs (.text resp) 0 (min 500 (count (.text resp)))))))

    {:result              result
     :input-tokens        input-tokens
     :input-cached-tokens input-cached-tokens
     :output-tokens       output-tokens
     :system-prompt       system-prompt
     :user-prompt         user-prompt
     :usage-opts          usage-opt}))

(defrecord ExampleGeminiVertexAI
  [^Client client
   ^String model-id
   ^long max-output-tokens
   ^double temperature
   reasoning-effort
   ^Tool tool
   ^ThinkingConfig thinking-config]

  api/LLM
  (provider-name [_this] "GeminiVertexAI")
  (get-model-id [_] model-id)

  (run-inference [this system-prompt user-prompt]
   ;; TODO: Add retries and consider handling exceptions
    (run-inference-unsafe this system-prompt user-prompt)))

(defn make-example-gemini-vertexai
  [{:keys [client
           model-id
           max-output-tokens
           temperature
           reasoning-effort
           tools
           timeout-seconds
           api-key
           location]
    :or   {client            nil
           model-id          gemini-3-pro-preview
           max-output-tokens 65536
           temperature       (float 0.0)
           reasoning-effort  :medium
           tools             nil
           timeout-seconds   300
           api-key           (config/env "VERTEX_AI_API_KEY")
           location          "global"}
    :as   args}]

  (log/info (str "Initializing ExampleGeminiVertexAI LLM client wrapper with: args" args))

  (let [tool (when (and tools (some #{"web_search"} tools))
               (-> (Tool/builder)
                   (.googleSearch (GoogleSearch/builder))
                   (.build)))

        thinking-budget (get-thinking-budget model-id reasoning-effort)
        thinking-config (when (pos? thinking-budget)
                          (log/info (format "Setting thinking budget for model-id=%s with %s effort to: %d tokens."
                                            model-id (name reasoning-effort) thinking-budget))
                          (-> (ThinkingConfig/builder)
                              (.thinkingBudget (int thinking-budget))
                              (.build)))

        http-options (-> (HttpOptions/builder)
                         (.timeout (int (* timeout-seconds 1000)))
                         (.build))
        client (or client
                   (-> (Client/builder)
                       (.vertexAI true)
                       (.apiKey api-key)
                       (.httpOptions http-options)
                       (.build)))]

    (->ExampleGeminiVertexAI client model-id max-output-tokens temperature reasoning-effort tool thinking-config)))
