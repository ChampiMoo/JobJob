package com.res.jobjob.includes

import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.res.jobjob.R

object MyToolbar {

    fun show(activity: AppCompatActivity, title: String?, upButton: Boolean) {
        val toolbar = activity.findViewById<Toolbar>(R.id.toolbar)
        activity.setSupportActionBar(toolbar)
        activity.supportActionBar!!.title = title
        activity.supportActionBar!!.setDisplayHomeAsUpEnabled(upButton)
    }
}