package com.upc.pry20231067.ui.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.upc.pry20231067.R
import com.upc.pry20231067.databinding.FragmentProfileBinding


class ProfileFragment : Fragment() {
    //

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentProfileBinding.inflate(layoutInflater, container, false)

        val cardSouvenir = binding.cardSouvenirs
        cardSouvenir.setOnClickListener{
            findNavController().navigate(ProfileFragmentDirections.actionNavigationProfileToSouvenirFragment())
        }

        val btnEdit = binding.btnEditProfile
        btnEdit.setOnClickListener {
            findNavController().navigate(ProfileFragmentDirections.actionNavigationProfileToEditProfileFragment())
        }

        val btnDelete = binding.btnDeleteAccount
        btnDelete.setOnClickListener {
//            showDialog()
        }
        return binding.root
    }
}