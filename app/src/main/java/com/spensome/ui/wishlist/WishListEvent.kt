package com.spensome.ui.wishlist

import com.spensome.model.Product

sealed interface WishListEvent {
    data class SelectProduct(val product: Product) : WishListEvent
    object CloseSelectedProduct : WishListEvent
    object AddNewProduct : WishListEvent
    data class SaveNewProduct(val product: Product) : WishListEvent
}
