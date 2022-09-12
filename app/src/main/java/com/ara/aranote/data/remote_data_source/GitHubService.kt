package com.ara.aranote.data.remote_data_source

import com.ara.aranote.data.model.RepoDto
import retrofit2.http.GET
import retrofit2.http.Path

interface GitHubService {

    @GET("repos/hashemi-hossein/{repoName}")
    suspend fun getRepoDetail(
        @Path("repoName") repoName: String,
    ): RepoDto
}
