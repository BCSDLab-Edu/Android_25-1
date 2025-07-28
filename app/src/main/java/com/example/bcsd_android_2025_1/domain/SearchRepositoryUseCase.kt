package com.example.bcsd_android_2025_1.domain

import androidx.paging.PagingData
import com.example.bcsd_android_2025_1.data.Repository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchRepositoryUseCase @Inject constructor(
    private val repository: GithubRepository
) {
    operator fun invoke(query: String): Flow<PagingData<Repository>> {
        return repository.searchRepositories(query)
    }
}