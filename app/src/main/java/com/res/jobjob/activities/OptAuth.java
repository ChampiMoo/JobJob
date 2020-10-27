package com.res.jobjob.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.res.jobjob.R;
import com.res.jobjob.activities.Clientes.RegistroCliente;
import com.res.jobjob.activities.Socios.RegistroSocios;

public class OptAuth extends AppCompatActivity {
    SharedPreferences mPref;

    Toolbar mToolbar;
    Button btnLogin, btnRegistrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opt_auth);

        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Seleccionar opcion");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        btnLogin = findViewById(R.id.btnLogin);
        btnRegistrar = findViewById(R.id.btnRegistrar);

        mPref = getApplicationContext().getSharedPreferences("typeUser",MODE_PRIVATE);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                irLogin();
            }
        });

        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                irRegistrar();
            }
        });





    }

    public void irLogin(){
        Intent intent = new Intent(OptAuth.this,Login.class);
        startActivity(intent);

    }

    public void irRegistrar(){

        String typeUser = mPref.getString("user","");
        if(typeUser.equals("cliente")){
            Intent intent = new Intent(OptAuth.this, RegistroCliente.class);
            startActivity(intent);
        }else{
            Intent intent = new Intent(OptAuth.this, RegistroSocios.class);
            startActivity(intent);
        }


    }
}