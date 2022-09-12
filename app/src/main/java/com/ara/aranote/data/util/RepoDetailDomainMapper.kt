package com.ara.aranote.data.util

import com.ara.aranote.data.model.RepoDto
import com.ara.aranote.domain.entity.RepoDetail
import com.ara.aranote.domain.util.Mapper

/**
 * Based on CLEAN Architecture
 * This class is using for mapping Database Model into Domain Entity
 */
class RepoDetailDomainMapper : Mapper<RepoDto, RepoDetail> {

    override fun map(t: RepoDto): RepoDetail {
        return RepoDetail(
            repoName = t.name,
            description = t.description,
            avatarUrl = t.owner.avatarUrl,
            owner = t.owner.login,
            watchers = t.watchers,
            topics = t.topics,
        )
    }
}
