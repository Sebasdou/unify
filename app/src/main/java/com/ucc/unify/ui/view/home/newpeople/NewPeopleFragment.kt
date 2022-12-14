package com.ucc.unify.ui.view.home.newpeople

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.ucc.unify.data.model.Filter
import com.ucc.unify.databinding.FragmentNewPeopleBinding

class NewPeopleFragment : Fragment() {
    private val TAG = "NewPeopleFragment"
    private val db = FirebaseFirestore.getInstance()
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private var _binding: FragmentNewPeopleBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNewPeopleBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val user = firebaseAuth.currentUser
        val userId = user?.uid
        val usersToShow = ArrayList<Any>()

        if(userId != null) {
            try {
                db.collection("users").document(userId).get()
                    .addOnSuccessListener { filterDocument ->
                        val filterMap = filterDocument.get("filter") as HashMap<*, *>
                        val filter = Filter(filterMap["ageFrom"] as Long, filterMap["ageTo"] as Long,
                            filterMap["sex"] as String, filterMap["interests"] as ArrayList<String>,
                            filterMap["major"] as String
                        )

                        // Users to show
                        db.collection("users")
                            .whereGreaterThanOrEqualTo("age", filter.ageFrom)
                            .whereLessThanOrEqualTo("age", filter.ageTo)
                            .whereEqualTo("sex", filter.sex)
                            .whereIn("interests", filter.interests)
                            .whereEqualTo("major", filter.major)
                            .get().addOnSuccessListener { usersDocuments ->
                                usersDocuments.forEach { user ->
                                    usersToShow.add(user)
                                }
                            }
                    }
            } catch(e: FirebaseFirestoreException) {
                showAlert("Ha ocurrido un error al obtener su filtro de personas.")
                Log.e(TAG, e.code.toString())
            }
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