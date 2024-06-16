package com.project.ecommerceapplication.view.screens.orders

import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.project.ecommerceapplication.model.remote.entity.FullOrderDetail
import com.project.ecommerceapplication.model.remote.entity.Order
import com.project.ecommerceapplication.ui.theme.largeTitle
import com.project.ecommerceapplication.ui.theme.smallCaption
import com.project.ecommerceapplication.ui.theme.smallTitle
import com.project.ecommerceapplication.view.components.AdminBottomBar
import com.project.ecommerceapplication.view.components.DropDownMenu
import com.project.ecommerceapplication.view.components.TopBar
import com.project.ecommerceapplication.view.components.UserBottomBar
import com.project.ecommerceapplication.viewmodel.AppViewModel
import com.project.ecommerceapplication.viewmodel.OrderDetailsViewModel
import com.project.ecommerceapplication.viewmodel.OrderViewModel
import com.project.ecommerceapplication.viewmodel.admin.ReportVM

@ExperimentalComposeUiApi
@ExperimentalMaterial3Api
@Composable
fun OrderDetails(
    navController: NavHostController,
    order_id: String,
    appViewModel: AppViewModel = viewModel(LocalContext.current as ComponentActivity),
    orderDetailsViewModel: OrderDetailsViewModel = viewModel(LocalContext.current as ComponentActivity),
    reportVM: ReportVM = viewModel(LocalContext.current as ComponentActivity),
    orderViewModel: OrderViewModel = viewModel(LocalContext.current as ComponentActivity),
) {
    val order = orderDetailsViewModel.getOrderById(order_id)
    val orderItemsWithProduct = orderDetailsViewModel.orderItemProducts
    orderViewModel.orderStatus.value = order.status
    val textModifier = Modifier
    Scaffold(topBar = { TopBar("Order Details", { navController.popBackStack() }) },
        content = { padding ->
            Column(
                Modifier
                    .padding(padding)
                    .padding(20.dp)
                    .fillMaxHeight()
            ) {

                Column(
                    verticalArrangement = Arrangement.spacedBy(5.dp),
                    modifier = Modifier.weight(0.30f)
                ) {
                    if (!appViewModel.isAdmin)
                        Row(
                            Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                        ) {
                            Text(text = "Status", textModifier, style = smallCaption)
                            Text(text = order.status, textModifier, style = smallTitle)
                        }
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        Text(text = "Date Ordered ", textModifier, style = smallCaption)
                        Text(text = order.date, textModifier, style = smallTitle)
                    }
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        Text(text = "Number of Items ", textModifier, style = smallCaption)
                        Text(text = "${order.items.size}", textModifier, style = smallTitle)
                    }
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        Text(text = "Total ", textModifier, style = smallCaption)
                        Text(text = "$${order.total}", textModifier, style = smallTitle)
                    }
                }
                if (appViewModel.isAdmin) {
                    Column(
                        Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.SpaceBetween,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        Text(text = "Order Status", style = largeTitle)
                        Spacer(modifier = Modifier.height(5.dp))
                        DropDownMenu(
                            options = reportVM.radioOptions.subList(
                                1,
                                reportVM.radioOptions.size
                            ), text = "Status", type = "orders", order.status
                        )
                        if (orderViewModel.isOrderUpdated.value) {
                            orderViewModel.updateStatus(order, orderViewModel.orderStatus.value)
                            orderViewModel.isOrderUpdated.value = false
                            val context = LocalContext.current
                            Toast.makeText(
                                context,
                                "Order status is updated to ${orderViewModel.orderStatus.value}",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                        }
                    }
                }
                Divider(Modifier.padding(vertical = 20.dp))

                LazyColumn(
                    Modifier.weight(1f)
                ) {
                    items(
                        items = orderItemsWithProduct.value
                    ) { orderItem ->
                        ItemCard(
                            navController,
                            item = orderItem,
                            order,
                            appViewModel.getCurrentUserId()
                        )
                    }
                }
            }
        },
        bottomBar = {
            if (appViewModel.isAdmin) AdminBottomBar(navController = navController)
            else UserBottomBar(navController = navController)
        })
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemCard(
    navController: NavHostController,
    item: FullOrderDetail,
    order: Order,
    currentUserId: String
) {
    Card(
        onClick = {
            item.product?.let { navController.navigate("product/${it.id}") }
        },
        elevation = CardDefaults.elevatedCardElevation(5.dp),
        shape = RoundedCornerShape(20.dp),
        modifier = Modifier
            .height(160.dp)
            .padding(top = 10.dp, bottom = 10.dp)
    ) {

        Row(
            Modifier
                .padding(10.dp)
                .fillMaxWidth()
        ) {
            item.product?.let {
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Image(
                        painter = rememberAsyncImagePainter(model = it.image),
                        contentDescription = null,
                        modifier = Modifier
                            .height(150.dp)
                            .width(100.dp)
                            .padding(5.dp),
                        contentScale = ContentScale.Fit
                    )
                }
            }

            Column(
                Modifier
                    .padding(start = 20.dp, bottom = 5.dp, top = 5.dp)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column(Modifier.padding(bottom = 5.dp)) {
                    item.product?.let {
                        Text(
                            text = it.title,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    item.orderItem?.let { Text(text = "Quantity:  ${it.quantity}") }
                    item.orderItem?.let { Text(text = "Price:        $${item.orderItem.price}") }

                    if (order.status.lowercase() == "delivered" && order.uid == currentUserId) {
                        Text(text = "REVIEW",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            modifier = Modifier.clickable {
                                navController.navigate("manageReview/${item.product.id}")
                            })
                    }
                }
            }
        }
    }
}