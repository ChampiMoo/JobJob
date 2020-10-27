package com.res.jobjob.activities.Clientes;

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
import com.res.jobjob.activities.Socios.MapSocio;
import com.res.jobjob.activities.Socios.RegistroSocios;
import com.res.jobjob.includes.MyToolbar;
import com.res.jobjob.modelos.Cliente;
import com.res.jobjob.providers.AuthProvider;
import com.res.jobjob.providers.ClienteProvider;

import dmax.dialog.SpotsDialog;

public class RegistroCliente extends AppCompatActivity {


    AuthProvider mAuthProvider;
    ClienteProvider mClienteProvider;


    AlertDialog mDialog;

    Button btnRegistrar;
    TextInputEditText mTextInputNombre, mTextInputCorreo, mTextInputPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_cliente);

        mAuthProvider = new AuthProvider();
        mClienteProvider = new ClienteProvider();
        MyToolbar.show(this,"Registro cliente",true);


        mDialog = new SpotsDialog.Builder().setContext(RegistroCliente.this).setMessage("Espere un momento").build();



        mTextInputNombre = findViewById(R.id.textInputNombreS);
        mTextInputCorreo = findViewById(R.id.textInputCorreoS);
        mTextInputPass = findViewById(R.id.textInputPassS);
        btnRegistrar = findViewById(R.id.btnRegistrarS);




        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickRegister();
            }
        });

    }



    private void clickRegister() {

        final String nombre = mTextInputNombre.getText().toString();
        final String correo = mTextInputCorreo.getText().toString();
        String contraseña = mTextInputPass.getText().toString();


        if(!nombre.isEmpty() && !correo.isEmpty() && !contraseña.isEmpty()){
            if(contraseña.length()>=6){
                mDialog.show();
                register(nombre,correo,contraseña);





            }else {
                Toast.makeText(RegistroCliente.this, "La contraseña debe tener al menos 6 caracteres", Toast.LENGTH_SHORT).show();
            }

        }else{

            Toast.makeText(RegistroCliente.this, "Ingrese todos los campos", Toast.LENGTH_SHORT).show();

        }





    }

    void register(final String nombre, final String correo, String contraseña){


        mAuthProvider.registrar(correo,contraseña).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                mDialog.hide();
                if(task.isSuccessful()){
                    String id = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    Cliente cliente = new Cliente(id,nombre, correo);
                   create(cliente);




                } else{
                    Toast.makeText(RegistroCliente.this, "No se pudo crear el usuario", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    void create(Cliente cliente){
        mClienteProvider.create(cliente).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){

                    Intent intent = new Intent(RegistroCliente.this, MapCliente.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);

                    //Toast.makeText(RegistroCliente.this, "El registro se realizo exitosamente", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(RegistroCliente.this, "No se pudo crear el cliente", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    /*
    private void saveUser(String id,String nombre, String correo) {
        String selectedUser = mPref.getString("user","");
        User user = new User();
        user.setCorreo(correo);
        user.setNombre(nombre);

        if(selectedUser.equals("socio")){
            mDatabase.child("User").child("Socios").child(id).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(RegistroCliente.this, "Registro exitoso", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(RegistroCliente.this, "Fallo el registro", Toast.LENGTH_SHORT).show();
                    }
                }
            });


        }else if(selectedUser.equals("cliente")){
            mDatabase.child("User").child("Clientes").child(id).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                    if(task.isSuccessful()){
                        Toast.makeText(RegistroCliente.this, "Registro exitoso", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(RegistroCliente.this, "Fallo el registro", Toast.LENGTH_SHORT).show();
                    }

                }
            });
        }


    }*/
}