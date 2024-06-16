package com.project.ecommerceapplication.viewmodel.admin

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import com.project.ecommerceapplication.model.repository.OrderRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import com.project.ecommerceapplication.model.remote.entity.*


class ReportVM(val context: Application) : AndroidViewModel(context) {
    private val orderRepository = OrderRepository

    private val currentDate: LocalDateTime = LocalDateTime.now()
    private val yesterdayDate: LocalDateTime = LocalDateTime.now().minusDays(15)
    private val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy")
    private val currentDateFormatted: String = currentDate.format(formatter)

    val radioOptions = listOf("All", "Processing", "Shipped", "Delivered")
    val finalOrderStatusValue = mutableStateOf(radioOptions[0])
    val tempOrderStatusValue = mutableStateOf("All")
    val startDate = mutableStateOf("N/A")
    val endDate = mutableStateOf(currentDateFormatted)
    var totalQuantity by mutableStateOf(0)
    var totalSales by mutableStateOf(0.0)
    var filteredOrders = mutableListOf<Order>()
    var isFiltered = false
    var totalProductsByStatus = mutableListOf<TotalProductInfo>()
    var totalProductsCount = mutableStateOf(0)

    fun filterReport(filterType: String): List<Order>? {
        var result = emptyList<Order>()
        runBlocking {
            this.launch(Dispatchers.IO) {
                result = if (finalOrderStatusValue.value == "All") orderRepository.getAllOrders()
                else orderRepository.getFilteredOrders(finalOrderStatusValue.value)
            }
        }

        when (filterType) {
            "order" -> return result
            "report" -> {

                if (startDate.value != "N/A") {
                    filteredOrders = result.filter {
                        LocalDate.parse(it.date, formatter).isAfter(
                            LocalDate.parse(
                                startDate.value, formatter
                            )
                        ) && LocalDate.parse(it.date, formatter)
                            .isBefore(LocalDate.parse(endDate.value, formatter).plusDays(1))
                    }.toMutableList()

                    return filteredOrders
                }
                else if(startDate.value == "N/A" && LocalDate.parse(endDate.value, formatter) == LocalDate.now())
                    filteredOrders = result.toMutableList()
                else
                    filteredOrders = result.filter { LocalDate.parse(it.date, formatter)
                        .isBefore(LocalDate.parse(endDate.value, formatter)) }.toMutableList()

            }
        }
        return null
    }

    fun getProductCountByStatus(order: Order) {
        if (totalProductsByStatus.isEmpty()) {
            //initialize list
            val processingProductInfo = TotalProductInfo(0, 0.0)
            val shippedProductInfo = TotalProductInfo(0, 0.0)
            val deliveredProductInfo = TotalProductInfo(0, 0.0)
            totalProductsByStatus.add(processingProductInfo)
            totalProductsByStatus.add(shippedProductInfo)
            totalProductsByStatus.add(deliveredProductInfo)
        }

        when (order.status.lowercase()) {
            "processing" -> {
                totalProductsByStatus[0].count += order.items.size
                totalProductsByStatus[0].amount += order.total
            }
            "shipped" -> {
                totalProductsByStatus[1].count += order.items.size
                totalProductsByStatus[1].amount += order.total
            }
            "delivered" -> {
                totalProductsByStatus[2].count += order.items.size
                totalProductsByStatus[2].amount += order.total
            }
        }

        totalSales =
            totalProductsByStatus[0].amount + totalProductsByStatus[1].amount + totalProductsByStatus[2].amount

        totalProductsCount.value =
            totalProductsByStatus[0].count + totalProductsByStatus[1].count + totalProductsByStatus[2].count

    }

    fun getProductDetails(pid: String, productList: List<Product>): Product? {

        for (product in productList) if (product.id == pid) return product

        return null
    }

}