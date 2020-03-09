package com.sktbd.driboard.ui.fragment

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.sktbd.driboard.R
import com.sktbd.driboard.databinding.LoginFragmentBinding
import com.sktbd.driboard.ui.viewmodel.LoginViewModel


class LoginFragment : Fragment() {

    private val CLIENT_ID: String = "332c4e1351a08f89683bffedcb5242571e807fcdc2030f4ae93d5d08e4955a8e"
    private val REDIRECT_URI: String = "driboard://callback"
    private var CODE: String = ""
    private var accessToken: String = ""

    private lateinit var binding: LoginFragmentBinding
    private lateinit var viewModel: LoginViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.login_fragment,
            container,
            false
        )

        viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)

        Log.i("LoginViewModel", "Called ViewModelProvider")
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.loginBtn.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse("https://dribbble.com/oauth/authorize?client_id=$CLIENT_ID&redirect_uri=$REDIRECT_URI&scope=public+upload")
            activity?.startActivity(intent)
        }
        accessToken = loadData()
        if (accessToken != "") {
            println("Already logged in")
            println(accessToken)
            this.findNavController().navigate(R.id.action_loginFragment_to_userFragment)
        } else {
            viewModel.accessToken.observe(viewLifecycleOwner, Observer {token ->
                val sharedPref: SharedPreferences.Editor = activity?.getSharedPreferences("auth", Context.MODE_PRIVATE)!!.edit()
                if (token != "") {
                    sharedPref.putString("accessToken", token)
                    Log.i("LoginActivity", "token changed $token")
                    sharedPref.apply()
                    this.findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToUserFragment(token))
                }
            })
        }
    }

    override fun onResume() {
        super.onResume()
        val data: Uri? = activity?.intent?.data
        println(accessToken)
        if (accessToken == "" && !TextUtils.isEmpty(data?.scheme)) {
            println("no accessToken, fetch a new one")
            val code: String? = data?.getQueryParameter("code")
            if (code != null && !TextUtils.isEmpty((code))) {
                Toast.makeText(activity, R.string.login, Toast.LENGTH_LONG).show()
                CODE = code
                viewModel.getAccessToken(CODE)
            }
        }
    }
//
    private fun loadData(): String {
        val sharedPref = activity?.getSharedPreferences("auth", Context.MODE_PRIVATE)
        val token: String =  sharedPref!!.getString("accessToken", "")!!
        return token
    }

}
