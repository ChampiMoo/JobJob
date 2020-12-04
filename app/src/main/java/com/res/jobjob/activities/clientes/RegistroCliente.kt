package com.res.jobjob.activities.clientes

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.res.jobjob.R
import com.res.jobjob.includes.MyToolbar
import com.res.jobjob.modelos.Cliente
import com.res.jobjob.providers.AuthProvider
import com.res.jobjob.providers.ClienteProvider
import dmax.dialog.SpotsDialog

class RegistroCliente : AppCompatActivity() {

    private var mAuthProvider: AuthProvider? = null
    private var mClienteProvider: ClienteProvider? = null
    private var mDialog: AlertDialog? = null
    private lateinit var btnRegistrar: Button
    private var mTextInputNombre: TextInputEditText? = null
    private var mTextInputCorreo: TextInputEditText? = null
    private var mTextInputPass: TextInputEditText? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro_cliente)
        mAuthProvider = AuthProvider()
        mClienteProvider = ClienteProvider()
        MyToolbar.show(this, "Registro cliente", true)
        mDialog = SpotsDialog.Builder().setContext(this@RegistroCliente).setMessage("Espere un momento").build()
        mTextInputNombre = findViewById(R.id.textInputNombreS)
        mTextInputCorreo = findViewById(R.id.textInputCorreoS)
        mTextInputPass = findViewById(R.id.textInputPassS)
        btnRegistrar = findViewById(R.id.btnRegistrarS)
        btnRegistrar.setOnClickListener { clickRegister() }
    }

    private fun clickRegister() {
        val nombre = mTextInputNombre!!.text.toString()
        val correo = mTextInputCorreo!!.text.toString()
        val password = mTextInputPass!!.text.toString()
        if (nombre.isNotEmpty() && correo.isNotEmpty() && password.isNotEmpty()) {
            if (password.length >= 6) {
                mDialog!!.show()
                register(nombre, correo, password)
            } else {
                Toast.makeText(this@RegistroCliente, "La contraseña debe tener al menos 6 caracteres", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this@RegistroCliente, "Ingrese todos los campos", Toast.LENGTH_SHORT).show()
        }
    }

    private fun register(nombre: String, correo: String, password: String?) {
        mAuthProvider!!.registrar(correo, password).addOnCompleteListener { task ->
            mDialog!!.hide()
            if (task.isSuccessful) {
                val id = FirebaseAuth.getInstance().currentUser!!.uid
                val cliente = Cliente(id, nombre, correo)
                create(cliente)
            } else {
                Toast.makeText(this@RegistroCliente, "No se pudo crear el usuario", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun create(cliente: Cliente) {
        mClienteProvider!!.create(cliente).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val intent = Intent(this@RegistroCliente, MapCliente::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(intent)
            } else {
                Toast.makeText(this@RegistroCliente, "No se pudo crear el cliente", Toast.LENGTH_SHORT).show()
            }
        }
    }
}