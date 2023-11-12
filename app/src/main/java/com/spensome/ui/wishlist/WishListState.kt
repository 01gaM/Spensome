package com.spensome.ui.wishlist

import com.spensome.model.Product

data class WishListState(
    val productsList: List<Product> = emptyList(),
    val selectedProduct: Product? = null
)
