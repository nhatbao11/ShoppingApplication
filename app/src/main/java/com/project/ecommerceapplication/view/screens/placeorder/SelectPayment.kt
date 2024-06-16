package com.project.ecommerceapplication.view.screens.placeorder

import androidx.activity.ComponentActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.project.ecommerceapplication.R
import com.project.ecommerceapplication.view.components.AdminBottomBar
import com.project.ecommerceapplication.view.components.TopBar
import com.project.ecommerceapplication.view.components.UserBottomBar
import com.project.ecommerceapplication.view.navigation.Screen
import com.project.ecommerceapplication.viewmodel.AppViewModel
import com.project.ecommerceapplication.viewmodel.PlaceOrderViewModel

@ExperimentalMaterial3Api
@Composable
fun SelectPayment(navController: NavHostController) {
    Scaffold(topBar = { TopBar("Select Payment", { navController.popBackStack() }) },
        content = { padding ->
            Column(
                Modifier
                    .padding(padding)
                    .padding(20.dp)
                    .fillMaxHeight()
            ) {
                val navToSummary = { navController.navigate(Screen.OrderSummary.route) }

                PaymentOptionCard(
                    optionText = "Cash on Delivery",
                    iconID = R.drawable.cash,
                    navToSummary = navToSummary
                )

                PaymentOptionCard(
                    optionText = "Credit/Debit Card",
                    iconID = R.drawable.creditcard,
                    navToSummary = navToSummary
                )

                PaymentOptionCard(
                    optionText = "PayPal", iconID = R.drawable.paypal, navToSummary = navToSummary
                )

                Divider(Modifier.padding(top = 10.dp))
            }
        },
        bottomBar = {
            if (viewModel<AppViewModel>(LocalContext.current as ComponentActivity).isAdmin) AdminBottomBar(
                navController = navController
            )
            else UserBottomBar(navController = navController)
        })
}

@ExperimentalMaterial3Api
@Composable
fun PaymentOptionCard(
    optionText: String,
    iconID: Int,
    navToSummary: () -> Unit,
    placeOrderViewModel: PlaceOrderViewModel = viewModel(LocalContext.current as ComponentActivity)
) {
    Card(
        onClick = {
            placeOrderViewModel.selectedPayment = optionText
            navToSummary()
        },
        Modifier
            .padding(top = 10.dp, bottom = 10.dp)
            .fillMaxWidth(),
        elevation = CardDefaults.elevatedCardElevation(5.dp),
        shape = RoundedCornerShape(20.dp)
    ) {

        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(id = iconID),
                contentDescription = "Payment option icon",
                Modifier
                    .size(50.dp)
                    .padding(horizontal = 10.dp)
            )
            Text(optionText)
        }
    }
}