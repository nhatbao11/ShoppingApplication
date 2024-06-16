package com.project.ecommerceapplication.viewmodel

import android.app.Application
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.AndroidViewModel
import com.project.ecommerceapplication.SearchBarState
import com.project.ecommerceapplication.model.remote.entity.Category
import com.project.ecommerceapplication.model.remote.entity.Product
import com.project.ecommerceapplication.model.repository.CategoryRepository
import com.project.ecommerceapplication.model.repository.ProductRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class HomeViewModel(val context: Application) : AndroidViewModel(context) {
    private val productRepository = ProductRepository
    private val categoryRepository = CategoryRepository

    private var filteredProductList = getProductList().toMutableStateList()
    var actionType by mutableStateOf("")
    val highlightList = listOf("Trending", "New Arrivals", "Top Ranked")
    var trendingProducts = mutableListOf<Product>()

    private val initialColor = Color.Red
    val list = getColorsList().toMutableStateList()

    fun setColor(highlight: String, color: Color) {
        val index = highlightList.indexOf(highlight)
        list[index] = color
    }

    private fun getColorsList(): List<Color> {
        return highlightList.map { (initialColor) }
    }

    fun getColor(highlight: String): Color {
        val index = highlightList.indexOf(highlight)
        return list[index]
    }

    fun getLikeProducts(query: String): List<Product> {
        var searchItems: List<Product> = listOf()
        runBlocking {
            this.launch(Dispatchers.IO) {
                val products = productRepository.getProductByName(query)
                searchItems = products
            }
        }
        return searchItems
    }

    fun setProductList(list: List<Product>) {
        filteredProductList = list.toMutableStateList()
    }

    fun getProductList(): List<Product> {
        return filteredProductList?.toList() ?: emptyList()
    }

    fun getAllCategories(): List<Category> {
        var categories: List<Category> = emptyList()
        runBlocking {
            this.launch(Dispatchers.IO) {
                val temp = categoryRepository.getAllCategories()
                categories = temp
            }
        }
        return categories
    }

    fun getCategoryProducts(query: String): List<Product>? {
        var categoryItems: List<Product>? = null
        runBlocking {
            this.launch(Dispatchers.IO) {
                val products = productRepository.getProductsByCategory(query)
                categoryItems = products
            }
        }
        return categoryItems
    }

    fun getProductsByHighlights5(highlight: String): List<Product> {
        val productList = getProductsByHighlights(highlight)
        return productList.subList(0, if (productList.size > 5) 5 else productList.size)
    }

    fun getProductsByHighlights(highlight: String): List<Product> {
        var products: List<Product> = emptyList()
        if (highlight == "New Arrivals") {
            runBlocking {
                this.launch(Dispatchers.IO) {
                    products = productRepository.getNewArrivals()
                }
            }
        } else if (highlight == "Top Ranked") {
            runBlocking {
                this.launch(Dispatchers.IO) {
                    products = productRepository.getTopRanked()
                }
            }
        } else if (highlight == "Trending") {
            runBlocking {
                this.launch(Dispatchers.IO) {
                    products = productRepository.getTrending()
                    trendingProducts = products.toMutableList()
                }
            }
        }
        return products
    }

    private val _searchBarState: MutableState<SearchBarState> =
        mutableStateOf(value = SearchBarState.CLOSED)
    val searchBarState: State<SearchBarState> = _searchBarState

    private val _searchTextState: MutableState<String> = mutableStateOf(value = "")
    val searchTextState: State<String> = _searchTextState

    fun updateSearchBarState(newValue: SearchBarState) {
        _searchBarState.value = newValue
    }

    fun updateSearchTextState(newValue: String) {
        _searchTextState.value = newValue
    }
}