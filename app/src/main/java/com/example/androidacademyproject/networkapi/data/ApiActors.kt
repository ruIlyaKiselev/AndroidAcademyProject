package com.example.androidacademyproject.networkapi.data

import com.google.gson.annotations.SerializedName

data class MovieCreditsMetaData(
        @SerializedName("cast")
        val cast: List<ActorFromApi>
)

data class ActorFromApi(
        val id: Int,
        val name: String,
        @SerializedName("profile_path")
        val imageUrl: String
)