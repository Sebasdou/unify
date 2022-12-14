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
            val userEmail = user.email

            try {
                db.collection("users").document(userId).get()
                    .addOnSuccessListener { doc ->
                        if (!doc.getBoolean("emailVerificationSent")!!) {
                            user.sendEmailVerification().addOnCompleteListener { secondTask ->
                                if (secondTask.isSuccessful) {
                                    Log.d(TAG, "Email de confirmación enviado a $userEmail.")
                                    try {
                                        db.collection("users").document(userId).update(
                                            "emailVerificationSent", true
                                        )
                                    } catch (e: FirebaseFirestoreException) {
                                        Log.e(TAG, e.code.toString())
                                    }
                                } else {
                                    Log.d(
                                        TAG,
                                        "Error al enviar email de confirmación a $userEmail."
                                    )
                                }
                            }
                        }
                    }
            } catch (e: FirebaseFirestoreException) {
                Log.e(TAG, e.code.toString())
            }

            setup(userId)
        }
    }

    private fun setup(userId: String) {
        val checkboxes = listOf(
            binding.LibrosCheck, binding.FutbolCheck,
            binding.CineCheck, binding.MusicaCheck,
            binding.BaileCheck, binding.EjercicioCheck
        )

        /*val librosCheck : CheckBox = binding.LibrosCheck
        val futbolCheck : CheckBox = binding
        val cineCheck : CheckBox = binding
        val musicaCheck : CheckBox = binding
        val baileCheck : CheckBox = binding
        val ejercicioCheck : CheckBox = binding*/

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

        for(i in 1..100) {
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
            val checkedInterests = ArrayList<String>()

            for(checkbox in checkboxes) {
                if(checkbox.isChecked) {
                    checkedInterests.add(checkbox.text.toString())
                }
            }

            val selectedMajor = binding.SpinnerMajor.selectedItem.toString()
            val selectedAgeFrom = binding.SpinnerAgeFrom.selectedItem.toString().toLong()
            val selectedAgeTo = binding.SpinnerAgeTo.selectedItem.toString().toLong()
            val selectedSex = binding.SpinnerSex.selectedItem.toString()

            try {
                val filter = Filter(
                    selectedAgeFrom, selectedAgeTo, selectedSex, checkedInterests, selectedMajor
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