package com.pry20231067.oficial.adapter.Place

import android.app.Activity
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.upc.pry20231067.R
import com.upc.pry20231067.data.Place.Place
import com.upc.pry20231067.ui.home.HomeFragment
import com.upc.pry20231067.ui.home.HomeFragmentDirections


class PlaceViewHolder(view: View, activity: FragmentActivity?): ViewHolder(view) {

    val navigator = Navigation.findNavController(
        activity = activity!!,
        R.id.nav_host_fragment_activity_main)
    val placeName = view.findViewById<TextView>(R.id.tv_name)
    val placeDescription = view.findViewById<TextView>(R.id.tv_description)
    val photo = view.findViewById<ImageView>(R.id.imgview_place)

    fun render(placeModel: Place){
        placeName.text = placeModel.name
        placeDescription.text = placeModel.description
        Glide.with(photo.context).load(placeModel.photo).into(photo)

        itemView.setOnClickListener{
            print("Click item")
            navigator.navigate(HomeFragmentDirections.actionNavigationHomeToPlaceItemFragment("${placeModel.toString()}"))
            Toast.makeText(photo.context, placeModel.name, Toast.LENGTH_SHORT).show()
        }
    }
}