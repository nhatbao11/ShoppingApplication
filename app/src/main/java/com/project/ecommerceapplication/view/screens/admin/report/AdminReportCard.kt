package com.project.ecommerceapplication.view.screens.admin.report

import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.project.ecommerceapplication.model.remote.entity.*
import com.project.ecommerceapplication.ui.theme.largeTitle
import com.project.ecommerceapplication.ui.theme.mediumTitle
import com.project.ecommerceapplication.ui.theme.smallCaption
import com.project.ecommerceapplication.ui.theme.smallTitle
import com.project.ecommerceapplication.viewmodel.admin.ReportVM

@Composable
fun AdminReportCardInit(order: Order, allProducts: List<Product>) {
    AdminReportCard(order, allProducts)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminReportCard(order: Order, allProducts: List<Product>) {
    val reportVM = viewModel<ReportVM>(LocalContext.current as ComponentActivity)

    Card(
        modifier = Modifier
            .padding(2.dp)
            .fillMaxWidth(),
        onClick = {},
        shape = CardDefaults.outlinedShape
    ) {

        Column(Modifier.padding(10.dp)) {
            Text(
                text = "Order# ${order.id}",
                style = largeTitle
            )
            Text(
                text = "Status ${order.status}",
                style = mediumTitle
            )
            Spacer(Modifier.height(5.dp))
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(
                    text = "${order.date}", style = smallTitle
                )
                Text(
                    text = "Total $${order.total}", style = smallTitle
                )
            }
            Divider(color= MaterialTheme.colorScheme.inversePrimary)
            Spacer(modifier = Modifier.height(5.dp))
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Product xQuantity", style = smallCaption)
                Text(text = "Price", style = smallCaption)
            }
            Spacer(modifier = Modifier.height(10.dp))

            for (items in order.items ?: emptyList()) {
                val product = reportVM.getProductDetails(items.pid, allProducts)
                val strList = product?.title?.split(" ")
                if (strList.isNullOrEmpty()) continue
                val str =
                    if (strList.size < 3) product.title else strList.subList(0, 3).joinToString(separator = " ")

                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Row() {
                        Text(text = str, style = smallTitle)
                        Text(
                            text = " x" + items.quantity, style = smallTitle
                        )
                    }
                    Text(text = "$${product.price}", style = smallTitle)
                }
            }
        }

//        Column(
//            Modifier
//                .padding(12.dp)
//                .fillMaxWidth()
//        ) {
//            Text(
//                text = "Product Name: YYYY",
//                fontWeight = FontWeight.Bold,
//                fontSize = 18.sp,
//                softWrap = true,
//                overflow = TextOverflow.Clip,
//            )
//            Text(
//                text = "Price: 200$",
//                softWrap = true,
//                overflow = TextOverflow.Clip,
//            )
//            Row(
//                horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()
//            ) {
//                Text(
//                    text = "Ordered Date: 12/01/2002",
//                    softWrap = true,
//                    overflow = TextOverflow.Clip,
//                )
//                Spacer(Modifier.width(10.dp))
//                Text(
//                    text = "Status: Progressing",
//                    softWrap = true,
//                    overflow = TextOverflow.Clip,
//                )
//            }
//        }
    }
}
