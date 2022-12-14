package com.ucc.unify.ui.view.home.newpeople

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import com.ucc.unify.R
import com.ucc.unify.data.model.Filter
import com.ucc.unify.databinding.FragmentNewPeopleBinding

class NewPeopleFragment : Fragment() {
    private val TAG = "NewPeopleFragment"
    private val db = FirebaseFirestore.getInstance()
    private val storage = FirebaseStorage.getInstance()
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
        val usersToShow = ArrayList<HashMap<*,*>>()

        binding.FilterButton.setOnClickListener() {
            findNavController().navigate(R.id.filterPeopleFragment)
        }

        /*if(userId != null) {
            try {
                db.collection("users").document(userId).get()
                    .addOnSuccessListener { filterDocument ->
                        val filterMap = filterDocument.get("filter") as HashMap<*, *>
                        val filter = Filter(filterMap["ageFrom"] as Long, filterMap["ageTo"] as Long,
                            filterMap["sex"] as String, filterMap["major"] as String
                        )

                        Log.e(TAG, filter.toString())

                        // Users to show
                        db.collection("users")
                            .whereIn("age", (filter.ageFrom..filter.ageTo).toList())
                            .whereEqualTo("sex", filter.sex)
                            .whereEqualTo("major", filter.major)
                            .get().addOnSuccessListener { userDocuments ->
                                userDocuments.documents.forEach { doc ->
                                    Log.e(TAG, doc.data.toString())
                                    usersToShow.add(doc.data as HashMap<*,*>)
                                }
                            }.addOnFailureListener() { userDocuments ->
                                Log.e("TAG", userDocuments.toString())
                            }
                    }
            } catch(e: FirebaseFirestoreException) {
                showAlert("Ha ocurrido un error al obtener su filtro de personas.")
                Log.e(TAG, e.code.toString())
            }

            if(usersToShow.size > 0) setup(usersToShow)
        }*/
    }

    private fun setup(usersToShow: ArrayList<HashMap<*,*>>) {
        var i = 0

        /*var profilePic = usersToShow[i]["profilePic"] as String?
            ?: return

        var storageReference = storage.reference.child(profilePic)

        Picasso.get().load(storageReference.downloadUrl.toString()).into(binding.ImageViewPerson)

        binding.LikeButton.setOnClickListener() {
            //isMatch(usersToShow[i])
            i++
            if(i >= usersToShow.size) {
                showAlert("Se acabaron los posibles matches.")
                return@setOnClickListener
            }

            profilePic = usersToShow[i]["profilePic"] as String?
                ?: return@setOnClickListener

            storageReference = storage.reference.child(profilePic)

            Picasso.get().load(storageReference.downloadUrl.toString()).into(binding.ImageViewPerson)
        }

        binding.DislikeButton.setOnClickListener() {
            i++
            if(i >= usersToShow.size) {
                showAlert("Se acabaron los posibles matches.")
                return@setOnClickListener
            }

            profilePic = usersToShow[i]["profilePic"] as String?
                ?: return@setOnClickListener

            storageReference = storage.reference.child(profilePic)

            Picasso.get().load(storageReference.downloadUrl.toString()).into(binding.ImageViewPerson)
        }*/

    }

    /*private fun isMatch(oppositeUser: HashMap<*,*>) {
        val meUser = hashMapOf(
            "yesMatch" to oppositeUser.[""]
        )

        val oppositeUser = hashMapOf(

        )
    }*/

    private fun showAlert(mensaje: String){
        val builder = AlertDialog.Builder(binding.root.context)
        builder.setTitle("Error")
        builder.setMessage(mensaje)
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }
}