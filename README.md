# speaker-notes

See the [AI-Powered_Speaker_Notes_Spell_Checker.pdf](docs/AI-Powered_Speaker_Notes_Spell_Checker.pdf) for more details.

## Prerequisites

You will need [Leiningen][] 2.0.0 or above installed.

[leiningen]: https://github.com/technomancy/leiningen

## Running

To start a web server for the application, run:

    lein run

## Project structure

### Backend
The backend is a simple ring server. We've defined an API/protocol for running LLM inference to abstract away the 
details of each LLM provider, in [llm.api](src/speaker_notes/llm/api.clj). For now, the only LLM provider supported is
Google Gemini, see [gemini.clj](src/speaker_notes/llm/providers/gemini.clj)


### Frontend
The cljs front end is in the [frontend](frontend) folder. For now it is just the stub: implementation to follow.
