package com.project.ecommerceapplication.view.screens.product

import androidx.activity.ComponentActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.project.ecommerceapplication.R
import com.project.ecommerceapplication.model.remote.entity.Review
import com.project.ecommerceapplication.view.components.AdminBottomBar
import com.project.ecommerceapplication.view.components.TopBar
import com.project.ecommerceapplication.view.components.UserBottomBar
import com.project.ecommerceapplication.view.navigation.Screen
import com.project.ecommerceapplication.viewmodel.AppViewModel
import com.project.ecommerceapplication.viewmodel.ProductViewModel

@ExperimentalComposeUiApi
@ExperimentalMaterial3Api
@Composable
fun ProductDetails(
    navController: NavHostController,
    productId: String,
    appViewModel: AppViewModel = viewModel(LocalContext.current as ComponentActivity),
    viewModel: ProductViewModel = viewModel(LocalContext.current as ComponentActivity)
) {
    val currentUserId = appViewModel.getCurrentUserId()
    viewModel.setCurrentProduct(productId)

    val product = viewModel.product

    val reviews = product.value.reviews
    val avgRating = viewModel.getProductAvgRating(reviews)
    val reviewCount = reviews.size

    val userBought = viewModel.userBoughtItem(currentUserId, productId)

    Scaffold(topBar = { TopBar(navBack = { navController.popBackStack() }) }, content = { padding ->
        Column(
            Modifier
                .padding(padding)
                .padding(20.dp)
                .fillMaxHeight()
        ) {
            var titleFontSize = 25.sp
            if (product.value.title.length > 55) titleFontSize = 22.sp
            Text(product.value.title, fontWeight = FontWeight.Bold, fontSize = titleFontSize)

            if (reviewCount != 0) {
                Row(Modifier.padding(bottom = 10.dp)) {
                    Text("⭐$avgRating", fontWeight = FontWeight.Bold)
                    Text(" ($reviewCount ${if (reviewCount == 1) "Review" else "Reviews"})")
                }
            }

            LazyColumn(
                Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                item() {
                    Image(
                        painter = rememberAsyncImagePainter(model = product.value.image),
                        contentDescription = "Product image",
                        Modifier.size(300.dp)
                    )
                }

                item() { Text(product.value.description) }

                item() { Divider() }

                item() {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Reviews", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        if (userBought) {
                            IconButton(onClick = {
                                navController.navigate("manageReview/${productId}")
                            }) {
                                Row() {
                                    Icon(
                                        imageVector = Icons.Outlined.Add,
                                        contentDescription = "Add Review",
                                        tint = Color.Blue
                                    )
                                }
                            }
                        }
                    }
                }

                if (reviewCount != 0) items(items = reviews) { ReviewCard(it) }
                else item() { Text("This product does not have any reviews yet.") }
            }

            val openDialog = remember { mutableStateOf(false) }
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("$${product.value.price}", fontWeight = FontWeight.Bold, fontSize = 25.sp)

                Button(
                    onClick = {
                        viewModel.addToCart(currentUserId, productId)
                        openDialog.value = true
                    }, Modifier.padding(top = 10.dp), enabled = product.value.stock != 0
                ) {
                    Text("ADD TO CART", fontSize = 15.sp, fontWeight = FontWeight.Bold)
                }
            }

            if (openDialog.value) {
                AlertDialog(onDismissRequest = { openDialog.value = false },
                    title = { Text("Added to Cart") },
                    text = {
                        Column() {
                            Text(
                                "Product added to cart successfully.", Modifier.padding(top = 10.dp)
                            )
                        }
                    },
                    confirmButton = {
                        Button(modifier = Modifier.fillMaxWidth(), onClick = {
                            openDialog.value = false
                            navController.navigate(Screen.Cart.route)
                        }) {
                            Text("Go to Cart")
                        }
                    },
                    dismissButton = {
                        Button(modifier = Modifier.fillMaxWidth(), onClick = {
                            openDialog.value = false
                            navController.navigate(Screen.Home.route)
                        }) {
                            Text("Continue Shopping")
                        }
                    })
            }
        }
    }, bottomBar = {
        if (appViewModel.isAdmin) AdminBottomBar(navController = navController)
        else UserBottomBar(navController = navController)
    }
    )
}

@ExperimentalComposeUiApi
@ExperimentalMaterial3Api
@Composable
fun ReviewCard(review: Review) {
    Card(
        Modifier
            .fillMaxWidth()
            .padding(top = 10.dp, bottom = 10.dp),
        elevation = CardDefaults.elevatedCardElevation(5.dp),
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(Modifier.padding(10.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                Row() {
                    Image(
                        painter = painterResource(id = R.drawable.profile),
                        contentDescription = "Review user profile picture",
                        Modifier
                            .size(25.dp)
                            .clip(RoundedCornerShape(15.dp))
                    )

                    Text(
                        text = review.date,
                        Modifier.padding(start = 5.dp, end = 5.dp),
                        fontWeight = FontWeight.Bold
                    )
                }
                Text("⭐".repeat(review.stars))
            }
            Text(review.title, fontWeight = FontWeight.Bold)
            Text(review.details)
        }
    }
}