package com.example.bcsd_android_2025_1.data

import com.google.gson.annotations.SerializedName

data class RepositoryDto (
    val id: Long,
    val name: String,
    @SerializedName("html_url") val htmlUrl: String
)