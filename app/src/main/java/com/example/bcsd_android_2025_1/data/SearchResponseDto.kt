package com.example.bcsd_android_2025_1.data

import com.google.gson.annotations.SerializedName

data class SearchResponseDto (
    @SerializedName("items")
    val items: List<RepositoryDto>
)