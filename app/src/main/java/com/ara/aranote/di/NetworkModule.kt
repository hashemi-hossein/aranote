package com.ara.aranote.di

import com.ara.aranote.data.model.RepoDto
import com.ara.aranote.data.remote_data_source.GitHubService
import com.ara.aranote.data.repository.RepoRepositoryImpl
import com.ara.aranote.data.util.RepoDetailDomainMapper
import com.ara.aranote.domain.entity.RepoDetail
import com.ara.aranote.domain.repository.RepoRepository
import com.ara.aranote.domain.util.Mapper
import com.ara.aranote.util.BASE_URL
import com.ara.aranote.util.ResourcesProvider
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun provideOkHttpClient(): OkHttpClient =
        OkHttpClient.Builder()
            // for logging received data
            .addInterceptor(HttpLoggingInterceptor().apply { level = Level.BODY })
            .build()

    @Singleton
    @Provides
    fun provideGitHubService(
        okHttpClient: OkHttpClient,
    ): GitHubService =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .build()
            .create(GitHubService::class.java)

    @Singleton
    @Provides
    fun provideRepoDetailDomainMapper(): Mapper<RepoDto, RepoDetail> =
        RepoDetailDomainMapper()

    @Singleton
    @Provides
    fun provideRepoRepository(
        resourcesProvider: ResourcesProvider,
        gitHubService: GitHubService,
        repoDetailDomainMapper: Mapper<RepoDto, RepoDetail>,
    ): RepoRepository = RepoRepositoryImpl(
        resourcesProvider = resourcesProvider,
        gitHubService = gitHubService,
        repoDetailDomainMapper = repoDetailDomainMapper,
    )
}
