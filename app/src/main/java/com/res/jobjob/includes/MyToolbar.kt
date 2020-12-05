package com.res.jobjob.includes

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.res.jobjob.R

object MyToolbar {

    @SuppressLint("UseCompatLoadingForDrawables", "ResourceAsColor")
    fun show(activity: AppCompatActivity, title: String?, upButton: Boolean) {
        val toolbar = activity.findViewById<Toolbar>(R.id.toolbar)
        activity.setSupportActionBar(toolbar)
        activity.supportActionBar!!.title = title
        activity.supportActionBar!!.setDisplayHomeAsUpEnabled(upButton)
        if (upButton) {
            val drawable: Drawable = activity.resources.getDrawable(R.drawable.ic_arrow)
            drawable.setColorFilter(Color.parseColor("#FFFFFF"), PorterDuff.Mode.SRC_ATOP)
            activity.supportActionBar!!.setHomeAsUpIndicator(drawable)
        }
    }
}