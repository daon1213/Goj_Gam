package com.daon.goj_gam.screen.main.home

import com.daon.goj_gam.databinding.FragmentHomeBinding
import com.daon.goj_gam.screen.base.BaseFragment
import org.koin.androidx.viewmodel.ext.android.viewModel


class HomeFragment: BaseFragment<HomeViewModel, FragmentHomeBinding>() {

    override val viewModel by viewModel<HomeViewModel>()

    override fun getViewBinding(): FragmentHomeBinding = FragmentHomeBinding.inflate(layoutInflater)

    override fun observeData() {

    }

    companion object {

        fun newInstance() = HomeFragment()

        const val TAG = "HomeFragment"

    }
}