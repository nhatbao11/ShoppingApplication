package com.project.ecommerceapplication.view.screens.orders

import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.ShoppingBasket
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.project.ecommerceapplication.R
import com.project.ecommerceapplication.model.remote.entity.Order
import com.project.ecommerceapplication.ui.theme.largeTitle
import com.project.ecommerceapplication.ui.theme.mediumCaption
import com.project.ecommerceapplication.view.components.AdminBottomBar
import com.project.ecommerceapplication.view.components.TopBar
import com.project.ecommerceapplication.view.components.UserBottomBar
import com.project.ecommerceapplication.viewmodel.AppViewModel
import com.project.ecommerceapplication.viewmodel.OrderViewModel
import androidx.compose.foundation.lazy.items


@ExperimentalMaterial3Api
@Composable
fun TrackOrders(
    navController: NavHostController,
    appViewModel: AppViewModel = viewModel(LocalContext.current as ComponentActivity),
    orderViewModel: OrderViewModel = viewModel(LocalContext.current as ComponentActivity),
) {
    orderViewModel.setUserId(
        appViewModel.getCurrentUserId()
    )

    val orders = orderViewModel.orders

    Scaffold(topBar = {
        TopBar(title = "Track Orders", { navController.popBackStack() })
    }, content = { padding ->
        LazyColumn(
            Modifier
                .padding(padding)
                .padding(20.dp)
        ) {
            items(items = orders.value) { order ->
                OrderCard(order = order, navController)
            }
        }

    }, bottomBar = {
        if (appViewModel.isAdmin) AdminBottomBar(navController = navController)
        else UserBottomBar(navController = navController)
    })
}

@ExperimentalMaterial3Api
@Composable
fun OrderCard(order: Order, navController: NavHostController) {
    Card(
        onClick = {
            navController.navigate("orderDetails/${order.id}")
        },

        Modifier
            .fillMaxWidth()
            .padding(top = 10.dp, bottom = 15.dp)
            .height(175.dp),
        elevation = CardDefaults.elevatedCardElevation(5.dp),
        shape = RoundedCornerShape(10.dp)
    ) {
        Column(
            Modifier
                .padding(10.dp)
                .fillMaxHeight()
        ) {
            Row() {
                Column(
                    verticalArrangement = Arrangement.spacedBy(5.dp),
                ) {
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "ID", style = largeTitle
                        )
                        Text(
                            text = order.id, style = largeTitle
                        )
                    }
                    Divider(color = Color.Black)
                    Spacer(modifier = Modifier.height(2.dp))
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        Text(
                            text = order.date, style = largeTitle
                        )
                    }
                    Row(
                        Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "Status:  ${order.status}", style = mediumCaption)
                        Text(text = "Total: $${order.total}", style = mediumCaption)
                    }
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            Row {
                StepBar(doneStatus = order.status)
            }
        }
    }
}

@Composable
fun StepBar(doneStatus: String) {
    val stepIsComplete: Int = when (doneStatus) {
        "Processing" -> 1
        "Shipped" -> 2
        else -> 3
    }

    Row(modifier = Modifier.fillMaxWidth(1f), horizontalArrangement = Arrangement.SpaceBetween) {
        Step("Processing", 1, stepIsComplete)
        Step("Shipped", 2, stepIsComplete)
        Step("delivered", 3, stepIsComplete)
    }
}

@Composable
fun Step(status: String, stepNumber: Int, state: Int) {

    val innerCircleColor = if (state >= stepNumber) Color.hsl(120f, 1f, 0.3922f, 1f) else Color.Red

    val statusIcon = if (state != 0 && stepNumber <= state) {
        painterResource(id = R.drawable.check_circle)
    } else {
        painterResource(id = R.drawable.pending_circle2)
    }

    val icon: ImageVector = when (status.lowercase()) {
        "processing" -> StatusIcons.PROCESSING
        "shipped" -> StatusIcons.SHIPPED
        else -> StatusIcons.DELIVERED
    }

    Column {
        Icon(
            painter = statusIcon, contentDescription = null, tint = innerCircleColor
        )
        Icon(
            imageVector = icon, contentDescription = null
        )
    }

}

object StatusIcons {
    val PROCESSING = Icons.Default.ShoppingBasket
    val SHIPPED = Icons.Default.LocalShipping
    val DELIVERED = Icons.Default.Inventory2
}

val String.color get() = Color(android.graphics.Color.parseColor(this))