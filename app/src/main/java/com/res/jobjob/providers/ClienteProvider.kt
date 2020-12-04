package com.res.jobjob.providers

import com.google.android.gms.tasks.Task
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.res.jobjob.modelos.Cliente
import java.util.*

class ClienteProvider {

    private var mDatabase: DatabaseReference = FirebaseDatabase.getInstance().reference.child("Users").child("Clientes")

    fun create(cliente: Cliente): Task<Void?> {
        val map: MutableMap<String, Any?> = HashMap()
        map["nombre"] = cliente.nombre
        map["correo"] = cliente.correo
        return mDatabase.child(cliente.id).setValue(map)
    }

}