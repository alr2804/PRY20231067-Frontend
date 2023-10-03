package com.upc.pry20231067.ui.reviews

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.upc.pry20231067.R
import com.upc.pry20231067.data.Place.Place
import com.upc.pry20231067.data.Review.Review
import com.upc.pry20231067.data.Review.ReviewProvider
import com.upc.pry20231067.databinding.FragmentReviewBinding
import com.upc.pry20231067.models.Adapter.Place.PlaceAdapter
import com.upc.pry20231067.models.Adapter.Review.ReviewAdapter
import com.upc.pry20231067.models.PlaceResponse
import com.upc.pry20231067.models.ReviewResponse
import com.upc.pry20231067.services.ApiService
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.SocketTimeoutException
import java.util.concurrent.TimeUnit

class ReviewFragment : Fragment() {

    var idPlace: String? = ""
    var idUser: String? = ""
    private var _binding: FragmentReviewBinding? = null

    private val reviewList = mutableListOf<Review>()
    private lateinit var adapter: ReviewAdapter
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentReviewBinding.inflate(layoutInflater, container, false)

        idPlace = arguments?.getString("idPlace")
        idUser = activity?.intent?.getStringExtra("idUser")

        val btn_add_review = binding.btnAddReview
        btn_add_review.setOnClickListener{
            findNavController().navigate(ReviewFragmentDirections.actionReviewFragmentToAddReviewFragment(idPlace!!, idUser!!))
        }

        getReviews()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView(view)
    }

    fun initRecyclerView(view: View){
        val recyclerView =view.findViewById<RecyclerView>(R.id.recycler_view_review)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = ReviewAdapter(reviewList)
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

    private fun getReviews(){
        val call = getRetrofit().create(ApiService::class.java).getReviewByPlaceID(idPlace!!)

        call.enqueue(object: Callback<ReviewResponse> {
            override fun onResponse(call: Call<ReviewResponse>, response: Response<ReviewResponse>){
                if(response.isSuccessful){
                    val places = response.body()
                    val placeData = places?.data ?: emptyList()
                    reviewList.clear()
                    reviewList.addAll(placeData)
                    adapter.notifyDataSetChanged()

                }
            }
            override fun onFailure(call: Call<ReviewResponse>, t: Throwable) {
                t.printStackTrace()
                if(t is SocketTimeoutException){
                    Toast.makeText(requireContext(), "Tiempo excedido de espera", Toast.LENGTH_SHORT)
                }
            }
        })

    }
}