package com.example.fetchgate.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.fetchgate.databinding.FragmentDetailBinding
import com.example.fetchgate.utils.bindImage


class DetailFragment : Fragment() {
    private lateinit var binding: FragmentDetailBinding
    private val args: DetailFragmentArgs by navArgs()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentDetailBinding.inflate(layoutInflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        receiveDetails()

    }

    private fun receiveDetails() {
        val detail = args.detail
        with(binding) {
            bindImage(detailImageView, detail.owner.avatar_url)
            detailFullName.text = detail.full_name
            detailID.text = detail.id.toString()
        }
    }
}
