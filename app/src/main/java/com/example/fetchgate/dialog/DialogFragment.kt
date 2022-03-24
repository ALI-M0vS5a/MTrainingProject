package com.example.fetchgate.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.example.fetchgate.R
import com.example.fetchgate.databinding.DialogLayoutBinding
import com.example.fetchgate.utils.bindImage
import com.example.fetchgate.utils.bindImageString


class CustomDialog : DialogFragment() {
    private lateinit var binding: DialogLayoutBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.dialog_layout, container, false
        )
        binding = DialogLayoutBinding.inflate(layoutInflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val receivedBundleImage = arguments?.getSerializable("clickImage")
        val image = binding.imageviewDialog
        bindImage(image, receivedBundleImage.toString())

        val receivedBundleImage2 = arguments?.getSerializable("clickImage2")
        val image2 = binding.imageviewDialog2
        bindImageString(image2,receivedBundleImage2.toString())

    }
}