package com.example.bcsd_android_2025_1.data

fun RepositoryDto.toDomain(): Repository {
    return Repository(id, name, htmlUrl)
}