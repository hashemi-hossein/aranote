package com.ara.aranote.domain.entity

data class RepoDetail(
    val repoName: String = "",
    val description: String? = "",
    val avatarUrl: String = "",
    val owner: String = "",
    val watchers: Int = 0,
    val topics: List<String> = emptyList(),
)
