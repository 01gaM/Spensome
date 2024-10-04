package com.spensome

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.spensome.data.ProductsRepository
import com.spensome.navigation.WishListNavGraph
import com.spensome.ui.theme.SpensomeTheme
import com.spensome.ui.screens.wishlist.WishListViewModel
import com.spensome.ui.screens.wishlist.new_product.NewProductViewModel

class MainActivity : ComponentActivity() {
    private val wishListViewModel by viewModels<WishListViewModel>()
    private val newProductViewModel by viewModels<NewProductViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadProductsList()
        setContent {
            SpensomeTheme {
                WishListNavGraph(
                    wishListViewModel = wishListViewModel,
                    newProductViewModel = newProductViewModel
                )
            }
        }
    }

    private fun loadProductsList() {
        wishListViewModel.updateProductsList(products = ProductsRepository.products)
    }
}
