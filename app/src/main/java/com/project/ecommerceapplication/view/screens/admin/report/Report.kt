package com.project.ecommerceapplication.view.screens.admin.report

import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.project.ecommerceapplication.model.remote.entity.*
import com.project.ecommerceapplication.ui.theme.header
import com.project.ecommerceapplication.ui.theme.mediumTitle
import com.project.ecommerceapplication.ui.theme.smallTitle
import com.project.ecommerceapplication.view.components.AdminBottomBar
import com.project.ecommerceapplication.view.components.TopBar
import com.project.ecommerceapplication.view.navigation.Screen
import com.project.ecommerceapplication.viewmodel.OrderViewModel
import com.project.ecommerceapplication.viewmodel.admin.DashboardVM
import com.project.ecommerceapplication.viewmodel.admin.ReportVM
import kotlin.math.round

@Composable
fun AdminReportsInit(navController: NavHostController) {

    val adminDashboardVM = viewModel<DashboardVM>(LocalContext.current as ComponentActivity)
    val orderVM = viewModel<OrderViewModel>(LocalContext.current as ComponentActivity)
    val products = remember { adminDashboardVM.getAllProducts() }
    val reportVM = viewModel<ReportVM>(LocalContext.current as ComponentActivity)
    var orders = emptyList<Order>()
    reportVM.totalProductsByStatus = emptyList<TotalProductInfo>().toMutableList()

    if (reportVM.isFiltered && reportVM.filteredOrders.isEmpty()) {
        Column(
            Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = "No orders within this range. Cannot generate report.")
            TextButton(onClick = {
                reportVM.isFiltered =
                    false; navController.navigate(Screen.AdminReport.route); reportVM.finalOrderStatusValue.value =
                "All"
            }, colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary)) {
                Text(text = "Generate default report", style = mediumTitle)
            }
            if (!reportVM.isFiltered) {
                orders = remember { orderVM.getAllOrders() }
                for (order in orders) {
                    reportVM.getProductCountByStatus(order)
                }
            }

        }

    } else {

        if (reportVM.isFiltered) { // not empty
            orders = reportVM.filteredOrders
            for (order in reportVM.filteredOrders) {
                reportVM.getProductCountByStatus(order)
            }
            when (reportVM.finalOrderStatusValue.value.lowercase()) {
                "processing" -> {
                    reportVM.totalProductsCount.value = reportVM.totalProductsByStatus[0].count
                    reportVM.totalSales = reportVM.totalProductsByStatus[0].amount
                }
                "shipped" -> {
                    reportVM.totalProductsCount.value = reportVM.totalProductsByStatus[1].count
                    reportVM.totalSales = reportVM.totalProductsByStatus[1].amount
                }
                "delivered" -> {
                    reportVM.totalProductsCount.value = reportVM.totalProductsByStatus[2].count
                    reportVM.totalSales = reportVM.totalProductsByStatus[2].amount
                }
            }
        } else {
            orders = remember { orderVM.getAllOrders() }
            for (order in orders) {
                reportVM.getProductCountByStatus(order)
            }
        }

        AdminReports(navController = navController, products, orders)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminReports(
    navController: NavHostController, allProducts: List<Product>, orders: List<Order>,
    reportVM: ReportVM = viewModel(LocalContext.current as ComponentActivity),
) {
    Scaffold(
        topBar = {
            TopBar("Reports", actions = {
                IconButton(onClick = {
                    navController.navigate(Screen.FilterReport.route)
                }) {
                    Icon(
                        imageVector = Icons.Default.FilterList,
                        contentDescription = "Localized description"
                    )
                }
            })
        },
        content = { padding ->
            Column(
                Modifier
                    .padding(padding)
                    .padding(16.dp)
            ) {

                var isCollapsed by remember { mutableStateOf(true) }

                Text(

                    text = "${reportVM.totalProductsCount.value} Products",
                    softWrap = true,
                    overflow = TextOverflow.Clip,
                    style = header
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row() {
                    Text(
                        text = "Start date: ${reportVM.startDate.value}",
                        softWrap = true,
                        overflow = TextOverflow.Clip,
                        style = smallTitle
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "End date: ${reportVM.endDate.value}",
                        softWrap = true,
                        overflow = TextOverflow.Clip,
                        style = smallTitle
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row() {
                    Text(
                        text = "Total amount $${round(reportVM.totalSales)}",
                        softWrap = true,
                        overflow = TextOverflow.Clip,
                        style = smallTitle
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "Status: ${reportVM.finalOrderStatusValue.value}",
                        softWrap = true,
                        overflow = TextOverflow.Clip,
                        style = smallTitle
                    )
                }
                if (!isCollapsed) {

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(text = "Processing", style = mediumTitle)
                    Row(
                        Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "${reportVM.totalProductsByStatus[0].count} products",
                            style = smallTitle
                        )
                        Text(
                            text = "Amount $${round(reportVM.totalProductsByStatus[0].amount)}",
                            style = smallTitle
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(text = "Shipped", style = mediumTitle)
                    Row(
                        Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "${reportVM.totalProductsByStatus[1].count} products",
                            style = smallTitle
                        )
                        Text(
                            text = "Amount $${round(reportVM.totalProductsByStatus[1].amount)}",
                            style = smallTitle
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(text = "Delivered", style = mediumTitle)
                    Row(
                        Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "${reportVM.totalProductsByStatus[2].count} products",
                            style = smallTitle
                        )
                        Text(
                            text = "Amount $${round(reportVM.totalProductsByStatus[2].amount)}",
                            style = smallTitle
                        )
                    }

                }

                Spacer(modifier = Modifier.height(4.dp))

                if (reportVM.finalOrderStatusValue.value.lowercase() == "all") TextButton(onClick = {
                    isCollapsed = !isCollapsed
                }) {
                    Text(text = if (!isCollapsed) "Collapse" else "Click for more details.")
                }

                Divider()
                Spacer(modifier = Modifier.height(8.dp))

                LazyColumn(
                    contentPadding = PaddingValues(all = 4.dp)
                ) {
                    items(items = orders) { order ->
                        AdminReportCardInit(order = order, allProducts)
                        Spacer(modifier = Modifier.height(5.dp))
                    }
                }
                Divider()
            }
        },
        bottomBar = { AdminBottomBar(navController = navController) } //navController = navController
    )
}