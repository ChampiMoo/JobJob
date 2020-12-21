package com.res.jobjob.data.api.db

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class DBOficios {

    private val db: FirebaseDatabase = Firebase.database

    fun getData(): LiveData<ArrayList<String>> {
        val mutableLiveData: MutableLiveData<ArrayList<String>> = MutableLiveData()
        val reference = db.getReference("Oficios")
        reference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val listaOficios: ArrayList<String> = ArrayList()
                snapshot.children.forEach {
                    val nombre: String = it.child("nombre").value.toString()
                    listaOficios.add(nombre)
                }
                mutableLiveData.value = listaOficios
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("Hi", "Error en la conexion de la base de datos")
            }
        })
        return mutableLiveData
    }
}