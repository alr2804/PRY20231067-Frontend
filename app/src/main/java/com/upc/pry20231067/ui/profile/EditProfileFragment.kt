package com.upc.pry20231067.ui.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.upc.pry20231067.R
import com.upc.pry20231067.databinding.FragmentEditProfileBinding
import com.upc.pry20231067.databinding.FragmentProfileBinding


class EditProfileFragment : Fragment() {

    private var _binding: FragmentEditProfileBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEditProfileBinding.inflate(layoutInflater, container, false)

        val btnSubmit = binding.editProfileSubmit
        btnSubmit.setOnClickListener{
            findNavController().navigate(EditProfileFragmentDirections.actionEditProfileFragmentToNavigationProfile())
        }

        return binding.root
    }
}