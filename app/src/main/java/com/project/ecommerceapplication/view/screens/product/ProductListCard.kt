package com.project.ecommerceapplication.view.screens.product

import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredHeightIn
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.project.ecommerceapplication.R
import com.project.ecommerceapplication.model.remote.entity.Product
import com.project.ecommerceapplication.viewmodel.AppViewModel
import com.project.ecommerceapplication.viewmodel.HomeViewModel
import com.project.ecommerceapplication.viewmodel.ProductViewModel
import com.project.ecommerceapplication.viewmodel.admin.ManageProductVM

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductListCard(
    product: Product, addedToast: Toast, navController: NavController
) {
    val appViewModel = viewModel<AppViewModel>(LocalContext.current as ComponentActivity)
    val currentUserId = appViewModel.getCurrentUserId()
    val isAdmin = appViewModel.isAdmin

    val productVM = viewModel<ProductViewModel>(LocalContext.current as ComponentActivity)
    val homeVM = viewModel<HomeViewModel>(LocalContext.current as ComponentActivity)
    val manageProductVM = viewModel<ManageProductVM>(LocalContext.current as ComponentActivity)
    val onProductDeleteDialog = remember { mutableStateOf(false) }

    var rating = 0.0
    if (product.reviews.size > 0) {
        for (review in product.reviews) rating += review.stars
        rating /= product.reviews.size
    }

    Card(
        modifier = Modifier
            .requiredHeightIn(115.dp, 160.dp)
            .fillMaxWidth(),
        onClick = {
            if (isAdmin) {
                if (homeVM.actionType == "see all" || homeVM.actionType == "search" || homeVM.actionType == "category") {
                    navController.navigate("product/${product.id}")
                } else {
                    manageProductVM.setCurrentProduct(product)
                    navController.navigate("manageProduct/Edit")
                }
            } else navController.navigate("product/${product.id}")
        },
        //colors = CardDefaults.cardColors(Color.Gray),
        elevation = CardDefaults.cardElevation(10.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
//                .background(Color.White)
                .padding(5.dp),
            horizontalArrangement = Arrangement.SpaceAround,

            ) {
            Column() {
                if (isAdmin) {
                    IconButton(modifier = Modifier.fillMaxHeight(0.2f), onClick = {
                        onProductDeleteDialog.value = true
                    }) {
                        Icon(
                            imageVector = Icons.Filled.Delete,
                            contentDescription = "Delete Product",
                            tint = Color.Red
                        )
                    }
                }
                Image(
                    painter = rememberAsyncImagePainter(model = product.image),
                    contentDescription = product.title,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .background(Color.White)
                        .fillMaxHeight(1f)
                        .width(120.dp),
                )
            }

            Spacer(modifier = Modifier.width(10.dp))
            Column(Modifier.fillMaxWidth()) {
                Text(
                    text = product.title,
                    modifier = Modifier
                        // .background(Color.White)
                        .height(25.dp)
                        .fillMaxWidth(),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Left
                )
                Spacer(modifier = Modifier.height(5.dp))
                Text(
                    text = product.description,
                    modifier = Modifier
                        //.background(Color.White)
                        .requiredHeight(50.dp)
                        .fillMaxWidth(),
                    overflow = TextOverflow.Ellipsis,
                    fontSize = 12.sp,
                    //color = Color.Gray,
                    lineHeight = 15.sp,
                    textAlign = TextAlign.Left,
                    softWrap = true
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.star),
                        contentDescription = "starRating",
                        modifier = Modifier.size(20.dp)
                    )

                    Text(
                        text = rating.toString(),
                        modifier = Modifier
                            //.background(Color.White)
                            .height(20.dp)
                            .fillMaxWidth()
                            .weight(2F),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Left
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                ElevatedButton(modifier = Modifier
                    .height(150.dp)
                    .fillMaxWidth()
                    .align(Alignment.Start),
                    //colors = ButtonDefaults.buttonColors(Color(240, 244, 239)),
                    onClick = {
                        productVM.addToCart(currentUserId, product.id)
                        addedToast.show()
                    }) {
                    Text(
                        text = "$${product.price}",
                        textAlign = TextAlign.Left,
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(2F),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        //color = Color.Black
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = "Add product to cart",
                        tint = Color.Black
                    )
                }
            }
        }
    }
    if (onProductDeleteDialog.value) {
        AlertDialog(onDismissRequest = { onProductDeleteDialog.value = false }, title = {
            Text(text = "Remove Product")
        }, text = {
            Text("Would you like to remove this product?")
        }, confirmButton = {
            Button(onClick = {
                onProductDeleteDialog.value = false
                productVM.deleteProduct(product.id)
            }) {
                Text("Yes")
            }
        }, dismissButton = {
            Button(onClick = {
                onProductDeleteDialog.value = false
            }) {
                Text("Cancel")
            }
        })
    }
}