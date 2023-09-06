package com.example.spensome.model

data class Product(
    val title: String,
    val price: Float,
    val link: String? = null,
    val description: String? = null,
    val imageRes: Int? = null,
)
