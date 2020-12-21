package com.res.jobjob.ui.main

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
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
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.android.synthetic.main.toolbar.*

class MainFragment: Fragment() {

    private lateinit var nvController: NavController
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(inflater: LayoutInflater,container: ViewGroup?,savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    @SuppressLint("CommitPrefEdits")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedPreferences = requireContext().getSharedPreferences("tipo", Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        nvController = Navigation.findNavController(view)
        Toolbar.show((activity as AppCompatActivity), toolbar, nvController)

        socio_button.setOnClickListener {
            editor.putString("usuario", "Socio")
            editor.apply()
            nvController.navigate(R.id.opcionInicioFragment)
        }

        cliente_button.setOnClickListener {
            editor.putString("usuario", "Cliente")
            editor.apply()
            nvController.navigate(R.id.opcionInicioFragment)
        }
    }
}