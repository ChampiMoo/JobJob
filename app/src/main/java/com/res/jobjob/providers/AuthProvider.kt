package com.res.jobjob.providers

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth

class AuthProvider {

    private var mAuth: FirebaseAuth = FirebaseAuth.getInstance()

    fun registrar(correo: String?, contraseña: String?): Task<AuthResult?> {
        return mAuth.createUserWithEmailAndPassword(correo!!, contraseña!!)
    }

    fun login(correo: String?, contraseña: String?): Task<AuthResult> {
        return mAuth.signInWithEmailAndPassword(correo!!, contraseña!!)
    }

    fun logout() {
        mAuth.signOut()
    }

    val id: String
        get() = FirebaseAuth.getInstance().currentUser!!.uid

    fun existSession(): Boolean {
        var exist = false
        if (mAuth.currentUser != null) {
            exist = true
        }
        return exist
    }

}