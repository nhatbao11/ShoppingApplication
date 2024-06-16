package com.project.ecommerceapplication.model.remote.api

import com.project.ecommerceapplication.model.remote.entity.*
import retrofit2.http.*

interface EmerceApi {
    @GET("users")
    suspend fun getUsers(): List<User>

    @GET("categories")
    suspend fun getCategories(): List<Category>

    @GET("products")
    suspend fun getProducts(): List<Product>
}