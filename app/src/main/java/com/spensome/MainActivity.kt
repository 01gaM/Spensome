package com.spensome

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.spensome.data.ProductsRepository
import com.spensome.ui.theme.SpensomeTheme
import com.spensome.ui.wishlist.WishListScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SpensomeTheme {
                WishListScreen(products = ProductsRepository.products)
            }
        }
    }
}
