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
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.fetchgate.adapter.ViewPagerAdapter
import com.example.fetchgate.add.AddViewModel
import com.example.fetchgate.add.AddViewModelFactory
import com.example.fetchgate.databinding.FragmentCreateBinding
import com.example.fetchgate.db.ItemDatabase
import com.example.fetchgate.network.Add
import com.example.fetchgate.network.ViewPager
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class CreateFragment : Fragment() {

    private lateinit var binding: FragmentCreateBinding
    private var imageFilePath: String? = null
    private val args: CreateFragmentArgs by navArgs()
    private lateinit var addViewModel: AddViewModel
    private lateinit var viewModel: CreateViewModel
    private lateinit var viewPagerRecyclerViewAdapter: ViewPagerAdapter
    private val images = ArrayList<ViewPager>()
    private var listOfImageFiles: MutableList<String> = mutableListOf()
    private var deletedImage: MutableList<String> = mutableListOf()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCreateBinding.inflate(layoutInflater)

        viewModel = ViewModelProvider(this)[CreateViewModel::class.java]


        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        addViewModel = initViewModel()
        takePictureIntent()
        selectPictureIntent()
        addToDatabase()
        initRecyclerViewPager()
//        if (savedInstanceState != null) {
//            viewPagerRecyclerViewAdapter.getItemId(savedInstanceState.getInt("pageItem",0))
//
//        }




    }

    private fun initRecyclerViewPager() {
        viewPagerRecyclerViewAdapter = ViewPagerAdapter(images)
        binding.viewPager.adapter = viewPagerRecyclerViewAdapter
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { _, _ -> }.attach()

        viewPagerRecyclerViewAdapter.setOnItemClickListener(object :
            ViewPagerAdapter.OnItemClickListener {
            override fun onLongClick(viewpager: ViewPager) {
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


    private fun takePictureIntent() {
        binding.buttonCapture.setOnClickListener {
            captureImage()
        }
    }

    @SuppressLint("QueryPermissionsNeeded")
    private fun captureImage() {
        try {
            val imageFile = createImageFile()
            val callCameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            if (callCameraIntent.resolveActivity(requireActivity().packageManager) != null) {
                val authorities = activity?.packageName + ".fileProvider"
                val imageUri =
                    FileProvider.getUriForFile(this.requireContext(), authorities, imageFile)
                callCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
                getResultFromCamera.launch(callCameraIntent)

            }
        } catch (e: IOException) {
            Toast.makeText(this.requireContext(), "Could not create file!", Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun selectPictureIntent() {
        binding.buttonSelect.setOnClickListener {
            selectImage()
        }
    }

    private fun selectImage() {
        val callGalleryIntent =
            Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        getResultFromGallery.launch(callGalleryIntent)
    }


    private fun addToDatabase() {
        if (!args.isFromAdd) {
            initRecyclerViewPager()
            "Update".also { binding.buttonCreate.text = it }
            receiveArgs()
            binding.editTextEmail.isVisible = false
            binding.editTextPhone.isVisible = false
        }
        binding.buttonCreate.setOnClickListener {
            if (args.isFromAdd) {
                val name = binding.editTextCarName.text.toString()
                val email = binding.editTextEmail.text.toString()
                val phoneNumber = binding.editTextPhone.text.toString()
                val image = listOfImageFiles

                if (name == "" || image.isEmpty() || email == "" || phoneNumber == "") {
                    allFieldRequired()
                } else {
                    val add = Add(0, name, phoneNumber, email, image)
                    if (image.size >= 3) {
                        addViewModel.addItem(add)
                        navigate()
                    } else {
                        snackBar2()
                    }
                }
            } else {
                val name = args.add?.Name
                val email = args.add?.Email
                val phoneNumber = args.add?.Phone
                val image = args.add?.image
                val nameUpdate = binding.editTextCarName.text.toString()
                val imageUpdate = listOfImageFiles
                val imageDeleted = deletedImage

                if (name == nameUpdate && imageUpdate.size == 0) {
                    addAll(image, imageDeleted)
                    val update2 =
                        Add(args.add!!.id, nameUpdate, phoneNumber!!, email!!, imageUpdate)
                    if (images.size >= 3) {
                        addViewModel.updateItem(update2)
                    } else {
                        snackBar()
                    }
                } else {
                    if (name == nameUpdate) {
                        addAll(image, imageDeleted)
                        val update3 = Add(args.add!!.id, name, phoneNumber!!, email!!, imageUpdate)
                        if (images.size >= 3) {
                            addViewModel.updateItem(update3)

                        } else {
                            snackBar2()
                        }

                    } else {
                        addAll(image, imageDeleted)
                        val update4 =
                            Add(args.add!!.id, nameUpdate, phoneNumber!!, email!!, imageUpdate)
                        if (images.size >= 3) {
                            addViewModel.updateItem(update4)
                        } else {
                            snackBar2()
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
        findNavController().navigate(com.example.fetchgate.R.id.action_createFragment_to_addFragment)

    }

    private fun allFieldRequired() {
        val snackBar = Snackbar.make(
            requireView(), "All fields required!!",
            Snackbar.LENGTH_SHORT
        )
        snackBar.show()
    }

    private fun snackBar2() {
        val snackBar = Snackbar.make(
            requireView(), "Please Add at least Three Images.",
            Snackbar.LENGTH_SHORT
        )
        snackBar.show()
    }

    private fun snackBar() {
        val snackBar = Snackbar.make(
            requireView(), "No Less Than 3 Images. ",
            Snackbar.LENGTH_SHORT
        )
        snackBar.show()
    }

    private val getResultFromCamera = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == Activity.RESULT_OK) {
            imageFilePath?.let { it1 ->
                listOfImageFiles.add(it1)
            }
            val view = imageFilePath?.let { it2 ->
                ViewPager(it2)
            }
            if (view != null) {
                viewPagerRecyclerViewAdapter.updateViewPagerList(view)
            }
        }
    }
    private val getResultFromGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == Activity.RESULT_OK) {

            val realPath = viewModel.getRealPathFromURI(this.requireContext(), it.data?.data)

            realPath?.let { it3 -> listOfImageFiles.add(it3) }
            val view = realPath?.let { it4 ->
                ViewPager(it4)

            }
            if (view != null) {
                viewPagerRecyclerViewAdapter.updateViewPagerList(view)
            }
        }
    }


    @SuppressLint("SimpleDateFormat")
    @Throws(IOException::class)
    fun createImageFile(): File {
        val timeStamp: String = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date())
        val imageFileName: String = "JPEG_" + timeStamp + "_"
        val storageDir: File? =
            requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        if (!storageDir!!.exists()) storageDir.mkdirs()
        val imageFile = File.createTempFile(imageFileName, ".jpg", storageDir)
        imageFilePath = imageFile.absolutePath
        return imageFile
    }

    private fun initViewModel(): AddViewModel {
        val application = requireNotNull(this.activity).application
        ItemDatabase.getInstance(application).itemDao
        val viewModelFactory = AddViewModelFactory(application)

        return ViewModelProvider(this, viewModelFactory)[AddViewModel::class.java]
    }
//     override fun onSaveInstanceState(outState: Bundle) {
//        super.onSaveInstanceState(outState)
//         outState.putInt("key",viewPagerRecyclerViewAdapter.itemCount)
//        Log.d("onSaveInstanceState","onSavedInstanceState Called")
//    }

}








