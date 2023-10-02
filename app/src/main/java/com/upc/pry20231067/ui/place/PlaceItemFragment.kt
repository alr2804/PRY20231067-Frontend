package com.upc.pry20231067.ui.place

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.upc.pry20231067.R
import com.upc.pry20231067.databinding.FragmentPlaceItemBinding


class PlaceItemFragment : Fragment() {

    private var _binding: FragmentPlaceItemBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentPlaceItemBinding.inflate(layoutInflater, container, false)

        val idPlace = arguments?.getString("idPlace")

        val name = idPlace?.substringAfter("name=")?.substringBefore(",")
        val description = idPlace?.substringAfter("description=")?.substringBefore(",")
        val photo = idPlace?.substringAfter("photo=")?.dropLast(1)


        binding.titlePlaceItem.text = name
        binding.descriptionPlaceItem.text = description
        val img_place_item = binding.imgPlaceItem
        Glide.with(img_place_item.context).load(photo).into(img_place_item)

        val btn_ver_reseñas = binding.btnResenas
        btn_ver_reseñas.setOnClickListener{
            findNavController().navigate(PlaceItemFragmentDirections.actionPlaceItemFragmentToReviewFragment())
        }

        return binding.root


    }

}