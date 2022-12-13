package com.ucc.unify.ui.view.home.blogs

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ucc.unify.R
import com.ucc.unify.databinding.FragmentBlogsBinding

class BlogsFragment : Fragment() {
    private var _binding: FragmentBlogsBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        return binding.root
    }
}