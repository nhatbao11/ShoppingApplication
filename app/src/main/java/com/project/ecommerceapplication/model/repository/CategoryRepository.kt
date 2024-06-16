package com.project.ecommerceapplication.model.repository

import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.firestoreSettings
import com.google.firebase.ktx.Firebase
import com.project.ecommerceapplication.model.remote.entity.Category
import kotlinx.coroutines.tasks.await

object CategoryRepository {
    val db by lazy { Firebase.firestore }
    private val categoryCollectionRef by lazy { db.collection("category") }

    init {
        val settings = firestoreSettings { isPersistenceEnabled = true }
        db.firestoreSettings = settings
    }

    fun insertCategory(category: Category) {
        val documentId = categoryCollectionRef.document().id
        category.id = documentId
        categoryCollectionRef.document(documentId).set(category)
    }

    suspend fun getAllCategories(): List<Category> = categoryCollectionRef.orderBy("cid", Query.Direction.ASCENDING).get().await().toObjects(
        Category::class.java
    )
}