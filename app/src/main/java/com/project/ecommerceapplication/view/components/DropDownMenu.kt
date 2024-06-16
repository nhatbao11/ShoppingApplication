package com.project.ecommerceapplication.view.components

import androidx.activity.ComponentActivity
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.project.ecommerceapplication.viewmodel.OrderViewModel
import com.project.ecommerceapplication.viewmodel.admin.ManageProductVM

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropDownMenu(options: List<String>, text: String, type: String, defaultOption: String) {
    val orderVM = viewModel<OrderViewModel>(LocalContext.current as ComponentActivity)
    val manageProductVM = viewModel<ManageProductVM>(LocalContext.current as ComponentActivity)

    var expanded by remember { mutableStateOf(false) }
    var selectedOptionText by remember { (mutableStateOf(defaultOption)) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
    ) {
        TextField(
            // The `menuAnchor` modifier must be passed to the text field for correctness.
            modifier = Modifier.menuAnchor(),
            readOnly = true,
            value = selectedOptionText,
            onValueChange = {},
            label = { Text(text) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            colors = ExposedDropdownMenuDefaults.textFieldColors(),
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            options.forEachIndexed() { index, selectionOption ->
                DropdownMenuItem(
                    text = { Text(selectionOption) },
                    onClick = {
                        when (type) {
                            "category" -> manageProductVM.cid = (index + 1).toString()
                            "orders" -> {
                                orderVM.orderStatus.value = selectionOption
                                orderVM.isOrderUpdated.value = true
                            }
                        }
                        expanded = false
                        selectedOptionText = selectionOption
                    },
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                )
            }
        }
    }
}