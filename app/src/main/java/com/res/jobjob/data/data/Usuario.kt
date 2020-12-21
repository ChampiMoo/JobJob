package com.res.jobjob.data.data

sealed class Usuario(
        open var id: String = "",
        open val nombre: String = "",
        open val correo: String = "",
        open val telefono: String = "",) {

    data class Socio(
            override var id: String = "",
            override val nombre: String = "",
            override val correo: String = "",
            override val telefono: String = "",
            val oficio: String = "") : Usuario()

    data class Cliente(
            override var id: String = "",
            override val nombre: String = "",
            override val correo: String = "",
            override val telefono: String = "") : Usuario()
}