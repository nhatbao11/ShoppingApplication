package com.project.ecommerceapplication.view.screens.home

import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.project.ecommerceapplication.model.remote.entity.Product
import com.project.ecommerceapplication.viewmodel.HomeViewModel
import com.project.ecommerceapplication.viewmodel.ProductViewModel

@Composable
fun ProductHighlights(
    highlight: String,
    navController: NavHostController,
    homeViewModel: HomeViewModel = viewModel(LocalContext.current as ComponentActivity),
    productVM: ProductViewModel = viewModel(LocalContext.current as ComponentActivity)
) {
    Row(
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = highlight,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            modifier = Modifier
                .padding(5.dp)
                .offset(x = 5.dp),
            textAlign = TextAlign.Left
        )

        TextButton(onClick = {
            homeViewModel.setColor(highlight, Color.Black)
            navController.navigate("productList/${highlight}/${"Highlight"}/-1/0")
            homeViewModel.actionType = "see all"
        }) {
            Text(
                text = "see all",
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                modifier = Modifier.padding(5.dp),
                textAlign = TextAlign.Right,
                color = homeViewModel.getColor(highlight)
            )
        }
    }

    LazyRow(
        contentPadding = PaddingValues(all = 10.dp),
        horizontalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        items(homeViewModel.getProductsByHighlights5(highlight)) { product: Product ->
            ProductCard(
                productVM.getProductAvgRating(product.reviews),
                product,
            ) { navController.navigate("product/${product.id}") }
        }
    }
}