package com.ucc.unify.ui.view.chats

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.navigation.fragment.findNavController
import com.ucc.unify.R
import com.ucc.unify.databinding.FragmentFilterChatsBinding

class FilterChatsFragment : Fragment() {
    private var _binding: FragmentFilterChatsBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentFilterChatsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val interestsAdapter = ArrayAdapter(binding.root.context, android.R.layout.simple_spinner_item,
            arrayOf("Libros", "Fútbol", "Películas", "Música", "Baile", "Gimnasio")
        )
        val majorAdapter = ArrayAdapter(binding.root.context, android.R.layout.simple_spinner_item,
            arrayOf("Ingeniería Bioquímica", "Ingeniería en Sistemas Computacionales",
                "Ingeniería Mecánica", "Ingeniería Electríca", "Ingeniería Mecatrónica")
        )
        val ageAdapter = ArrayAdapter(binding.root.context, android.R.layout.simple_spinner_item,
            (17..30).map { "$it" }
        )
        val sexAdapter = ArrayAdapter(binding.root.context, android.R.layout.simple_spinner_item,
            arrayOf("Hombre", "Mujer")
        )
        binding.spGender.adapter = sexAdapter
        binding.spAgeTo.adapter = ageAdapter
        binding.spAgeFrom.adapter = ageAdapter
        binding.spMajor.adapter = majorAdapter
        binding.spInterests.adapter = interestsAdapter

        binding.btnFilter.setOnClickListener { findNavController().popBackStack() }
    }
}