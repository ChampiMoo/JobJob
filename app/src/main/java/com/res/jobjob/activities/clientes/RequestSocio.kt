package com.res.jobjob.activities.clientes

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.airbnb.lottie.LottieAnimationView
import com.res.jobjob.R

class RequestSocio : AppCompatActivity() {

    private lateinit var mAnimation: LottieAnimationView
    private var mTextView: TextView? = null
    private var mButtonCancelar: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_request_socio)
        mAnimation = findViewById(R.id.animation)
        mTextView = findViewById(R.id.textViewBuscandoSocio)
        mButtonCancelar = findViewById(R.id.btnCancelarServicio)
        mAnimation.playAnimation()
    }
}