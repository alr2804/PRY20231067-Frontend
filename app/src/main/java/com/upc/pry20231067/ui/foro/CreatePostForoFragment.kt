package com.upc.pry20231067.ui.foro

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.navigation.fragment.findNavController
import com.upc.pry20231067.R
import com.upc.pry20231067.databinding.FragmentCreatePostForoBinding
import com.upc.pry20231067.databinding.FragmentProfileBinding
import com.upc.pry20231067.models.UpdateUserResponse
import com.upc.pry20231067.services.ApiService
import com.upc.pry20231067.ui.foro.dao.CreatePostForoRequest
import com.upc.pry20231067.ui.foro.dao.PostForoRequest
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


class CreatePostForoFragment : Fragment() {

    var idUser: String? = ""
    private var _binding: FragmentCreatePostForoBinding? = null
    private val binding get() = _binding!!

    lateinit var progressBar: ProgressBar
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCreatePostForoBinding.inflate(layoutInflater, container, false)

        progressBar = binding.fragmentCreatePostforoProgressBar
        idUser = arguments?.getString("idUser")
        binding.headerTitleCreatePostForo.text = idUser
        val btnSubmit = binding.createPostForoSubmit
        btnSubmit.setOnClickListener {
            sendCreatePost()
        }
        return binding.root
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

    fun sendCreatePost(){
        progressBar.visibility = View.VISIBLE
        val contentInput = binding.etContentCreatePostForo.text.toString().trim()
        var request = CreatePostForoRequest(contentInput, idUser!!)
        val call = getRetrofit().create(ApiService::class.java).createPostForo(request)
        call.enqueue(object: Callback<PostForoRequest> {
            override fun onResponse(call: Call<PostForoRequest>, response: Response<PostForoRequest>){
                if(response.isSuccessful){
                    progressBar.visibility = View.GONE
                    findNavController().navigate(CreatePostForoFragmentDirections.actionCreatePostForoFragmentToMyPostForoFragment())
                }
            }

            override fun onFailure(call: Call<PostForoRequest>, t: Throwable) {

            }
        })
    }
}