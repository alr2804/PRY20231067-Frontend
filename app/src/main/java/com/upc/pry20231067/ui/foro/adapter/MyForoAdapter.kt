package com.upc.pry20231067.ui.foro.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.upc.pry20231067.R
import com.upc.pry20231067.ui.foro.models.PostForo


class MyForoAdapter(private val postForoList: List<PostForo>, activity: FragmentActivity?): RecyclerView.Adapter<MyForoViewHolder>() {

    val ac = activity
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyForoViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return MyForoViewHolder(layoutInflater.inflate(R.layout.item_my_post_foro, parent, false), ac)
    }

    override fun getItemCount(): Int {
        return postForoList.size
    }

    override fun onBindViewHolder(holder: MyForoViewHolder, position: Int) {
        val item = postForoList[position]
        holder.render(item)
    }

}