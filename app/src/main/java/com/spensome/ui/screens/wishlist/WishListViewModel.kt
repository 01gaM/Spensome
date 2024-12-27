package com.spensome.ui.screens.wishlist

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.spensome.data.ItemsRepository
import com.spensome.model.Product
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WishListViewModel @Inject constructor(
    private val itemsRepository: ItemsRepository
) : ViewModel() {
    private val _state = MutableStateFlow(WishListState())
    val state = _state.asStateFlow()

    init {
        initProductsList()
    }

    fun onEvent(event: WishListEvent) {
        when (event) {
            is WishListEvent.CloseSelectedProduct -> updateSelectedProduct(selectedProduct = null)

            is WishListEvent.SelectProduct -> updateSelectedProduct(selectedProduct = event.product)

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
                            price = item.price,
                            imageUri = try {
                                Uri.parse(item.imageUri)
                            } catch (e: NullPointerException) {
                                null
                            },
                            link = item.link
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

    private fun updateShouldScrollToBottom(shouldScroll: Boolean) {
        _state.update {
            it.copy(shouldScrollToBottom = shouldScroll)
        }
    }

    // endregion
}
