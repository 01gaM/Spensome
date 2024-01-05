package com.spensome.ui.screens.wishlist

import com.spensome.model.Product

data class WishListState(
    val productsList: List<Product> = emptyList(),
    val selectedProduct: Product? = null
)
