package com.upc.pry20231067.ui.reviews

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.upc.pry20231067.R
import com.upc.pry20231067.databinding.FragmentAddReviewBinding

class AddReviewFragment : Fragment() {

    private var _binding: FragmentAddReviewBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddReviewBinding.inflate(layoutInflater, container, false)

        val rating = binding.ratingBarAddReview
        val textRatingvalue = binding.tvPruebaReting
        val btnSubmit = binding.btnSubmitAddReview
        rating.setOnRatingBarChangeListener{ ratingB, fl, b ->
            textRatingvalue.text = ratingB.rating.toString()
        }

        btnSubmit.setOnClickListener{
            findNavController().navigate(AddReviewFragmentDirections.actionAddReviewFragmentToReviewFragment())
        }
        return  binding.root
    }

}