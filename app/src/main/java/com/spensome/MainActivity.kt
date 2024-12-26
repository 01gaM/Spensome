package com.spensome

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.spensome.navigation.WishListNavGraph
import com.spensome.ui.theme.SpensomeTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SpensomeTheme {
                WishListNavGraph()
            }
        }
    }
}
