package com.example.fetchgate.add

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fetchgate.R
import com.example.fetchgate.adapter.AddRecyclerViewAdapter
import com.example.fetchgate.databinding.FragmentAddBinding
import com.example.fetchgate.databinding.FragmentCreateBinding
import com.example.fetchgate.db.ItemDatabase
import com.example.fetchgate.network.Add


class AddFragment : Fragment() {

    var mLastClickTime = 0
    private lateinit var binding: FragmentAddBinding
    private lateinit var binding2: FragmentCreateBinding
    private lateinit var addRecyclerViewAdapter: AddRecyclerViewAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddBinding.inflate(layoutInflater)
        binding2 = FragmentCreateBinding.inflate(layoutInflater)

        return binding.root

    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val addViewModel = initViewModel()
        initRecyclerView(addViewModel)
        navigate()

    }

    private fun initRecyclerView(addViewModel: AddViewModel) {
        addRecyclerViewAdapter = AddRecyclerViewAdapter()
        binding.recyclerviewAdd.adapter = addRecyclerViewAdapter
        binding.recyclerviewAdd.layoutManager = LinearLayoutManager(this.context)

        addRecyclerViewAdapter.setOnItemClickListener(object :
            AddRecyclerViewAdapter.OnItemClickListener {
            override fun onDeleteIconClicked(add: Add) {
                addViewModel.delete(add)
                Toast.makeText(context, "${add.Name} Deleted ", Toast.LENGTH_LONG).show()
            }

            @SuppressLint( "SetTextI18n")
            override fun onItemClicked(add: Add) {
                val b1 = false.also { binding2.buttonCreate.isVisible = it }
                val b2 = true.also { binding2.buttonUpdate.isVisible = it }
                findNavController().navigate(AddFragmentDirections.actionAddFragmentToCreateFragment(b2,b1,add))

            }

            override fun onImageClicked(imageUrl: String) {
                Log.d("Output", imageUrl)
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return
                }
                mLastClickTime = SystemClock.elapsedRealtime().toInt()
                Log.d("Output", "imageClicked2")
                val bundle = bundleOf("clickImage2" to imageUrl)

                findNavController().navigate(
                    R.id.action_addFragment_to_customDialog,
                    bundle
                )

            }
        })
        binding.lifecycleOwner = this
    }

    private fun initViewModel(): AddViewModel {
        val application = requireNotNull(this.activity).application
        val dataSources = ItemDatabase.getInstance(application).itemDao
        val viewModelFactory = AddViewModelFactory(dataSources, application)
        val addViewModel = ViewModelProvider(this, viewModelFactory)[AddViewModel::class.java]


        addViewModel.allItem.observe(
            viewLifecycleOwner
        ) { list ->
            list?.let {
                addRecyclerViewAdapter.updateList(it)
            }
        }
        return addViewModel
    }


    private fun navigate() {
        binding.fab.setOnClickListener {
                val b2 = false.also { binding2.buttonUpdate.isVisible = it }
                val b3 = true.also { binding2.buttonCreate.isVisible = it }
                findNavController().navigate(AddFragmentDirections.actionAddFragmentToCreateFragment(b2,b3))

        }
    }

}


