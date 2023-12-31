package com.upc.pry20231067.ui.foro

import android.os.Bundle
import android.text.Editable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.upc.pry20231067.R
import com.upc.pry20231067.ui.foro.models.PostForo
import com.upc.pry20231067.databinding.FragmentForoBinding
import com.upc.pry20231067.ui.foro.adapter.ForoAdapter
import com.upc.pry20231067.ui.foro.dao.PostForoResponse
import com.upc.pry20231067.services.ApiService
import com.upc.pry20231067.services.RetrofitClient
import com.upc.pry20231067.ui.foro.dao.PostForoRequest
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class ForoFragment : Fragment() {

//    lateinit var tabLayout: TabLayout
//    lateinit var viewPager: ViewPager2
//    lateinit var viewPagerAdapter: ViewPagerAdapter

    var idUser: String? = ""
    private var _binding: FragmentForoBinding? = null
    private val binding get() = _binding!!

    private val postForoList = mutableListOf<PostForo>()
    private lateinit var adapter: ForoAdapter
    lateinit var progressBar: ProgressBar
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentForoBinding.inflate(layoutInflater, container, false)

        idUser = activity?.intent?.getStringExtra("idUser")

        buttonsNavigation()

        progressBar = binding.fragmentForoProgressBar
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getPostForo()
        initRecyclerView(view)

    }

    fun initRecyclerView(view: View){
        val recyclerView =view.findViewById<RecyclerView>(R.id.recycler_view_foro)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = ForoAdapter(postForoList)
        recyclerView.adapter = adapter
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

    fun buttonsNavigation(){
        var btnMypost = binding.btnMyPostForo
        btnMypost.setOnClickListener{
            findNavController().navigate(ForoFragmentDirections.actionForoFragmentToMyPostForoFragment())
        }
    }

    private fun getPostForo(){
        progressBar.visibility = View.VISIBLE
        val call = getRetrofit().create(ApiService::class.java).getPostsForo()
        call.enqueue(object: Callback<PostForoResponse> {
            override fun onResponse(call: Call<PostForoResponse>, response: Response<PostForoResponse>){
                val postsForo = response.body()
                if(response.isSuccessful){
                    progressBar.visibility = View.GONE
                    val postForoData = postsForo?.data ?: emptyList()

                    postForoList.clear()
                    postForoList.addAll(postForoData)
                    adapter.notifyDataSetChanged()
                }
            }

            override fun onFailure(call: Call<PostForoResponse>, t: Throwable) {
                progressBar.visibility = View.GONE
            }
        })
    }
}