package com.example.movierama.data.network.reviews

import com.google.gson.annotations.SerializedName

data class ReviewsResponse(
    @SerializedName("reviews") val reviewNetworks: List<ReviewNetwork>,
    @SerializedName("page") val page: Int,
    @SerializedName("total_pages") val totalPages: Int,
    @SerializedName("total_results") val totalResults: Int
)

data class ReviewNetwork(
    @SerializedName("id") val id: Long,
    @SerializedName("author") val author: String,
    @SerializedName("author_details") val authorDetails: AuthorDetails,
    @SerializedName("content") val content: String,
    @SerializedName("created_at") val createdAt: String,
    @SerializedName("updated_at") val updatedAt: String,
    @SerializedName("url") val url: String
){
    fun toUiReview() = Review(
        id = id,
        author = author,
        content = content
    )
}

data class AuthorDetails(
    @SerializedName("avatar_path") val avatarPath: String,
    @SerializedName("name") val name: String,
    @SerializedName("rating") val rating: Int,
    @SerializedName("username") val username: String
)

data class Review(
    val id: Long,
    val author: String,
    val content: String
)