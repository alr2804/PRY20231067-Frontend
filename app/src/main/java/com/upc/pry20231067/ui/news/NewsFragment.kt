package com.upc.pry20231067.ui.news

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.upc.pry20231067.R
import com.upc.pry20231067.data.News.NewProvider
import com.upc.pry20231067.databinding.FragmentCollectibleBinding
import com.upc.pry20231067.databinding.FragmentNewsBinding
import com.upc.pry20231067.models.Adapter.News.NewsAdapter

class NewsFragment : Fragment() {

    private var _binding: FragmentNewsBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNewsBinding.inflate(layoutInflater, container, false)

        val btnFavoriteNews = binding.btnNewsFavorites
        btnFavoriteNews.setOnClickListener {
            findNavController().navigate(NewsFragmentDirections.actionNavigationNewsToFavoriteNewsFragment())
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView(view)
    }

    fun initRecyclerView(view: View){
        val recyclerView =view.findViewById<RecyclerView>(R.id.recycler_view_news)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = NewsAdapter(NewProvider.listNews)
    }
}