package com.project.ecommerceapplication.view.screens.admin.dashboard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.project.ecommerceapplication.ui.theme.mediumTitle
import com.project.ecommerceapplication.ui.theme.smallTitle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatsCard(icon: ImageVector, title: String, info: String, onClick: ()-> Unit) {
    Card(
        modifier = Modifier
            .size(100.dp)
            .fillMaxHeight(0.6F),
        elevation = CardDefaults.cardElevation(8.dp),
        onClick = onClick
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(5.dp),
            verticalArrangement = Arrangement.SpaceAround,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = icon, contentDescription = "dashboard stat icon",
                modifier = Modifier
                    .weight(1.25f),
            )
            Spacer(modifier = Modifier.height(10.dp))

            Text(text = info, modifier= Modifier.weight(1.5f), style = mediumTitle)
            Text(text = title, modifier= Modifier.weight(1.5f).wrapContentSize(Alignment.Center, true), style = smallTitle)


        }


    }
}