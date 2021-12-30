package br.com.gmfonseca.datastoresample.infra.utils

import kotlinx.serialization.json.Json

val json = Json {
    encodeDefaults = true
    ignoreUnknownKeys = true
}
