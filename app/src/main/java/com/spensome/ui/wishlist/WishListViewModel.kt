package com.spensome.ui.wishlist

import androidx.lifecycle.ViewModel
import com.spensome.model.Product
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class WishListViewModel : ViewModel() {
    private val _state = MutableStateFlow(WishListState())
    val state = _state.asStateFlow()

    fun onEvent(event: WishListEvent) {
        when (event) {
            WishListEvent.CloseSelectedProduct -> updateSelectedProduct(selectedProduct = null)
            is WishListEvent.SelectProduct -> updateSelectedProduct(selectedProduct = event.product)
        }
    }

    fun updateProductsList(products: List<Product>) {
        _state.update {
            it.copy(productsList = products)
        }
    }

    // region private

    private fun updateSelectedProduct(selectedProduct: Product?) {
        _state.update {
            it.copy(selectedProduct = selectedProduct)
        }
    }

    // endregion
}
