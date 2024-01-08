package com.spensome.ui.screens.wishlist

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
            is WishListEvent.CloseSelectedProduct -> updateSelectedProduct(selectedProduct = null)

            is WishListEvent.SelectProduct -> updateSelectedProduct(selectedProduct = event.product)

            is WishListEvent.AddNewProduct -> addNewProduct(product = event.product)

            is WishListEvent.ScrollToBottom -> updateShouldScrollToBottom(shouldScroll = false)
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

    private fun addNewProduct(product: Product) {
        _state.update {
            val newProductList = it.productsList.toMutableList()
            newProductList.add(product)
            it.copy(
                productsList = newProductList,
                shouldScrollToBottom = true
            )
        }
    }

    private fun updateShouldScrollToBottom(shouldScroll: Boolean) {
        _state.update {
            it.copy(shouldScrollToBottom = shouldScroll)
        }
    }

    // endregion
}
