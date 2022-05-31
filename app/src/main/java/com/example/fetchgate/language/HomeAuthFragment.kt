package com.example.fetchgate.language

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.fetchgate.databinding.FragmentHomeAuthBinding
import com.example.fetchgate.language.base.BaseFragment
import com.example.fetchgate.network.User
import com.example.fetchgate.network.UserApi
import com.example.fetchgate.repository.UserRepository
import com.example.fetchgate.utils.ResourceAuth
import com.example.fetchgate.utils.visible
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking


class HomeAuthFragment : BaseFragment<HomeViewModel,FragmentHomeAuthBinding,UserRepository>() {


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.progressbar.visible(false)

        viewModel.getUser()

        viewModel.user.observe(viewLifecycleOwner) {
            when (it) {
                is ResourceAuth.Success -> {
                    binding.progressbar.visible(false)
                    updateUI(it.value.user)
                }
                is ResourceAuth.Loading -> {
                    binding.progressbar.visible(true)
                }
                else -> {}
            }
        }
    }

    override fun getViewModel() = HomeViewModel::class.java

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentHomeAuthBinding.inflate(inflater,container,false)

    override fun getFragmentRepository(): UserRepository {
        val token = runBlocking {userPreferences.authToken.first()}
        val api = remoteDataSource.buildApi(UserApi::class.java,token)
        return UserRepository(api)
    }

    private fun updateUI(user: User){
        with(binding){
            tv.text = user.email
        }
    }
}