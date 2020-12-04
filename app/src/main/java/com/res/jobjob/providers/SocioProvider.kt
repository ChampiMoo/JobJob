package com.res.jobjob.providers

import com.google.android.gms.tasks.Task
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.res.jobjob.modelos.Socio

class SocioProvider {

    private val mDatabase: DatabaseReference = FirebaseDatabase.getInstance().reference.child("Users").child("Socios")

    fun create(socio: Socio): Task<Void?> {
        return mDatabase.child(socio.id).setValue(socio)
    }
}