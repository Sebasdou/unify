package com.ucc.unify.ui.view.home.newpeople

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.ucc.unify.data.model.Filter
import com.ucc.unify.databinding.FragmentFilterPeopleBinding

class FilterPeopleFragment : Fragment() {
    private val TAG = "FilterPeopleFragment"
    private val db = FirebaseFirestore.getInstance()
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private var _binding: FragmentFilterPeopleBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFilterPeopleBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val user = firebaseAuth.currentUser
        val userId = user?.uid

        if(userId != null) {
            setup(userId)
        }
    }

    private fun setup(userId: String) {
        val adapterMajors = ArrayAdapter<String>(
            binding.root.context, android.R.layout.simple_spinner_item
        )

        adapterMajors.addAll(
            listOf("Ingenieria en Sistemas", "Comunicaciones", "Mecatronica",
                "Psicologia", "Derecho", "Arquitectura")
        )

        val adapterAges = ArrayAdapter<Int>(
            binding.root.context, android.R.layout.simple_spinner_item
        )

        val ageList = ArrayList<Int>()

        for(i in 17..50) {
            ageList.add(i)
        }

        adapterAges.addAll(ageList)

        val adapterSexes = ArrayAdapter<String>(
            binding.root.context, android.R.layout.simple_spinner_dropdown_item
        )

        adapterSexes.addAll(listOf("Mujer", "Hombre"))

        binding.SpinnerMajor.adapter = adapterMajors
        binding.SpinnerAgeFrom.adapter = adapterAges
        binding.SpinnerAgeTo.adapter = adapterAges
        binding.SpinnerSex.adapter = adapterSexes

        binding.SaveButton.setOnClickListener {
            val selectedMajor = binding.SpinnerMajor.selectedItem.toString()
            val selectedAgeFrom = binding.SpinnerAgeFrom.selectedItem.toString().toLong()
            val selectedAgeTo = binding.SpinnerAgeTo.selectedItem.toString().toLong()
            val selectedSex = binding.SpinnerSex.selectedItem.toString()

            if(selectedAgeTo - selectedAgeFrom > 9) {
                showAlert("Elija un rango de edades más pequeño.")
                return@setOnClickListener
            }

            try {
                val filter = Filter(
                    selectedAgeFrom, selectedAgeTo, selectedSex, selectedMajor
                )

                db.collection("users").document(userId).update(
                    "filter", filter
                )
            } catch(e: FirebaseFirestoreException) {
                showAlert("Ha ocurrido un error al actualizar sus datos.")
                Log.e(TAG, e.code.toString())
            }

            findNavController().popBackStack()

        }
    }

    private fun showAlert(mensaje: String){
        val builder = AlertDialog.Builder(binding.root.context)
        builder.setTitle("Error")
        builder.setMessage(mensaje)
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }
}