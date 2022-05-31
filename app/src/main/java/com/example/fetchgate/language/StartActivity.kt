package com.example.fetchgate.language

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.asLiveData
import androidx.navigation.findNavController
import androidx.navigation.navArgs
import com.example.fetchgate.MainActivity
import com.example.fetchgate.R
import com.example.fetchgate.language.data.UserPreferences
import com.example.fetchgate.utils.startNewActivity


class StartActivity : AppCompatActivity() {

    private val args: StartActivityArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)

        val userPreferences = UserPreferences(this)
        userPreferences.authToken.asLiveData().observe(this) {
              //  val activity =
            //        if (it == null) StartActivity::class.java else MainActivity::class.java
            //    startNewActivity(activity)

            Toast.makeText(this, it ?: "token s null" , Toast.LENGTH_SHORT).show()
        }

        try {
            if (args.isFromOverview) {
                findNavController(R.id.nav_host_fragment_start).navigate(R.id.action_authFragment_to_languageFragment)
            }
        } catch (e: Exception) {
            return
        }

    }
}