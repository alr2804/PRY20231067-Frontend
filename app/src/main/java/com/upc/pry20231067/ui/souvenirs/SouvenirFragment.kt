package com.upc.pry20231067.ui.souvenirs

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.upc.pry20231067.R
import com.upc.pry20231067.data.Souvenir.SouvenirProvider
import com.upc.pry20231067.databinding.FragmentCollectibleBinding
import com.upc.pry20231067.databinding.FragmentSouvenirBinding
import com.upc.pry20231067.models.Adapter.Souvenir.SouvenirAdapter

class SouvenirFragment : Fragment() {

    private var _binding: FragmentSouvenirBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentSouvenirBinding.inflate(layoutInflater, container, false)



        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView(view)
    }

    fun initRecyclerView(view: View){
        val recyclerView =view.findViewById<RecyclerView>(R.id.recycler_view_souvenir)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = SouvenirAdapter(SouvenirProvider.listSouvenir)
    }
}