package com.upc.pry20231067.ui.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.upc.pry20231067.R
import com.upc.pry20231067.databinding.FragmentMapsBinding
import com.upc.pry20231067.databinding.FragmentRegisterBinding
import com.upc.pry20231067.models.RegisterRequest
import com.upc.pry20231067.services.ApiService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    val url: String = "https://api-ar-app.onrender.com/auth/sign-up"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Inflate the layout for this fragment
        buttonsNavigation()

        return root
    }


    fun buttonsNavigation(){
        val btn = binding.buttonLogin
        btn.setOnClickListener{
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }

        val btnRegister = binding.buttonRegister
        btnRegister.setOnClickListener{
            register()
//            findNavController().navigate(RegisterFragmentDirections.actionRegisterFragmentToNavGraph2())
        }
    }

    private fun getRetrofit(): Retrofit {
        return Retrofit.Builder().baseUrl("https://api-ar-app.onrender.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private fun register(){
        CoroutineScope(Dispatchers.IO).launch {
            val firstnameValue = binding.editTextFirstName.text.toString().trim()
            val lastnameValue = binding.editTextLastName.text.toString().trim()
            val emailValue = binding.editTextEmail.text.toString().trim()
            val usernameValue = binding.editTextUsername.text.toString().trim()
            val passwordValue = binding.editTextPassword.text.toString().trim()

            val userRegister = RegisterRequest(firstnameValue, lastnameValue, emailValue, usernameValue, passwordValue)
            val call = getRetrofit().create(ApiService::class.java).register(url, userRegister)
//            val responseLogin = call.body()
            activity?.runOnUiThread{
                if(call.isSuccessful){
//                    val userInfo = responseLogin?.data
                    findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
                }else{
                    Toast.makeText(context, "Invalid register", Toast.LENGTH_SHORT)
                }
            }
        }
    }
}