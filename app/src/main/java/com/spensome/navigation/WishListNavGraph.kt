package com.spensome.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.spensome.ui.screens.wishlist.WishListScreen
import com.spensome.ui.screens.wishlist.WishListViewModel
import com.spensome.ui.screens.wishlist.new_product.AddNewProductScreen
import com.spensome.ui.screens.wishlist.new_product.NewProductViewModel

@Composable
fun WishListNavGraph() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = WishListDestination.WISH_LIST
    ) {
        composable(route = WishListDestination.WISH_LIST) {
            val wishListViewModel: WishListViewModel = hiltViewModel()
            val state by wishListViewModel.state.collectAsState()
            WishListScreen(
                state = state,
                onEvent = wishListViewModel::onEvent,
                navController = navController
            )
        }

        composable(route = WishListDestination.NEW_PRODUCT) {
            val newProductViewModel: NewProductViewModel = hiltViewModel()
            val state by newProductViewModel.state.collectAsState()
            AddNewProductScreen(
                state = state,
                onEvent = newProductViewModel::onEvent,
                onNavigateUp = { navController.navigateUp() }
            )
        }
    }
}
