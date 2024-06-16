package com.project.ecommerceapplication.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.project.ecommerceapplication.model.remote.entity.Address
import com.project.ecommerceapplication.model.remote.entity.CartWithProduct
import com.project.ecommerceapplication.model.remote.entity.Order
import com.project.ecommerceapplication.model.remote.entity.OrderItem
import com.project.ecommerceapplication.model.remote.entity.OrderStatus
import com.project.ecommerceapplication.model.repository.CartRepository
import com.project.ecommerceapplication.model.repository.OrderRepository
import com.project.ecommerceapplication.model.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class PlaceOrderViewModel(val context: Application) : AndroidViewModel(context) {
    private val orderRepository = OrderRepository
    private val cartRepository = CartRepository
    private val userRepository = UserRepository

    lateinit var selectedAddress: Address
    lateinit var selectedPayment: String

    fun placeOrder(cartItems: List<CartWithProduct>, totalPrice: Double) {
        val currentDate = DateTimeFormatter.ofPattern("MM/dd/yyyy").format(LocalDateTime.now())
        val currentUserId = cartItems[0].cart.uid

        val order = Order(
            uid = currentUserId,
            aid = selectedAddress.id,
            date = currentDate,
            total = totalPrice,
            status = OrderStatus.Processing.name,
            items = mutableListOf<OrderItem>()
        )

        viewModelScope.launch(Dispatchers.IO) {
            val orderList = mutableListOf<OrderItem>()
            cartItems.forEach {
                val orderItem = OrderItem(
                    pid = it.cart.pid,
                    price = it.product.price,
                    quantity = it.cart.quantity
                )
                orderList.add(orderItem)
            }

            order.items = orderList
            orderRepository.insertOrder(order)
            cartRepository.deleteCartByUser(currentUserId)

            // Update user tier (Silver, Gold) based on number of orders
            val userOrderCount = orderRepository.getOrderByUserId(currentUserId).size
            var type = "New"

            if (userOrderCount >= 5) type = "Gold"
            else if (userOrderCount >= 1) type = "Silver"

            val user = userRepository.getUserById(currentUserId)
            user?.let {

                if (user.type != type) {
                    user.type = type
                    userRepository.updateUser(user)
                }
            }
        }
    }
}