package com.res.jobjob.ui.opcion

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.res.jobjob.R
import com.res.jobjob.ui.includes.Toolbar
import kotlinx.android.synthetic.main.fragment_opcion_inicio.*
import kotlinx.android.synthetic.main.toolbar.*

class OpcionInicioFragment: Fragment() {

    private lateinit var nvController: NavController

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_opcion_inicio, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        nvController = Navigation.findNavController(view)
        Toolbar.show((activity as AppCompatActivity), toolbar, nvController)
        Toolbar.addBackIcon((activity as AppCompatActivity).resources, nvController, toolbar)
        registrar_button.setOnClickListener { nvController.navigate(R.id.registrarFragment) }
        iniciar_button.setOnClickListener { nvController.navigate(R.id.loginFragment) }
    }
}