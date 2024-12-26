package com.spensome.ui.screens.wishlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.spensome.data.Item
import com.spensome.data.ItemsRepository
import com.spensome.model.Product
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class WishListViewModel(private val itemsRepository: ItemsRepository) : ViewModel() {
    private val _state = MutableStateFlow(WishListState())
    val state = _state.asStateFlow()

    init {
        initProductsList()
    }

    fun onEvent(event: WishListEvent) {
        when (event) {
            is WishListEvent.CloseSelectedProduct -> updateSelectedProduct(selectedProduct = null)

            is WishListEvent.SelectProduct -> updateSelectedProduct(selectedProduct = event.product)

            is WishListEvent.AddNewProduct -> addNewProduct(product = event.product)

            is WishListEvent.ScrollToBottom -> updateShouldScrollToBottom(shouldScroll = false)
        }
    }

    private fun initProductsList() {
        viewModelScope.launch {
            itemsRepository.getAllItemsStream().collect { items ->
                _state.update {
                    it.copy(productsList = items.map { item ->
                        Product(
                            title = item.name,
                            price = item.price
                        )
                    })
                }
            }
        }
    }

    // region private

    private fun updateSelectedProduct(selectedProduct: Product?) {
        _state.update {
            it.copy(selectedProduct = selectedProduct)
        }
    }

    private fun addNewProduct(product: Product) {
        viewModelScope.launch {
            itemsRepository.insertItem(product.toItem())
        }
        _state.update {
            it.copy(
                shouldScrollToBottom = true
            )
        }
    }

    private fun updateShouldScrollToBottom(shouldScroll: Boolean) {
        _state.update {
            it.copy(shouldScrollToBottom = shouldScroll)
        }
    }

    private fun Product.toItem(): Item {
        return Item(name = this.title, price = this.price)
    }

    // endregion
}
