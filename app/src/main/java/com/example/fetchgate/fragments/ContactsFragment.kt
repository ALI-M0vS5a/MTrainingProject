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


class ContactsFragment : Fragment() {

    private lateinit var binding: FragmentContactsBinding
    private lateinit var contactAdapter: ContactsAdapter
    private var contactProvider: ContactProvider? = null
    private lateinit var contactsList: List<ContactDataTransfer>

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

        contactProvider = ContactProvider(requireContext().contentResolver)
        initRecyclerView()
        initViewModel()
        checkForContactPermission()

        binding.edtSearch.doOnTextChanged { text, _, _, _ ->
            val query = text.toString().lowercase(Locale.getDefault())
            filterWithQuery(query)
        }
    }

    private fun filterWithQuery(query: String) {
        if (query.isNotEmpty()) {
            val filteredList: List<ContactDataTransfer> = onFilterChanged(query)
            contactAdapter.submitList(filteredList)
        } else if (query.isEmpty()) {
            contactAdapter.submitList(contactsList)
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

    private fun initRecyclerView() {
        contactAdapter = ContactsAdapter()
        binding.rvContacts.adapter = contactAdapter
        binding.rvContacts.layoutManager = LinearLayoutManager(requireContext())
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
        ) { list ->
            list?.let {
                contactsList = it
                contactAdapter.submitList(it)
            }
        }
    }
}