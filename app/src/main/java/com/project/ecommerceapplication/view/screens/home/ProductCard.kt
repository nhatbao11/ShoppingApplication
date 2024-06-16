package com.project.ecommerceapplication.view.screens.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidthIn
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.project.ecommerceapplication.R
import com.project.ecommerceapplication.model.remote.entity.Product
import com.project.ecommerceapplication.ui.theme.mediumCaption
import com.project.ecommerceapplication.ui.theme.mediumTitle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductCard(rating: Double, product: Product, navToProduct: () -> Unit) {
    Card(
        modifier = Modifier
            .height(250.dp)
            .requiredWidthIn(100.dp, 150.dp),
        onClick = { navToProduct() },
        //colors = CardDefaults.cardColors(Color.Gray)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                //.background(Color.White)
                .padding(5.dp),
            verticalArrangement = Arrangement.SpaceAround,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = rememberAsyncImagePainter(model = product.image),
                contentDescription = "Product Image",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .background(Color.White)
                    .height(150.dp)
                    .width(150.dp)
            )

            Spacer(modifier = Modifier.height(5.dp))

            Column() {
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "$${product.price}",
//                        fontSize = 18.sp,
//                        fontWeight = FontWeight.Bold,
                        style = mediumTitle,
                        maxLines = 2,
                        overflow = TextOverflow.Clip
                    )
                    Row() {
                        Image(
                            painter = painterResource(id = R.drawable.star),
                            contentDescription = "starRating",
                            modifier = Modifier.size(20.dp)
                        )
                        Text(
                            "$rating",
                            Modifier.padding(end = 5.dp),
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                        )
                    }
                }

                Spacer(modifier = Modifier.height(5.dp))

                Text(
                    product.title, style = mediumCaption, maxLines = 2, overflow = TextOverflow.Clip
                )
            }
        }
    }
}