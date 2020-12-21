package com.res.jobjob.vm.login

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.res.jobjob.R
import com.res.jobjob.data.api.AuthUser
import com.res.jobjob.data.api.db.ClienteDB
import com.res.jobjob.data.api.db.ControllerDB
import com.res.jobjob.data.api.db.SocioDB

class LoginView : ViewModel() {

    private val authUser: AuthUser = AuthUser()
    private val loginSuccess: MutableLiveData<Boolean> = MutableLiveData()
    val liveData: MutableLiveData<Boolean> get() = loginSuccess
    private lateinit var dbController: ControllerDB
    private lateinit var view: View
    private lateinit var tipo: String

    fun setData(view: View, tipo: String) {
        this.view = view
        this.tipo = tipo
    }

    fun iniciar() {
        val correo: String = errorCampoVacio(view.findViewById(R.id.correo_textLayout), view.findViewById(R.id.correo_textEdit))
        val password: String = errorCampoVacio(view.findViewById(R.id.password_textLayout), view.findViewById(R.id.password_textEdit))
        if (correo.isNotEmpty() && password.isNotEmpty()) {
            authUser.iniciar(correo, password).observeForever {
                dbController = when (tipo) {
                    "Socio" -> SocioDB()
                    else -> ClienteDB()
                }
                dbController.existe(authUser.getUID()).observeForever {
                    if (it) loginSuccess.value = true
                    else {
                        authUser.logout()
                        loginSuccess.value = false
                    }
                }
            }
        }
    }

    private fun errorCampoVacio(textLayout: TextInputLayout, textEdit: TextInputEditText): String {
        val data: String = textEdit.text.toString()
        if (data.isEmpty()) { textLayout.error = "Campo Vacio" }
        return data
    }
}