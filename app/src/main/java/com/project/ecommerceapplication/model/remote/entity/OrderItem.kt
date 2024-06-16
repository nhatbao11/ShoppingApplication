package com.project.ecommerceapplication.model.remote.entity

data class OrderItem(
    val pid: String = "", val quantity: Int = 1, val price: Double
) {
    constructor() : this(pid = "", quantity = 1, price = 0.0)
}