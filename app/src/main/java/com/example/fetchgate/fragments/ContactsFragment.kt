package com.example.fetchgate.fragments

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fetchgate.adapter.ContactsAdapter
import com.example.fetchgate.contactprovider.ContactProvider
import com.example.fetchgate.databinding.FragmentContactsBinding
import com.example.fetchgate.db.ItemDatabase
import com.example.fetchgate.network.ContactDataTransfer
import kotlinx.coroutines.*
import java.util.*


@Suppress("UNCHECKED_CAST")
class ContactsFragment : Fragment() {

    private lateinit var binding: FragmentContactsBinding
    private lateinit var contactAdapter: ContactsAdapter
    private var contactProvider: ContactProvider? = null
    private lateinit var alphabetsList: ArrayList<Char>
    lateinit var contactsList: List<ContactDataTransfer>


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentContactsBinding.inflate(layoutInflater)

        return binding.root


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        contactsList = arrayListOf()
        alphabetsList = arrayListOf()

        contactProvider = ContactProvider(requireContext().contentResolver)
        checkForContactPermission()
        initViewModel()

        initRecyclerViews()

        binding.edtSearch.doOnTextChanged { text, _, _, _ ->
            val query = text.toString().lowercase(Locale.getDefault())
            filterWithQuery(query)
        }


    }

    private fun filterWithQuery(query: String) {
        if (query.isNotEmpty()) {
            val filteredList: List<ContactDataTransfer> = onFilterChanged(query)
            contactAdapter.updateList(filteredList)
        } else if (query.isEmpty()) {
            contactAdapter.updateList(contactsList)
        }
    }

    private fun onFilterChanged(filterQuery: String): List<ContactDataTransfer> {
        val filteredList = ArrayList<ContactDataTransfer>()
        for (currentContact in contactsList) {
            if (currentContact.name.lowercase(Locale.getDefault()).contains(filterQuery) ||
                currentContact.number.contains(filterQuery)
            ) {
                filteredList.add(currentContact)

            }
        }
        return filteredList
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun getPhoneData() {
        val contactViewModel = initViewModel()
        lifecycleScope.launchWhenCreated {
            GlobalScope.launch(Dispatchers.Main) {
                withContext(Dispatchers.IO) {
                    contactProvider?.getAllContactsFromProvider(contactViewModel)
                }
            }
        }
    }

    private fun checkForContactPermission() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_CONTACTS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestReadContactsPermission.launch(Manifest.permission.READ_CONTACTS)
        } else {
            getPhoneData()
        }
    }

    private val requestReadContactsPermission = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {
        when (it) {
            true -> {
                getPhoneData()
                Toast.makeText(requireContext(), "Permission Granted", Toast.LENGTH_SHORT)
                    .show()
            }
            false -> {
                Toast.makeText(
                    requireContext(),
                    "Fail to Obtained Permission",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }


    private fun initViewModel(): ContactsViewModel {
        val application = requireNotNull(this.activity).application
        ItemDatabase.getInstance(application).itemDao
        val viewModelFactory = ContactsViewModelFactory(application)
        val viewModel =
            ViewModelProvider(this, viewModelFactory)[ContactsViewModel::class.java]
        observe(viewModel)
        return viewModel
    }

    private fun observe(viewModel: ContactsViewModel) {
        viewModel.allContacts.observe(
            viewLifecycleOwner
        ) {
            it.let { it1 ->
                contactsList = it1
                contactAdapter.updateList(it1)
            }
        }
    }

//    private fun contactFirstLetterNotExist(it: List<ContactDataTransfer>) =
//
//        !alphabetsList.containsAll(it.groupBy {
//            it.name.first().uppercaseChar()
//        }.map {
//            it.key
//        })

//    private fun getContactFirstCharacter(it: List<ContactDataTransfer>) {
//
//        alphabetsList.addAll(it.groupBy { it2 ->
//
//            it2.name.first().uppercaseChar()
//
//        }.map { it1 ->
//            it1.key
//        }
//        )
//        alphabetAdapter = AlphabetAdapter(alphabetsList,requireContext(),this)
//        binding.rvAlphabets.layoutManager = LinearLayoutManager(requireContext())
//        binding.rvAlphabets.adapter = alphabetAdapter
//
//    }


    private fun initRecyclerViews() {
        contactAdapter = ContactsAdapter()

        with(binding) {
            rvContacts.layoutManager = LinearLayoutManager(requireContext())
            rvContacts.adapter = contactAdapter
            rvContacts.setIndexTextSize(12)
            rvContacts.setIndexBarTextColor("#000000")
            rvContacts.setIndexBarColor("#cdced2")
            rvContacts.setIndexbarHighLateTextColor("#FF4081")
            rvContacts.setIndexBarHighLateTextVisibility(true)
            rvContacts.setIndexBarTransparentValue(1.0.toFloat())

        }
        contactAdapter.setOnItemClickListener(object : ContactsAdapter.OnItemClickListener{
            override fun onImageClicked(imgUrl: String) {
                Toast.makeText(requireContext(), "imageClicked", Toast.LENGTH_SHORT).show()

            }

        })
    }




//    override fun onAlphabetClicked(alphabet: Char) {
//
//        Log.i("MVS", alphabet + "")
//        Log.i("MVS", contactsList.toString())
//        val data = contactsList.indexOfFirst {
//            it.name.startsWith(alphabet, ignoreCase = true)
//        }
//        val layoutManager: LinearLayoutManager =
//            binding.rvContacts.layoutManager as LinearLayoutManager
//        layoutManager.scrollToPositionWithOffset(data, 0)
//    }

}