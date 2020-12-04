package com.res.jobjob.activities

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.res.jobjob.R
import com.res.jobjob.activities.clientes.RegistroCliente
import com.res.jobjob.activities.socios.RegistroSocios

class OptAuth : AppCompatActivity() {

    private lateinit var mPref: SharedPreferences
    private lateinit var mToolbar: Toolbar
    private lateinit var btnLogin: Button
    private lateinit var btnRegistrar: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_opt_auth)
        mToolbar = findViewById(R.id.toolbar)
        setSupportActionBar(mToolbar)
        supportActionBar!!.title = "Seleccionar opcion"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        btnLogin = findViewById(R.id.btnLogin)
        btnRegistrar = findViewById(R.id.btnRegistrar)
        mPref = applicationContext.getSharedPreferences("typeUser", MODE_PRIVATE)
        btnLogin.setOnClickListener{ irLogin() }
        btnRegistrar.setOnClickListener{ irRegistrar() }
    }

    private fun irLogin() {
        val intent = Intent(this@OptAuth, Login::class.java)
        startActivity(intent)
    }

    private fun irRegistrar() {
        val typeUser = mPref.getString("user", "")
        if (typeUser == "cliente") {
            val intent = Intent(this@OptAuth, RegistroCliente::class.java)
            startActivity(intent)
        } else {
            val intent = Intent(this@OptAuth, RegistroSocios::class.java)
            startActivity(intent)
        }
    }
}