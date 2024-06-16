package com.project.ecommerceapplication.view.screens.admin.manageProducts

import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.project.ecommerceapplication.model.remote.entity.*
import com.project.ecommerceapplication.view.components.AdminBottomBar
import com.project.ecommerceapplication.view.components.DropDownMenu
import com.project.ecommerceapplication.view.components.TopBar
import com.project.ecommerceapplication.view.navigation.Screen
import com.project.ecommerceapplication.viewmodel.HomeViewModel
import com.project.ecommerceapplication.viewmodel.ProductViewModel
import com.project.ecommerceapplication.viewmodel.admin.ManageProductVM

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ManageProductInit(
    navController: NavHostController,
    actionType: String?
) {
    val homeVM = viewModel<HomeViewModel>(LocalContext.current as ComponentActivity)
    val categoryList = remember { homeVM.getAllCategories() }
    ManageProduct(
        navController,
        categoryList = categoryList,
        actionType = actionType!!
    )
}

@ExperimentalComposeUiApi
@ExperimentalMaterial3Api
@Composable
fun ManageProduct(
    navController: NavHostController,
    categoryList: List<Any> = emptyList(),
    actionType: String = "",
    viewModel: ManageProductVM = viewModel(LocalContext.current as ComponentActivity),
    productVM: ProductViewModel = viewModel(LocalContext.current as ComponentActivity)
) {
    var btnText = "Save"
    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    fun validateInputs(): Boolean {
        if (actionType == "Add") if (viewModel.cid.isBlank() || viewModel.cid == "-1") {
            Toast.makeText(context, "Invalid Input", Toast.LENGTH_SHORT).show()
            return true
        }


        if (viewModel.stock.isBlank() || viewModel.price.isBlank() || viewModel.description.isBlank() || viewModel.title.isBlank()) {
            Toast.makeText(context, "Invalid Inputs", Toast.LENGTH_SHORT).show()
            return true
        }

        return false
    }

    Scaffold(modifier = Modifier.clickable(
        interactionSource = remember { MutableInteractionSource() }, indication = null
    ) {
        keyboardController?.hide()
        focusManager.clearFocus()
    },
        topBar = { TopBar("$actionType Product", { navController.popBackStack(); }) },
        content = { padding ->
            Column(
                Modifier
                    .padding(padding)
                    .padding(20.dp)
                    .fillMaxSize(),
            ) {
                Column(
                    Modifier
                        .verticalScroll(rememberScrollState())
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    CategoryDropDown(categoryList as List<Category>, viewModel.cid)

                    OutlinedTextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = viewModel.title,
                        onValueChange = {
                            viewModel.title = it
                        },
                        label = { Text("Product Title") },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
                    )

                    OutlinedTextField(
                        value = viewModel.price,
                        onValueChange = {
                            viewModel.price = it
                        },
                        label = { Text("Product Price") },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth(),
                    )

                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(50.dp, 300.dp),
                        value = viewModel.description,
                        onValueChange = { viewModel.description = it },
                        label = { Text("Product Description") },
                        placeholder = { },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                    )

                    OutlinedTextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = viewModel.stock,
                        onValueChange = { viewModel.stock = it },
                        label = { Text("Product Stock") },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )

                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(50.dp, 300.dp),
                        value = viewModel.imageUrl,
                        onValueChange = { viewModel.imageUrl = it },
                        label = { Text("Product Image") },
                        placeholder = { },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                    )

                    if (viewModel.imageUrl !== "") {
                        Image(
                            painter = rememberAsyncImagePainter(viewModel.imageUrl),
                            contentDescription = "Product Image",
                            modifier = Modifier.size(150.dp)
                        )
                    }

                    Button(
                        onClick = {

                            if (!validateInputs()) {
                                if (actionType == "Edit") viewModel.updateProduct()
                                else viewModel.addProduct()

                                productVM.getProducts()
                                navController.navigate(Screen.AdminProductList.route)
                            }
                        }, Modifier.align(Alignment.End)
                    ) {
                        if (actionType == "Add") {
                            btnText = "Add"
                        }
                        Text(text = btnText)
                    }
                }
            }
        },
        bottomBar = { AdminBottomBar(navController = navController) })
}

@Composable
fun CategoryDropDown(list: List<Category>, cid: String) {
    var index = 0
    if (cid != "") {
        for (category in list) {
            if (category.cid == cid.toInt()) {
                index = (cid.toInt()) - 1
            }
        }
    }
    val categoryName = if (cid != "") list[index].name else "Not Selected"
    val options = list.map { it.name }

    DropDownMenu(options = options, text = "Category", type = "category", categoryName)
}