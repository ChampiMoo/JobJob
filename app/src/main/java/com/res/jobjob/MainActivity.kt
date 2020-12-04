package com.res.jobjob

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.res.jobjob.activities.clientes.MapCliente
import com.res.jobjob.activities.OptAuth
import com.res.jobjob.activities.socios.MapSocio

class MainActivity : AppCompatActivity() {

    private lateinit var btnSoySocio: Button
    private lateinit var btnSoyCliente: Button
    private lateinit var mPref: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btnSoyCliente = findViewById(R.id.btnSoyCliente)
        btnSoySocio = findViewById(R.id.btnSoySocio)
        mPref = applicationContext.getSharedPreferences("typeUser", MODE_PRIVATE)
        val editor = mPref.edit()
        btnSoySocio.setOnClickListener {
            editor.putString("user", "socio")
            editor.apply()
            val i = Intent(this@MainActivity, OptAuth::class.java)
            startActivity(i)
        }
        btnSoyCliente.setOnClickListener {
            editor.putString("user", "cliente")
            editor.apply()
            startActivity(Intent(this@MainActivity, OptAuth::class.java))
        }
    }

    override fun onStart() {
        super.onStart()
        if (FirebaseAuth.getInstance().currentUser != null) {
            val user = mPref.getString("user", "")
            if (user == "cliente") {
                val intent = Intent(this@MainActivity, MapCliente::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(intent)
            } else {
                val intent = Intent(this@MainActivity, MapSocio::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(intent)
            }
        }
    }
}