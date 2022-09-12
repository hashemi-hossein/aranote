package com.ara.aranote.data.repository

import com.ara.aranote.R
import com.ara.aranote.data.model.RepoDto
import com.ara.aranote.data.remote_data_source.GitHubService
import com.ara.aranote.domain.entity.RepoDetail
import com.ara.aranote.domain.repository.RepoRepository
import com.ara.aranote.domain.util.Mapper
import com.ara.aranote.util.ResourcesProvider
import com.ara.aranote.util.Result
import retrofit2.HttpException
import java.net.UnknownHostException

/**
 * Based on SINGLE-SOURCE-OF-TRUTH strategy
 * it will try to read data from API
 * in case of exceptions (like 403, no internet, etc)
 * returns [Result.Error]
 */
class RepoRepositoryImpl(
    private val resourcesProvider: ResourcesProvider,
    private val gitHubService: GitHubService,
    private val repoDetailDomainMapper: Mapper<RepoDto, RepoDetail>,
) : RepoRepository {

    override suspend fun getRepoDetail(repoName: String): Result<RepoDetail> {
        val result = try {
            val repoDto = gitHubService.getRepoDetail(repoName)
            Result.Success(repoDetailDomainMapper.map(repoDto))
        } catch (e: Exception) {
            Result.Error(handleAPIErrors(e))
        }

        return result
    }

    private fun handleAPIErrors(e: Exception) =
        when (e) {
            is UnknownHostException -> resourcesProvider.getString(R.string.api_errors_no_internet)
            is HttpException ->
                when (e.code()) {
                    403 -> resourcesProvider.getString(R.string.api_errors_http403)
                    else -> e.response()?.errorBody()?.string().orEmpty()
                }
            else -> resourcesProvider.getString(R.string.api_errors_undefined, e.message)
        }
}
