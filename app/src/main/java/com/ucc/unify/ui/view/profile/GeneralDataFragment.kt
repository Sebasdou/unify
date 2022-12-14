package com.ucc.unify.ui.view.profile

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
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.ucc.unify.data.model.Filter
import com.ucc.unify.databinding.FragmentGeneralDataBinding

class GeneralDataFragment : Fragment() {
    private val TAG = "GeneralDataFragment"
    private val db = FirebaseFirestore.getInstance()
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private var _binding: FragmentGeneralDataBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGeneralDataBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val user = firebaseAuth.currentUser
        val userId = user?.uid

        if (userId != null) {
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

            setup(user, userId)
        }
    }

    private fun setup(user: FirebaseUser, userId: String) {
        val editTextName = binding.generalData.EditTextName

        val checkboxes = listOf(
            binding.generalData.LibrosCheck, binding.generalData.FutbolCheck,
            binding.generalData.CineCheck, binding.generalData.MusicaCheck,
            binding.generalData.BaileCheck, binding.generalData.EjercicioCheck
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

        binding.generalData.SpinnerMajor.adapter = adapterMajors
        binding.generalData.SpinnerAge.adapter = adapterAges
        binding.generalData.SpinnerSex.adapter = adapterSexes

        binding.SaveButton.setOnClickListener {
            val name = editTextName.text.toString()
            val checkedInterests = ArrayList<String>()

            for(checkbox in checkboxes) {
                if(checkbox.isChecked) {
                    checkedInterests.add(checkbox.text.toString())
                }
            }

            val selectedMajor = binding.generalData.SpinnerMajor.selectedItem.toString()
            val selectedAge = binding.generalData.SpinnerAge.selectedItem.toString().toInt()
            val selectedSex = binding.generalData.SpinnerSex.selectedItem.toString()

            val oppositeSex = if(selectedSex == "Mujer") "Hombre" else "Mujer"
            val interests = arrayListOf(
                "Libros", "Futbol", "Cine", "Musica", "Baile", "Ejercicio"
            )

            val filter = Filter(17, 30, oppositeSex, interests, selectedMajor)

            try {
                val profileUpdates = userProfileChangeRequest {
                    displayName = name
                }

                user.updateProfile(profileUpdates)

                db.collection("users").document(userId).update(
                    "name", name,
                    "interests", checkedInterests,
                    "major", selectedMajor,
                    "age", selectedAge,
                    "sex", selectedSex,
                    "hasProfileData", true,
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
