package com.example.fetchgate

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.fetchgate.add.AddFragmentDirections
import com.example.fetchgate.create.CreateFragmentDirections
import com.example.fetchgate.detail.DetailFragmentDirections
import com.example.fetchgate.fragments.ContactsFragmentDirections
import com.example.fetchgate.notifications.NotificationFragmentDirections
import com.example.fetchgate.overview.OverviewFragmentDirections
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView


class MainActivity : AppCompatActivity() {
    private lateinit var actionBar: ActionBar
    private lateinit var drawer: DrawerLayout
    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var navView: NavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        navView = findViewById(R.id.navView)
        drawer = findViewById(R.id.drawerLayout)

        toggle = ActionBarDrawerToggle(this, drawer, R.string.open, R.string.close)
        drawer.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar = supportActionBar!!

        val bottomNavView =
            findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        val navHostFragment = findViewById<View>(R.id.nav_host_fragment)
        bottomNavView.setupWithNavController(navHostFragment.findNavController())

        // val navController = this.findNavController(R.id.nav_host_fragment)
        // NavigationUI.setupActionBarWithNavController(this, navController)


        val receivedValueFromArabic = intent.extras!!.getBoolean("fromArabic")
        val receivedValueFromEnglish = intent.extras!!.getBoolean("fromEnglish")
        if (receivedValueFromArabic) {
            actionBar.setTitle(R.string.app_arabic_name)
        } else if (receivedValueFromEnglish) {
            actionBar.setTitle(R.string.app_english_name)
        }

        navView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.overviewFragment -> {
                    if (i() == R.id.addFragment) {
                        navController().navigate(R.id.action_addFragment_to_overviewFragment)
                    }
                    if (i() == R.id.overviewFragment) {
                        drawer.close()
                    }
                    if (i() == R.id.otherFragment) {
                        navController().navigate(R.id.action_otherFragment_to_overviewFragment)
                    }
                    if (i() == R.id.thatFragment) {
                        navController().navigate(R.id.action_thatFragment_to_overviewFragment)
                    }
                    if (i() == R.id.detailFragment) {
                        navController().navigate(R.id.action_detailFragment_to_overviewFragment)
                    }
                    if (i() == R.id.createFragment) {
                        navController().navigate(R.id.action_createFragment_to_overviewFragment)
                    }
                    drawer.close()
                }
                R.id.nav_Settings -> {
                    if (i() == R.id.overviewFragment) {
                        navController().navigate(
                            OverviewFragmentDirections.actionOverviewFragmentToStartActivity2(true)
                        )
                    }
                    if (i() == R.id.addFragment) {
                        navController().navigate(
                            AddFragmentDirections.actionAddFragmentToStartActivity2(
                                true
                            )
                        )
                    }
                    if (i() == R.id.createFragment) {
                        navController().navigate(
                            CreateFragmentDirections.actionCreateFragmentToStartActivity2(
                                true
                            )
                        )
                    }
                    if (i() == R.id.detailFragment) {
                        navController().navigate(
                            DetailFragmentDirections.actionDetailFragmentToStartActivity2(
                                true
                            )
                        )
                    }
                    if (i() == R.id.thatFragment) {
                        navController().navigate(
                            ContactsFragmentDirections.actionThatFragmentToStartActivity2(
                                true
                            )
                        )
                    }
                    if (i() == R.id.otherFragment) {
                        navController().navigate(
                            NotificationFragmentDirections.actionOtherFragmentToStartActivity2(
                                true
                            )
                        )
                    }
                    drawer.close()
                }
            }
            true
        }
    }

    private fun i() = navController().currentDestination!!.id

    private fun navController() = findNavController(R.id.nav_host_fragment)

//    override fun onSupportNavigateUp(): Boolean {
//        val navController = this.findNavController(R.id.nav_host_fragment)
//        return navController.navigateUp()
//    }
}