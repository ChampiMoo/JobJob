package com.res.jobjob.ui.login

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.res.jobjob.vm.login.LoginView
import dmax.dialog.SpotsDialog
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.toolbar.*

class LoginFragment : Fragment() {

    private val viewModel: LoginView by lazy { ViewModelProviders.of(this)[LoginView::class.java] }
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var nvController: NavController
    private lateinit var dialog: AlertDialog

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.setData(view, getTypeUser())
        nvController = Navigation.findNavController(view)
        dialog = SpotsDialog.Builder().setContext(requireContext()).setMessage("Iniciando seccion").build()
        Toolbar.show((activity as AppCompatActivity), toolbar, nvController)
        Toolbar.addBackIcon((activity as AppCompatActivity).resources, nvController, toolbar)
        iniciar_button.setOnClickListener {
            dialog.show()
            viewModel.iniciar()
        }
        cancel_button.setOnClickListener { nvController.navigateUp() }
        viewModel.liveData.observe(requireActivity(), { if (it) cambiarActivity() else error() })
    }

    private fun error() {
        dialog.dismiss()
        Toast.makeText(requireContext(), "Error al iniciar seccion", Toast.LENGTH_SHORT).show()
    }

    private fun getTypeUser(): String {
        sharedPreferences = requireContext().getSharedPreferences("tipo", Context.MODE_PRIVATE)
        return sharedPreferences.getString("usuario", "").toString()
    }

    private fun cambiarActivity() {
        dialog.dismiss()
        val intent = when (getTypeUser()) {
            "Socio" -> Intent(context, SocioActivity::class.java)
            else -> Intent(context, ClienteActivity::class.java)
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
    }
}