# Architecture Notes

The application is split into backend (sources in [src](../src)) and a thin cljs frontend (to be defined and implemented).

## Backend
The Backend is a simple ring/jetty server that, for the time being, exposes only one endpoint:

```
    POST /suggest-correction
```

see [handler](../src/speaker_notes/handler.clj) that will take the speaker notes and return a list of issues:

```json
{
  "issues": [
    {
      "fragment": "string (verbatim substring from the input)",
      "description": "string (what is wrong and why, in plain language)",
      "suggestion": "string (a corrected/improved rewrite of the fragment or a specific edit instruction suitable for spoken delivery)"
    }
  ]
}
```

### State Lifecycle Management

[Clojure Mount](https://github.com/tolitius/mount) has been chosen to manage application state for its simplicity and
cross-platform support (clj + cljs).

### LLM Client
A thin wrapper for Gemini has been implemented, see [gemini.clj](../src/speaker_notes/llm/providers/gemini.clj), that
conforms to the LLM protocol define in [llm/api.clj](../src/speaker_notes/llm/api.clj).
The protocol will make it easy to swap LLM providers without having to change unrelated code.

### Config
LLM API keys are read from environment variables or the .env file that should be created based on the
[.env_template](../resources/.env.template)


## Frontend

For now the frontend is just a template, i.e. no implementation.
1
