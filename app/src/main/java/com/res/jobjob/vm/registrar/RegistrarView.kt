package com.res.jobjob.vm.registrar

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.res.jobjob.R
import com.res.jobjob.data.api.AuthUser
import com.res.jobjob.data.api.db.ClienteDB
import com.res.jobjob.data.api.db.ControllerDB
import com.res.jobjob.data.api.db.DBOficios
import com.res.jobjob.data.api.db.SocioDB
import com.res.jobjob.data.data.Usuario

class RegistrarView : ViewModel() {

    private val dbOficios by lazy { DBOficios() }
    private val authUser: AuthUser = AuthUser()
    private val successRegisterUser: MutableLiveData<Boolean> = MutableLiveData()
    val liveData: LiveData<Boolean> get() = successRegisterUser
    private lateinit var dbController: ControllerDB
    private lateinit var view: View
    private lateinit var tipo: String
    var oficio: String = ""

    fun setData(view: View, tipo: String) {
        this.view = view
        this.tipo = tipo
    }

    fun fetchOficiosSpinner(): LiveData<ArrayList<String>> {
        val mutableData: MutableLiveData<ArrayList<String>> = MutableLiveData()
        dbOficios.getData().observeForever { listaOficios -> mutableData.value = listaOficios }
        return mutableData
    }

    fun registrarUsuario() {
        val correo: String = errorCampoVacio(view.findViewById(R.id.correo_textLayout), view.findViewById(R.id.correo_textEdit))
        val password: String = errorCampoVacio(view.findViewById(R.id.password_textLayout), view.findViewById(R.id.password_textEdit))
        if (correo.isNotEmpty() && password.isNotEmpty()) {
            authUser.registrar(correo, password).observeForever { if (it) addDB(correo) }
        }
        else successRegisterUser.value = false
    }

    private fun addDB(correo: String) {
        val nombre: String = errorCampoVacio(view.findViewById(R.id.nombre_textLayout), view.findViewById(R.id.nombre_textEdit))
        val telefono: String = errorCampoVacio(view.findViewById(R.id.telefono_textLayout), view.findViewById(R.id.telefono_textEdit))
        if (nombre.isNotEmpty() && telefono.isNotEmpty() && correo.isNotEmpty()) {
           val usuario: Usuario = when (tipo) {
                "Socio" -> {
                    dbController = SocioDB()
                    Usuario.Socio(authUser.getUID(), nombre, correo, telefono, oficio)
                } else -> {
                    dbController = ClienteDB()
                    Usuario.Cliente(authUser.getUID(), nombre, correo, telefono)
                }
            }
            dbController.add(usuario = usuario).observeForever { successRegisterUser.value = it }
        }
    }

    private fun errorCampoVacio(textLayout: TextInputLayout, textEdit: TextInputEditText): String {
        val data: String = textEdit.text.toString()
        if (data.isEmpty()) {
            successRegisterUser.value = false
            textLayout.error = "Campo Vacio"
        }
        return data
    }
}