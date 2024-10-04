package com.spensome.ui.screens.wishlist.new_product

import android.net.Uri
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class NewProductViewModel : ViewModel() {
    private val _state = MutableStateFlow(NewProductState())
    val state = _state.asStateFlow()

    fun onEvent(event: NewProductEvent) {
        when (event) {
            is NewProductEvent.AddToWishListClicked -> handleAddToWishListClicked()
            is NewProductEvent.ChangeLink -> updateLink(link = event.link)
            is NewProductEvent.ChangeName -> updateName(name = event.name)
            is NewProductEvent.ChangePrice -> updatePrice(price = event.price)
            is NewProductEvent.SelectImage -> updateImage(imageUri = event.uri)
        }
    }

    // region private

    private fun handleAddToWishListClicked() {
        state.value.link.let { link ->
            validateLink(link = link)
            if (!state.value.linkHasError) {
                //addToWishList() // TODO
            }
        }
    }

    private fun validateLink(link: String?) {
        val isValid = link.isNullOrEmpty() || isLinkValid(parseUri(link))
        _state.update {
            it.copy(linkHasError = !isValid)
        }
    }

    private fun updateLink(link: String) {
        _state.update {
            it.copy(link = link)
        }
    }

    private fun updateName(name: String) {
        _state.update {
            it.copy(name = name)
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

    private fun isLinkValid(link: String): Boolean {
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

    // endregion

}
