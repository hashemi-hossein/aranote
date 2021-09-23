package com.ara.aranote.data.repository

import com.ara.aranote.data.local_data_source.NoteDao
import com.ara.aranote.data.model.NoteModel
import com.ara.aranote.domain.entity.Note
import com.ara.aranote.domain.repository.NoteRepository
import com.ara.aranote.domain.util.DomainMapper
import com.ara.aranote.util.INVALID_NOTE_ID
import com.ara.aranote.util.TAG
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import timber.log.Timber
import javax.inject.Inject

class NoteRepositoryImpl
@Inject constructor(
    private val noteDao: NoteDao,
    private val domainMapper: DomainMapper<NoteModel, Note>
) : NoteRepository {

    override suspend fun insertNote(note: Note): Int {
        val result = noteDao.insertNote(domainMapper.mapFromDomainEntity(note))
        Timber.tag(TAG).d("insert note result = $result")
        return result?.toInt() ?: INVALID_NOTE_ID
    }

    override fun observeNotes(): Flow<List<Note>> {
        return noteDao.observeNotes().map {
            domainMapper.toDomainList(it)
        }
    }

    override suspend fun getNote(id: Int): Note? {
        return noteDao.getNote(id)?.let {
            domainMapper.mapToDomainEntity(it)
        }
    }

    override suspend fun updateNote(note: Note): Boolean {
        val result = noteDao.updateNote(domainMapper.mapFromDomainEntity(note))
        Timber.tag(TAG).d("update note result = $result")
        return result == 1
    }

    override suspend fun deleteNote(note: Note): Boolean {
        val result = noteDao.deleteNote(domainMapper.mapFromDomainEntity(note))
        Timber.tag(TAG).d("delete note result = $result")
        return result == 1
    }

    override suspend fun getLastId(): Int {
        return noteDao.getLastId() ?: INVALID_NOTE_ID
    }

    override suspend fun getAllNotesWithAlarm(): List<Note> {
        return noteDao.getAllNotesWithAlarm()?.let {
            domainMapper.toDomainList(it)
        } ?: listOf()
    }
}