package com.ucc.unify.ui.view.groups

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.ucc.unify.R
import com.ucc.unify.databinding.FragmentGroupsBinding
import com.ucc.unify.ui.common.FilterDialogFrament

class GroupsFragment : Fragment() {
    private var _binding: FragmentGroupsBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentGroupsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val groups = arrayOf("Basquetbol", "Voleibol", "Pastoral",
            "Ajedrez", "Acondicionamiento de pesa",
            "eSports", "Lectura"
        )
        binding.listGroups.adapter = ArrayAdapter(binding.root.context,
            R.layout.item_list, R.id.text_view, groups)
        binding.searchView.btnFilter.setOnClickListener {
            val dialog = FilterDialogFrament.newInstance(
                arrayOf("Nombre", "Categoría", "Fecha"),
                "Filtrar por: "
            )
            dialog.show(childFragmentManager, "dialog")
        }
    }
}