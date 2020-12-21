package com.res.jobjob.data.api.db

import androidx.lifecycle.LiveData
import com.res.jobjob.data.data.Usuario

interface ControllerDB {

    fun getUsuario(id: String): LiveData<Usuario>
    fun existe(id: String): LiveData<Boolean>
    fun add(usuario: Usuario): LiveData<Boolean>
}