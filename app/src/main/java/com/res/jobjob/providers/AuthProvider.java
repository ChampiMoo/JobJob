package com.res.jobjob.providers;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class AuthProvider {
    FirebaseAuth mAuth;

    public AuthProvider(){

        mAuth = FirebaseAuth.getInstance();

    }

    public Task<AuthResult> registrar(String correo, String contraseña){
        return mAuth.createUserWithEmailAndPassword(correo,contraseña);

    }

    public Task<AuthResult> login(String correo, String contraseña){
        return mAuth.signInWithEmailAndPassword(correo,contraseña);

    }

    public void logout(){
        mAuth.signOut();

    }

    public String getId(){

        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    public boolean existSession(){
        boolean exist = false ;
        if(mAuth.getCurrentUser() != null){
            exist = true;
        }return exist;
    }


}
