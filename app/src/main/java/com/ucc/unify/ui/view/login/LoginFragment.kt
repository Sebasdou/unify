package com.ucc.unify.ui.view.login

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.ucc.unify.R
import com.ucc.unify.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {

    private val TAG = "LoginFragment"
    private val db = FirebaseFirestore.getInstance()
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        isSessionActive()
        setup()
    }

    private fun setup() {
        binding.LoginButton.setOnClickListener {
            val email = binding.EmailEditText.text.toString()
            val password = binding.PasswordEditText.text.toString()

            // Validaciones
            if(email.isEmpty() || password.isEmpty()) {
                showAlert("Las credenciales son requeridas.")
                return@setOnClickListener
            }

            firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(activity as Activity) { task ->
                if(!task.isSuccessful) {
                    try {
                        throw task.exception!!
                    } catch(e: FirebaseAuthInvalidCredentialsException) {
                        showAlert("El email o la contraseña son incorrectos.")
                    } catch(e: FirebaseAuthUserCollisionException) {
                        showAlert("El usuario no existe.")
                    } catch(e: Exception) {
                        showAlert("Se ha producido un error al autenticar el usuario.")
                        Log.e(TAG, e.message.toString())
                    }
                } else {
                    val userId = firebaseAuth.currentUser!!.uid

                    try {
                        db.collection("users")
                            .document(userId).get().addOnSuccessListener { doc ->
                                val hasProfile : Boolean? = doc.get("hasProfileData") as Boolean?

                                if(hasProfile == true) {
                                    findNavController().navigate(R.id.homeFragment)
                                } else {
                                    findNavController().navigate(R.id.signInFragment)
                                }
                        }
                    } catch(e: FirebaseFirestoreException) {
                        showAlert("Ha ocurrido un error al verificar el estado de su cuenta.")
                        Log.e(TAG, e.code.toString())
                    }
                }
            }
        }
    }

    private fun showAlert(mensaje: String){
        val builder = AlertDialog.Builder(activity as Activity)
        builder.setTitle("Error")
        builder.setMessage(mensaje)
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun isSessionActive() {
        val sessionPrefs = binding.root.context.getSharedPreferences(
            getString(R.string.prefs_file), Context.MODE_PRIVATE
        )
        val userId = sessionPrefs.getString("userId", null)

        if(userId != null) {
            binding.root.visibility = View.INVISIBLE

            findNavController().navigate(R.id.homeFragment)
        }
    }

}
