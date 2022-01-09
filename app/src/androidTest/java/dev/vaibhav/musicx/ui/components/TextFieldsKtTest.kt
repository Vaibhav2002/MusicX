package dev.vaibhav.musicx.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.filters.LargeTest
import com.google.common.truth.Truth.assertThat
import dev.vaibhav.musicx.ui.theme.MusicXTheme
import dev.vaibhav.musicx.ui.utils.SEARCH_BAR_CLOSE_TAG
import dev.vaibhav.musicx.ui.utils.SEARCH_BAR_TAG
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@LargeTest
@ExperimentalComposeUiApi
class TextFieldsKtTest {

    @get:Rule
    val composeRule = createComposeRule()

    private val searchQuery = mutableStateOf("")

    @Before
    fun setUp() {
        composeRule.setContent {
            MusicXTheme {
                SearchBar(
                    searchQuery = searchQuery.value,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    searchQuery.value = it
                }
            }
        }
    }

    @Test
    fun focusIsClearedWhenCloseButtonIsPressed() {
        // when
        composeRule.onNodeWithTag(SEARCH_BAR_TAG).performTextInput("Hello")
        // asserting if focused when text is entered
        composeRule.onNodeWithTag(SEARCH_BAR_TAG).assertIsFocused()

        // clicking on close icon to remove focus
        composeRule.onNodeWithTag(SEARCH_BAR_CLOSE_TAG).performClick()

        // asserting focus is lost
        composeRule.onNodeWithTag(SEARCH_BAR_TAG).assertIsNotFocused()
    }

    @Test
    fun textIsClearedWhenCloseButtonIsPressed() {
        val query = "Hello"

        // when
        // asserting if text is correct
        composeRule.onNodeWithTag(SEARCH_BAR_TAG).performTextInput(query)
        assertThat(searchQuery.value).isEqualTo(query)

        // clicking on close icon to remove focus
        composeRule.onNodeWithTag(SEARCH_BAR_CLOSE_TAG).performClick()

        // asserting text is cleared
        assertThat(searchQuery.value).isEqualTo("")
    }

    @Test
    fun focusIsLostWhenImeActionIsPressed() {
        val query = "Hello"
        // when
        composeRule.onNodeWithTag(SEARCH_BAR_TAG).performTextInput(query)
        // asserting focus is present
        composeRule.onNodeWithTag(SEARCH_BAR_TAG).assertIsFocused()

        // clicking on keyboard ime action
        composeRule.onNodeWithTag(SEARCH_BAR_TAG).performImeAction()

        // asserting focus is lost
        composeRule.onNodeWithTag(SEARCH_BAR_TAG).assertIsNotFocused()
    }
}
