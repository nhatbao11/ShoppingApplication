package com.project.ecommerceapplication.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.firestoreSettings
import com.google.firebase.ktx.Firebase
import com.project.ecommerceapplication.model.remote.entity.User
import com.project.ecommerceapplication.model.remote.repo.EmerceRepo
import com.project.ecommerceapplication.model.repository.CategoryRepository
import com.project.ecommerceapplication.model.repository.ProductRepository
import com.project.ecommerceapplication.model.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await

class AppViewModel(val context: Application) : AndroidViewModel(context) {
    private val userRepository = UserRepository
    private var currentUserId: String = ""
    private var currentUser: User? = null
    var isAdmin = false

    fun setCurrentUserId(userId: String) {
        currentUserId = userId
        if (userId == "") return

        viewModelScope.launch {
            try {
                currentUser = userRepository.getUserById(userId)
                Log.d("AppViewModel", "Current user: $currentUser")
                isAdmin = currentUser?.isAdmin ?: false
                Log.d("AppViewModel", "isAdmin: $isAdmin")
            } catch (e: Exception) {
                Log.e("AppViewModel", "Error getting user", e)

            }
        }
    }

    fun getCurrentUserId(): String {
        return currentUserId
    }

    fun getCurrentUser(): User? {
        if (currentUserId == "") return null
        runBlocking {
            this.launch(Dispatchers.IO) {
                currentUser = userRepository.getUserById(currentUserId)
            }
        }
        return currentUser
    }

    init {
        initializeDb()
    }

    private fun initializeDb() {
        viewModelScope.launch {
            val db by lazy { Firebase.firestore }
            val userCollectionRef by lazy { db.collection("user") }
            db.firestoreSettings = firestoreSettings { isPersistenceEnabled = true }

            val queryResult = userCollectionRef.limit(1).get().await()
            if (queryResult.isEmpty) {
                val apiProducts = runBlocking { EmerceRepo.getProducts() }
                val apiCategory = runBlocking { EmerceRepo.getCategories() }
                val apiUsers = runBlocking { EmerceRepo.getUsers() }

                Log.d("API", apiProducts.toString())
                Log.d("API", apiCategory.toString())
                Log.d("API", apiUsers.toString())

                apiProducts.forEach { ProductRepository.insertProduct(it) }
                apiCategory.forEach { CategoryRepository.insertCategory(it) }
                apiUsers.forEach { UserRepository.insertUser(it) }
            }
        }
    }
}