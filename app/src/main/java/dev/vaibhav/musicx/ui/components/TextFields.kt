package dev.vaibhav.musicx.ui.components

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.vaibhav.musicx.R
import dev.vaibhav.musicx.ui.theme.MusicXTheme
import dev.vaibhav.musicx.ui.utils.SEARCH_BAR_CLOSE_TAG
import dev.vaibhav.musicx.ui.utils.SEARCH_BAR_TAG

@ExperimentalComposeUiApi
@Composable
fun SearchBar(
    searchQuery: String,
    modifier: Modifier = Modifier,
    onSearchQueryChanged: (String) -> Unit
) {
    val contentColor = MaterialTheme.colorScheme.onTertiaryContainer
    val focusManager = LocalFocusManager.current
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surface,
        contentColor = contentColor,
        tonalElevation = 4.dp
    ) {
        OutlinedTextField(
            value = searchQuery,
            onValueChange = onSearchQueryChanged,
            modifier = Modifier
                .fillMaxWidth()
                .testTag(SEARCH_BAR_TAG)
                .focusRequester(focusRequester),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(onSearch = {
                focusManager.clearFocus()
                keyboardController?.hide()
            }),
            textStyle = MaterialTheme.typography.titleMedium.copy(color = contentColor),
            singleLine = true,
            maxLines = 1,
            label = {
                Text(
                    text = stringResource(R.string.search_here),
                    color = contentColor.copy(alpha = 0.6f)
                )
            },
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_search),
                    modifier = Modifier.padding(start = 8.dp),
                    contentDescription = stringResource(R.string.search_here)
                )
            },
            trailingIcon = {
                if (searchQuery.isNotBlank() && searchQuery.isNotEmpty()) {
                    Icon(
                        imageVector = Icons.Rounded.Close,
                        contentDescription = "Close",
                        modifier = Modifier
                            .testTag(SEARCH_BAR_CLOSE_TAG)
                            .clickable {
                                onSearchQueryChanged("")
                                focusManager.clearFocus()
                            }
                    )
                }
            },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent
            )
        )
    }
}

@ExperimentalComposeUiApi
@Preview(showBackground = true)
@Composable
fun SearchBarPreview() {
    MusicXTheme {
        SearchBar(
            searchQuery = "",
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp),
            onSearchQueryChanged = {}
        )
    }
}

@ExperimentalComposeUiApi
@Preview(uiMode = UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun SearchBarPreviewDark() {
    MusicXTheme {
        var query by remember { mutableStateOf("Hello vro") }
        SearchBar(
            searchQuery = query,
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp),
            onSearchQueryChanged = { query = it }
        )
    }
}
