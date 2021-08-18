package com.example.androidacademyproject.networkapi.data

data class ApiGenresMetaData(
        val genres: List<ApiGenres>
)

data class ApiGenres (
        val id: Int,
        val name: String
)