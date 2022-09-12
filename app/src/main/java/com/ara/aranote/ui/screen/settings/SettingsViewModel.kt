package com.ara.aranote.ui.screen.settings

import com.ara.aranote.domain.usecase.repo_detail.GetRepoDetailUseCase
import com.ara.aranote.domain.usecase.user_preferences.ObserveUserPreferencesUseCase
import com.ara.aranote.domain.usecase.user_preferences.WriteUserPreferencesUseCase
import com.ara.aranote.util.BaseViewModel
import com.ara.aranote.util.HDataBackup
import com.ara.aranote.util.REPO_NAME
import com.ara.aranote.util.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel
@Inject constructor(
    private val observeUserPreferencesUseCase: ObserveUserPreferencesUseCase,
    private val writeUserPreferencesUseCase: WriteUserPreferencesUseCase,
    private val getRepoDetailUseCase: GetRepoDetailUseCase,
    private val hDataBackup: HDataBackup,
) : BaseViewModel<SettingsState, SettingsIntent, SettingsSingleEvent>() {

    override fun initialState(): SettingsState = SettingsState()

    init {
        sendIntent(SettingsIntent.ObserveUserPreferences)
        sendIntent(SettingsIntent.LoadRepoDetail)
    }

    override suspend fun handleIntent(intent: SettingsIntent, state: SettingsState) {
        when (intent) {
            is SettingsIntent.ObserveUserPreferences ->
                observeFlow("Settings_observeUserPreferences") {
                    observeUserPreferencesUseCase().collect {
                        sendIntent(SettingsIntent.ShowUserPreferences(it))
                    }
                }
            is SettingsIntent.ShowUserPreferences -> Unit

            is SettingsIntent.WriteUserPreferences<*> ->
                writeUserPreferencesUseCase(intent.kProperty, intent.value)

            is SettingsIntent.ExportData -> hDataBackup.importData(intent.uri, intent.onComplete)
            is SettingsIntent.ImportData -> hDataBackup.exportData(intent.uri, intent.onComplete)

            is SettingsIntent.LoadRepoDetail ->
                when (val result = getRepoDetailUseCase(REPO_NAME)) {
                    is Result.Success -> sendIntent(SettingsIntent.ShowRepoDetail(result.data))
                    is Result.Error -> {
                        triggerSingleEvent(SettingsSingleEvent.ShowError(result.errorMessage))
                    }
                }
            is SettingsIntent.ShowRepoDetail -> Unit
        }
    }

    override val reducer: Reducer<SettingsState, SettingsIntent>
        get() = SettingsReducer()
}

internal class SettingsReducer : BaseViewModel.Reducer<SettingsState, SettingsIntent> {

    override fun reduce(state: SettingsState, intent: SettingsIntent): SettingsState =
        when (intent) {
            is SettingsIntent.ObserveUserPreferences -> state
            is SettingsIntent.ShowUserPreferences -> state.copy(userPreferences = intent.userPreferences)

            is SettingsIntent.WriteUserPreferences<*> -> state

            is SettingsIntent.ExportData -> state
            is SettingsIntent.ImportData -> state

            is SettingsIntent.LoadRepoDetail -> state
            is SettingsIntent.ShowRepoDetail -> state.copy(repo = intent.repo)
        }
}
