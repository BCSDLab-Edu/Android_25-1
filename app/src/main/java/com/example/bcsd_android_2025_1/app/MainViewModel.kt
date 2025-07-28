package com.example.bcsd_android_2025_1.app

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.bcsd_android_2025_1.domain.SearchRepositoryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val searchUseCase: SearchRepositoryUseCase
) : ViewModel() {

    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query

    val repositories = _query
        .debounce(300)
        .distinctUntilChanged()
        .flatMapLatest { searchUseCase(it) }
        .cachedIn(viewModelScope)

    fun onQueryChanged(input: String) {
        _query.value = input
    }
}