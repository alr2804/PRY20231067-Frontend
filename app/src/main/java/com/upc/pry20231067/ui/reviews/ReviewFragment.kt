package com.upc.pry20231067.ui.reviews

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.upc.pry20231067.R
import com.upc.pry20231067.data.Review.ReviewProvider
import com.upc.pry20231067.databinding.FragmentReviewBinding
import com.upc.pry20231067.models.Adapter.Review.ReviewAdapter

class ReviewFragment : Fragment() {

    private var _binding: FragmentReviewBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentReviewBinding.inflate(layoutInflater, container, false)

        val btn_add_review = binding.btnAddReview
        btn_add_review.setOnClickListener{
            findNavController().navigate(ReviewFragmentDirections.actionReviewFragmentToAddReviewFragment())
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView(view)
    }

    fun initRecyclerView(view: View){
        val recyclerView =view.findViewById<RecyclerView>(R.id.recycler_view_review)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = ReviewAdapter(ReviewProvider.listReview)
    }
}