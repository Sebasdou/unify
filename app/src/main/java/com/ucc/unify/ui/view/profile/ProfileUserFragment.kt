package com.ucc.unify.ui.view.profile

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.storage.FirebaseStorage
import com.ucc.unify.R
import com.ucc.unify.databinding.FragmentProfileUserBinding

class ProfileUserFragment : Fragment() {
    private val firebaseAuth = FirebaseAuth.getInstance()
    val user = firebaseAuth.currentUser
    private val userId = user!!.uid
    private val storage = FirebaseStorage.getInstance()
    private val pickImage = 100
    private var imageUri: Uri? = null
    private var _binding: FragmentProfileUserBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileUserBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val sessionPrefs = binding.root.context.getSharedPreferences(
            getString(R.string.prefs_file), Context.MODE_PRIVATE
        )
        val options = arrayOf("Datos generales", "Privacidad", "Personalización",
            "Preferencias de institución", "Preferencias de comunidad",
            "Ayuda", "Comentarios", "Cerrar Sesión"
        )
        binding.listOptions.adapter = ArrayAdapter(binding.root.context,
            R.layout.item_list, R.id.text_view, options)
        binding.listOptions.setOnItemClickListener { _, _, i, _ ->
            when(i){
                0 -> {
                    findNavController().navigate(R.id.generalDataFragment)
                }
                7 -> {

                }
            }
        }

        if (user != null) {
            setup(user, userId)
        }
    }

    private fun setup(user: FirebaseUser, userId: String) {
        binding.ChoosePicButton.setOnClickListener {
            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(gallery, pickImage)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && requestCode == pickImage) {
            imageUri = data?.data
            Log.e("LOL", imageUri.toString())
            binding.ProfilePictureView.setImageURI(imageUri)

            val storageRef = storage.reference
            val userPicsRef = storageRef.child("$userId/profilePicture/profilePicture.jpg")

            val uploadTask = userPicsRef.putFile(imageUri!!)

            uploadTask.addOnFailureListener {
                showAlert("Ha ocurrido un error al guardar sus datos.")
            }.addOnSuccessListener {
                val downloadUri: Task<Uri> = userPicsRef.downloadUrl

                val profileUpdates = userProfileChangeRequest {
                    photoUri = Uri.parse(downloadUri.toString())
                }

                user?.updateProfile(profileUpdates)
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