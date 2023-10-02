package com.upc.pry20231067.models.Adapter.Souvenir

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.upc.pry20231067.R
import com.upc.pry20231067.data.Souvenir.Souvenir


class SouvenirAdapter(private val souvenirList: List<Souvenir>): RecyclerView.Adapter<SouvenirViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SouvenirViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return SouvenirViewHolder(layoutInflater.inflate(R.layout.item_souvenir, parent, false))
    }

    override fun getItemCount(): Int {
        return souvenirList.size
    }

    override fun onBindViewHolder(holder: SouvenirViewHolder, position: Int) {
        val item = souvenirList[position]
        holder.render(item)
    }

}