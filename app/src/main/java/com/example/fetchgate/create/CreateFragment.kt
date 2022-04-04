package com.example.fetchgate.create

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.fetchgate.R
import com.example.fetchgate.adapter.ViewPagerAdapter
import com.example.fetchgate.add.AddViewModel
import com.example.fetchgate.add.AddViewModelFactory
import com.example.fetchgate.databinding.FragmentCreateBinding
import com.example.fetchgate.databinding.ItemViewPagerBinding
import com.example.fetchgate.db.ItemDatabase
import com.example.fetchgate.network.Add
import com.example.fetchgate.network.ViewPager
import com.google.android.material.snackbar.Snackbar
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


@Suppress("DEPRECATION")
class CreateFragment : Fragment() {

    private lateinit var binding: FragmentCreateBinding
    private lateinit var pagerBinding: ItemViewPagerBinding
    private var imageFilePath: String? = null
    private val args: CreateFragmentArgs by navArgs()
    private lateinit var addViewModel: AddViewModel
    private lateinit var viewPagerRecyclerViewAdapter: ViewPagerAdapter
    private val images = ArrayList<ViewPager>()
    private var listOfImageFiles: MutableList<String> = mutableListOf()
    private var deletedImage: MutableList<String> = mutableListOf()


    companion object {
        const val CAMERA_REQUEST_CODE = 0
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCreateBinding.inflate(layoutInflater)
        pagerBinding = ItemViewPagerBinding.inflate(layoutInflater)

        return binding.root
    }


    @SuppressLint("QueryPermissionsNeeded")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        addViewModel = initViewModel()
        takePictureIntent()
        addToDatabase()
        initRecyclerViewPager()

    }

    private fun initRecyclerViewPager() {
        viewPagerRecyclerViewAdapter = ViewPagerAdapter(images)
        binding.viewPager.adapter = viewPagerRecyclerViewAdapter
        viewPagerRecyclerViewAdapter.setOnItemClickListener(object :
            ViewPagerAdapter.OnItemClickListener {
            override fun onLongClickedFromAdd(viewpager: ViewPager) {
                if (images.size > 1) {
                    viewPagerRecyclerViewAdapter.deleteItem(viewpager)
                    deletedImage.add(viewpager.image)
                } else {
                    Toast.makeText(
                        requireContext(),
                        "We're afraid we won't be able to remove this image!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        })
    }

    private fun receiveArgs() {
        val add = args.add
        binding.editTextCarName.setText(add?.Name)
        for (i in add?.image!!) {
            val images = ViewPager(i)
            viewPagerRecyclerViewAdapter.updateViewPagerList(images)
            Log.d("receivedImages", images.toString())
        }
    }

    @SuppressLint("QueryPermissionsNeeded")
    private fun takePictureIntent() {
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

    @SuppressLint("NotifyDataSetChanged", "SetTextI18n")
    private fun addToDatabase() {
        if (!args.isFromAdd) {
            initRecyclerViewPager()
            binding.buttonCreate.text = "Update"
            receiveArgs()
        }
        binding.buttonCreate.setOnClickListener {
            if (args.isFromAdd) {
                val name = binding.editTextCarName.text.toString()
                val image = listOfImageFiles

                if (name == "" || image.isEmpty()) {
                    allFieldRequiredSnackBar()
                } else {
                    val add = Add(0, name, image)
                    if (image.size >= 3) {
                        addViewModel.addItem(add)
                        navigate()
                    } else {
                        atLeastThreeImagesSnackBar()
                    }
                }
            } else {
                val name = args.add?.Name
                val image = args.add?.image
                val nameUpdate = binding.editTextCarName.text.toString()
                val imageUpdate = listOfImageFiles
                val imageDeleted = deletedImage

                if (name == nameUpdate && imageUpdate.size == 0) {
                    addAll(image, imageDeleted)
                    val update2 = Add(args.add!!.id, nameUpdate, imageUpdate)
                    if (images.size >= 3) {
                        addViewModel.updateItem(update2)
                    } else {
                        noLessThanThreeImagesSnackBar()
                    }
                } else {
                    if (name == nameUpdate) {
                        addAll(image, imageDeleted)
                        val update3 = Add(args.add!!.id, name, imageUpdate)
                        if (images.size >= 3) {
                            addViewModel.updateItem(update3)

                        } else {
                            atLeastThreeImagesSnackBar()
                        }

                    } else {
                        addAll(image, imageDeleted)
                        val update4 = Add(args.add!!.id, nameUpdate, imageUpdate)
                        if (images.size >= 3) {
                            addViewModel.updateItem(update4)
                        } else {
                            atLeastThreeImagesSnackBar()
                        }
                    }
                }
                navigate()
            }
        }
    }

    private fun addAll(
        image: List<String>?,
        imageDeleted: MutableList<String>
    ) {
        if (image != null) {
            listOfImageFiles.addAll(image)
            for (i in imageDeleted) {

                listOfImageFiles.remove(i)

            }
        }
    }


    private fun navigate() {
        findNavController().navigate(R.id.action_createFragment_to_addFragment)
    }

    private fun allFieldRequiredSnackBar() {
        val snackBar = Snackbar.make(
            requireView(), "All fields required!!",
            Snackbar.LENGTH_SHORT
        )
        snackBar.show()
    }

    private fun atLeastThreeImagesSnackBar() {
        val snackBar = Snackbar.make(
            requireView(), "Please Add at least Three Images.",
            Snackbar.LENGTH_SHORT
        )
        snackBar.show()
    }

    private fun noLessThanThreeImagesSnackBar() {
        val snackBar = Snackbar.make(
            requireView(), "No Less Than 3 Images. ",
            Snackbar.LENGTH_SHORT
        )
        snackBar.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            CAMERA_REQUEST_CODE -> {
                if (resultCode == Activity.RESULT_OK) {
                    imageFilePath?.let { listOfImageFiles.add(it) }
                    val view = imageFilePath?.let {
                        ViewPager(it)
                    }
                    if (view != null) {
                        viewPagerRecyclerViewAdapter.updateViewPagerList(view)
                    }
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


//    private fun setScaledBitmap(): Bitmap {
//        val imageViewWidth = pagerBinding.imageViewCar.width
//        val imageViewHeight = pagerBinding.imageViewCar.height
//
//        val bmOptions = BitmapFactory.Options()
//        bmOptions.inJustDecodeBounds = true
//        BitmapFactory.decodeFile(imageFilePath, bmOptions)
//        val bitmapWidth = bmOptions.outWidth
//        val bitmapHeight = bmOptions.outHeight
//
//        val scaleFactor =
//            (bitmapWidth / imageViewWidth).coerceAtMost(bitmapHeight / imageViewHeight)
//
//        bmOptions.inJustDecodeBounds = false
//        bmOptions.inSampleSize = scaleFactor
//
//        return BitmapFactory.decodeFile(imageFilePath, bmOptions)
//
//    }


    private fun initViewModel(): AddViewModel {
        val application = requireNotNull(this.activity).application
        ItemDatabase.getInstance(application).itemDao
        val viewModelFactory = AddViewModelFactory(application)

        return ViewModelProvider(this, viewModelFactory)[AddViewModel::class.java]
    }
}





