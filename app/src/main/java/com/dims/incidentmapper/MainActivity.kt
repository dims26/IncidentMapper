package com.dims.incidentmapper

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        //Transition back to regular theme
        setTheme(R.style.Theme_IncidentMapper)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}