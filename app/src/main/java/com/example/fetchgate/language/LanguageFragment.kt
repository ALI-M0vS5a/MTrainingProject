package com.example.fetchgate.language

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.fetchgate.MainActivity
import com.example.fetchgate.R
import com.example.fetchgate.databinding.FragmentLanguageBinding
import com.example.fetchgate.utils.updateLocale
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import java.util.*


class LanguageFragment : Fragment() {

    private lateinit var binding: FragmentLanguageBinding
    private lateinit var mGoogleSignInClient: GoogleSignInClient


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentLanguageBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()

        mGoogleSignInClient = GoogleSignIn.getClient(requireContext(), gso)



        binding.arabicSwitch.setOnClickListener {
            attachArabic(requireContext())
            val intent = Intent(requireContext(), MainActivity::class.java)
            intent.putExtra("fromArabic", true)
            startActivity(intent)

        }
        binding.englishSwitch.setOnClickListener {
            attachEnglish(requireContext())
            val intent = Intent(requireContext(), MainActivity::class.java)
            intent.putExtra("fromEnglish", true)
            startActivity(intent)
        }
        binding.signOutButton.setOnClickListener {
            signOut()
        }

    }

    private fun attachArabic(newBase: Context?) {
        val localeToSwitchTo = Locale("ar")
        updateConf(localeToSwitchTo)
        if (newBase != null) {
            updateLocale(newBase, localeToSwitchTo)
        }
    }

    private fun attachEnglish(newBase: Context?) {
        val localeToSwitchTo = Locale("en")
        updateConf(localeToSwitchTo)
        if (newBase != null) {
            updateLocale(newBase, localeToSwitchTo)
        }

    }

    private fun updateConf(localeToSwitchTo: Locale) {
        val res: Resources = resources
        val dm: DisplayMetrics = res.displayMetrics
        val conf: Configuration = res.configuration
        conf.setLocale(localeToSwitchTo)
        res.updateConfiguration(conf, dm)
    }

    private fun signOut() {
        mGoogleSignInClient.signOut()
            .addOnSuccessListener {
                findNavController().navigate(R.id.action_languageFragment_to_authFragment)
                Toast.makeText(requireContext(), "SignedOut", Toast.LENGTH_LONG).show()

            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()

            }
    }
}