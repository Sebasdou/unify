package com.ucc.unify.ui.view.chats

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.navigation.fragment.findNavController
import com.ucc.unify.R
import com.ucc.unify.databinding.FragmentChatsBinding
import com.ucc.unify.ui.common.FilterDialogFrament

class ChatsFragment : Fragment() {
    private var _binding: FragmentChatsBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentChatsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val chats = arrayOf("María Rivero", "Juan Escutia", "Francisco Madero", "Rodolfo Márquez")

        binding.listChats.adapter = ArrayAdapter(binding.root.context,
            R.layout.item_chat, R.id.text_view, chats)
        binding.searchView.btnFilter.setOnClickListener {
            findNavController().navigate(R.id.filterChatsFragment)
        }
        binding.listChats.setOnItemClickListener { _, _, i, _ ->
            when(i) {
                0 -> { findNavController().navigate(R.id.chatFragment) }
            }
        }
    }
}