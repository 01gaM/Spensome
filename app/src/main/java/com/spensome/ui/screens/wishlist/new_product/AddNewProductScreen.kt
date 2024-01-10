package com.spensome.ui.screens.wishlist.new_product

import android.content.res.Configuration
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.StringRes

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip

import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.spensome.R
import com.spensome.model.Product
import com.spensome.ui.theme.SpensomeTheme

private enum class ProductFieldType {
    STRING,
    NUMBER,
    LINK
}

@Composable
fun AddNewProductScreen(
    modifier: Modifier = Modifier,
    onProductAdded: (Product) -> Unit = {},
    onBackClicked: () -> Unit = {}
) {
    val name = remember { mutableStateOf(TextFieldValue()) }
    val price = remember { mutableStateOf(TextFieldValue()) }
    val link = remember { mutableStateOf(TextFieldValue()) }
    val imageUri = remember { mutableStateOf<Uri?>(null) }
    val focusManager = LocalFocusManager.current

    Column(
        modifier = modifier
            .background(color = MaterialTheme.colorScheme.background)
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = { focusManager.clearFocus() }
                )
            }
            .padding(16.dp)
    ) {
        Text(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(bottom = 16.dp),
            text = "New product",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.primary
        )

        ProductField(
            labelId = R.string.product_title,
            fieldValueState = name,
            modifier = Modifier.padding(
                bottom = dimensionResource(id = R.dimen.padding_medium)
            ),
            isRequired = true
        )

        ProductField(
            labelId = R.string.product_price,
            fieldValueState = price,
            productFieldType = ProductFieldType.NUMBER,
            modifier = Modifier.padding(
                bottom = dimensionResource(id = R.dimen.padding_medium)
            ),
            isRequired = true
        )

        ProductField(
            labelId = R.string.product_link,
            fieldValueState = link,
            productFieldType = ProductFieldType.LINK,
            isLastField = true,
            modifier = Modifier.padding(
                bottom = dimensionResource(id = R.dimen.padding_medium)
            )
        )

        ImagePicker(imageUriState = imageUri)

        Spacer(modifier = Modifier.weight(weight = 1f))

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .align(alignment = Alignment.CenterHorizontally),
            enabled = name.value.text.isNotBlank() && price.value.text.isNotBlank(),
            onClick = {
                onProductAdded(
                    Product(
                        title = name.value.text,
                        price = price.value.text.toFloatOrNull() ?: 0f,
                        link = link.value.text,
                        imageUri = imageUri.value
                    )
                )
            }
        ) {
            Text(text = "ADD")
        }

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .align(alignment = Alignment.CenterHorizontally),
            onClick = onBackClicked
        ) {
            Text(text = "CANCEL")
        }
    }
}

@Composable
private fun ProductField(
    modifier: Modifier = Modifier,
    @StringRes labelId: Int,
    fieldValueState: MutableState<TextFieldValue>,
    productFieldType: ProductFieldType = ProductFieldType.STRING,
    isLastField: Boolean = false,
    isRequired: Boolean = false
) {
    var isError by remember { mutableStateOf(false) }
    OutlinedTextField(
        modifier = modifier.fillMaxWidth(),
        value = fieldValueState.value,
        label = {
            var label = stringResource(id = labelId)
            if (isRequired) {
                label += " *"
            }
            Text(text = label)
        },
        onValueChange = {
            fieldValueState.value = it
            isError = isRequired && fieldValueState.value.text.isBlank()
        },
        keyboardOptions = KeyboardOptions(
            imeAction = if (isLastField) ImeAction.Done else ImeAction.Next,
            keyboardType = when (productFieldType) {
                ProductFieldType.STRING -> KeyboardType.Text
                ProductFieldType.NUMBER -> KeyboardType.Number
                ProductFieldType.LINK -> KeyboardType.Uri
            }
        ),
        isError = isError
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
    imageUriState: MutableState<Uri?>
) {
    // TODO: select image from camera
    // TODO: delete image, change image
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            uri?.let {
                imageUriState.value = uri
            }
        }
    )

    Column(modifier = modifier) {
        Text(
            text = "Image",
            modifier = Modifier.padding(vertical = 8.dp),
            color = MaterialTheme.colorScheme.onBackground
        )

        if (imageUriState.value == null) {
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(shape = RoundedCornerShape(size = 4.dp))
                    .background(color = MaterialTheme.colorScheme.surfaceVariant)
                    .clickable(onClick = { galleryLauncher.launch("image/*") })
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = null,
                    modifier = Modifier.padding(all = 4.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Text(
                    text = "Pick image",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 14.sp,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        } else {
            Image(
                painter = rememberAsyncImagePainter(model = imageUriState.value),
                contentDescription = null,
                modifier = Modifier
                    .clip(shape = RoundedCornerShape(size = 4.dp))
                    .size(size = 100.dp)
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
private fun AddNewProductScreenPreview() {
    SpensomeTheme {
        AddNewProductScreen()
    }
}

// endregion