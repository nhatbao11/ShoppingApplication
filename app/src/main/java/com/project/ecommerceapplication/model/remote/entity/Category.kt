package com.project.ecommerceapplication.model.remote.entity

import kotlinx.serialization.Serializable

@Serializable
data class Category(
    var id: String = "",
    var cid: Int = -1,
    val name: String,
) {
    constructor() : this(id = "", cid = -1, name = "")
}