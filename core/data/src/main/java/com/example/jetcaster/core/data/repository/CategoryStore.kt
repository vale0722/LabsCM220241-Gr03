package com.example.jetcaster.core.data.repository

import android.util.Log
import com.example.jetcaster.core.data.database.model.Category
import com.example.jetcaster.core.data.database.model.EpisodeToPodcast
import com.example.jetcaster.core.data.database.model.PodcastWithExtraInfo
import kotlinx.coroutines.flow.Flow
import com.example.jetcaster.core.data.api.CategoryApi
import com.example.jetcaster.core.data.api.RetrofitClient
import kotlinx.coroutines.flow.flow

interface CategoryStore {
    fun categoriesSortedByPodcastCount(
        limit: Int = Integer.MAX_VALUE
    ): Flow<List<Category>>

    fun podcastsInCategorySortedByPodcastCount(
        categoryId: Long,
        limit: Int = Int.MAX_VALUE
    ): Flow<List<PodcastWithExtraInfo>>

    fun episodesFromPodcastsInCategory(
        categoryId: Long,
        limit: Int = Integer.MAX_VALUE
    ): Flow<List<EpisodeToPodcast>>

    suspend fun addCategory(category: Category): Long

    suspend fun addPodcastToCategory(podcastUri: String, categoryId: Long)

    fun getCategory(name: String): Flow<Category?>
}

class RemoteCategoryStore : CategoryStore {
    private val categoryApi: CategoryApi = RetrofitClient.retrofit.create(CategoryApi::class.java)

    override fun categoriesSortedByPodcastCount(limit: Int): Flow<List<Category>> = flow {
        val response = categoryApi.getCategoriesSortedByPodcastCount(limit)
        if (response.isSuccessful) {
            response.body()?.let { emit(it) }
        } else {
            Log.d("CATEGORIES", response.message())
        }
    }

    override fun podcastsInCategorySortedByPodcastCount(
        categoryId: Long,
        limit: Int
    ): Flow<List<PodcastWithExtraInfo>> = flow {
        try {
            val response = categoryApi.getPodcastsInCategorySortedByPodcastCount(categoryId, limit)
            if (response.isSuccessful) {
                response.body()?.let { emit(it) }
            } else {
                Log.d("CATEGORIES", response.message())
            }
        } catch (t: Throwable) {
            Log.d("CATEGORIES", t.stackTraceToString())
        }
    }

    override fun episodesFromPodcastsInCategory(
        categoryId: Long,
        limit: Int
    ): Flow<List<EpisodeToPodcast>> = flow {
        val response = categoryApi.getEpisodesFromPodcastsInCategory(categoryId, limit)
        if (response.isSuccessful) {
            response.body()?.let { emit(it) }
        } else {
            Log.d("CATEGORIES", response.message())
        }
    }

    override suspend fun addCategory(category: Category): Long {
        val response = categoryApi.addCategory(category)
        return if (response.isSuccessful) {
            response.body() ?: 0L
        } else {
            Log.d("CATEGORIES", response.message())
            0L
        }
    }

    override suspend fun addPodcastToCategory(podcastUri: String, categoryId: Long) {
        val response = categoryApi.addPodcastToCategory(categoryId, podcastUri)
        if (!response.isSuccessful) {
            Log.d("CATEGORIES", response.message())
        }
    }

    override fun getCategory(name: String): Flow<Category?> = flow {
        val response = categoryApi.getCategory(name)
        if (response.isSuccessful) {
            emit(response.body())
        } else {
            Log.d("CATEGORIES", response.message())
        }
    }
}
