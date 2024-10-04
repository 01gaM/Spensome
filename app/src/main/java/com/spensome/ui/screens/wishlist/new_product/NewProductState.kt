package com.spensome.ui.screens.wishlist.new_product

import android.net.Uri

data class NewProductState(
    val name: String = "",
    val price: Float = 0f,
    val link: String? = null,
    val linkHasError: Boolean = false,
    val imageUri: Uri? = null
)
