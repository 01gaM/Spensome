package com.spensome.ui.screens.wishlist.selected_product

import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.spensome.R
import com.spensome.model.Product
import com.spensome.ui.theme.SpensomeTheme

@Composable
fun ProductScreen(
    modifier: Modifier = Modifier,
    product: Product
) {
    // TODO: add item editing
    val imagePainter = rememberAsyncImagePainter(model = product.imageUri)
    val uriHandler = LocalUriHandler.current

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(all = dimensionResource(id = R.dimen.padding_large)),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = if (product.imageUri != null) {
                imagePainter
            } else {
                painterResource(id = R.drawable.ic_launcher_foreground)
            },
            contentDescription = "Wish list item image",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .padding(all = dimensionResource(id = R.dimen.padding_medium))
                .clip(shape = MaterialTheme.shapes.medium)
                .size(size = 120.dp)
                .background(color = MaterialTheme.colorScheme.secondaryContainer)
        )

        Column(
            modifier = Modifier.padding(
                horizontal = dimensionResource(id = R.dimen.padding_large)
            )
        ) {
            ProductParameter(
                parameterName = R.string.product_title,
                parameterValue = product.title
            )

            ProductParameter(
                parameterName = R.string.product_price,
                parameterValue = "${product.price} $" // TODO: add currency
            )

            ProductParameter(
                parameterName = R.string.product_link,
                parameterValue = product.link,
                parameterTextColor = SpensomeTheme.linkColor(),
                modifier = Modifier.clickable {
                    product.link?.let {
                        uriHandler.openUri(uri = it)
                    }
                }
            )
        }
    }
}

@Composable
fun ProductParameter(
    modifier: Modifier = Modifier,
    @StringRes parameterName: Int,
    parameterValue: String?,
    parameterTextColor: Color = MaterialTheme.colorScheme.onSurface
) {
    if (!parameterValue.isNullOrBlank()) {
        Row(modifier = modifier) {
            Text(
                text = stringResource(id = parameterName) + ": ",
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = parameterValue,
                style = MaterialTheme.typography.bodyLarge,
                color = parameterTextColor
            )
        }
    }
}

// region preview

@Preview
@Composable
fun ProductScreenPreview() {
    SpensomeTheme {
        ProductScreen(
            product = Product(
                title = "Test",
                price = 105f
            )
        )
    }
}
