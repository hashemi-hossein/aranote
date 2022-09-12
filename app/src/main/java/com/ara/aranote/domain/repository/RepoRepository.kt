package com.ara.aranote.domain.repository

import com.ara.aranote.domain.entity.RepoDetail
import com.ara.aranote.util.Result

interface RepoRepository {
    suspend fun getRepoDetail(repoName: String): Result<RepoDetail>
}
