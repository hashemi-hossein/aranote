package com.ara.aranote.ui.screens

import android.content.Context
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.ara.aranote.R
import com.ara.aranote.domain.entity.Note
import com.ara.aranote.util.HDateTime
import com.ara.aranote.util.plus
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.time.Duration
import kotlin.time.ExperimentalTime

@RunWith(AndroidJUnit4::class)
@SmallTest
class NoteDetailScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val context = ApplicationProvider.getApplicationContext<Context>()

    private lateinit var note: MutableState<Note>
    private var backPressResult: Boolean? = null

    @Before
    fun setUp() {
        note = mutableStateOf(Note(0, "", HDateTime.getCurrentDateTime()))
        backPressResult = null
        composeTestRule.setContent {
            NoteDetailScreen(
                note = note.value,
                onNoteChanged = { note.value = it },
                onBackPressed = { backPressResult = it },
                isNewNote = true,
            )
        }
    }

    @Test
    fun entering_text() {
        // act
        composeTestRule.onNodeWithText(note.value.text).performTextInput("hello")

        // assert
        composeTestRule.onNodeWithText("hello").assertExists()
        assertThat(note.value.text).isEqualTo("hello")
    }

    @Test
    fun addAlarmVisibility() {
        // assert
        composeTestRule.onNodeWithContentDescription(context.getString(R.string.cd_add_alarm))
            .assertIsDisplayed().assertHasClickAction()
        composeTestRule.onNodeWithContentDescription(context.getString(R.string.cd_delete_alarm))
            .assertDoesNotExist()
    }

    @Test
    fun open_alarm_bottomSheet() {
        // act
        composeTestRule.onNodeWithContentDescription(context.getString(R.string.cd_add_alarm))
            .assertIsDisplayed().performClick()

        // assert
        composeTestRule.onNodeWithContentDescription(context.getString(R.string.cd_set_alarm))
            .assertIsDisplayed()
    }

    @Test
    fun set_invalid_alarm() {
        // act
        composeTestRule.onNodeWithContentDescription(context.getString(R.string.cd_add_alarm))
            .assertIsDisplayed().performClick()
        composeTestRule.onNodeWithContentDescription(context.getString(R.string.cd_set_alarm))
            .assertIsDisplayed().performClick()

        // assert
        composeTestRule.onNodeWithText(context.getString(R.string.invalid_date_and_time))
            .assertIsDisplayed()
    }

    @OptIn(ExperimentalTime::class)
    @Test
    fun set_and_reset_alarmDateTime() {
        // arrange
        val newAlarmDateTime = HDateTime.getCurrentDateTime().plus(Duration.hours(25))

        // act
        composeTestRule.onNodeWithContentDescription(context.getString(R.string.cd_add_alarm))
            .assertIsDisplayed().performClick()
        note.value = note.value.copy(alarmDateTime = newAlarmDateTime)

        // assert
        composeTestRule.onNodeWithText(
            HDateTime.formatDateAndTime(
                newAlarmDateTime,
                isDate = true
            )
        ).assertIsDisplayed()
        composeTestRule.onNodeWithText(
            HDateTime.formatDateAndTime(
                newAlarmDateTime,
                isDate = false
            )
        ).assertIsDisplayed()

        // act
        composeTestRule.onNodeWithContentDescription(context.getString(R.string.cd_reset_date_and_time))
            .assertIsDisplayed().performClick()

        // assert
        composeTestRule.onNodeWithText(
            HDateTime.formatDateAndTime(
                HDateTime.getCurrentDateTime(),
                isDate = true
            )
        ).assertIsDisplayed()
        composeTestRule.onNodeWithText(
            HDateTime.formatDateAndTime(
                HDateTime.getCurrentDateTime(),
                isDate = false
            )
        ).assertIsDisplayed()
    }

    @Test
    fun delete_alarm() {
        // arrange
        note.value = note.value.copy(alarmDateTime = HDateTime.getCurrentDateTime())

        // act
        composeTestRule.onNodeWithContentDescription(context.getString(R.string.cd_delete_alarm))
            .assertIsDisplayed().performClick()

        // assert
        composeTestRule.onNodeWithContentDescription(context.getString(R.string.cd_delete_alarm))
            .assertDoesNotExist()
        assertThat(note.value.alarmDateTime).isNull()
    }

    @Test
    fun back_button_press() {
        // act
        composeTestRule.onNodeWithContentDescription(context.getString(R.string.cd_happbar_back))
            .assertIsDisplayed().performClick()

        // assert
        assertThat(backPressResult).isFalse()
    }

    @Test
    fun phone_back_press() {
        // act
        Espresso.pressBack()

        // assert
        assertThat(backPressResult).isFalse()
    }

    @Test
    fun fab_click_when_no_text() {
        // act
        composeTestRule.onNodeWithContentDescription(context.getString(R.string.cd_discard))
            .performClick()
        // snackbar
        // this way can be used
        // https://github.com/android/compose-samples/blob/main/JetNews/app/src/sharedTest/java/com/example/jetnews/HomeScreenTests.kt
        composeTestRule.onNodeWithText(context.getString(R.string.discard)).assertDoesNotExist()

        // assert
        assertThat(backPressResult).isTrue()
    }

    @Test
    fun fab_click_when_text() {
        // arrange
        note.value = note.value.copy(text = "hello")

        // act
        composeTestRule.onNodeWithContentDescription(context.getString(R.string.cd_discard))
            .performClick()
        // snackbar
        composeTestRule.onNodeWithText(context.getString(R.string.discard))
            .assertIsDisplayed().performClick()

        // assert
        assertThat(backPressResult).isTrue()
    }
}