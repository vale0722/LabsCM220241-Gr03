package com.example.jetcaster.core.data.api

import com.example.jetcaster.core.data.database.model.Category
import com.example.jetcaster.core.data.database.model.Episode
import com.example.jetcaster.core.data.database.model.EpisodeToPodcast
import com.example.jetcaster.core.data.database.model.PodcastWithExtraInfo
import retrofit2.Response
import retrofit2.http.*

interface EpisodeApi {
    @GET("episode")
    suspend fun getEpisodeWithUri(
        @Query("episodeUri") episodeUri: String
    ): Response<Episode>

    @GET("episode/1/podcast")
    suspend fun episodeAndPodcastWithUri(
        @Query("episodeUri") episodeUri: String
    ): Response<EpisodeToPodcast>

    @GET("episode/1/podcasts")
    suspend fun episodesInPodcast(
        @Query("podcastUri") episodeUri: String
    ): Response<List<EpisodeToPodcast>>

    @GET("episode/1/podcasts")
    suspend fun episodesInPodcasts(): Response<List<EpisodeToPodcast>>


    @POST("episodes")
    suspend fun addEpisode(@Body episodes: Collection<Episode>): Response<Long>
}
