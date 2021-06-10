package com.dims.incidentmapper.screens

import android.app.AlertDialog
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.dims.incidentmapper.R
import com.dims.incidentmapper.databinding.FragmentIncidentsBinding
import com.dims.incidentmapper.databinding.ReportIncidentDialogBinding
import com.dims.incidentmapper.databinding.WelcomeDialogBinding
import com.dims.incidentmapper.utils.IncidentType
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import java.util.*


@AndroidEntryPoint
class IncidentsFragment : Fragment() {

    lateinit var binding: FragmentIncidentsBinding
    lateinit var typeAdapter: ArrayAdapter<String>
    private val vm: IncidentsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        typeAdapter = ArrayAdapter(
            requireContext(),
            R.layout.support_simple_spinner_dropdown_item,
            resources.getStringArray(
                R.array.type_list
            )
        )
        binding = FragmentIncidentsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(vm.callback)
        showWelcomeDialog()

        vm.mapClickLiveData.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let { latLng ->
                var title = ""
                val dialogBinding = ReportIncidentDialogBinding.inflate(layoutInflater)
                val dialog = MaterialAlertDialogBuilder(requireContext())
                    .setView(dialogBinding.root)
                    .setCancelable(false)
                    .show()
                dialogBinding.typeAutoCompleteTextView.setAdapter(typeAdapter)
                dialogBinding.typeAutoCompleteTextView.onItemClickListener =
                    AdapterView.OnItemClickListener { parent, _, position, _ ->
                        title = parent?.getItemAtPosition(position) as String? ?: ""
                        println("Selection $title")
                    }
                dialogBinding.addButton.setOnClickListener {
                    if (title.isNotEmpty()) {
                        val type = IncidentType.valueOf(
                            title.trim()
                                .replace(" ", "_")
                                .toUpperCase(Locale.ENGLISH)
                        )
                        vm.addIncident(
                            type,
                            dialogBinding.descriptionEdittext.text.toString(),
                            latLng
                        )
                        dialog.dismiss()
                    }
                }
                dialogBinding.cancelButton.setOnClickListener { dialog.dismiss() }
            }
        }
    }

    private fun showWelcomeDialog() {
        val sharedPreferences: SharedPreferences = requireContext().getSharedPreferences("welcomeDialog", MODE_PRIVATE)
        val value = sharedPreferences.getInt("welcomeMessage", 1)
        if (value == 1) {
            val dialogBinding = WelcomeDialogBinding.inflate(layoutInflater)
            val dialog = MaterialAlertDialogBuilder(requireContext())
                .setView(dialogBinding.root)
                .setCancelable(false)
                .show()
            dialogBinding.okButton.setOnClickListener {
                dialog.cancel()
                sharedPreferences.edit().putInt("welcomeMessage", 0).apply()
            }
        }
    }
}