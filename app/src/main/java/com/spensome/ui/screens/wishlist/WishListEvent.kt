package com.spensome.ui.screens.wishlist

import com.spensome.model.Product

sealed interface WishListEvent {
    data class SelectProduct(val product: Product) : WishListEvent
    object CloseSelectedProduct : WishListEvent
    data class AddNewProduct(val product: Product) : WishListEvent
    object ScrollToBottom: WishListEvent
}