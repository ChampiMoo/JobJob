package com.res.jobjob.data.api.db

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.res.jobjob.data.data.Usuario

class ClienteDB : ControllerDB {

    private val db: FirebaseDatabase = Firebase.database

    override fun getUsuario(id: String): LiveData<Usuario> {
        val mutableData: MutableLiveData<Usuario> = MutableLiveData()
        val reference = db.getReference("Clientes").child(id)
        reference.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val cliente: Usuario? = snapshot.getValue(Usuario.Cliente::class.java)
                    mutableData.value = cliente
                }
            }

            override fun onCancelled(error: DatabaseError) { }

        })
        return mutableData
    }

    override fun existe(id: String): LiveData<Boolean> {
        val mutableData: MutableLiveData<Boolean> = MutableLiveData()
        val reference = db.getReference("Clientes").child(id)
        reference.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) { mutableData.value = snapshot.exists() }

            override fun onCancelled(error: DatabaseError) { }

        })
        return mutableData
    }

    override fun add(usuario: Usuario): LiveData<Boolean> {
        val mutableData: MutableLiveData<Boolean> = MutableLiveData()
        val reference = db.getReference("Clientes").child(usuario.id)
        reference.setValue(usuario).addOnCompleteListener { mutableData.value = it.isSuccessful }
        return mutableData
    }
}