
package com.example.fetchgate.overview

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.fetchgate.R
import com.example.fetchgate.adapter.LoadingAdapter
import com.example.fetchgate.adapter.RecyclerViewPagingAdapter
import com.example.fetchgate.databinding.FragmentOverviewBinding
import com.example.fetchgate.network.Result
import kotlinx.coroutines.flow.collectLatest


class OverviewFragment : Fragment() {

    var mLastClickTime = 0
    private lateinit var binding: FragmentOverviewBinding
    private lateinit var recyclerViewPagingAdapter: RecyclerViewPagingAdapter
    private lateinit var viewModel: OverviewViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentOverviewBinding.inflate(layoutInflater)

        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView()
        initViewModel()
        swipeRefresh()


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
                        if(checkForInternet(requireContext())) run {
                            recyclerViewPagingAdapter.retry()
                        }else{
                            Toast.makeText(requireContext(), "Internet Connection Problem!!", Toast.LENGTH_SHORT).show()
                        }
                    }
                )

            recyclerViewPagingAdapter.setOnItemClickListener(object :
                RecyclerViewPagingAdapter.OnItemClickListener {
                override fun onItemClicked(result: Result) {

                    findNavController().navigate(
                        OverviewFragmentDirections.actionOverviewFragmentToDetailFragment(result)
                    )
                }

                @SuppressLint("InflateParams")
                override fun onImageClicked(imageUrl: String) {

                    if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                        return
                    }
                    mLastClickTime = SystemClock.elapsedRealtime().toInt()
                    Log.d("Output", "imageClicked")
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
                recyclerViewPagingAdapter.submitData(it)
                binding.swipe.isRefreshing = false
            }
        }
    }

    private fun swipeRefresh() {
        val swiped = binding.swipe
        swiped.setOnRefreshListener {
            if (checkForInternet(this.requireContext())) {
                recyclerViewPagingAdapter.refresh()
            }else{
                Toast.makeText(requireContext(), "Internet Connection Problem!!", Toast.LENGTH_SHORT).show()
                binding.swipe.isRefreshing = false
            }
        }
    }

        private fun checkForInternet(context: Context): Boolean {
            val connectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val network = connectivityManager.activeNetwork ?: return false
                val activeNetwork =
                    connectivityManager.getNetworkCapabilities(network) ?: return false
                return when {
                    activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                    activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                    else -> false
                }
            } else {
                @Suppress("DEPRECATION")
                val networkInfo = connectivityManager.activeNetworkInfo ?: return false
                @Suppress("DEPRECATION")
                return networkInfo.isConnected
            }
        }
    }

