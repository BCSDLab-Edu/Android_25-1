package com.example.bcsd_android_2025_1.domain

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.example.bcsd_android_2025_1.data.GithubApi
import com.example.bcsd_android_2025_1.data.Repository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import com.example.bcsd_android_2025_1.data.toDomain


class GithubRepositoryImpl@Inject constructor(private val api: GithubApi) : GithubRepository{
    override fun searchRepositories(query: String): Flow<PagingData<Repository>> {
        return Pager(
            config = PagingConfig(pageSize = 30),
            pagingSourceFactory = { GithubPagingSource(api, query) }
        ).flow.map { pagingData -> pagingData.map { it.toDomain() } }
    }
}