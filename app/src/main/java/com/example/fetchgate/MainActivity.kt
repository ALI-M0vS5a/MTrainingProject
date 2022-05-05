package com.example.fetchgate

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : AppCompatActivity() {
    private lateinit var actionBar: ActionBar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        actionBar = supportActionBar!!

        val bottomNavView =
            findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        val navHostFragment = findViewById<View>(R.id.nav_host_fragment)
        bottomNavView.setupWithNavController(navHostFragment.findNavController())

        val navController = this.findNavController(R.id.nav_host_fragment)
        NavigationUI.setupActionBarWithNavController(this, navController)


        val receivedValueFromArabic = intent.extras!!.getBoolean("fromArabic")
        val receivedValueFromEnglish = intent.extras!!.getBoolean("fromEnglish")
        if (receivedValueFromArabic) {
            actionBar.setTitle(R.string.app_arabic_name)
        } else if (receivedValueFromEnglish) {
            actionBar.setTitle(R.string.app_english_name)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = this.findNavController(R.id.nav_host_fragment)
        return navController.navigateUp()
    }
}