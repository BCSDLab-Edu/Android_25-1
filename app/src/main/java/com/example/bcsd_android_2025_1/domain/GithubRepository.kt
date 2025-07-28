package com.example.bcsd_android_2025_1.domain

import androidx.paging.PagingData
import com.example.bcsd_android_2025_1.data.Repository
import kotlinx.coroutines.flow.Flow

interface GithubRepository {
    fun searchRepositories(query: String): Flow<PagingData<Repository>>
}