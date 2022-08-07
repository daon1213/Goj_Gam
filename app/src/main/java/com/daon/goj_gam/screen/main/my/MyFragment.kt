package com.daon.goj_gam.screen.main.my

import android.app.Activity
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.daon.goj_gam.R
import com.daon.goj_gam.databinding.FragmentMyBinding
import com.daon.goj_gam.extension.load
import com.daon.goj_gam.model.order.OrderModel
import com.daon.goj_gam.screen.base.BaseFragment
import com.daon.goj_gam.screen.main.review.AddReviewActivity
import com.daon.goj_gam.util.provider.ResourcesProvider
import com.daon.goj_gam.widget.adapter.ModelRecyclerAdapter
import com.daon.goj_gam.widget.adapter.listener.order.OrderListListener
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel


class MyFragment : BaseFragment<MyViewModel, FragmentMyBinding>() {
    override val viewModel: MyViewModel by viewModel()

    private val resourceProvider by inject<ResourcesProvider>()
    override fun getViewBinding(): FragmentMyBinding =
        FragmentMyBinding.inflate(layoutInflater)

    private val gso: GoogleSignInOptions by lazy {
        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
    }

    private val gsc by lazy { GoogleSignIn.getClient(requireActivity(), gso) }
    private val firebaseAuth by lazy { FirebaseAuth.getInstance() }

    private val loginLauncher =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            Log.d(",MyPageFragment", result.toString())
            if (result.resultCode == Activity.RESULT_OK) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                try {
                    task.getResult(ApiException::class.java)?.let { account ->
                        viewModel.saveToken(account.idToken ?: throw java.lang.Exception())
                    } ?: kotlin.run {
                        throw Exception()
                    }
                } catch(exception: Exception){
                    exception.toString()
                    binding.progressbar.isGone = true
                }
            }
        }

    private val adapter by lazy {
        ModelRecyclerAdapter<OrderModel, MyViewModel>(
            listOf(),
            viewModel,
            resourceProvider,
            adapterListener = object : OrderListListener {
                override fun onClickItem(orderModel: OrderModel) {
                    startActivity(
                        AddReviewActivity.newInstance(
                            requireContext(),
                            restaurantTitle = orderModel.restaurantTitle,
                            orderId = orderModel.orderId
                        )
                    )
                }
            }
        )
    }

    override fun initViews() = with(binding) {
        super.initViews()
        recyclerView.adapter = adapter
        loginButton.setOnClickListener {
            signInGoogle()
        }
        logoutButton.setOnClickListener {
            firebaseAuth.signOut()
            viewModel.signOut()
        }
    }

    private fun signInGoogle() {
        val signInIntent = gsc.signInIntent
        loginLauncher.launch(signInIntent)
    }

    override fun observeData() = viewModel.myStateLiveData.observe(viewLifecycleOwner) {
        when(it) {
            is MyState.Error -> handleErrorState(it)
            MyState.Loading -> handleLoadingState()
            is MyState.Login -> handleLoginState(it)
            is MyState.Success -> handleSuccessState(it)
            MyState.UnInitialized -> Unit
        }
    }

    private fun handleLoadingState() {
        binding.loginRequiredGroup.isGone = true
        binding.progressbar.isVisible = true
    }

    private fun handleSuccessState(state: MyState.Success) = with(binding) {
        progressbar.isGone = true

        when (state) {
            is MyState.Success.Registered -> handleRegisteredState(state)
            MyState.Success.NotRegistered -> {
                profileGroup.isGone = true
                loginRequiredGroup.isVisible = true
            }
        }
    }

    private fun handleRegisteredState(state: MyState.Success.Registered) = with(binding) {
        profileGroup.isVisible = true
        loginRequiredGroup.isGone = true

        profileImageView.load(state.profileImageUri.toString(), 60f)
        userNameTextView.text = state.userName

        adapter.submitList(state.orderList)
    }

    private fun handleLoginState(state: MyState.Login) {
        binding.progressbar.isVisible = true
        val credential = GoogleAuthProvider.getCredential(state.idToken, null)
        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    val user = firebaseAuth.currentUser
                    viewModel.setUserInfo(user)
                } else {
                    firebaseAuth.signOut()
                }
            }
    }

    private fun handleErrorState(state: MyState.Error) {
        Toast.makeText(requireContext(), getString(state.messageId), Toast.LENGTH_SHORT).show()
    }

    companion object {
        const val TAG = "MyFragment"
        fun newInstance() = MyFragment()
    }
}