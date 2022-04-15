package com.example.fetchgate.add

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fetchgate.adapter.AddRecyclerViewAdapter
import com.example.fetchgate.databinding.FragmentAddBinding
import com.example.fetchgate.db.ItemDatabase
import com.example.fetchgate.network.Add
import com.google.android.material.snackbar.Snackbar


class AddFragment : Fragment() {

    private lateinit var binding: FragmentAddBinding
    private lateinit var addRecyclerViewAdapter: AddRecyclerViewAdapter



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentAddBinding.inflate(layoutInflater)

        return binding.root

    }


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

        binding.recyclerviewAdd.apply {
            val decoration = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
            addItemDecoration(decoration)
        }

        addRecyclerViewAdapter.setOnItemClickListener(object :
            AddRecyclerViewAdapter.OnItemClickListener {

            override fun onItemClicked(add: Add) {

                findNavController().navigate(
                    AddFragmentDirections.actionAddFragmentToCreateFragment(
                        false,
                        add
                    )
                )
            }

            override fun onItemLongClick(add: Add) {
                val snackBar = Snackbar.make(
                    view!!, add.Name,
                    Snackbar.LENGTH_LONG

                ).setAction("Delete") {
                    addViewModel.delete(add)
                    if (view != null) {
                        val snackBar = Snackbar.make(
                            view!!, "Item Deleted",
                            Snackbar.LENGTH_LONG
                        )
                        snackBar.show()
                    }
                }
                snackBar.show()
            }
        })
        binding.lifecycleOwner = this
    }

    private fun initViewModel(): AddViewModel {
        val application = requireNotNull(this.activity).application
        ItemDatabase.getInstance(application).itemDao
        val viewModelFactory = AddViewModelFactory(application)
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

            findNavController().navigate(
                AddFragmentDirections.actionAddFragmentToCreateFragment(
                    true
                )
            )
        }
    }

}

