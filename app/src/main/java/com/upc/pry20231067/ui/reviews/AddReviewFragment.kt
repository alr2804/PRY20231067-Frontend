package com.upc.pry20231067.ui.reviews

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.upc.pry20231067.R
import com.upc.pry20231067.databinding.FragmentAddReviewBinding
import com.upc.pry20231067.models.ReviewRequest
import com.upc.pry20231067.models.ReviewResponseUnique
import com.upc.pry20231067.models.ReviewUniqueResponse
import com.upc.pry20231067.models.UpdateUserResponse
import com.upc.pry20231067.services.ApiService
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class AddReviewFragment : Fragment() {

    var idPlace: String? = ""
    var idUser: String? = ""

    private var _binding: FragmentAddReviewBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddReviewBinding.inflate(layoutInflater, container, false)

        idPlace = arguments?.getString("idPlace")
        idUser = arguments?.getString("idUser")

        val rating = binding.ratingBarAddReview
        val textRatingvalue = binding.tvPruebaReting
        val btnSubmit = binding.btnSubmitAddReview
        rating.setOnRatingBarChangeListener{ ratingB, fl, b ->
            textRatingvalue.text = ratingB.rating.toString()
        }

        btnSubmit.setOnClickListener{
            addReview()
        }
        return  binding.root
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


    fun addReview(){
        val content = binding.etCommentAddReview.text.toString().trim()
        val rating = binding.ratingBarAddReview.rating
        val reviewReq = ReviewRequest(content, rating, idUser!!, idPlace!!)
        val call = getRetrofit().create(ApiService::class.java).createReview(reviewReq)

        call.enqueue(object: Callback<ReviewUniqueResponse> {
            override fun onResponse(call: Call<ReviewUniqueResponse>, response: Response<ReviewUniqueResponse>){
                if(response.isSuccessful){

                    findNavController().navigate(AddReviewFragmentDirections.actionAddReviewFragmentToReviewFragment())

                }
            }

            override fun onFailure(call: Call<ReviewUniqueResponse>, t: Throwable) {

            }



        })
    }

}