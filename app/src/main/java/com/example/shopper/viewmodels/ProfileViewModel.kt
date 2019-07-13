package com.example.shopper.viewmodels

import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.shopper.data.FirebaseQueryData
import com.example.shopper.helpers.Constants
import com.example.shopper.helpers.ProfileDeserializer
import com.example.shopper.models.Profile
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask

class ProfileViewModel(uid: String) : ViewModel() {

    private val database = FirebaseDatabase.getInstance().reference
    private val storage = FirebaseStorage.getInstance().reference

    private val profileDatabaseReference = database.child(Constants.FirebaseProfiles).child(uid)
    private val profileStorageReference = storage.child("images/users/$uid/profile_image.jpg")


    val profileLiveData = Transformations.map(FirebaseQueryData(profileDatabaseReference), ProfileDeserializer()) as MutableLiveData<Profile>

    fun updateProfile(profile: Profile, photo: ByteArray? = null) {
        Log.i("ViewModel", "Photo is null: " + (photo == null))
        if (photo == null) {
            profileDatabaseReference.updateChildren(profile.toMap())
        } else {
            val uploadTask = profileStorageReference.putBytes(photo)

            uploadTask.continueWithTask(Continuation<UploadTask.TaskSnapshot, Task<Uri>> { task ->
                if (!task.isSuccessful) {
                    task.exception?.let {
                        throw it
                    }
                }
                return@Continuation profileStorageReference.downloadUrl
            }).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val uri = task.result
                    profile.imageUrl = uri.toString()
                    profileDatabaseReference.updateChildren(profile.toMap())
                } else {
                    // Handle failures
                    // ...
                }
            }
        }
    }

}