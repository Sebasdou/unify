package com.ucc.unify.ui.common

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.ucc.unify.R
import com.ucc.unify.databinding.DialogFragmentFilterBinding

class FilterDialogFragment : DialogFragment() {
    private lateinit var options: Array<String>
    private var title = ""
    private var _binding: DialogFragmentFilterBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        options = arguments?.getStringArray(ITEMS) ?: emptyArray()
        title = arguments?.getString(TITLE) ?: "Sín título"

    }
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = DialogFragmentFilterBinding.inflate(LayoutInflater.from(context))
        binding.listFilter.adapter = ArrayAdapter(binding.root.context,
            R.layout.dialog_fragment_filter, R.id.text_view, options)
        return AlertDialog.Builder(requireContext())
            .setView(binding.root)
            .setTitle(title)
            .create()
    }
    companion object {
        private const val ITEMS = "items"
        private const val TITLE= "title"
        fun newInstance(items: Array<String>, title: String ): FilterDialogFragment =
            FilterDialogFragment().apply {
                arguments = Bundle().apply {
                    putStringArray(ITEMS, items)
                    putString(TITLE, title)
                }
            }
    }
}
