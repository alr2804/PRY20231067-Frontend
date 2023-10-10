package com.upc.pry20231067.ui.foro

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.navigation.fragment.findNavController
import com.upc.pry20231067.R
import com.upc.pry20231067.databinding.FragmentEditPostForoBinding
import com.upc.pry20231067.databinding.FragmentMyPostForoBinding
import com.upc.pry20231067.models.PlaceResponse
import com.upc.pry20231067.services.ApiService
import com.upc.pry20231067.ui.foro.dao.PostForoRequest
import com.upc.pry20231067.ui.foro.dao.UpdatePostForoRequest
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


class EditPostForoFragment : Fragment() {

    var idPostForo: String? = ""
    lateinit var progressBar: ProgressBar

    private var _binding: FragmentEditPostForoBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEditPostForoBinding.inflate(layoutInflater, container, false)

        val btnSubmitEdit = binding.editPostForoSubmit
        btnSubmitEdit.setOnClickListener {
            sendEdit()
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progressBar = binding.fragmentEditPostforoProgressBar
        val itemPostForo = arguments?.getString("objPost")
        idPostForo = itemPostForo?.substringAfter("_id=")?.substringBefore(",")
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

    fun sendEdit(){
        val contentInput = binding.etContentEditPostForo.text.toString().trim()
        var request = UpdatePostForoRequest(contentInput)
        val call = getRetrofit().create(ApiService::class.java).updatePostForo(idPostForo!!,request)
        call.enqueue(object: Callback<PostForoRequest> {
            override fun onResponse(call: Call<PostForoRequest>, response: Response<PostForoRequest>){
                if(response.isSuccessful){
                    findNavController().navigate(EditPostForoFragmentDirections.actionEditPostForoFragmentToMyPostForoFragment())
                }
            }

            override fun onFailure(call: Call<PostForoRequest>, t: Throwable) {
                t.printStackTrace()
            }

        })
    }

}