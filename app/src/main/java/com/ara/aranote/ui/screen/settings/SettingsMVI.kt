package com.ara.aranote.ui.screen.settings

import android.net.Uri
import com.ara.aranote.data.datastore.UserPreferences
import com.ara.aranote.domain.entity.RepoDetail
import com.ara.aranote.util.MviIntent
import com.ara.aranote.util.MviSingleEvent
import com.ara.aranote.util.MviState
import kotlin.reflect.KProperty1

data class SettingsState(
    val userPreferences: UserPreferences = UserPreferences(),
    val repo: RepoDetail? = null,
) : MviState

sealed interface SettingsIntent : MviIntent {
    object ObserveUserPreferences : SettingsIntent
    data class ShowUserPreferences(val userPreferences: UserPreferences) : SettingsIntent

    data class WriteUserPreferences<T>(
        val kProperty: KProperty1<UserPreferences, T>,
        val value: T
    ) : SettingsIntent

    data class ImportData(val uri: Uri, val onComplete: () -> Unit) : SettingsIntent
    data class ExportData(val uri: Uri, val onComplete: () -> Unit) : SettingsIntent

    object LoadRepoDetail : SettingsIntent
    data class ShowRepoDetail(val repo: RepoDetail) : SettingsIntent
}

sealed interface SettingsSingleEvent : MviSingleEvent {
    data class ShowError(val errorMessage: String) : SettingsSingleEvent
}
