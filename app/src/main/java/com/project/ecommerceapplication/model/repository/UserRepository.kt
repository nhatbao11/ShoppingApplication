package com.project.ecommerceapplication.model.repository

import android.util.Log
import com.project.ecommerceapplication.model.remote.entity.User
import com.google.firebase.firestore.firestoreSettings
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await


object UserRepository {
    val db by lazy { Firebase.firestore }
    private val userCollectionRef by lazy { db.collection("user") }

    init {
        val settings = firestoreSettings {
            isPersistenceEnabled = true
        }
        db.firestoreSettings = settings
    }

    suspend fun getUserById(userId: String): User? =
        userCollectionRef.document(userId).get().await().toObject(User::class.java)

    suspend fun getUserByEmail(email: String): User? {
        val users =
            userCollectionRef.whereEqualTo("email", email).get().await().toObjects(User::class.java)
        return if (users.isEmpty()) null
        else{
            Log.d("AppViewModel", "isAdmin: $users")

            users[0]}
    }


    fun updateUser(user: User) {
        userCollectionRef.document(user.id).set(user)
    }

    fun insertUser(user: User) {
        val documentId = userCollectionRef.document().id
        user.id = documentId
        userCollectionRef.document(documentId).set(user)
    }
}
