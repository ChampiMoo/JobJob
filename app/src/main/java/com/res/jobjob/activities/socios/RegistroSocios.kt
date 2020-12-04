package com.res.jobjob.activities.socios

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
import com.res.jobjob.modelos.Socio
import com.res.jobjob.providers.AuthProvider
import com.res.jobjob.providers.SocioProvider
import dmax.dialog.SpotsDialog

class RegistroSocios : AppCompatActivity() {

    private var mAuthProvider: AuthProvider? = null
    private var mSocioProvider: SocioProvider? = null
    private lateinit var btnRegistrarS: Button
    private var mTextInputNombreS: TextInputEditText? = null
    private var mTextInputCorreoS: TextInputEditText? = null
    private var mTextInputPassS: TextInputEditText? = null
    private var mTextInputOficioS: TextInputEditText? = null
    private var mTextInputTelS: TextInputEditText? = null
    private var mDialog: AlertDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro_socios)
        mAuthProvider = AuthProvider()
        mSocioProvider = SocioProvider()
        MyToolbar.show(this, "Registro cliente", true)
        mDialog = SpotsDialog.Builder().setContext(this@RegistroSocios).setMessage("Espere un momento").build()
        mTextInputOficioS = findViewById(R.id.textInputOficioS)
        mTextInputTelS = findViewById(R.id.textInputTelS)
        mTextInputNombreS = findViewById(R.id.textInputNombreS)
        mTextInputCorreoS = findViewById(R.id.textInputCorreoS)
        mTextInputPassS = findViewById(R.id.textInputPassS)
        btnRegistrarS = findViewById(R.id.btnRegistrarS)
        btnRegistrarS.setOnClickListener { clickRegister() }
    }

    private fun clickRegister() {
        val nombre = mTextInputNombreS!!.text.toString()
        val correo = mTextInputCorreoS!!.text.toString()
        val password = mTextInputPassS!!.text.toString()
        val oficio = mTextInputOficioS!!.text.toString()
        val tel = mTextInputTelS!!.text.toString()
        if (nombre.isNotEmpty() && correo.isNotEmpty() && password.isNotEmpty() && oficio.isNotEmpty() && tel.isNotEmpty()) {
            if (password.length >= 6) {
                mDialog!!.show()
                register(nombre, correo, password, oficio, tel)
            } else {
                Toast.makeText(this@RegistroSocios, "La contraseña debe tener al menos 6 caracteres", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this@RegistroSocios, "Ingrese todos los campos", Toast.LENGTH_SHORT).show()
        }
    }

    private fun register(nombre: String, correo: String, password: String?, oficio: String, tel: String) {
        mAuthProvider!!.registrar(correo, password).addOnCompleteListener { task ->
            mDialog!!.hide()
            if (task.isSuccessful) {
                val id = FirebaseAuth.getInstance().currentUser!!.uid
                val socio = Socio(id, nombre, correo, oficio, tel)
                create(socio)
            } else {
                Toast.makeText(this@RegistroSocios, "No se pudo crear el usuario", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun create(socio: Socio) {
        mSocioProvider!!.create(socio).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val intent = Intent(this@RegistroSocios, MapSocio::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(intent)

                //Toast.makeText(RegistroSocios.this, "El registro se realizo exitosamente", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this@RegistroSocios, "No se pudo crear el cliente", Toast.LENGTH_SHORT).show()
            }
        }
    }
}