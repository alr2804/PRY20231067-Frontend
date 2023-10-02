package com.upc.pry20231067.models.Adapter.Place


import android.view.LayoutInflater

import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.pry20231067.oficial.adapter.Place.PlaceViewHolder
import com.upc.pry20231067.R

import com.upc.pry20231067.data.Place.Place


class PlaceAdapter(private val placeList: List<Place>, activity: FragmentActivity?): RecyclerView.Adapter<PlaceViewHolder>() {

    val ac = activity
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaceViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return PlaceViewHolder(layoutInflater.inflate(R.layout.item_place, parent, false), ac)
    }

    override fun getItemCount(): Int {
        return placeList.size
    }

    override fun onBindViewHolder(holder: PlaceViewHolder, position: Int) {
        val item = placeList[position]

        holder.render(item)
    }

}