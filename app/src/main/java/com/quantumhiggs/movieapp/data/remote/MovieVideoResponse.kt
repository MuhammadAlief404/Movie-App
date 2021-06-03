package com.quantumhiggs.movieapp.data.remote

import com.google.gson.annotations.SerializedName

data class MovieVideoResponse(

    @field:SerializedName("id")
    val id: Int? = null,

    @field:SerializedName("results")
    val results: List<Video?>? = null
)

data class Video(

    @field:SerializedName("site")
    val site: String? = null,

    @field:SerializedName("size")
    val size: Int? = null,

    @field:SerializedName("iso_3166_1")
    val iso31661: String? = null,

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("id")
    val id: String? = null,

    @field:SerializedName("type")
    val type: String? = null,

    @field:SerializedName("iso_639_1")
    val iso6391: String? = null,

    @field:SerializedName("key")
    val key: String? = null
)
