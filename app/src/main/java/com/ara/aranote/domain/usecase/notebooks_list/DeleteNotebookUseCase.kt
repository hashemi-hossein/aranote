package com.ara.aranote.domain.usecase.notebooks_list

import com.ara.aranote.domain.entity.Notebook
import com.ara.aranote.domain.repository.NoteRepository
import com.ara.aranote.domain.repository.NotebookRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class DeleteNotebookUseCase @Inject constructor(
    private val notebookRepository: NotebookRepository,
    private val noteRepository: NoteRepository,
) {
    suspend operator fun invoke(notebook: Notebook) {
        val notesOfNotebook = noteRepository.observe(notebook.id).first()
        for (note in notesOfNotebook) {
            noteRepository.delete(note)
        }
        notebookRepository.delete(notebook)
    }
}
