package com.upc.pry20231067.ui.profile

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.fragment.app.FragmentManager
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.upc.pry20231067.LoginActivity
import com.upc.pry20231067.MainActivity
import com.upc.pry20231067.R
import com.upc.pry20231067.databinding.FragmentProfileBinding
import com.upc.pry20231067.models.RegisterResponse
import com.upc.pry20231067.models.ReviewUniqueResponse
import com.upc.pry20231067.models.UserResponseUnique
import com.upc.pry20231067.services.ApiService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.awaitResponse
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


class ProfileFragment : Fragment() {
    //
    var idUser: String? = ""

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    lateinit var progressBar: ProgressBar
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
            var builder: AlertDialog.Builder = AlertDialog.Builder(requireActivity())
            builder.setMessage("Quieres eliminar la cuenta?").setPositiveButton("Confirmar", object: DialogInterface.OnClickListener{
                override fun onClick(dialogInterface: DialogInterface, i: Int){

                    val call = getRetrofit().create(ApiService::class.java).deleteUser("$idUser")
                    call.enqueue(object: Callback<UserResponseUnique>{
                        override fun onResponse(call: Call<UserResponseUnique>, response: Response<UserResponseUnique>){
                            if(response.isSuccessful){
                                val intent = Intent(requireContext(), LoginActivity::class.java)
                                startActivity(intent)
                            }
                        }
                        override fun onFailure(call: Call<UserResponseUnique>, t: Throwable) {

                        }
                    })

                }
            }).setNegativeButton("Cancelar", object: DialogInterface.OnClickListener{
                override fun onClick(dialogInterface: DialogInterface, i: Int){

                }
            })

            var dialog: AlertDialog = builder.create()
            dialog.show()
//            showDialog()
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progressBar = binding.fragmentProfileProgressBar
        getUserInfo()

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

    private fun getUserInfo(){
        progressBar.visibility = View.VISIBLE
        val call = getRetrofit().create(ApiService::class.java).getUserByID("$idUser")
        call.enqueue(object: Callback<UserResponseUnique>{
            override fun onResponse(call: Call<UserResponseUnique>, response: Response<UserResponseUnique>){
                if(response.isSuccessful){
                    progressBar.visibility = View.GONE
                    val userString = response.body()?.data.toString()
//                    val id = userString.substringAfter("_id=").substringBefore(",")
                    val firstname = userString.substringAfter("firstname=").substringBefore(",")
                    val lastname = userString.substringAfter("lastname=").substringBefore(",")
                    val username = userString.substringAfter("username=").substringBefore(",")
                    val urlImageProfile = userString.substringAfter("urlImageProfile=").substringBefore(",")
                    val email = userString.substringAfter("email=").substringBefore(",")//
//
                    binding.tvFirstname.text = firstname
                    binding.tvLastname.text = lastname
                    binding.tvUsername.text = username
                    binding.tvEmail.text = email
                    Glide.with(binding.imageView3.context).load(urlImageProfile).into(binding.imageView3)


                }
            }

            override fun onFailure(call: Call<UserResponseUnique>, t: Throwable) {

            }
        })

    }
}