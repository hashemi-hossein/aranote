package com.ara.aranote.ui.component

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.ara.aranote.R
import com.ara.aranote.domain.entity.RepoDetail
import com.ara.aranote.util.RepoBaseUrl

@Composable
internal fun RepoDetail(
    repo: RepoDetail,
) {
    Card(
        modifier = Modifier
            .padding(dimensionResource(id = R.dimen.default_margin)).fillMaxWidth(),
        elevation = dimensionResource(id = R.dimen.card_elevation),
        shape = RoundedCornerShape(dimensionResource(id = R.dimen.card_corner_radius))
    ) {
        val context = LocalContext.current

        Column(
            modifier = Modifier
                .padding(dimensionResource(id = R.dimen.default_margin)),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(7.dp)
        ) {
            AsyncImage(
                model = repo.avatarUrl,
                contentDescription = stringResource(R.string.repo_avatar_image),
                modifier = Modifier
                    .padding(10.dp)
                    .size(100.dp)
            )
            Label(
                text = stringResource(
                    id = R.string.screen_repodetail_owner,
                    repo.owner
                )
            )
            Label(
                text = stringResource(
                    id = R.string.screen_repodetail_repoName,
                    repo.repoName
                )
            )
            if (repo.description != null)
                Label(
                    text = stringResource(
                        id = R.string.screen_repodetail_description,
                        repo.description!!
                    )
                )
            Label(
                text = stringResource(
                    id = R.string.screen_repodetail_watchers,
                    repo.watchers
                )
            )
            Button(onClick = {
                openRepoUrl(context, repo.repoName)
            }) {
                Text(text = stringResource(R.string.open_repository))
            }
        }
    }
}

fun openRepoUrl(context: Context, name: String) {
    Intent(Intent.ACTION_VIEW).apply {
        this.data = Uri.parse(RepoBaseUrl + name)
        context.startActivity(this)
    }
}

@Composable
private fun Label(text: String) {
    Text(text = text, textAlign = TextAlign.Center, style = MaterialTheme.typography.body1)
}

@Preview
@Composable
private fun Preview() {
    RepoDetail(
        RepoDetail(
            repoName = "repo name",
            description = "repo description",
            avatarUrl = "https://avatars.githubusercontent.com/u/91213707?v=4",
            owner = "hossein",
            watchers = 2,
            topics = listOf("android", "kotlin")
        )
    )
}
