package com.ara.aranote.util.alarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.ara.aranote.domain.repository.NoteRepository
import com.ara.aranote.util.CoroutineDispatcherProvider
import com.ara.aranote.util.Result
import com.ara.aranote.util.TAG
import com.ara.aranote.util.millis
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class BootReceiver : BroadcastReceiver() {

    @Inject
    lateinit var noteRepository: NoteRepository

    @Inject
    lateinit var coroutineDispatcherProvider: CoroutineDispatcherProvider

    private val job: Job = Job()
    private val coroutineScope = CoroutineScope(job + coroutineDispatcherProvider.io)

    override fun onReceive(context: Context?, intent: Intent?) {
        Timber.tag(TAG).d("BootReceiver -- onReceive -- intent?.action=${intent?.action}")

        if (context != null && intent?.action == "android.intent.action.BOOT_COMPLETED") {
            coroutineScope.launch {
                when (val result = noteRepository.getAllNotesWithAlarm()) {
                    is Result.Success -> {
                        val notes = result.data
                        println(notes.toString())

                        for (note in notes) {
                            hManageAlarm(
                                context = context,
                                doesCreate = true,
                                noteId = note.id,
                                triggerAtMillis = note.alarmDateTime?.millis()
                                    ?: System.currentTimeMillis()
                            )
                        }
                    }
                    is Result.Error -> println(result)
                }
            }
        }
    }
}
