package com.example.jetcaster.core.model

import com.example.jetcaster.core.data.database.model.Podcast
import com.example.jetcaster.core.data.database.model.PodcastWithExtraInfo
import java.time.OffsetDateTime

/**
 * External data layer representation of a podcast.
 */
data class PodcastInfo(
    val uri: String = "",
    val title: String = "",
    val author: String = "",
    val imageUrl: String = "",
    val description: String = "",
    val isSubscribed: Boolean? = null,
    val lastEpisodeDate: OffsetDateTime? = null,
)

fun Podcast.asExternalModel(): PodcastInfo =
    PodcastInfo(
        uri = this.uri,
        title = this.title,
        author = this.author ?: "",
        imageUrl = this.imageUrl ?: "",
        description = this.description ?: "",
    )

fun PodcastWithExtraInfo.asExternalModel(): PodcastInfo =
    this.podcast.asExternalModel().copy(
        isSubscribed = isFollowed,
        lastEpisodeDate = lastEpisodeDate,
    )
