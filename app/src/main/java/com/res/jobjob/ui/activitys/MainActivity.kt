package com.res.jobjob.ui.activitys

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.res.jobjob.R
import com.res.jobjob.data.api.AuthUser
import com.res.jobjob.data.api.AuthUserI

class MainActivity : AppCompatActivity() {

    private val authUser: AuthUserI = AuthUser()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}