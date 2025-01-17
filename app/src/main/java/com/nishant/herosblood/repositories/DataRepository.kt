package com.nishant.herosblood.repositories

import android.net.Uri
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import com.nishant.herosblood.models.BloodRequestData
import com.nishant.herosblood.models.UserData
import com.nishant.herosblood.models.UserLocationData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DataRepository {
    private val db = FirebaseFirestore.getInstance()
    private val storageRef = FirebaseStorage.getInstance().reference

    fun saveUserData(
        user: UserData,
        completeCallback: (Task<Void>) -> Unit,
        failureCallback: (Exception) -> Unit
    ) {
        user.userId?.let {
            db.collection("users").document(it)
                .set(user)
                .addOnCompleteListener(completeCallback)
                .addOnFailureListener(failureCallback)
        }
    }

    fun saveBloodRequest(
        bloodRequestData: BloodRequestData,
        completeCallback: (Task<Void>) -> Unit,
        failureCallback: (Exception) -> Unit
    ) {
        bloodRequestData.userId?.let {
            val timeStamp = bloodRequestData.userId + bloodRequestData.createdAt
            db.collection("bloodRequests")
                .document(it)
                .collection("activeRequests")
                .document(timeStamp)
                .set(bloodRequestData)
                .addOnCompleteListener(completeCallback)
                .addOnFailureListener(failureCallback)
        }
    }

    fun updateUserAvailability(
        userId: String,
        newValue: String,
        completeCallback: (Task<Void>) -> Unit,
        failureCallback: (Exception) -> Unit
    ) {
        userId.let {
            db.collection("users").document(it)
                .update("available", newValue)
                .addOnCompleteListener(completeCallback)
                .addOnFailureListener(failureCallback)
        }
    }

    fun readUserData(
        userId: String,
        completeCallback: (DocumentSnapshot) -> Unit,
        failureCallback: (Exception) -> Unit
    ) {
        db.collection("users").document(userId)
            .get()
            .addOnSuccessListener(completeCallback)
            .addOnFailureListener(failureCallback)
    }

    suspend fun getAllDonors(
        userId: String,
        completeCallback: (QuerySnapshot) -> Unit,
        failureCallback: (Exception) -> Unit
    ) {
        withContext(Dispatchers.IO) {
            db.collection("users")
                .whereEqualTo("available", "true")
                .whereNotEqualTo("userId", userId)
                .get()
                .addOnSuccessListener(completeCallback)
                .addOnFailureListener(failureCallback)
        }
    }

    suspend fun getDonorList(
        userId: String,
        bloodType: String,
        completeCallback: (QuerySnapshot) -> Unit,
        failureCallback: (Exception) -> Unit
    ) {
        withContext(Dispatchers.IO) {
            db.collection("users")
                .whereEqualTo("bloodGroup", bloodType)
                .whereEqualTo("available", "true")
                .whereNotEqualTo("userId", userId)
                .get()
                .addOnSuccessListener(completeCallback)
                .addOnFailureListener(failureCallback)
        }
    }

    suspend fun getSearchDonorList(
        userId: String,
        bloodType: String,
        city: String,
        completeCallback: (QuerySnapshot) -> Unit,
        failureCallback: (Exception) -> Unit
    ) {
        withContext(Dispatchers.IO) {
            db.collection("users")
                .whereEqualTo("bloodGroup", bloodType)
                .whereEqualTo("city", city)
                .whereEqualTo("available", "true")
                .whereNotEqualTo("userId", userId)
                .get()
                .addOnSuccessListener(completeCallback)
                .addOnFailureListener(failureCallback)
        }
    }

    fun uploadProfilePicture(
        userId: String,
        file: Uri,
        onCompleteCallback: (Task<UploadTask.TaskSnapshot>) -> Unit,
        onFailureCallback: (Exception) -> Unit
    ) {
        storageRef.child("ProfilePicture").child(userId).putFile(file)
            .addOnCompleteListener(onCompleteCallback)
            .addOnFailureListener(onFailureCallback)
    }

    suspend fun saveUserLocation(
        locationData: UserLocationData
    ) {
        withContext(Dispatchers.IO) {
            db.collection("userLocationData")
                .document(locationData.userId!!)
                .set(locationData)
        }
    }

    suspend fun getUserLocation(
        userId: String,
        onCompleteCallback: (QuerySnapshot) -> Unit,
        onFailureCallback: (Exception) -> Unit
    ) {
        withContext(Dispatchers.IO) {
            db.collection("userLocationData")
                .whereEqualTo("userId", userId)
                .get()
                .addOnSuccessListener(onCompleteCallback)
                .addOnFailureListener(onFailureCallback)
        }
    }
}