package com.res.jobjob.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.res.jobjob.R;
import com.res.jobjob.activities.Clientes.MapCliente;
import com.res.jobjob.activities.Clientes.RegistroCliente;
import com.res.jobjob.activities.Socios.MapSocio;
import com.res.jobjob.includes.MyToolbar;

import dmax.dialog.SpotsDialog;

public class Login extends AppCompatActivity {

    TextInputEditText mtextInputCorreo, mtextInputPass;
    Button btnLoginL;
    SharedPreferences mPref;
    FirebaseAuth mAuth;
    DatabaseReference mDatabase;

    AlertDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        MyToolbar.show(this,"Login de usuario",true);
        mtextInputCorreo = findViewById(R.id.textInputCorreoS);
        mtextInputPass = findViewById(R.id.textInputPassS);
        mPref = getApplicationContext().getSharedPreferences("typeUser",MODE_PRIVATE);
        btnLoginL = findViewById(R.id.btnLoginL);


        mDialog = new SpotsDialog.Builder().setContext(Login.this).setMessage("Espere un momento").build();

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        
        btnLoginL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

    }

    private void login() {

        String correo = mtextInputCorreo.getText().toString();
        String contraseña = mtextInputPass.getText().toString();

        if(!correo.isEmpty() && !contraseña.isEmpty()){
            if(contraseña.length()>=6){

                mDialog.show();

                mAuth.signInWithEmailAndPassword(correo,contraseña).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful()){
                            String user = mPref.getString("user","");
                            if(user.equals("cliente")){

                                Intent intent = new Intent(Login.this, MapCliente.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);

                            }else{
                                Intent intent = new Intent(Login.this, MapSocio.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);


                            }

                        }else{
                            Toast.makeText(Login.this, "Usuario o contraseña incorrecto", Toast.LENGTH_SHORT).show();
                        }

                        mDialog.dismiss();

                    }
                });

            }
        }



    }
}