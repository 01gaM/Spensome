package com.example.spensome

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.spensome.data.ProductsRepository
import com.example.spensome.ui.theme.SpensomeTheme
import com.example.spensome.ui.wishlist.WishListScreen

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
