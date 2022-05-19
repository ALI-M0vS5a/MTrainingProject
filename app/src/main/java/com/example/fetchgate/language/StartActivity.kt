package com.example.fetchgate.language

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.navArgs
import com.example.fetchgate.R


class StartActivity : AppCompatActivity() {

    private val args: StartActivityArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)

        try {
            if (args.isFromOverview) {
                findNavController(R.id.nav_host_fragment_start).navigate(R.id.action_authFragment_to_languageFragment)
            }
        } catch (e: Exception) {
            return
        }
    }
}