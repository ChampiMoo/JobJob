package com.res.jobjob.data.api

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class AuthUser : AuthUserI {

    private val auth: FirebaseAuth = Firebase.auth

    fun registrar(correo: String, password: String): LiveData<Boolean> {
        val mutableData = MutableLiveData<Boolean>()
        mutableData.value = false
        auth.createUserWithEmailAndPassword(correo, password).addOnSuccessListener { mutableData.value = true }
        return mutableData
    }

    fun iniciar(correo: String, password: String): LiveData<Boolean> {
        val mutableData = MutableLiveData<Boolean>()
        auth.signInWithEmailAndPassword(correo, password).addOnSuccessListener { mutableData.value = true }
        return mutableData
    }

    override fun getUID() = auth.uid ?: ""

    override fun getIniciado(): Boolean {
        return auth.currentUser != null
    }

    fun logout() {
        auth.signOut()
    }
}