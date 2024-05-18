/*
 * Copyright 2020 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.jetcaster.core.data.repository

import android.util.Log
import com.example.jetcaster.core.data.api.EpisodeApi
import com.example.jetcaster.core.data.api.PodcastApi
import com.example.jetcaster.core.data.api.RetrofitClient
import com.example.jetcaster.core.data.database.dao.PodcastFollowedEntryDao
import com.example.jetcaster.core.data.database.dao.PodcastsDao
import com.example.jetcaster.core.data.database.dao.TransactionRunner
import com.example.jetcaster.core.data.database.model.Category
import com.example.jetcaster.core.data.database.model.Podcast
import com.example.jetcaster.core.data.database.model.PodcastFollowedEntry
import com.example.jetcaster.core.data.database.model.PodcastWithExtraInfo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

interface PodcastStore {
    /**
     * Return a flow containing the [Podcast] with the given [uri].
     */
    fun podcastWithUri(uri: String): Flow<Podcast>

    /**
     * Return a flow containing the [PodcastWithExtraInfo] with the given [podcastUri].
     */
    fun podcastWithExtraInfo(podcastUri: String): Flow<PodcastWithExtraInfo>

    /**
     * Returns a flow containing the entire collection of podcasts, sorted by the last episode
     * publish date for each podcast.
     */
    fun podcastsSortedByLastEpisode(
        limit: Int = Int.MAX_VALUE
    ): Flow<List<PodcastWithExtraInfo>>

    /**
     * Returns a flow containing a list of all followed podcasts, sorted by the their last
     * episode date.
     */
    fun followedPodcastsSortedByLastEpisode(
        limit: Int = Int.MAX_VALUE
    ): Flow<List<PodcastWithExtraInfo>>

    /**
     * Returns a flow containing a list of podcasts such that its name partially matches
     * with the specified keyword
     */
    fun searchPodcastByTitle(
        keyword: String,
        limit: Int = Int.MAX_VALUE
    ): Flow<List<PodcastWithExtraInfo>>

    /**
     * Return a flow containing a list of podcast such that it belongs to the any of categories
     * specified with categories parameter and its name partially matches with the specified
     * keyword.
     */
    fun searchPodcastByTitleAndCategories(
        keyword: String,
        categories: List<Category>,
        limit: Int = Int.MAX_VALUE
    ): Flow<List<PodcastWithExtraInfo>>

    suspend fun togglePodcastFollowed(podcastUri: String)

    suspend fun followPodcast(podcastUri: String)

    suspend fun unfollowPodcast(podcastUri: String)

    /**
     * Add a new [Podcast] to this store.
     *
     * This automatically switches to the main thread to maintain thread consistency.
     */
    suspend fun addPodcast(podcast: Podcast)

    suspend fun isEmpty(): Boolean
}

class RemotePodcastStore constructor() : PodcastStore {
    private val podcastApi: PodcastApi = RetrofitClient.retrofit.create(PodcastApi::class.java)

    override fun podcastWithUri(uri: String): Flow<Podcast> = flow {
        val response = podcastApi.podcastWithUri(uri)
        if (response.isSuccessful) {
            response.body()?.let { emit(it) }
        } else {
            Log.d("PODCAST", response.message())
        }
    }

    override fun podcastWithExtraInfo(podcastUri: String): Flow<PodcastWithExtraInfo> = flow {
        val response = podcastApi.podcastWithExtraInfo(podcastUri)
        if (response.isSuccessful) {
            response.body()?.let { emit(it) }
        } else {
            Log.d("PODCAST", response.message())
        }
    }

    override fun podcastsSortedByLastEpisode(
        limit: Int
    ): Flow<List<PodcastWithExtraInfo>> = flow {
        val response = podcastApi.podcastsSortedByLastEpisode(limit)
        if (response.isSuccessful) {
            response.body()?.let { emit(it) }
        } else {
            Log.d("PODCAST", response.message())
        }
    }

    override fun followedPodcastsSortedByLastEpisode(
        limit: Int
    ): Flow<List<PodcastWithExtraInfo>> = flow {
        val response = podcastApi.podcastsSortedByLastEpisode(limit)
        if (response.isSuccessful) {
            response.body()?.let { emit(it) }
        } else {
            Log.d("PODCAST", response.message())
        }
    }

    override fun searchPodcastByTitle(
        keyword: String,
        limit: Int
    ): Flow<List<PodcastWithExtraInfo>> = flow {
        val response = podcastApi.podcastsSortedByLastEpisode(limit)
        if (response.isSuccessful) {
            response.body()?.let { emit(it) }
        } else {
            Log.d("PODCAST", response.message())
        }
    }

    override fun searchPodcastByTitleAndCategories(
        keyword: String,
        categories: List<Category>,
        limit: Int
    ): Flow<List<PodcastWithExtraInfo>> = flow {
        val response = podcastApi.podcastsSortedByLastEpisode(limit)
        if (response.isSuccessful) {
            response.body()?.let { emit(it) }
        } else {
            Log.d("PODCAST", response.message())
        }
    }

    override suspend fun followPodcast(podcastUri: String) {
    }

    override suspend fun togglePodcastFollowed(podcastUri: String) {
    }

    override suspend fun unfollowPodcast(podcastUri: String) {
    }

    override suspend fun addPodcast(podcast: Podcast) {
    }

    override suspend fun isEmpty(): Boolean = false
}
