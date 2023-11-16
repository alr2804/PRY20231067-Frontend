package com.upc.pry20231067.ui.foro.adapter

import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.upc.pry20231067.R
import com.upc.pry20231067.ui.foro.models.PostForo

class ForoViewHolder(view: View): ViewHolder(view) {

    val contentItem = view.findViewById<TextView>(R.id.tv_content_item_post_foro)

    fun render(newModel: PostForo){
        contentItem.text = newModel.content

        itemView.setOnClickListener{
            print("Click item")
//            Navigation.findNavController(itemView).navigate(HomeFragmentDirections.actionHomeFragmentToPlaceItemFragment())
            Toast.makeText(contentItem.context, "foro view holder", Toast.LENGTH_SHORT).show()
        }
    }
}