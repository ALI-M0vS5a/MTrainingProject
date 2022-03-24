package com.example.fetchgate.create

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.fetchgate.R
import com.example.fetchgate.add.AddViewModel
import com.example.fetchgate.add.AddViewModelFactory
import com.example.fetchgate.databinding.FragmentCreateBinding
import com.example.fetchgate.db.ItemDatabase
import com.example.fetchgate.network.Add
import com.example.fetchgate.utils.bindImageString
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class CreateFragment : Fragment() {

    private lateinit var binding: FragmentCreateBinding
    private var imageFilePath: String? = null
    private val args: CreateFragmentArgs by navArgs()

    companion object {
        const val CAMERA_REQUEST_CODE = 0
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCreateBinding.inflate(layoutInflater)

        return binding.root
    }

    @SuppressLint("QueryPermissionsNeeded")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dispatchTakePictureIntent()
        addDataToTheRecyclerView()
        receiveArgs()
    }

    private fun receiveArgs() {
        val add = args.add
        with(binding) {
            bindImageString(imageViewCar, add?.image)
            editTextCarName.setText(add?.Name)
        }
        val update = args.update
        val update2 = args.update2
        with(binding) {
            buttonUpdate.isVisible = update2
            buttonCreate.isVisible = update
            buttonUpdate.isVisible = update
            buttonCreate.isVisible = update2
        }
    }

    @SuppressLint("QueryPermissionsNeeded")
    private fun dispatchTakePictureIntent() {
        binding.buttonCapture.setOnClickListener {
            try {
                val imageFile = createImageFile()
                val callCameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                if (callCameraIntent.resolveActivity(requireActivity().packageManager) != null) {
                    val authorities = activity?.packageName + ".fileprovider"
                    val imageUri =
                        FileProvider.getUriForFile(this.requireContext(), authorities, imageFile)
                    callCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
                    startActivityForResult(callCameraIntent, CAMERA_REQUEST_CODE)
                }
            } catch (e: IOException) {
                Toast.makeText(this.requireContext(), "Could not create file!", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun addDataToTheRecyclerView() {
        binding.buttonCreate.setOnClickListener {
            val (name, image) = pair()

            if (name == "" || image == null) {
                toast()
            } else {
                val add = Add(0, name, image)
                val addViewModel = initViewModel()
                addViewModel.addItem(add)
                navigate()
            }
        }
        binding.buttonUpdate.setOnClickListener {
            val (name, image) = pair()
            if (name == "" || image == null) {
                toast()
            } else {
                val updated = Add(args.add!!.id, name, image)
                val addViewModel = initViewModel()
                addViewModel.updateItem(updated)
                navigate()
            }
        }
    }

    private fun pair(): Pair<String, String?> {
        val name = binding.editTextCarName.text.toString()
        val image = imageFilePath
        return Pair(name, image)
    }

    private fun navigate() {
        findNavController().navigate(R.id.action_createFragment_to_addFragment)
    }

    private fun toast() {
        val text = "Please fill all fields!!!"
        val duration = Toast.LENGTH_SHORT
        val toast = Toast.makeText(context, text, duration)
        toast.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            CAMERA_REQUEST_CODE -> {
                if (resultCode == Activity.RESULT_OK) {
                    binding.imageViewCar.setImageBitmap(setScaledBitmap())
                }
            }
            else -> {
                Toast.makeText(
                    this.requireContext(),
                    "Unrecognized request code",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    @SuppressLint("SimpleDateFormat")
    @Throws(IOException::class)
    fun createImageFile(): File {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName: String = "JPEG_" + timeStamp + "_"
        val storageDir: File? =
            requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        if (!storageDir!!.exists()) storageDir.mkdirs()
        val imageFile = File.createTempFile(imageFileName, ".jpg", storageDir)
        imageFilePath = imageFile.absolutePath
        return imageFile
    }


    private fun setScaledBitmap(): Bitmap {
        val imageViewWidth = binding.imageViewCar.width
        val imageViewHeight = binding.imageViewCar.height

        val bmOptions = BitmapFactory.Options()
        bmOptions.inJustDecodeBounds = true
        BitmapFactory.decodeFile(imageFilePath, bmOptions)
        val bitmapWidth = bmOptions.outWidth
        val bitmapHeight = bmOptions.outHeight

        val scaleFactor =
            (bitmapWidth / imageViewWidth).coerceAtMost(bitmapHeight / imageViewHeight)

        bmOptions.inJustDecodeBounds = false
        bmOptions.inSampleSize = scaleFactor

        return BitmapFactory.decodeFile(imageFilePath, bmOptions)

    }

    private fun initViewModel(): AddViewModel {
        val application = requireNotNull(this.activity).application
        val dataSources = ItemDatabase.getInstance(application).itemDao
        val viewModelFactory = AddViewModelFactory(dataSources, application)

        return ViewModelProvider(this, viewModelFactory)[AddViewModel::class.java]
    }

}





