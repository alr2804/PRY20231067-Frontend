package com.upc.pry20231067.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.upc.pry20231067.MainActivity
import com.upc.pry20231067.R
import com.upc.pry20231067.databinding.FragmentLoginBinding
import com.upc.pry20231067.databinding.FragmentRegisterBinding
import com.upc.pry20231067.models.LoginRequest
import com.upc.pry20231067.models.LoginResponse
import com.upc.pry20231067.models.PlaceResponse
import com.upc.pry20231067.services.ApiService
import com.upc.pry20231067.services.RetrofitClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class LoginFragment : Fragment() {

    private val retrofitService = RetrofitClient.getRetrofit()
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    val url: String = "https://api-ar-app.onrender.com/auth/sign-in"

    lateinit var progressBar: ProgressBar


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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progressBar = binding.fragmentLoginProgressBar
    }

//    fun showProgressBar(){
//        progressBar.visibility = View.VISIBLE
//    }
//
//    fun hideProgressBar(){
//        progressBar.visibility = View.GONE
//    }

    fun buttonsNavigation(){
        val btnRegister = binding.buttonRegister
        btnRegister.setOnClickListener{
//            findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToRegisterFragment())
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }

        val btnLogin = binding.buttonLogin
        btnLogin.setOnClickListener{
//            findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToNavGraph2())
//            val intent = Intent(requireContext(), MainActivity::class.java)
//            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//            startActivity(intent)
            login()
        }
    }

    private fun getRetrofit(): Retrofit {
        // Crear una instancia de OkHttpClient personalizada
        val okHttpClient = OkHttpClient.Builder()
            .readTimeout(30, TimeUnit.SECONDS) // Configura el tiempo de espera de lectura
            .connectTimeout(30, TimeUnit.SECONDS) // Configura el tiempo de espera de conexión
            .build()

        return Retrofit.Builder().baseUrl("https://api-ar-app.onrender.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
    }



    private fun login(){
        progressBar.visibility = View.VISIBLE
        val usernameValue = binding.editTextUsername.text.toString().trim()
        val passwordValue = binding.editTextPassword.text.toString().trim()
        val userLogin = LoginRequest(usernameValue, passwordValue)
        val call = getRetrofit().create(ApiService::class.java).login(url, userLogin)
//            val responseLogin = call.body()
        call.enqueue(object: Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>){
                if(response.isSuccessful){
                    progressBar.visibility = View.GONE
                    val intent = Intent(requireContext(), MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    val idUser = response.body()?.data?._id
                    intent.putExtra("idUser", idUser)
                    startActivity(intent)

                } else {
                    if(response.code() == 404){
                        Toast.makeText(requireContext(), "Username or Password invalid", Toast.LENGTH_SHORT)

                    }
                    progressBar.visibility = View.GONE
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                progressBar.visibility = View.GONE
            }

        })

    }




    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null  // Prevent memory leaks
    }

}