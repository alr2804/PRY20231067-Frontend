package com.upc.pry20231067.ui.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.FragmentManager
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.upc.pry20231067.R
import com.upc.pry20231067.databinding.FragmentProfileBinding
import com.upc.pry20231067.services.ApiService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.awaitResponse
import retrofit2.converter.gson.GsonConverterFactory


class ProfileFragment : Fragment() {
    //
    var idUser: String? = ""

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentProfileBinding.inflate(layoutInflater, container, false)

        idUser = activity?.intent?.getStringExtra("idUser")


        val cardSouvenir = binding.cardSouvenirs
        cardSouvenir.setOnClickListener{
            findNavController().navigate(ProfileFragmentDirections.actionNavigationProfileToSouvenirFragment())
        }

        val btnEdit = binding.btnEditProfile
        btnEdit.setOnClickListener {
            findNavController().navigate(ProfileFragmentDirections.actionNavigationProfileToEditProfileFragment("$idUser"))
        }

        val btnDelete = binding.btnDeleteAccount
        btnDelete.setOnClickListener {
//            showDialog()
        }

        getUserInfo()
        return binding.root
    }

    private fun getRetrofit(): Retrofit {
        return Retrofit.Builder().baseUrl("https://api-ar-app.onrender.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private fun getUserInfo(){
        CoroutineScope(Dispatchers.IO).launch {
            val call = getRetrofit().create(ApiService::class.java).getUserByID("$idUser")
            val response = call.body()
            activity?.runOnUiThread{
                if(call.isSuccessful){
                    val userData = response?.data
                    val userString = userData.toString()

                    val id = userString?.substringAfter("_id=")?.substringBefore(",")
                    val firstname = userString?.substringAfter("firstname=")?.substringBefore(",")
                    val lastname = userString?.substringAfter("lastname=")?.substringBefore(",")
                    val username = userString?.substringAfter("username=")?.substringBefore(",")
                    val urlImageProfile = userString?.substringAfter("urlImageProfile=")?.substringBefore(",")
                    val email = userString?.substringAfter("email=")?.substringBefore(",")//

                    binding.tvFirstname.text = firstname
                    binding.tvLastname.text = lastname
                    binding.tvUsername.text = username
                    binding.tvEmail.text = email
                    Glide.with(binding.imageView3.context).load(urlImageProfile).into(binding.imageView3)
//                    placeList.addAll(placeData)
//                    adapter.notifyDataSetChanged()
                }
            }
        }
    }
}