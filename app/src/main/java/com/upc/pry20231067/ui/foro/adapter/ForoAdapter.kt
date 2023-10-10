package com.upc.pry20231067.ui.foro.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.upc.pry20231067.R
import com.upc.pry20231067.ui.foro.models.PostForo


class ForoAdapter(private val postForoList: List<PostForo>): RecyclerView.Adapter<ForoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ForoViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ForoViewHolder(layoutInflater.inflate(R.layout.item_post_foro, parent, false))
    }

    override fun getItemCount(): Int {
        return postForoList.size
    }

    override fun onBindViewHolder(holder: ForoViewHolder, position: Int) {
        val item = postForoList[position]
        holder.render(item)
    }

}