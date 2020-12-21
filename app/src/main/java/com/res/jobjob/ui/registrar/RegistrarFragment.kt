package com.res.jobjob.ui.registrar

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.res.jobjob.R
import com.res.jobjob.ui.activitys.ClienteActivity
import com.res.jobjob.ui.activitys.SocioActivity
import com.res.jobjob.ui.includes.Toolbar
import com.res.jobjob.vm.registrar.RegistrarView
import dmax.dialog.SpotsDialog
import kotlinx.android.synthetic.main.fragment_registrar.*
import kotlinx.android.synthetic.main.toolbar.*

class RegistrarFragment : Fragment() {

    private val viewModel: RegistrarView by lazy { ViewModelProviders.of(this)[RegistrarView::class.java] }
    private val adapter: ArrayAdapter<String> by lazy { ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_item) }
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var dialog: android.app.AlertDialog
    private lateinit var nvController: NavController

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_registrar, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        nvController = Navigation.findNavController(view)
        viewModel.setData(view, getUserType())
        Toolbar.show((activity as AppCompatActivity), toolbar, nvController)
        Toolbar.addBackIcon((activity as AppCompatActivity).resources, nvController, toolbar)
        dialog = SpotsDialog.Builder().setContext(requireContext()).setMessage("Registrando usuario").build()
        updateUI()
        registrar_button.setOnClickListener {
            dialog.show()
            viewModel.registrarUsuario()
        }
        cancel_button.setOnClickListener { nvController.navigateUp() }
        oficio_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                viewModel.oficio = parent!!.getItemAtPosition(position).toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) { }

        }
        viewModel.liveData.observe(requireActivity(), {
            if (it) success()
            else {
                dialog.dismiss()
                error()
            }
        })
    }

    private fun success() {
        dialog.dismiss()
        val intent = when (getUserType()) {
            "Socio" -> Intent(requireContext(), SocioActivity::class.java)
            else -> Intent(requireContext(), ClienteActivity::class.java)
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
    }

    private fun error() { Toast.makeText(requireContext(), "Error al registrar usuario", Toast.LENGTH_SHORT).show() }

    private fun updateUI() {
        when (getUserType()) {
            "Socio" -> { spinnerDatos() }
            "Cliente" -> { oficio_linear.visibility = LinearLayout.GONE }
        }
    }

    private fun getUserType(): String {
        sharedPreferences = requireContext().getSharedPreferences("tipo", Context.MODE_PRIVATE)
        return sharedPreferences.getString("usuario", "").toString()
    }

    private fun spinnerDatos() {
        viewModel.fetchOficiosSpinner().observe(requireActivity(), {
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            adapter.addAll(it)
            oficio_spinner.adapter = adapter
        })
    }
}