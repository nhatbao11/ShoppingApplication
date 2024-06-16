package com.project.ecommerceapplication.view.screens.admin.dashboard

import android.util.Log
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.project.ecommerceapplication.model.remote.entity.*
import com.project.ecommerceapplication.model.remote.entity.OrderItem
import com.project.ecommerceapplication.ui.theme.mediumCaption
import com.project.ecommerceapplication.ui.theme.mediumTitle
import com.project.ecommerceapplication.ui.theme.smallCaption
import com.project.ecommerceapplication.view.components.TopBar
import com.project.ecommerceapplication.viewmodel.OrderViewModel
import com.project.ecommerceapplication.viewmodel.admin.DashboardVM
import com.project.ecommerceapplication.viewmodel.admin.ReportVM
import kotlin.math.round

@Composable
fun DetailsPageInit(navController: NavHostController, pageType: String?, title: String?) {
    val orderVM = viewModel<OrderViewModel>(LocalContext.current as ComponentActivity)
    val dashboardVM = viewModel<DashboardVM>(LocalContext.current as ComponentActivity)
    var orderItems = emptyList<OrderItem>();
    var products = emptyList<Product>();
    var orders = remember { orderVM.getAllOrders() }

    if (pageType?.lowercase() != "orders") {
        orderItems = remember { orderVM.getAllOrderItems() }
        products = remember { dashboardVM.getAllProducts() }
    }
    DetailsPage(navController, pageType, title, orderItems, orders, products)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsPage(
    navController: NavHostController,
    pageType: String?,
    title: String?,
    orderItems: List<OrderItem>,
    orders: List<Order>,
    products: List<Product>,
    reportVM: ReportVM = viewModel(LocalContext.current as ComponentActivity),
) {
    Scaffold(topBar = { TopBar(title ?: "", { navController.popBackStack() }) },
        content = { padding ->
            Column(
                modifier = Modifier.padding(padding),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Log.d("type", "$pageType")
                if (pageType?.lowercase() == "orders") {


                    Column() {
                        LazyColumn(
                            contentPadding = PaddingValues(all = 10.dp)
                        ) {
                            items(items = orders) { order ->
                                LatestOrderCard(navController, order = order)
                                Spacer(modifier = Modifier.height(5.dp))
                            }
                        }

                    }
                } else { // pageType == Sales

                    Column(Modifier.padding(10.dp)) {

                        LazyColumn(
                            Modifier.fillMaxHeight(0.75f),
                            contentPadding = PaddingValues(all = 8.dp)
                        ) {
                            items(items = orderItems ?: emptyList()) { orderItem ->
                                val product =
                                    remember { reportVM.getProductDetails(orderItem.pid, products) }
                                if (product != null) {
                                    product.let {
                                        val strList = product.title.split(" ")
                                        val str = if (strList?.size!! < 3) product.title
                                        else strList.subList(0, 3).joinToString(separator = " ")

                                        Card(
                                            modifier = Modifier
                                                .padding(2.dp)
                                                .height(96.dp)
                                                .fillMaxWidth(),
                                            //colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surfaceTint),
                                        ) {
                                            Column(
                                                Modifier
                                                    .padding(10.dp)
                                                    .fillMaxHeight(),
                                                verticalArrangement = Arrangement.Center
                                            ) {
                                                Row(
                                                    modifier = Modifier.fillMaxWidth(),
                                                    horizontalArrangement = Arrangement.SpaceBetween,
                                                    verticalAlignment = Alignment.CenterVertically
                                                ) {
                                                    Text(text = "ID", style = mediumTitle)
                                                    Row(
                                                        Modifier.fillMaxWidth(0.5f),
                                                        horizontalArrangement = Arrangement.SpaceBetween
                                                    ) {
                                                        Text(
                                                            text = "Quantity", style = mediumTitle
                                                        )
                                                        Text(text = "Total", style = mediumTitle)
                                                    }
                                                }

                                                Row(
                                                    modifier = Modifier.fillMaxWidth(),
                                                    horizontalArrangement = Arrangement.SpaceBetween,
                                                    verticalAlignment = Alignment.CenterVertically
                                                ) {
                                                    Text(
                                                        text = product.id.substring(0, 3),
                                                        textAlign = TextAlign.Left,
                                                        style = smallCaption
                                                    )
                                                    Row(
                                                        Modifier.fillMaxWidth(0.4f),
                                                        horizontalArrangement = Arrangement.SpaceBetween
                                                    ) {
                                                        Text(
                                                            text = "x" + orderItem.quantity.toString(),
                                                            style = smallCaption,
                                                            textAlign = TextAlign.Right
                                                        )
                                                        Text(
                                                            text = "$${orderItem.price}",
                                                            textAlign = TextAlign.Right,
                                                            style = smallCaption
                                                        )
                                                    }
                                                }
                                                Spacer(modifier = Modifier.height(4.dp))
                                                Text(text = "Name", style = mediumTitle)
                                                Text(text = str, style = smallCaption)
                                            }
                                        }
                                    }
                                }
                            }

                        }

                        Spacer(modifier = Modifier.height(8.dp))
                        Divider()

                        Column(
                            Modifier.padding(12.dp), verticalArrangement = Arrangement.SpaceAround
                        ) {
                            reportVM.totalQuantity = 0
                            reportVM.totalSales = 0.0
                            for (orderItem in orderItems ?: emptyList()) {
                                reportVM.totalQuantity += orderItem.quantity
                            }
                            for (order in orders) reportVM.totalSales += (order.total)
                            Text(
                                text = "Total quantity ordered: ", style = mediumCaption
                            )
                            Text(text = reportVM.totalQuantity.toString(), style = mediumTitle)
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(text = "Total revenue:", style = mediumCaption)
                            Text(text = "$" + round(reportVM.totalSales), style = mediumTitle)
                        }

                    }
                }

            }
        })
}