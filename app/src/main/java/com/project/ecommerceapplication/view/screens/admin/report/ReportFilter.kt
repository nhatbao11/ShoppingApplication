package com.project.ecommerceapplication.view.screens.admin.report

import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.project.ecommerceapplication.model.remote.entity.Order
import com.project.ecommerceapplication.view.components.DatePickerDialog
import com.project.ecommerceapplication.view.components.TopBar
import com.project.ecommerceapplication.viewmodel.admin.ReportVM
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminReportFilter(
    navController: NavHostController,
    reportVM: ReportVM = viewModel(LocalContext.current as ComponentActivity),
) {
    val currentDate: LocalDate = LocalDate.now()
    val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy")
    val currentDateFormatted: String = currentDate.format(formatter)

    var error by remember { mutableStateOf(false) }
    val startDate = remember { mutableStateOf("N/A") }
    val endDate = remember { mutableStateOf(currentDateFormatted) }
    Scaffold(topBar = {
        TopBar("Filter Report", { navController.popBackStack() }, actions = {
            TextButton(onClick = {

                if (startDate.value != "N/A" && LocalDate.parse(endDate.value, formatter)
                        .isBefore(LocalDate.parse(startDate.value, formatter))
                ) {
                    error = true
                } else {
                    reportVM.startDate.value = startDate.value
                    reportVM.endDate.value = endDate.value
                    reportVM.finalOrderStatusValue.value = reportVM.tempOrderStatusValue.value
                    reportVM.filterReport("report")?.toMutableList()
                        ?: emptyList<Order>().toMutableList()
                    reportVM.isFiltered = true

                    navController.popBackStack()
                }
            }) {
                Text(text = "Apply")
            }
        })
    }, content = { padding ->
        Column(
            Modifier
                .padding(padding)
                .padding(12.dp)
        ) {
            val context = LocalContext.current
            if (error) {
                Toast.makeText(context, "End date cannot be after Start date.", Toast.LENGTH_SHORT)
                    .show()
                error = false
            }
            val focusManager = LocalFocusManager.current

            Text(text = "Status", fontSize = 22.sp, fontWeight = FontWeight.Bold)
            StatusRadioButton()
            Text(text = "Start Date", fontSize = 22.sp, fontWeight = FontWeight.Bold)
            //Date Picker
            val calendar = Calendar.getInstance()
            calendar.add(Calendar.DAY_OF_MONTH, -1)

            val dateDialog = DatePickerDialog(
                startDate, context, calendar, calendar.timeInMillis
            )
            OutlinedTextField(
                value = startDate.value,
                modifier = Modifier.clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }) {
                    focusManager.clearFocus()
                    dateDialog.show()
                },
                onValueChange = {},
                label = { Text("start date") },
                singleLine = true,
                enabled = false,
            )

            Text(text = "End Date", fontSize = 22.sp, fontWeight = FontWeight.Bold)
            //Date Picker
            val calendar2 = Calendar.getInstance()
            val dateDialog2 = DatePickerDialog(
                endDate , context, calendar2, calendar2.timeInMillis
            )
            OutlinedTextField(
                value = endDate.value,
                modifier = Modifier.clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }) {
                    focusManager.clearFocus()
                    dateDialog2.show()
                },
                onValueChange = {},
                label = { Text("end date") },
                singleLine = true,
                enabled = false,
            )
        }
    })
}

@Composable
fun StatusRadioButton() {
    val reportVM = viewModel<ReportVM>(LocalContext.current as ComponentActivity)
// Note that Modifier.selectableGroup() is essential to ensure correct accessibility behavior
    Column(Modifier.selectableGroup()) {
        reportVM.radioOptions.forEach { text ->
            Row(
                Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .selectable(
                        selected = (text ==  reportVM.tempOrderStatusValue.value),
                        onClick = {  reportVM.tempOrderStatusValue.value = text },
                        role = Role.RadioButton
                    )
                    .padding(horizontal = 16.dp), verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = (text ==  reportVM.tempOrderStatusValue.value),
                    onClick = null // null recommended for accessibility with screenreaders
                )
                Text(
                    text = text,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(start = 16.dp)
                )
            }
        }
    }
}