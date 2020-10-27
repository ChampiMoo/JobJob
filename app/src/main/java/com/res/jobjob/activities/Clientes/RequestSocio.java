package com.res.jobjob.activities.Clientes;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.res.jobjob.R;

public class RequestSocio extends AppCompatActivity {

    private LottieAnimationView mAnimation;
    private TextView mTextView;
    private Button mButtonCancelar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_socio);

       mAnimation = findViewById(R.id.animation);
        mTextView = findViewById(R.id.textViewBuscandoSocio);
        mButtonCancelar = findViewById(R.id.btnCancelarServicio);

        mAnimation.playAnimation();
    }
}