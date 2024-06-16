package com.project.ecommerceapplication.model.remote.entity

import kotlinx.serialization.Serializable

@Serializable
data class Address(
    var id: String = "",
    val uid: String = "",
    val name: String,
    val unit: Int,
    val building: Int,
    val street: Int,
    val zone: Int,
    val phone: String,
    val poBox: Int,
) {
    constructor() : this(
        id = "",
        uid = "",
        name = "",
        unit = 0,
        building = 0,
        street = 0,
        zone = 0,
        phone = "",
        poBox = 0
    )
}