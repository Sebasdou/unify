package com.ucc.unify.ui.view.home

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.ucc.unify.R
import com.ucc.unify.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {
    private val firebaseAuth = FirebaseAuth.getInstance()
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val user = firebaseAuth.currentUser
        val userId = user!!.uid

        val sessionPrefs = binding.root.context.getSharedPreferences(
            getString(R.string.prefs_file), Context.MODE_PRIVATE
        ).edit()
        sessionPrefs.putString("userId", userId)
        sessionPrefs.apply()

        binding.toNewPeople.item.setOnClickListener {
            findNavController().navigate(R.id.newPeopleFragment)
        }
        binding.toBlog.item.setOnClickListener {
            findNavController().navigate(R.id.blogsFragment)
        }

    }
}