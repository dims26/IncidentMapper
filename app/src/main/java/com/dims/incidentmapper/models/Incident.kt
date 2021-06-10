package com.dims.incidentmapper.models

import com.dims.incidentmapper.utils.IncidentType

data class Incident(val id: String,
                    val lat: Double,
                    val lng: Double,
                    val type: IncidentType,
                    val description: String)