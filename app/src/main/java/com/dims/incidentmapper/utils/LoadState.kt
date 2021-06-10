package com.dims.incidentmapper.utils

sealed class LoadState
object Idle : LoadState()
object Loading : LoadState()
data class Loaded<T>(val data: T) : LoadState()
data class LoadError(val message: String) : LoadState()
