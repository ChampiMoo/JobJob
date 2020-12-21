package com.res.jobjob.ui.includes

import android.annotation.SuppressLint
import android.content.res.Resources
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import com.res.jobjob.R

object Toolbar {

    fun show(activity: AppCompatActivity, toolbar: Toolbar, nvController: NavController) {
        activity.setSupportActionBar(toolbar)
        activity.setupActionBarWithNavController(nvController)
    }

    fun iconMenu(activity: AppCompatActivity, nvController: NavController, navigationView: NavigationView, drawerLayout: DrawerLayout, toolbar: Toolbar) {
        val appBarConfiguration = AppBarConfiguration(nvController.graph, drawerLayout)
        toolbar.setupWithNavController(nvController, appBarConfiguration)
        nvController.addOnDestinationChangedListener { _, _, _ ->
            toolbar.navigationIcon = createIconMenu(activity.resources)
        }
        navigationView.setupWithNavController(nvController)
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun createIconMenu(resources: Resources): Drawable {
        val icon: Drawable = resources.getDrawable(R.drawable.ic_menu, resources.newTheme())
        icon.setColorFilter(Color.parseColor("#FFFFFF"), PorterDuff.Mode.SRC_ATOP)
        return icon
    }

    fun addBackIcon(resources: Resources, nvController: NavController, toolbar: Toolbar) {
        nvController.addOnDestinationChangedListener { _, _, _ ->
            toolbar.navigationIcon = createIconBack(resources)
            toolbar.setNavigationOnClickListener { nvController.navigateUp() }
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun createIconBack(resources: Resources): Drawable {
        val icon: Drawable = resources.getDrawable(R.drawable.ic_arrow, resources.newTheme())
        icon.setColorFilter(Color.parseColor("#ffffff"), PorterDuff.Mode.SRC_ATOP)
        return icon
    }
}