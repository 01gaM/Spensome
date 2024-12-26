package com.spensome.ui.screens.wishlist.new_product

import android.net.Uri

sealed interface NewProductEvent {
    data class ChangeName(val name: String) : NewProductEvent
    data class ChangePrice(val price: String) : NewProductEvent
    data class ChangeLink(val link: String) : NewProductEvent
    data class SelectImage(val uri: Uri?) : NewProductEvent
    data object AddToWishListClicked: NewProductEvent
}
