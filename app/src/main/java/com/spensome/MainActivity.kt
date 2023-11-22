package com.spensome

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.spensome.data.ProductsRepository
import com.spensome.ui.theme.SpensomeTheme
import com.spensome.ui.wishlist.WishListScreen
import com.spensome.ui.wishlist.WishListViewModel

class MainActivity : ComponentActivity() {
    private val viewModel by viewModels<WishListViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadProductsList()
        setContent {
            SpensomeTheme {
                val state by viewModel.state.collectAsState()
                WishListScreen(
                    state = state,
                    onEvent = viewModel::onEvent
                )
            }
        }
    }

    private fun loadProductsList() {
        viewModel.updateProductsList(products = ProductsRepository.products)
    }
}
