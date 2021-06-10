package com.dims.incidentmapper.repositories

import com.dims.incidentmapper.models.Incident
import com.dims.incidentmapper.utils.IncidentType
import com.dims.incidentmapper.utils.LoadError
import com.dims.incidentmapper.utils.LoadState
import com.dims.incidentmapper.utils.Loaded
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.flow.MutableStateFlow
import java.util.*
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class IncidentRepository @Inject constructor(private val firestore: FirebaseFirestore) {

    suspend fun getIncidents() : LoadState = suspendCoroutine { continuation ->
        firestore.collection("incidents")
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val incidents = task.result?.documents?.map { doc ->
                        Incident(
                            id = doc.getString("id")!!,
                            lat = doc.getDouble("lat")!!,
                            lng = doc.getDouble("lng")!!,
                            type = IncidentType.valueOf(doc.getString("type")!!.toUpperCase(Locale.ENGLISH)),
                            description = doc.getString("description")!!
                        )
                    }
                    continuation.resume(Loaded(incidents ?: emptyList()))
                } else continuation.resume(LoadError(task.exception?.message ?: "Incident load failed"))
            }
    }

    fun monitorIncidents(loadFlow: MutableStateFlow<LoadState>) {
        firestore.collection("incidents")
            .addSnapshotListener { value, error ->
                if (error != null) {
                    loadFlow.value = LoadError("Failed to load data")
                    return@addSnapshotListener
                }

                val incidents = value?.documents?.map { doc ->
                    Incident(
                        id = doc.getString("id")!!,
                        lat = doc.getDouble("lat")!!,
                        lng = doc.getDouble("lng")!!,
                        type = IncidentType.valueOf(doc.getString("type")!!.toUpperCase(Locale.ENGLISH)),
                        description = doc.getString("description")!!
                    )
                }
                loadFlow.value = Loaded(incidents ?: emptyList())
            }
    }

    suspend fun addIncident(type: IncidentType,
                            description: String,
                            latLng: LatLng): Boolean = suspendCoroutine { continuation ->
        val id = UUID.randomUUID().toString()
        val map = hashMapOf<String, Any>()
        map["id"] = id
        map["lat"] = latLng.latitude
        map["lng"] = latLng.longitude
        map["type"] = type
        map["description"] = description

        firestore.collection("incidents")
            .document(id)
            .set(map)
            .addOnCompleteListener { continuation.resume(it.isSuccessful) }
    }
}