package com.project.ecommerceapplication.view.screens.product

import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.project.ecommerceapplication.model.remote.entity.Product
import com.project.ecommerceapplication.view.components.AdminBottomBar
import com.project.ecommerceapplication.view.components.TopBar
import com.project.ecommerceapplication.view.components.UserBottomBar
import com.project.ecommerceapplication.viewmodel.AppViewModel
import com.project.ecommerceapplication.viewmodel.HomeViewModel
import com.project.ecommerceapplication.viewmodel.ProductViewModel
import com.project.ecommerceapplication.viewmodel.admin.ManageProductVM

@ExperimentalMaterial3Api
@Composable
fun ProductList(
    navController: NavHostController,
    title: String?,
    param: String?,
    type: String?,
    cid: String?,
    filterStatus: Int?,
    appViewModel: AppViewModel = viewModel(LocalContext.current as ComponentActivity),
    homeViewModel: HomeViewModel = viewModel(LocalContext.current as ComponentActivity)
) {
    val productVM = viewModel<ProductViewModel>(LocalContext.current as ComponentActivity)
    val manageProductVM = viewModel<ManageProductVM>(LocalContext.current as ComponentActivity)

    val navRoute = remember {
        mutableStateOf("FilterProducts/-1/$title")
    }

    val productFlowList = productVM.products.value
    var products: List<Product>? = emptyList()

    if (param != "") products = homeViewModel.getLikeProducts(param!!)
    else if (filterStatus != 1) { // highlight or category --- type != "Filtered"
        when (type) {
            "Highlight" -> {
                products = homeViewModel.getProductsByHighlights(title ?: "")
            }
            "Category" -> {
                navRoute.value = "FilterProducts/$cid/$title"
                products = homeViewModel.getCategoryProducts(cid!!)
            }
            "Admin" -> {
                homeViewModel.actionType = ""
                navRoute.value = "manageProduct/Add"
                products = productFlowList
            }
        }
    } else { // "Filtered"
        navRoute.value = "FilterProducts/$cid/$title" ///-1/Normal";
        products = homeViewModel.getProductList()
    }

    Scaffold(topBar = {
        TopBar(title!!, { navController.popBackStack() }, actions = {
            IconButton(onClick = {
                if (type == "Admin") {
                    manageProductVM.resetCurrentProduct()
                    navController.navigate(navRoute.value)

                } else {
                    navController.navigate(navRoute.value)
                }
            }) {
                var imageVector = Icons.Filled.FilterList
                if (type == "Admin") imageVector = Icons.Filled.Add

                Icon(
                    imageVector = imageVector, contentDescription = "Localized description"
                )
            }
        })
    }, content = { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val addedCartToast =
                Toast.makeText(LocalContext.current, "Added to Cart", Toast.LENGTH_SHORT)

            if (products?.isEmpty() == true) {
                var navRoute = "FilterProducts/${if (cid == "") "-1" else cid}/$title"
                if (filterStatus != 1)
                    navRoute = "home"
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Text("No search results found!", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    Button(onClick = { navController.navigate(navRoute) }) { //go home.
                        Text("Try again")
                    }
                }


            } else {
                LazyColumn(
                    contentPadding = PaddingValues(all = 10.dp),
                    verticalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    items(items = products!!, key = { product -> product.id }) { product ->
                        ProductListCard(
                            product,
                            addedCartToast,
                            navController
                        )
                    }
                }
            }
        }
    }, bottomBar = {
        if (appViewModel.isAdmin) AdminBottomBar(navController = navController)
        else UserBottomBar(navController = navController)
    })
}