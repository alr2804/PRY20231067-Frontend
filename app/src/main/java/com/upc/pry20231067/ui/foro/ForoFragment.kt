package com.upc.pry20231067.ui.foro

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.upc.pry20231067.R
import com.upc.pry20231067.data.Foro.PostsForoProvider
import com.upc.pry20231067.databinding.FragmentForoBinding
import com.upc.pry20231067.models.Adapter.Foro.ForoAdapter

class ForoFragment : Fragment() {

    private var _binding: FragmentForoBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentForoBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView(view)

    }

    fun initRecyclerView(view: View){
        val recyclerView =view.findViewById<RecyclerView>(R.id.recycler_view_foro)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = ForoAdapter(PostsForoProvider.listPostsForo)
    }
}