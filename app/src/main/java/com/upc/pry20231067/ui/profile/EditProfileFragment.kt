package com.upc.pry20231067.ui.profile

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.upc.pry20231067.MainActivity
import com.upc.pry20231067.R
import com.upc.pry20231067.databinding.FragmentEditProfileBinding
import com.upc.pry20231067.databinding.FragmentProfileBinding
import com.upc.pry20231067.models.LoginRequest
import com.upc.pry20231067.models.UpdateUserRequest
import com.upc.pry20231067.models.UpdateUserResponse
import com.upc.pry20231067.models.UserResponse
import com.upc.pry20231067.services.ApiService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class EditProfileFragment : Fragment() {



    var idUser: String? = ""
    val idUserAux: String = "6515fce13559f1d4ca773037"
    private var _binding: FragmentEditProfileBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEditProfileBinding.inflate(layoutInflater, container, false)

        idUser = arguments?.getString("idUser")

        val btnSubmit = binding.editProfileSubmit
        btnSubmit.setOnClickListener{
            updateUser()
//            findNavController().navigate(EditProfileFragmentDirections.actionEditProfileFragmentToNavigationProfile())
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    private fun getRetrofit(): Retrofit {
        return Retrofit.Builder().baseUrl("https://api-ar-app.onrender.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private fun updateUser(){
        val usernameValue = binding.etUsernameEditProfile.text.toString().trim()
        val firstnameValue = binding.etFirstnameEditProfile.text.toString().trim()
        val lastnameValue = binding.etLastnameEditProfile.text.toString().trim()
        val emailValue = binding.etEmailEditProfile.text.toString().trim()
        val updateRequest = UpdateUserRequest(firstnameValue, lastnameValue, usernameValue, emailValue)
        val call = getRetrofit().create(ApiService::class.java).updateUser(idUser!!, updateRequest)
        call.enqueue(object: Callback<UpdateUserResponse>{
            override fun onResponse(call: Call<UpdateUserResponse>,response: Response<UpdateUserResponse>){
                if(response.isSuccessful){
                    findNavController().navigate(EditProfileFragmentDirections.actionEditProfileFragmentToNavigationProfile())

                } else {

                }
            }

            override fun onFailure(call: Call<UpdateUserResponse>, t: Throwable) {
                // Maneja errores de comunicación, como falta de conexión o problemas en el servidor
            }
        })

    }
}