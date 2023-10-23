package edu.iu.habahram.simpleinsta

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import edu.iu.habahram.simpleinsta.model.User


class CreateFragment : Fragment() {
    val TAG = "CreateFragment"
    private var signedInUser: User? = null
    private var photoUri: Uri? = null
    private lateinit var firestoreDb: FirebaseFirestore
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_create, container, false)
        // Registers a photo picker activity launcher in single-select mode.
        val imageView = view.findViewById<ImageView>(R.id.imageView)
        val btnPickImage = view.findViewById<Button>(R.id.btnPickImage)
        val btnSubmit = view.findViewById<Button>(R.id.btnSubmit)
        val etDescription = view.findViewById<EditText>(R.id.etDescription)

        val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            // Callback is invoked after the user selects a media item or closes the
            // photo picker.
            if (uri != null) {
                Log.d(TAG, "Selected URI: $uri")
                photoUri = uri
                imageView.setImageURI(uri)
            } else {
                Log.d(TAG, "No media selected")
            }
        }
        btnPickImage.setOnClickListener {
            Log.i(TAG, "Open up image picker on device")

            // Launch the photo picker and let the user choose only images.
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        btnSubmit.setOnClickListener {
            postThePhoto(etDescription.text.toString())
        }
        getTheCurrentUser()

        return view
    }

    private fun getTheCurrentUser() {
        firestoreDb = FirebaseFirestore.getInstance()
        firestoreDb.collection("users")
            .document(FirebaseAuth.getInstance().currentUser?.uid as String)
            .get()
            .addOnSuccessListener { userSnapshot ->
                signedInUser = userSnapshot.toObject(User::class.java)
                Log.i(TAG, "signed in user: $signedInUser")
            }
            .addOnFailureListener { exception ->
                Log.i(TAG, "Failure fetching signed in user", exception)
            }
    }

    private fun postThePhoto(description: String) {
        if (photoUri == null) {
            Toast.makeText(this.requireContext(), "No photo selected", Toast.LENGTH_SHORT).show()
            return
        }
        if (description.isBlank()) {
            Toast.makeText(this.requireContext(), "Description cannot be empty", Toast.LENGTH_SHORT).show()
            return
        }
        if (signedInUser == null) {
            Toast.makeText(this.requireContext(), "No signed in user, please wait", Toast.LENGTH_SHORT).show()
            return
        }
    }


}