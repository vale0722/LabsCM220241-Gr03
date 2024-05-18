package com.example.jetcaster.core.data.api

import com.example.jetcaster.core.data.database.model.Category
import com.example.jetcaster.core.data.database.model.Episode
import com.example.jetcaster.core.data.database.model.EpisodeToPodcast
import com.example.jetcaster.core.data.database.model.Podcast
import com.example.jetcaster.core.data.database.model.PodcastWithExtraInfo
import retrofit2.Response
import retrofit2.http.*

interface PodcastApi {
    @GET("podcast")
    suspend fun podcastWithUri(
        @Query("uri") uri: String
    ): Response<Podcast>

    @GET("podcast/complete")
    suspend fun podcastWithExtraInfo(
        @Query("uri") uri: String
    ): Response<PodcastWithExtraInfo>

    @GET("podcasts")
    suspend fun podcastsSortedByLastEpisode(
        @Query("limit") limit: Int
    ): Response<List<PodcastWithExtraInfo>>
}
