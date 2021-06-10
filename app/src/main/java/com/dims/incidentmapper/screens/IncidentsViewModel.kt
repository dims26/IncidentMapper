package com.dims.incidentmapper.screens

import android.app.Application
import android.graphics.Bitmap
import android.graphics.Canvas
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.*
import com.dims.incidentmapper.R
import com.dims.incidentmapper.models.Incident
import com.dims.incidentmapper.repositories.IncidentRepository
import com.dims.incidentmapper.utils.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class IncidentsViewModel @Inject constructor(private val incidentRepository: IncidentRepository,
                                             application: Application) : AndroidViewModel(application) {

    private val incidentFlow = MutableStateFlow<LoadState>(Idle)
    private val _mapClickLiveData: MutableLiveData<Event<LatLng>> = MutableLiveData()
    val mapClickLiveData : LiveData<Event<LatLng>> get() = _mapClickLiveData

    val callback = OnMapReadyCallback { googleMap ->
        val abuja = LatLng(9.0546462, 7.2542687)
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(abuja, 7f))
        googleMap.setOnMapClickListener { latLng -> _mapClickLiveData.postValue(Event(latLng)) }

        incidentRepository.monitorIncidents(incidentFlow)

        updateMarkers(googleMap)
    }

    fun addIncident(type: IncidentType, description: String, latLng: LatLng) {
        viewModelScope.launch {
            if (incidentRepository.addIncident(type, description, latLng))
                Toast.makeText(getApplication(), "Incident added", Toast.LENGTH_SHORT).show()
            else
                Toast.makeText(getApplication(), "Unable to add incident", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateMarkers(googleMap: GoogleMap) {
        viewModelScope.launch {
            incidentFlow.collectLatest {
                when (it) {
                    Idle -> { }
                    is LoadError -> { /*To be done*/ }
                    is Loaded<*> -> {
                        googleMap.clear()
                        @Suppress("UNCHECKED_CAST")
                        val incidents = it.data as List<Incident>
                        incidents.forEach { incident ->
                            val latLng = LatLng(incident.lat, incident.lng)
                            googleMap.addMarker(
                                MarkerOptions()
                                    .position(latLng)
                                    .title(incident.type.name)
                                    .snippet(incident.description)
                                    .icon(getMarker(incident.type))
                            )
                        }
                    }
                    Loading -> { /*To be done*/ }
                }
            }
        }
    }

    private fun getMarker(incidentType: IncidentType) =
        when(incidentType) {
            IncidentType.ACCIDENT -> BitmapDescriptorFactory.fromBitmap(bitmapFromVector(R.drawable.accident)!!)
            IncidentType.ROAD_BLOCK -> BitmapDescriptorFactory.fromBitmap(bitmapFromVector(R.drawable.road_block)!!)
            IncidentType.THEFT -> BitmapDescriptorFactory.fromBitmap(bitmapFromVector(R.drawable.theft)!!)
            IncidentType.ROAD_CONSTRUCTION -> BitmapDescriptorFactory.fromBitmap(bitmapFromVector(R.drawable.road_construction)!!)
            IncidentType.FIRE -> BitmapDescriptorFactory.fromBitmap(bitmapFromVector(R.drawable.fire)!!)
            IncidentType.OTHER -> BitmapDescriptorFactory.defaultMarker()
        }

    private fun bitmapFromVector(vectorResId: Int): Bitmap? =
        ContextCompat.getDrawable(getApplication(), vectorResId)?.run {
            setBounds(0, 0, intrinsicWidth, intrinsicHeight)
            val bitmap = Bitmap.createBitmap(intrinsicWidth, intrinsicHeight, Bitmap.Config.ARGB_8888)
            draw(Canvas(bitmap))
            bitmap
        }
}