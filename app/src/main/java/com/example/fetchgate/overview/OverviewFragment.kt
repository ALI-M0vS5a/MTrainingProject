
package com.example.fetchgate.overview


import android.os.Bundle
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.PagingData
import androidx.paging.filter
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.fetchgate.R
import com.example.fetchgate.adapter.LoadingAdapter
import com.example.fetchgate.adapter.RecyclerViewPagingAdapter
import com.example.fetchgate.databinding.FragmentOverviewBinding
import com.example.fetchgate.network.Result
import kotlinx.coroutines.flow.collectLatest
import java.util.*


class OverviewFragment : Fragment() {

    var mLastClickTime = 0
    private lateinit var binding: FragmentOverviewBinding
    private lateinit var recyclerViewPagingAdapter: RecyclerViewPagingAdapter
    private lateinit var viewModel: OverviewViewModel
    private lateinit var resultsList: PagingData<Result>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentOverviewBinding.inflate(layoutInflater)

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        resultsList = PagingData.empty()


        initRecyclerView()
        initViewModel()
        swipeRefresh()

        binding.edtSearch.doOnTextChanged { text, _, _, _ ->
            val query = text.toString().lowercase(Locale.getDefault())
            filterWithQuery(query)

        }
    }


    private fun initRecyclerView() {

        val recyclerview = binding.recyclerviewMain
        recyclerview.apply {
            val decoration =
                DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
            addItemDecoration(decoration)
            recyclerViewPagingAdapter = RecyclerViewPagingAdapter()

            adapter = recyclerViewPagingAdapter
                .withLoadStateHeaderAndFooter(

                    header = LoadingAdapter {
                        recyclerViewPagingAdapter::retry
                    },
                    footer = LoadingAdapter {
                        if (viewModel.checkForInternet(requireContext())) run {
                            recyclerViewPagingAdapter.retry()
                        } else {
                            Toast.makeText(
                                requireContext(),
                                "Internet Connection Problem!!",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                )

            recyclerViewPagingAdapter.setOnItemClickListener(object :
                RecyclerViewPagingAdapter.OnItemClickListener {
                override fun onItemClicked(result: Result) {

                    try {
                        findNavController().navigate(
                            OverviewFragmentDirections.actionOverviewFragmentToDetailFragment(result)
                        )
                    } catch (e: Exception) {
                        return
                    }
                }


                override fun onImageClicked(imageUrl: String) {

                    if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                        return
                    }
                    mLastClickTime = SystemClock.elapsedRealtime().toInt()
                    val bundle = bundleOf("clickImage" to imageUrl)
                    findNavController().navigate(
                        R.id.action_overviewFragment_to_customDialog,
                        bundle
                    )
                }
            })
        }
    }


    private fun initViewModel() {
        viewModel = ViewModelProvider(this)[OverviewViewModel::class.java]
        getListData()
    }

    private fun getListData() {
        lifecycleScope.launchWhenCreated {
            viewModel.getListData().collectLatest {
                resultsList = it
                recyclerViewPagingAdapter.submitData(it)
                binding.swipe.isRefreshing = false

            }
        }
    }

    private fun swipeRefresh() {
        val swiped = binding.swipe
        swiped.setOnRefreshListener {
            if (viewModel.checkForInternet(this.requireContext())) {
                recyclerViewPagingAdapter.refresh()
            } else {
                Toast.makeText(
                    requireContext(),
                    "Internet Connection Problem!!",
                    Toast.LENGTH_SHORT
                ).show()
                binding.swipe.isRefreshing = false
            }
        }
    }

    private fun filterWithQuery(query: String) {
        if (query.isNotEmpty()) {
            val filteredList: PagingData<Result> = onFilterChanged(query)
            lifecycleScope.launchWhenCreated {
                recyclerViewPagingAdapter.submitData(filteredList)
            }

        } else {
            if (query.isEmpty()) {
                lifecycleScope.launchWhenCreated {
                    recyclerViewPagingAdapter.submitData(resultsList)

                }
            }
        }
    }

    private fun onFilterChanged(filterQuery: String): PagingData<Result> {

        return resultsList.filter { it.full_name.contains(filterQuery, true) }
    }




}






