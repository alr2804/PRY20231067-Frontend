package com.upc.pry20231067.ui.foro.adapter

import android.content.DialogInterface
import android.content.Intent
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.FragmentActivity
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.upc.pry20231067.LoginActivity
import com.upc.pry20231067.R
import com.upc.pry20231067.models.UserResponseUnique
import com.upc.pry20231067.services.ApiService
import com.upc.pry20231067.ui.foro.MyPostForoFragmentDirections
import com.upc.pry20231067.ui.foro.dao.PostForoRequest
import com.upc.pry20231067.ui.foro.models.PostForo
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class MyForoViewHolder(view: View, activity: FragmentActivity?): ViewHolder(view) {

    val ac = activity
    val navigator = Navigation.findNavController(activity = activity!!, R.id.nav_host_fragment_activity_main)

    val contentItem = view.findViewById<TextView>(R.id.tv_content_item_post_foro)
    val btnEditPostForo = view.findViewById<ImageButton>(R.id.btn_edit_my_post_foro)
    val btnDeletePostForo = view.findViewById<ImageButton>(R.id.btn_delete_my_post_foro)

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
    fun render(postForoModel: PostForo){
        contentItem.text = postForoModel.content

        btnEditPostForo.setOnClickListener{
            navigator.navigate(MyPostForoFragmentDirections.actionMyPostForoFragmentToEditPostForoFragment("${postForoModel.toString()}"))
        }

        btnDeletePostForo.setOnClickListener {
            var builder: AlertDialog.Builder = AlertDialog.Builder(ac!!)
            builder.setMessage("Quieres eliminar la publicación?").setPositiveButton("Confirmar", object: DialogInterface.OnClickListener{
                override fun onClick(dialogInterface: DialogInterface, i: Int){
                    val call = getRetrofit().create(ApiService::class.java).deletePostForo("${postForoModel._id}")
                    call.enqueue(object: Callback<PostForoRequest>{
                        override fun onResponse(call: Call<PostForoRequest>, response: Response<PostForoRequest>){

                        }

                        override fun onFailure(call: Call<PostForoRequest>, t: Throwable) {

                        }
                    })

                }
            }).setNegativeButton("Cancelar", object: DialogInterface.OnClickListener{
                override fun onClick(dialogInterface: DialogInterface, i: Int){

                }
            })

            var dialog: AlertDialog = builder.create()
            dialog.show()
        }

    }
}