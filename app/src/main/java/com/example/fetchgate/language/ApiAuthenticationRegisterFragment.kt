package com.example.fetchgate.language

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.fetchgate.databinding.FragmentApiAuthenticationRegisterBinding


class ApiAuthenticationRegisterFragment : Fragment() {

    private lateinit var binding: FragmentApiAuthenticationRegisterBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentApiAuthenticationRegisterBinding.inflate(layoutInflater)
        return binding.root

    }
}