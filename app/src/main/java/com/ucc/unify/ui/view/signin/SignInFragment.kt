package com.ucc.unify.ui.view.signin

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
import com.ucc.unify.R
import com.ucc.unify.data.model.Filter
import com.ucc.unify.databinding.FragmentSignInBinding

class SignInFragment : Fragment() {

    private val TAG = "SignInFragment"
    private val db = FirebaseFirestore.getInstance()
    private val firebaseAuth = FirebaseAuth.getInstance()
    private var _binding: FragmentSignInBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignInBinding.inflate(inflater, container, false)
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
                        val isEmailSent : Boolean? = doc.getBoolean("emailVerificationSent")
                        if(isEmailSent == false || isEmailSent == null) {
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

    private fun setup(user: FirebaseUser,userId: String) {
        val nameEditText = binding.generalData.EditTextName

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

        for(i in 17..80) {
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

        binding.SignInButton.setOnClickListener {
            val name = nameEditText.text.toString()

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

            val filter = Filter(17, 26, oppositeSex, selectedMajor)

            try {
                val profileUpdates = userProfileChangeRequest {
                    displayName = name
                }

                user.updateProfile(profileUpdates)

                val newUserInfo = hashMapOf(
                    "name" to name,
                    "interests" to checkedInterests,
                    "major" to selectedMajor,
                    "age" to selectedAge,
                    "sex" to selectedSex,
                    "hasProfileData" to true,
                    "filter" to filter
                )

                db.collection("users").document(userId).set(newUserInfo)
            } catch(e: FirebaseFirestoreException) {
                showAlert("Ha ocurrido un error al actualizar sus datos.")
                Log.e(TAG, e.code.toString())
            }

            findNavController().navigate(R.id.homeFragment)
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
