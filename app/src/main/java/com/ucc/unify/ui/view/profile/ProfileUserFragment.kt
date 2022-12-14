package com.ucc.unify.ui.view.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import androidx.navigation.fragment.findNavController
import com.ucc.unify.R
import com.ucc.unify.databinding.FragmentProfileUserBinding

class ProfileUserFragment : Fragment() {
    private var _binding: FragmentProfileUserBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentProfileUserBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val options = arrayOf("Datos generales", "Privacidad", "Personalización",
            "Preferencias de institución", "Preferencias de comunidad",
            "Ayuda", "Comentarios"
        )
        binding.listOptions.adapter = ArrayAdapter(binding.root.context,
            R.layout.item_list, R.id.text_view, options)
        binding.listOptions.setOnItemClickListener { _, _, i, _ ->
            when(i){
                0 -> { findNavController().navigate(R.id.generalDataFragment) }
            }
        }

    }
}