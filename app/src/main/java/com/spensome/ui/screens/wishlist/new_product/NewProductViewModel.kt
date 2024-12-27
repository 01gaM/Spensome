package com.spensome.ui.screens.wishlist.new_product

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.spensome.R
import com.spensome.data.Item
import com.spensome.data.ItemsRepository
import com.spensome.model.Product
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

@HiltViewModel
class NewProductViewModel @Inject constructor(
    private val itemsRepository: ItemsRepository,
    application: Application
) : AndroidViewModel(application) {
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
            withContext(Dispatchers.IO) {
                val savedImageUri = product.imageUri?.let {
                    saveImageToLocalStorage(it)
                }
                itemsRepository.insertItem(product.toItem(imageUri = savedImageUri))
            }
        }
    }

    private fun saveImageToLocalStorage(tempImageUri: Uri): String? {
        val context = getApplication<Application>().applicationContext
        val contentResolver = context.contentResolver
        val fileName = "image_${System.currentTimeMillis()}.jpg"
        val file = File(context.filesDir, fileName)

        try {
            contentResolver.openInputStream(tempImageUri)?.use { inputStream ->
                FileOutputStream(file).use { outputStream ->
                    inputStream.copyTo(outputStream)
                }
            }
            return file.absolutePath
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return null
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

    private fun Product.toItem(imageUri: String?): Item {
        return Item(
            name = title,
            price = price,
            imageUri = imageUri,
            link = link
        )
    }

    // endregion

}
