package com.ara.aranote.test_util

import com.ara.aranote.data.model.NoteModel
import com.ara.aranote.data.model.NotebookModel
import com.ara.aranote.domain.entity.Note
import com.ara.aranote.domain.entity.Notebook
import kotlinx.datetime.LocalDateTime

object TestUtil {

    val tDateTime = LocalDateTime.parse("2021-01-01T00:00")
    val tDateTime2 = LocalDateTime.parse("2022-01-01T00:00")
    val tNoteEntity = Note(
        id = 1,
        notebookId = 1,
        text = "test",
        addedDateTime = tDateTime,
        alarmDateTime = tDateTime,
    )
    val tNoteEntity2 = Note(
        id = 2,
        notebookId = 2,
        text = "test2",
        addedDateTime = tDateTime2,
        alarmDateTime = tDateTime2,
    )
    val tNoteModel = NoteModel(
        id = 1,
        notebookId = 1,
        text = "test",
        addedDateTime = tDateTime,
        alarmDateTime = tDateTime,
    )
    val tNoteModel2 = NoteModel(
        id = 2,
        notebookId = 2,
        text = "test2",
        addedDateTime = tDateTime2,
        alarmDateTime = tDateTime2,
    )
    val tNoteEntityList = listOf(tNoteEntity, tNoteEntity2)
    val tNoteModelList = listOf(tNoteModel, tNoteModel2)

    val tNotebookEntity = Notebook(
        id = 1,
        name = "notebook",
    )
    val tNotebookEntity2 = Notebook(
        id = 2,
        name = "notebook2",
    )
    val tNotebookModel = NotebookModel(
        id = 1,
        name = "notebook",
    )
    val tNotebookModel2 = NotebookModel(
        id = 2,
        name = "notebook2",
    )
    val tNotebookEntityList = listOf(tNotebookEntity, tNotebookEntity2)
    val tNotebookModelList = listOf(tNotebookModel, tNotebookModel2)
}
