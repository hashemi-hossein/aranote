package com.ara.aranote.domain.usecase.repo_detail

import com.ara.aranote.domain.entity.RepoDetail
import com.ara.aranote.domain.repository.RepoRepository
import com.ara.aranote.util.Result
import javax.inject.Inject

class GetRepoDetailUseCase @Inject constructor(
    private val repoRepository: RepoRepository,
) {
    suspend operator fun invoke(repoName: String): Result<RepoDetail> {
        return repoRepository.getRepoDetail(repoName)
    }
}
