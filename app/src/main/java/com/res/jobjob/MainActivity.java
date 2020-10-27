package com.res.jobjob;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.res.jobjob.activities.Clientes.MapCliente;
import com.res.jobjob.activities.Login;
import com.res.jobjob.activities.OptAuth;
import com.res.jobjob.activities.Socios.MapSocio;

public class MainActivity extends AppCompatActivity {

    Button btnSoySocio, btnSoyCliente;
    SharedPreferences mPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnSoyCliente = findViewById(R.id.btnSoyCliente);
        btnSoySocio = findViewById(R.id.btnSoySocio);

        mPref = getApplicationContext().getSharedPreferences("typeUser",MODE_PRIVATE);
        final SharedPreferences.Editor editor= mPref.edit();


        btnSoySocio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putString("user","socio");
                editor.apply();
                Intent i = new Intent(MainActivity.this, OptAuth.class);
                startActivity(i);

            }
        });

        btnSoyCliente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putString("user","cliente");
                editor.apply();
                startActivity( new Intent(MainActivity.this,OptAuth.class));
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        if(FirebaseAuth.getInstance().getCurrentUser() != null){
            String user = mPref.getString("user","");
            if(user.equals("cliente")){

                Intent intent = new Intent(MainActivity.this, MapCliente.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);

            }else{
                Intent intent = new Intent(MainActivity.this, MapSocio.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);


            }
        }
    }
}