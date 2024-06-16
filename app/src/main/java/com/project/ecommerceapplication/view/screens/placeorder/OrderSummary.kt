package com.project.ecommerceapplication.view.screens.placeorder

import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.project.ecommerceapplication.model.remote.entity.Address
import com.project.ecommerceapplication.model.remote.entity.CartWithProduct
import com.project.ecommerceapplication.view.components.AdminBottomBar
import com.project.ecommerceapplication.view.components.SuccessDialog
import com.project.ecommerceapplication.view.components.TopBar
import com.project.ecommerceapplication.view.components.UserBottomBar
import com.project.ecommerceapplication.view.navigation.Screen
import com.project.ecommerceapplication.viewmodel.AppViewModel
import com.project.ecommerceapplication.viewmodel.CartViewModel
import com.project.ecommerceapplication.viewmodel.PlaceOrderViewModel

@ExperimentalMaterial3Api
@Composable
fun OrderSummary(
    navController: NavHostController,
    appViewModel: AppViewModel = viewModel(LocalContext.current as ComponentActivity),
    placeOrderViewModel: PlaceOrderViewModel = viewModel(LocalContext.current as ComponentActivity),
    cartViewModel: CartViewModel = viewModel(LocalContext.current as ComponentActivity),
) {
    Scaffold(topBar = { TopBar("Order Summary", { navController.popBackStack() }) },
        content = { padding ->
            Column(
                Modifier
                    .padding(padding)
                    .padding(20.dp)
                    .fillMaxHeight()
            ) {
                val userCart = cartViewModel.userCart.value
                val totalPrice = cartViewModel.grandTotal.value

                Column(
                    Modifier.weight(5f, true)
                ) {
                    SummaryPageSubtitleText("ITEMS")

                    LazyColumn(
                        Modifier
                            .weight(1f)
                            .padding(bottom = 5.dp)
                    ) {
                        items(items = userCart, key = { userCart -> userCart.cart.id }) {
                            SummaryPageItemRow(it)
                        }
                    }

                    Row(
                        Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("TOTAL:", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                        Text("$$totalPrice", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    }

                    SummaryPageDivider()
                }

                // Selected Address
                Column(Modifier.weight(2.5f)) {
                    SummaryPageSubtitleText("DELIVERING TO")
                    SummaryPageAddressDetails(placeOrderViewModel.selectedAddress)
                    SummaryPageDivider()
                }

                // Selected Payment
                Column(Modifier.weight(1f)) {
                    SummaryPageSubtitleText("PAYMENT METHOD")
                    Text(placeOrderViewModel.selectedPayment)
                    SummaryPageDivider()
                }

                Row(
                    Modifier
                        .fillMaxWidth()
                        .weight(0.5f),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.Bottom
                ) {
                    val openDialog = remember { mutableStateOf(false) }
                    Button(onClick = {
                        placeOrderViewModel.placeOrder(userCart, totalPrice)
                        openDialog.value = true
                    }) {
                        Text("Place Order")
                    }

                    SuccessDialog(
                        openDialog, "Order Confirmed", "Thank you for shopping with us!", "Dismiss"
                    ) {
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.Home.route) { inclusive = true }
                        }
                    }
                }
            }
        },
        bottomBar = {
            if (appViewModel.isAdmin) AdminBottomBar(
                navController = navController
            )
            else UserBottomBar(navController = navController)
        }

    )
}

@Composable
fun SummaryPageSubtitleText(text: String) {
    Text(text, Modifier.padding(bottom = 8.dp), fontSize = 18.sp, fontWeight = FontWeight.Bold)
}

@Composable
fun SummaryPageItemRow(cartItem: CartWithProduct) {
    Card(
        Modifier
            .fillMaxWidth()
            .padding(top = 10.dp, bottom = 10.dp),
        elevation = CardDefaults.elevatedCardElevation(5.dp)
    ) {
        Column(
            Modifier
                .padding(10.dp)
                .fillMaxHeight()
                .fillMaxWidth(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = cartItem.product.title,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(top = 5.dp, end = 5.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "$${cartItem.product.price}")

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(text = "x ${cartItem.cart.quantity}")
                }
            }
        }
    }
}

@Composable
fun SummaryPageAddressDetails(address: Address) {
    Card(Modifier.fillMaxWidth()) {
        Column(Modifier.padding(10.dp)) {
            Text(address.name, fontWeight = FontWeight.Bold)
            Text("Unit ${address.unit}, Building ${address.building}")
            Text("Street ${address.street}, Zone ${address.zone}")
            Text("PO Box: ${address.poBox}")
            Text(address.phone)
        }
    }
}

@Composable
fun SummaryPageDivider() {
    Divider(Modifier.padding(top = 10.dp, bottom = 10.dp))
}