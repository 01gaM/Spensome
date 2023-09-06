package com.example.spensome.ui.wishlist

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.spensome.R
import com.example.spensome.data.ProductsRepository
import com.example.spensome.model.Product
import com.example.spensome.ui.theme.SpensomeTheme

@Composable
fun WishListScreen(
    modifier: Modifier = Modifier,
    products: List<Product>
) {
    Column(
        modifier = modifier
            .background(color = MaterialTheme.colorScheme.background)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center
    ) {
        if (products.isEmpty()) {
            Text(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                text = stringResource(id = R.string.wishlist_empty_message),
                color = Color.Black.copy(alpha = 0.5f),
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
        } else {
            WishList(
                products = ProductsRepository.products
            )
        }
    }
}

@Composable
fun WishListItem(
    modifier: Modifier = Modifier,
    product: Product
) {
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = modifier
    ) {
        Box(
            contentAlignment = Alignment.TopCenter,
            modifier = Modifier
                .fillMaxSize()
                .clickable {
                    // TODO
                }
        ) {
            Column(
                modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_large)),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(
                        id = product.imageRes ?: R.drawable.ic_launcher_foreground
                    ),
                    contentDescription = stringResource(
                        id = R.string.wishlist_item_icon_description
                    ),
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .clip(shape = MaterialTheme.shapes.medium)
                        .background(color = MaterialTheme.colorScheme.secondary)
                )
                Text(
                    text = "${product.price} $",
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(
                        top = dimensionResource(id = R.dimen.padding_medium),
                        bottom = dimensionResource(id = R.dimen.padding_small)
                    )
                )
                Text(
                    text = product.title,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.secondary
                )
            }
        }
    }
}

@Composable
fun WishList(
    modifier: Modifier = Modifier,
    products: List<Product>
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = modifier,
        contentPadding = PaddingValues(
            all = dimensionResource(id = R.dimen.padding_large)
        ),
        verticalArrangement = Arrangement.spacedBy(
            space = dimensionResource(id = R.dimen.padding_large)
        ),
        horizontalArrangement = Arrangement.spacedBy(
            space = dimensionResource(id = R.dimen.padding_large)
        )
    ) {
        items(products) { product ->
            WishListItem(
                product = product,
                modifier = Modifier.height(210.dp)
            )
        }
    }
}

// region preview

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    name = "NightMode"
)
@Composable
private fun WishListScreenPreview() {
    SpensomeTheme {
        WishListScreen(products = ProductsRepository.products)
    }
}

@Preview(
    heightDp = 200,
    widthDp = 200
)
@Composable
private fun WishListItemPreview() {
    SpensomeTheme {
        WishListItem(
            product = Product(
                title = "Test",
                price = 105f
            )
        )
    }
}
