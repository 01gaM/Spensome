package com.spensome.ui.screens.wishlist

import com.spensome.model.Product

sealed interface WishListEvent {
    data class SelectProduct(val product: Product) : WishListEvent
    data object CloseSelectedProduct : WishListEvent
    data object ScrollToBottom : WishListEvent
}
