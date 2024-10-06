package com.spensome.ui.screens.wishlist.new_product

import android.net.Uri
import androidx.annotation.StringRes

data class NewProductState(
    // Fields
    val title: String = "",
    val price: Float = 0f,
    val link: String? = null,
    val imageUri: Uri? = null,

    // Errors
    @StringRes val titleErrorId: Int? = null,
    @StringRes val priceErrorId: Int? = null,
    @StringRes val linkErrorId: Int? = null
)
