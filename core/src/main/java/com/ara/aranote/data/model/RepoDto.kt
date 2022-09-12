package com.ara.aranote.data.model

import com.google.gson.annotations.SerializedName

/**
 * The received data from API including all below fields.
 */
data class RepoDto(
    val name: String,

    val description: String?,

    val owner: Owner,

    @SerializedName("watchers_count")
    val watchers: Int,

    val topics: List<String>,
)

data class Owner(
    @SerializedName("avatar_url")
    val avatarUrl: String,

    val login: String,
)
