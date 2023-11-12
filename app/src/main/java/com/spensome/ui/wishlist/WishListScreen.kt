package com.spensome.ui.wishlist

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.spensome.R
import com.spensome.data.ProductsRepository
import com.spensome.model.Product
import com.spensome.ui.theme.SpensomeTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WishListScreen(
    modifier: Modifier = Modifier,
    state: WishListState = WishListState(),
    onEvent: (WishListEvent) -> Unit = {}
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    val bottomSheetState = rememberModalBottomSheetState()

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                title = { WishListTopBar() },
                scrollBehavior = scrollBehavior,
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = modifier
                .background(color = MaterialTheme.colorScheme.background)
                .fillMaxSize()
                .padding(paddingValues = paddingValues),
            verticalArrangement = Arrangement.Center
        ) {
            if (state.productsList.isEmpty()) {
                Text(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    text = stringResource(id = R.string.wishlist_empty_message),
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f)
                )
            } else {
                WishList(
                    products = ProductsRepository.products,
                    onProductSelected = { product ->
                        onEvent(WishListEvent.SelectProduct(product))
                    }
                )
            }
        }

        state.selectedProduct?.let { product ->
            ModalBottomSheet(
                sheetState = bottomSheetState,
                onDismissRequest = { onEvent(WishListEvent.CloseSelectedProduct) }
            ) {
                ProductScreen(
                    product = product,
                    modifier = Modifier.padding(bottom = 45.dp)
                )
            }
        }
    }
}

@Composable
fun WishListItem(
    modifier: Modifier = Modifier,
    product: Product,
    onProductSelected: (Product) -> Unit = {}
) {
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = modifier
    ) {
        Box(
            contentAlignment = Alignment.TopCenter,
            modifier = Modifier
                .fillMaxSize()
                .clickable { onProductSelected(product) }
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
    products: List<Product>,
    onProductSelected: (Product) -> Unit = {}
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = modifier,
        contentPadding = PaddingValues(
            start = dimensionResource(id = R.dimen.padding_large),
            end = dimensionResource(id = R.dimen.padding_large),
            top = dimensionResource(id = R.dimen.padding_medium),
            bottom = dimensionResource(id = R.dimen.padding_large)
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
                modifier = Modifier.height(210.dp),
                onProductSelected = onProductSelected
            )
        }
    }
}

@Preview
@Composable
fun WishListTopBar(modifier: Modifier = Modifier) {
    Text(
        text = stringResource(id = R.string.wishlist_screen_titile),
        textAlign = TextAlign.Center,
        color = MaterialTheme.colorScheme.secondary,
        style = MaterialTheme.typography.titleLarge,
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                all = dimensionResource(id = R.dimen.padding_large)
            )
    )
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
        WishListScreen(
            state = WishListState().copy(productsList = ProductsRepository.products)
        )
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
