package edu.iu.habahram.simpleinsta

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.toObjects
import edu.iu.habahram.simpleinsta.model.Post
import edu.iu.habahram.simpleinsta.model.User

class PostsViewModel : ViewModel() {
    val TAG = "PostsViewModel"
    var signedInUser: User? = null
    private val _posts: MutableLiveData<MutableList<Post>> = MutableLiveData()
    val posts: LiveData<List<Post>>
        get() = _posts as LiveData<List<Post>>

    init {

        val  firestoreDB = FirebaseFirestore.getInstance()
//        firestoreDB.collection("users")
//            .document(FirebaseAuth.getInstance().currentUser?.uid as String)
//            .get()
//            .addOnSuccessListener { userSnapshot ->
//                signedInUser = userSnapshot.toObject<User>()
//                Log.i(TAG, "signed in user: $signedInUser")
//            }
//            .addOnFailureListener { exception ->
//                Log.i(TAG, "Failure fetching signed in user", exception)
//            }
        var postsReference = firestoreDB
            .collection("posts")
            .limit(30)
            .orderBy("creation_time_ms", Query.Direction.DESCENDING)

        postsReference.addSnapshotListener { snapshot, exception ->
            if (exception != null || snapshot == null) {
                Log.e(TAG, "Exception when querying posts", exception)
                return@addSnapshotListener
            }
            val postList = snapshot.toObjects<Post>()
            _posts.value = postList as MutableList<Post>
            for (post in postList) {
                Log.i(TAG, "Post ${post}")
            }
        }
    }
}