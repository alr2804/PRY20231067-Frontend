package com.upc.pry20231067.models.Adapter.Foro

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.upc.pry20231067.R
import com.upc.pry20231067.data.Foro.PostForo

class ForoViewHolder(view: View): ViewHolder(view) {

    val titleItem = view.findViewById<TextView>(R.id.tv_title_item_post_foro)
    val contentItem = view.findViewById<TextView>(R.id.tv_content_item_post_foro)

    fun render(newModel: PostForo){
        titleItem.text = newModel.title
        contentItem.text = newModel.content

        itemView.setOnClickListener{
            print("Click item")
//            Navigation.findNavController(itemView).navigate(HomeFragmentDirections.actionHomeFragmentToPlaceItemFragment())
            Toast.makeText(titleItem.context, newModel.title, Toast.LENGTH_SHORT).show()
        }
    }
}