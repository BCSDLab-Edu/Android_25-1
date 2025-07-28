package com.example.bcsd_android_2025_1.domain

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.bcsd_android_2025_1.data.GithubApi
import com.example.bcsd_android_2025_1.data.RepositoryDto

class GithubPagingSource(private val api: GithubApi, private val query: String) : PagingSource<Int, RepositoryDto>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, RepositoryDto> {
        return try {
            val page = params.key ?: 1
            val response = api.searchRepositories(query, page)
            val items = response.items
            LoadResult.Page(
                data = items,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (items.isEmpty()) null else page + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, RepositoryDto>): Int? = null
}