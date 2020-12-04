package com.res.jobjob.activities

import android.app.AlertDialog
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.res.jobjob.R
import com.res.jobjob.activities.clientes.MapCliente
import com.res.jobjob.activities.socios.MapSocio
import com.res.jobjob.includes.MyToolbar
import dmax.dialog.SpotsDialog

class Login : AppCompatActivity() {
    private var mtextInputCorreo: TextInputEditText? = null
    private var mtextInputPass: TextInputEditText? = null
    private lateinit var btnLoginL: Button
    private var mPref: SharedPreferences? = null
    private var mAuth: FirebaseAuth? = null
    private var mDatabase: DatabaseReference? = null
    private var mDialog: AlertDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        MyToolbar.show(this, "Login de usuario", true)
        mtextInputCorreo = findViewById(R.id.textInputCorreoS)
        mtextInputPass = findViewById(R.id.textInputPassS)
        mPref = applicationContext.getSharedPreferences("typeUser", MODE_PRIVATE)
        btnLoginL = findViewById(R.id.btnLoginL)
        mDialog = SpotsDialog.Builder().setContext(this@Login).setMessage("Espere un momento").build()
        mAuth = FirebaseAuth.getInstance()
        mDatabase = FirebaseDatabase.getInstance().reference
        btnLoginL.setOnClickListener{ login() }
    }

    private fun login() {
        val correo = mtextInputCorreo!!.text.toString()
        val password = mtextInputPass!!.text.toString()
        if (correo.isNotEmpty() && password.isNotEmpty()) {
            if (password.length >= 6) {
                mDialog!!.show()
                mAuth!!.signInWithEmailAndPassword(correo, password).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val user = mPref!!.getString("user", "")
                        if (user == "cliente") {
                            val intent = Intent(this@Login, MapCliente::class.java)
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                            startActivity(intent)
                        } else {
                            val intent = Intent(this@Login, MapSocio::class.java)
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                            startActivity(intent)
                        }
                    } else {
                        Toast.makeText(this@Login, "Usuario o contraseña incorrecto", Toast.LENGTH_SHORT).show()
                    }
                    mDialog!!.dismiss()
                }
            }
        }
    }
}