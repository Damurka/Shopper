package com.example.shopper.viewmodels

import android.net.Uri
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

    /**
     * Firebase database reference
     */
    private val database = FirebaseDatabase.getInstance().reference

    /**
     * Firebase Storage reference
     */
    private val storage = FirebaseStorage.getInstance().reference

    /**
     * Firebase database profiles reference
     */
    private val profileDatabaseReference = database.child(Constants.FirebaseProfiles).child(uid)

    /**
     * Storage Reference to the current user profile storage area
     */
    private val profileStorageReference = storage.child("images/users/$uid/profile_image.jpg")


    /**
     * Current User's profile as LiveData
     */
    val profileLiveData = Transformations.map(FirebaseQueryData(profileDatabaseReference), ProfileDeserializer()) as MutableLiveData<Profile>

    /**
     * Update the current users profile
     */
    fun updateProfile(profile: Profile, photo: ByteArray? = null) {
        if (photo == null) {
            // If photo is null update just the profile
            profileDatabaseReference.updateChildren(profile.toMap())
        } else {
            // if there is a photo upload the new photo
            // then get the reference the photo add to the profile then
            // upload the profile with the photo url
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