package com.project.ecommerceapplication.view.screens.productFilter

import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.util.toRange
import androidx.lifecycle.viewmodel.compose.viewModel
import com.project.ecommerceapplication.viewmodel.FilterViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PriceFilterComponent(filterVM: FilterViewModel = viewModel(
    LocalContext.current as ComponentActivity
), scope: CoroutineScope, state: ModalBottomSheetState
){

    Row(
        Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "Price",
            textAlign = TextAlign.Left,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            softWrap = true,
            overflow = TextOverflow.Clip,
        )
        Row() {
            Spacer(Modifier.width(20.dp))
            TextButton(onClick = {
                scope.launch {
                    filterVM.startPriceText =
                        filterVM.getStartPrice().toInt().toString()
                    filterVM.endPriceText = filterVM.getEndPrice().toInt().toString()
                    state.show()
                }
            }) {
                Icon(
                    Icons.Outlined.Edit, contentDescription = "Localized description"
                )
                Text("Adjust Price")
            }
            TextButton(onClick = {
                filterVM.setPriceRange(0f..5000f)
                filterVM.setPriceEnd(0f..5000f)
            }) {
                Text(
                    text = "Reset",
                    textAlign = TextAlign.Left,
                )
            }
        }

    }

    PriceRangeSlider(filterVM)
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PriceRangeSlider(
    filterViewModel: FilterViewModel = viewModel(
        LocalContext.current as ComponentActivity
    )
) {

    Column() {
        Row(
            Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row() {
                Text("Minimum: ")
                Text(
                    "$${filterViewModel.getPriceRange().toRange().lower.toInt()}",
                    fontWeight = FontWeight.Bold
                )
            }
            Row() {
                Text("Maximum: ")
                Text(
                    "$${filterViewModel.getPriceRange().toRange().upper.toInt()}",
                    fontWeight = FontWeight.Bold
                )
            }
        }

        RangeSlider(
            modifier = Modifier.semantics { contentDescription = "Localized Description" },
            value = filterViewModel.getPriceRange(),
            onValueChange = { filterViewModel.setPriceRange(it) }, //colors = Color.Black
            steps = 50,
            valueRange = 0f..5000f,
            onValueChangeFinished = {
                // launch some business logic update with the state you hold
                // viewModel.updateSelectedSliderValue(sliderPosition)
                filterViewModel.setPriceEnd(filterViewModel.getPriceRange())
            }
        )
    }
}