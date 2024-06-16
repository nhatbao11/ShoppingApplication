package com.project.ecommerceapplication.model.remote.entity

import kotlinx.serialization.Serializable

@Serializable
data class User(
    var id: String = "",
    var firstName: String,
    var lastName: String,
    var email: String,
    var password: String,
    var dob: String,
    var isAdmin: Boolean,
    var type: String
) {
    constructor() : this(
        dob = "",
        email = "",
        firstName = "",
        id = "",
        isAdmin = false,
        lastName = "",
        password = "",
        type = ""
    )
}