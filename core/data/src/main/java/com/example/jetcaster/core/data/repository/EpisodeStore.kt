package com.example.jetcaster.core.data.repository

import android.util.Log
import com.example.jetcaster.core.data.api.EpisodeApi
import com.example.jetcaster.core.data.api.RetrofitClient
import com.example.jetcaster.core.data.database.dao.EpisodesDao
import com.example.jetcaster.core.data.database.model.Episode
import com.example.jetcaster.core.data.database.model.EpisodeToPodcast
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

interface EpisodeStore {
    fun episodeWithUri(episodeUri: String): Flow<Episode>
    fun episodeAndPodcastWithUri(episodeUri: String): Flow<EpisodeToPodcast>
    fun episodesInPodcast(
        podcastUri: String,
        limit: Int = Integer.MAX_VALUE
    ): Flow<List<EpisodeToPodcast>>

    fun episodesInPodcasts(
        podcastUris: List<String>,
        limit: Int = Integer.MAX_VALUE
    ): Flow<List<EpisodeToPodcast>>

    suspend fun addEpisodes(episodes: Collection<Episode>)

    suspend fun isEmpty(): Boolean
}

class LocalEpisodeStore(
    private val episodesDao: EpisodesDao
) : EpisodeStore {
    /**
     * Returns a flow containing the episode given [episodeUri].
     */
    override fun episodeWithUri(episodeUri: String): Flow<Episode> {
        return episodesDao.episode(episodeUri)
    }

    override fun episodeAndPodcastWithUri(episodeUri: String): Flow<EpisodeToPodcast> =
        episodesDao.episodeAndPodcast(episodeUri)

    /**
     * Returns a flow containing the list of episodes associated with the podcast with the
     * given [podcastUri].
     */
    override fun episodesInPodcast(
        podcastUri: String,
        limit: Int
    ): Flow<List<EpisodeToPodcast>> {
        return episodesDao.episodesForPodcastUri(podcastUri, limit)
    }
    /**
     * Returns a list of episodes for the given podcast URIs ordering by most recently published
     * to least recently published.
     */
    override fun episodesInPodcasts(
        podcastUris: List<String>,
        limit: Int
    ): Flow<List<EpisodeToPodcast>> =
        episodesDao.episodesForPodcasts(podcastUris, limit)

    /**
     * Add a new [Episode] to this store.
     *
     * This automatically switches to the main thread to maintain thread consistency.
     */
    override suspend fun addEpisodes(episodes: Collection<Episode>) =
        episodesDao.insertAll(episodes)

    override suspend fun isEmpty(): Boolean = episodesDao.count() == 0
}


class RemoteEpisodeStore : EpisodeStore {
    private val episodesApi: EpisodeApi = RetrofitClient.retrofit.create(EpisodeApi::class.java)

    override fun episodeWithUri(episodeUri: String): Flow<Episode> = flow {
        val response = episodesApi.getEpisodeWithUri(episodeUri)
        if (response.isSuccessful) {
            response.body()?.let { emit(it) }
        } else {
            Log.d("EPISODE", response.message())
        }
    }

    override fun episodeAndPodcastWithUri(episodeUri: String): Flow<EpisodeToPodcast>  = flow  {
        val response = episodesApi.episodeAndPodcastWithUri(episodeUri)
        if (response.isSuccessful) {
            response.body()?.let { emit(it) }
        } else {
            Log.d("EPISODE", response.message())
        }
    }

    override fun episodesInPodcast(podcastUri: String, limit: Int): Flow<List<EpisodeToPodcast>>  = flow  {
        val response = episodesApi.episodesInPodcast(podcastUri)
        if (response.isSuccessful) {
            response.body()?.let { emit(it) }
        } else {
            Log.d("EPISODE", response.message())
        }
    }

    override fun episodesInPodcasts(
        podcastUris: List<String>,
        limit: Int
    ): Flow<List<EpisodeToPodcast>>  = flow  {
        val response = episodesApi.episodesInPodcasts()
        if (response.isSuccessful) {
            response.body()?.let { emit(it) }
        } else {
            Log.d("EPISODE", response.message())
        }
    }

    override suspend fun addEpisodes(episodes: Collection<Episode>)  {
        val response = episodesApi.addEpisode(episodes)

        if (response.isSuccessful) {
            response.body() ?: 0L
        } else {
            Log.d("CATEGORIES", response.message())
        }
    }

    override suspend fun isEmpty(): Boolean  {
       return false
    }
}
