package com.res.jobjob.ui.activitys

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.google.android.material.navigation.NavigationView
import com.res.jobjob.R
import com.res.jobjob.ui.includes.Toolbar
import kotlinx.android.synthetic.main.activity_socio.*
import kotlinx.android.synthetic.main.toolbar.*

class SocioActivity : AppCompatActivity() {

    private lateinit var nvController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_socio)
    }

    override fun onStart() {
        super.onStart()
        nvController = findNavController(R.id.nav_host_fragment)
        Toolbar.iconMenu(this, nvController, nav_view, drawer_layout, toolbar)
    }
}