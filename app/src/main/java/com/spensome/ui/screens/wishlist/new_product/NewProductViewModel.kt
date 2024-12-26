package com.spensome.ui.screens.wishlist.new_product

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spensome.R
import com.spensome.data.Item
import com.spensome.data.ItemsRepository
import com.spensome.model.Product
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewProductViewModel @Inject constructor(
    private val itemsRepository: ItemsRepository
) : ViewModel() {
    private val _state = MutableStateFlow(NewProductState())
    val state = _state.asStateFlow()

    fun onEvent(event: NewProductEvent) {
        when (event) {
            is NewProductEvent.AddToWishListClicked -> handleAddToWishListClicked()
            is NewProductEvent.ChangeLink -> updateLink(link = event.link)
            is NewProductEvent.ChangeName -> updateName(name = event.name)
            is NewProductEvent.ChangePrice -> updatePrice(price = event.price)
            is NewProductEvent.SelectImage -> updateImage(imageUri = event.uri)
            is NewProductEvent.ScreenLaunched -> clearState()
        }
    }

    // region private

    private fun handleAddToWishListClicked() {
        if (isTitleValid()
                .and(isPriceValid())
                .and(isLinkValid())
        ) {
            with(state.value) {
                addNewProduct(
                    Product(
                        title = title,
                        price = price,
                        link = link,
                        imageUri = imageUri
                    )
                )
            }
        }
    }

    private fun addNewProduct(product: Product) {
        viewModelScope.launch {
            itemsRepository.insertItem(product.toItem())
        }
    }

    private fun isLinkValid(): Boolean {
        state.value.link.let { link ->
            val isValid = link.isNullOrEmpty() || checkLinkValid(parseUri(link))
            _state.update {
                it.copy(
                    linkErrorId = if (isValid) {
                        null
                    } else {
                        R.string.new_product_invalid_link_error
                    }
                )
            }
            return isValid
        }
    }

    private fun isTitleValid(): Boolean {
        val isValid = state.value.title.isNotEmpty()
        _state.update {
            it.copy(titleErrorId = if (isValid) null else R.string.required_field_error)
        }
        return isValid
    }

    private fun isPriceValid(): Boolean {
        val isValid = state.value.price > 0.0f
        _state.update {
            it.copy(priceErrorId = if (isValid) null else R.string.required_field_error)
        }
        return isValid
    }

    private fun updateLink(link: String) {
        _state.update {
            it.copy(link = link)
        }
    }

    private fun updateName(name: String) {
        _state.update {
            it.copy(title = name)
        }
    }

    private fun updatePrice(price: String) {
        _state.update {
            it.copy(price = price.toFloatOrNull() ?: 0f)
        }
    }

    private fun updateImage(imageUri: Uri?) {
        _state.update {
            it.copy(imageUri = imageUri)
        }
    }

    private fun clearState() = _state.update { NewProductState() }

    private fun checkLinkValid(link: String): Boolean {
        val httpsPattern =
            "^https://(?:www\\.)?[a-zA-Z0-9-]+(?:\\.[a-zA-Z]+)+(?::\\d{1,5})?(?:/\\S*)?$".toRegex()
        return link.matches(httpsPattern)
    }

    private fun parseUri(input: String): String {
        val httpsScheme = "https"
        val httpScheme = "http"
        // If the input doesn't start with a scheme ("http://" or "https://"),
        // assume it's a URL and prepend "https://"
        val urlWithScheme = if (!input.startsWith(httpsScheme) && !input.startsWith(httpScheme)) {
            "$httpsScheme://$input"
        } else {
            input
        }

        val url = Uri.parse(urlWithScheme).let {
            if (it.scheme != httpsScheme) {
                it.buildUpon().scheme(httpsScheme).build()
            } else {
                it
            }
        }

        return url.toString()
    }

    private fun Product.toItem(): Item {
        return Item(name = this.title, price = this.price)
    }

    // endregion

}
