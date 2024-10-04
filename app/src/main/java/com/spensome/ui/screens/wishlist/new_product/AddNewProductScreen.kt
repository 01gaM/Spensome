package com.spensome.ui.screens.wishlist.new_product

import android.content.res.Configuration
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.StringRes
import androidx.compose.foundation.BorderStroke

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha

import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.spensome.R
import com.spensome.ui.theme.SpensomeTheme

private enum class ProductFieldType {
    STRING,
    NUMBER,
    LINK
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddNewProductScreen(
    modifier: Modifier = Modifier,
    state: NewProductState = NewProductState(),
    onEvent: (NewProductEvent) -> Unit = {},
    onBackClicked: () -> Unit = {}
) {
    val focusManager = LocalFocusManager.current

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "New item",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClicked) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Arrow back icon",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .background(color = MaterialTheme.colorScheme.background)
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectTapGestures(
                        onTap = { focusManager.clearFocus() }
                    )
                }
                .padding(
                    top = innerPadding.calculateTopPadding(),
                    start = 16.dp,
                    end = 16.dp,
                    bottom = 16.dp
                )
        ) {
            ProductField(
                modifier = Modifier.padding(
                    bottom = dimensionResource(id = R.dimen.padding_medium)
                ),
                labelId = R.string.product_title,
                fieldValue = state.name,
                isRequired = true,
                onValueChange = { onEvent(NewProductEvent.ChangeName(name = it)) }
            )

            ProductField(
                modifier = Modifier.padding(
                    bottom = dimensionResource(id = R.dimen.padding_medium)
                ),
                labelId = R.string.product_price,
                fieldValue = state.price.toString(),
                productFieldType = ProductFieldType.NUMBER,
                isRequired = true,
                suffix = " $",
                onValueChange = { onEvent(NewProductEvent.ChangePrice(price = it)) }
            )

            // TODO: show link error text
            ProductField(
                modifier = Modifier.padding(
                    bottom = dimensionResource(id = R.dimen.padding_medium)
                ),
                labelId = R.string.product_link,
                fieldValue = state.link ?: "",
                productFieldType = ProductFieldType.LINK,
                isLastField = true,
                onValueChange = { onEvent(NewProductEvent.ChangeLink(link = it)) }
            )

            ImagePicker(
                imageUri = state.imageUri,
                onImageChanged = { onEvent(NewProductEvent.SelectImage(uri = it)) }
            )

            Spacer(modifier = Modifier.weight(weight = 1f))

            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(alignment = Alignment.CenterHorizontally),
                enabled = state.name.isNotBlank() && state.price != 0.0f,
                onClick = { onEvent(NewProductEvent.AddToWishListClicked) }
            ) {
                Text(text = "ADD TO WISHLIST")
            }
        }
    }
}

@Composable
private fun ProductField(
    modifier: Modifier = Modifier,
    @StringRes labelId: Int,
    fieldValue: String,
    productFieldType: ProductFieldType = ProductFieldType.STRING,
    isLastField: Boolean = false,
    isRequired: Boolean = false,
    suffix: String? = null,
    onValueChange: (String) -> Unit = {}
) {
    var isError by remember { mutableStateOf(false) }
    OutlinedTextField(
        modifier = modifier.fillMaxWidth(),
        value = fieldValue,
        label = {
            var label = stringResource(id = labelId)
            if (isRequired) {
                label += " *"
            }
            Text(text = label)
        },
        onValueChange = {
            onValueChange(it)
            isError = isRequired && fieldValue.isBlank()
        },
        keyboardOptions = KeyboardOptions(
            imeAction = if (isLastField) ImeAction.Done else ImeAction.Next,
            keyboardType = when (productFieldType) {
                ProductFieldType.STRING -> KeyboardType.Text
                ProductFieldType.NUMBER -> KeyboardType.Number
                ProductFieldType.LINK -> KeyboardType.Uri
            }
        ),
        isError = isError,
        suffix = { suffix?.let { Text(it) } }
    )

    if (isError) {
        Text(
            text = "Field is required",
            color = MaterialTheme.colorScheme.error,
            fontSize = 12.sp,
            modifier = Modifier.padding(start = 16.dp)
        )
    }
}

@Composable
private fun ImagePicker(
    modifier: Modifier = Modifier,
    imageUri: Uri?,
    onImageChanged: (Uri?) -> Unit
) {
    // TODO: select image from camera
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            uri?.let {
                onImageChanged(uri)
            }
        }
    )

    Column(modifier = modifier) {
        Text(
            text = "Image",
            modifier = Modifier.padding(vertical = 8.dp),
            color = MaterialTheme.colorScheme.onBackground
        )

        if (imageUri == null) {
            Card(modifier = Modifier.size(100.dp)) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clickable(onClick = { galleryLauncher.launch("image/*") })
                ) {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = null,
                        modifier = Modifier
                            .padding(all = 4.dp)
                            .align(Alignment.Center)
                            .size(80.dp)
                            .alpha(0.3f),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

        } else {
            Card(
                modifier = Modifier.size(100.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    Image(
                        painter = rememberAsyncImagePainter(model = imageUri),
                        contentDescription = null,
                        modifier = Modifier.size(size = 100.dp),
                        contentScale = ContentScale.Crop
                    )

                    OutlinedIconButton(
                        border = BorderStroke(
                            width = 2.dp,
                            color = MaterialTheme.colorScheme.error
                        ),
                        colors = IconButtonDefaults.filledIconButtonColors(
                            containerColor = MaterialTheme.colorScheme.background,
                            contentColor = MaterialTheme.colorScheme.error
                        ),
                        onClick = { onImageChanged(null) },
                        content = {
                            Icon(
                                imageVector = Icons.Rounded.Clear,
                                contentDescription = "Clear image icon"
                            )
                        },
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .size(35.dp)
                            .padding(5.dp)
                    )
                }
            }
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
private fun AddNewProductScreenPreview() {
    SpensomeTheme {
        AddNewProductScreen()
    }
}

// endregion
