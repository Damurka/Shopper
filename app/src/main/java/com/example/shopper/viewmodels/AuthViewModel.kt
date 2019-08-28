package com.example.shopper.viewmodels

import android.app.Activity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.shopper.helpers.Constants
import com.example.shopper.models.Profile
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.iid.FirebaseInstanceId


class AuthViewModel : ViewModel() {
    /**
     * Authentication states
     */
    enum class AuthenticationState {
        AUTHENTICATED,
        UNAUTHENTICATED,
        INVALID_AUTHENTICATION,
        UNVERIFIED_ACCOUNT,

        REGISTRATION_CANCELLED,
        REGISTRATION_COMPLETED,

        INVALID_ACCOUNT_INFO
    }

    /**
     * Firebase Authentication Instance
     */
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    /**
     * Firebase database reference
     */
    private val database: DatabaseReference = FirebaseDatabase.getInstance().reference

    /**
     * InstanceId used to generate the id for cloud messaging
     */
    private val instanceId: FirebaseInstanceId = FirebaseInstanceId.getInstance()

    /**
     * Authentication state as LiveData
     */
    val authenticationState = MutableLiveData<AuthenticationState>()

    /**
     * Current user Id
     */
    val userId: String get() = auth.currentUser?.uid!!

    /**
     * Current Email
     */
    val email: String get() = auth.currentUser!!.email as String

    init {
        // If there is the instance of the current user set the
        // AuthenticationState as Authenticated otherwise unauthenticated
        if (auth.currentUser == null) {
            refuseAuthentication()
        } else {
            acceptAuthentication()
        }

        try {
            auth.currentUser?.reload()
        } catch (e: FirebaseAuthInvalidUserException) {
            refuseAuthentication()
        }

    }

    /**
     * Generates the instanceId for first time users and updates the
     * database
     */
    private fun generateInstanceId() {
        instanceId.instanceId
            .addOnCompleteListener(OnCompleteListener {
                if (!it.isSuccessful) {
                    return@OnCompleteListener
                }

                // Get new Instance ID token
                val token = it.result?.token

                // Update the profiles instanceId
                database.child("profiles")
                    .child(userId)
                    .child("instanceId")
                    .setValue(token)
            })
    }

    /**
     * Authenticates the user tith email ans password
     */
    fun authenticate(activity: Activity, email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(activity) { task ->
                if (task.isSuccessful) {
                    if (auth.currentUser!!.isEmailVerified) {
                        // If user name and password coreect and email is verified
                        // Authenticate
                        acceptAuthentication()
                    } else {
                        // if email is not verified unauthenticate
                        authenticationState.value = AuthenticationState.UNVERIFIED_ACCOUNT
                    }
                } else {
                    // Invalid login
                    // Email or password invalid
                    authenticationState.value = AuthenticationState.INVALID_AUTHENTICATION
                }
            }
    }

    /**
     * Signs out the user abd sets the authentication an unauthenticated
     */
    fun signOut() {
        auth.signOut()
        refuseAuthentication()
    }

    /**
     * Registers a user
     */
    fun createAccount(activity: Activity, email: String, password: String, profile: Profile) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(activity) { task ->
                if (task.isSuccessful) {
                    // If successful registration update the profiles database and
                    // send verification email and logout
                    database.child(Constants.FirebaseProfiles).child(auth.currentUser?.uid!!).setValue(profile)
                    auth.currentUser!!.sendEmailVerification()
                    auth.signOut()

                    authenticationState.value = AuthenticationState.REGISTRATION_COMPLETED
                } else {
                    // Some information is invalid or the user already exists
                    invalidAccountInfo()
                }
            }
    }

    /**
     * set authenticationState as authenticated and generates the InstanceId
     */
    private fun acceptAuthentication() {
        authenticationState.value = AuthenticationState.AUTHENTICATED
        generateInstanceId()
    }

    /**
     * Sets the authenticationState as unauthenticated
     */
    fun refuseAuthentication() {
        authenticationState.value = AuthenticationState.UNAUTHENTICATED
    }

    /**
     * Sets the authenticationState as registration cancelled
     */
    fun userCancelledRegistration() {
        authenticationState.value = AuthenticationState.REGISTRATION_CANCELLED
    }

    /**
     * Sets the authenticationState as invalid accou t info
     */
    fun invalidAccountInfo() {
        authenticationState.value = AuthenticationState.INVALID_ACCOUNT_INFO
    }

}