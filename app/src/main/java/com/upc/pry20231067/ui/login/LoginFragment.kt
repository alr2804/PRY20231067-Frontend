package com.upc.pry20231067.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.upc.pry20231067.MainActivity
import com.upc.pry20231067.R
import com.upc.pry20231067.databinding.FragmentLoginBinding
import com.upc.pry20231067.databinding.FragmentRegisterBinding
import com.upc.pry20231067.models.LoginRequest
import com.upc.pry20231067.services.ApiService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    val url: String = "https://api-ar-app.onrender.com/auth/sign-in"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Inflate the layout for this fragment
        buttonsNavigation()

        return root
//
    }

    fun buttonsNavigation(){
        val btnRegister = binding.buttonRegister
        btnRegister.setOnClickListener{
//            findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToRegisterFragment())
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }

        val btnLogin = binding.buttonLogin
        btnLogin.setOnClickListener{
//            findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToNavGraph2())
            val intent = Intent(requireContext(), MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
//            login()
        }
    }

    private fun getRetrofit(): Retrofit {
        return Retrofit.Builder().baseUrl("https://api-ar-app.onrender.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private fun login(){
        CoroutineScope(Dispatchers.IO).launch {
            val usernameValue = binding.editTextUsername.text.toString().trim()
            val passwordValue = binding.editTextPassword.text.toString().trim()
            val userLogin = LoginRequest(usernameValue, passwordValue)
            val call = getRetrofit().create(ApiService::class.java).login(url, userLogin)
//            val responseLogin = call.body()
            activity?.runOnUiThread{
                if(call.isSuccessful){
//                    val userInfo = responseLogin?.data
//                    val intent = Intent(activity, PagesActivity()::class.java)
//                    intent.putExtra("userID", responseLogin?.data?._id)
//                    findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToNavGraph2())
                    val intent = Intent(requireContext(), MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)

                }else{
                    Toast.makeText(context, "Invalid Login", Toast.LENGTH_SHORT)
                }
            }
        }
    }




    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null  // Prevent memory leaks
    }

}