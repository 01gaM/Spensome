package com.spensome.model

import android.net.Uri

data class Product(
    val title: String,
    val price: Float,
    val link: String? = null,
    val imageUri: Uri? = null
)
