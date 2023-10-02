package com.upc.pry20231067.ui.collectible

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.upc.pry20231067.R
import com.upc.pry20231067.databinding.FragmentCollectibleBinding
import com.upc.pry20231067.databinding.FragmentProfileBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [CollectibleFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CollectibleFragment : Fragment() {

    private var _binding: FragmentCollectibleBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCollectibleBinding.inflate(layoutInflater, container, false)

        
        return binding.root
    }

}