package com.res.jobjob.activities.Socios;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.res.jobjob.R;
import com.res.jobjob.includes.MyToolbar;
import com.res.jobjob.modelos.Socio;
import com.res.jobjob.providers.AuthProvider;
import com.res.jobjob.providers.SocioProvider;

import dmax.dialog.SpotsDialog;

public class RegistroSocios extends AppCompatActivity {

    AuthProvider mAuthProvider;
    SocioProvider mSocioProvider;

    Button btnRegistrarS;
    TextInputEditText mTextInputNombreS,mTextInputCorreoS,mTextInputPassS,mTextInputOficioS,mTextInputTelS;
    AlertDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_socios);

        mAuthProvider = new AuthProvider();
        mSocioProvider = new SocioProvider();
        MyToolbar.show(this,"Registro cliente",true);


        mDialog = new SpotsDialog.Builder().setContext(RegistroSocios.this).setMessage("Espere un momento").build();


        mTextInputOficioS = findViewById(R.id.textInputOficioS);
        mTextInputTelS = findViewById(R.id.textInputTelS);
        mTextInputNombreS = findViewById(R.id.textInputNombreS);
        mTextInputCorreoS = findViewById(R.id.textInputCorreoS);
        mTextInputPassS = findViewById(R.id.textInputPassS);
        btnRegistrarS = findViewById(R.id.btnRegistrarS);




        btnRegistrarS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickRegister();
            }
        });

    }



    private void clickRegister() {

        final String nombre = mTextInputNombreS.getText().toString();
        final String correo = mTextInputCorreoS.getText().toString();
        String contraseña = mTextInputPassS.getText().toString();
        String oficio = mTextInputOficioS.getText().toString();
        String tel = mTextInputTelS.getText().toString();



        if(!nombre.isEmpty() && !correo.isEmpty() && !contraseña.isEmpty() && !oficio.isEmpty() && !tel.isEmpty()){
            if(contraseña.length()>=6){
                mDialog.show();
                register(nombre,correo,contraseña,oficio,tel);





            }else {
                Toast.makeText(RegistroSocios.this, "La contraseña debe tener al menos 6 caracteres", Toast.LENGTH_SHORT).show();
            }

        }else{

            Toast.makeText(RegistroSocios.this, "Ingrese todos los campos", Toast.LENGTH_SHORT).show();

        }





    }

    void register( final String nombre, final String correo, final String contraseña, final String oficio, final String tel){


        mAuthProvider.registrar(correo,contraseña).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                mDialog.hide();
                if(task.isSuccessful()){
                    String id = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    Socio socio = new Socio(id,nombre, correo,oficio,tel);

                    create(socio);




                } else{
                    Toast.makeText(RegistroSocios.this, "No se pudo crear el usuario", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    void create(Socio socio){
        mSocioProvider.create(socio).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){

                    Intent intent = new Intent(RegistroSocios.this, MapSocio.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);

                    //Toast.makeText(RegistroSocios.this, "El registro se realizo exitosamente", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(RegistroSocios.this, "No se pudo crear el cliente", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }
}
