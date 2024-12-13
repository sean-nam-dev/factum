package com.devflow.factum.domain.core

sealed interface DataError: Error {
    enum class Network: Error {
        UNAVAILABLE,
        NOT_FOUND,
        INTERNAL_UNKNOWN,
        EXTERNAL_UNKNOWN,
        DOCUMENT_NULL
    }
    enum class Local: Error {
        DISK_FULL,
        IO,
        SQL,
        CACHE_EMPTY,
        UNKNOWN
    }
}