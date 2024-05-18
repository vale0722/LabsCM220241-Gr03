package com.example.jetcaster.core.data.api

import com.example.jetcaster.core.data.database.model.Category
import com.example.jetcaster.core.data.database.model.EpisodeToPodcast
import com.example.jetcaster.core.data.database.model.PodcastWithExtraInfo
import retrofit2.Response
import retrofit2.http.*

interface CategoryApi {
    @GET("categories")
    suspend fun getCategoriesSortedByPodcastCount(
        @Query("limit") limit: Int
    ): Response<List<Category>>

    @GET("categories/1/podcasts")
    suspend fun getPodcastsInCategorySortedByPodcastCount(
        @Query("limit") limit: Int
    ): Response<List<PodcastWithExtraInfo>>

    @GET("categories/1/episodes")
    suspend fun getEpisodesFromPodcastsInCategory(
        @Query("limit") limit: Int
    ): Response<List<EpisodeToPodcast>>

    @POST("categories")
    suspend fun addCategory(@Body category: Category): Response<Long>

    @POST("categories/1/podcasts")
    suspend fun addPodcastToCategory(
        @Body podcastUri: String
    ): Response<Void>

    @GET("categories/search")
    suspend fun getCategory(@Query("name") name: String): Response<Category?>
}
