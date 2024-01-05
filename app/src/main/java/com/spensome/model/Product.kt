package com.spensome.model

data class Product(
    val title: String,
    val price: Float,
    val link: String? = null,
    val imageRes: Int? = null,
)
