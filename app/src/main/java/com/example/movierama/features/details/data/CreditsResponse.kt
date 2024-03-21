package com.example.movierama.features.details.data

import com.google.gson.annotations.SerializedName

data class CreditsResponse(
    @SerializedName("cast") val castList: List<Cast>,
    @SerializedName("crew") val crewList: List<Crew>,
    @SerializedName("id") val id: Long
) {
    fun getDirector(): String {
        return crewList
            .filter { it.department == CrewDepartment.DIRECTING.department }
            .joinToString(", ") { it.name }
    }

    fun getCast(): String {
        return castList
            .filter { it.knownForDepartment == CastDepartment.ACTING.department }
            .joinToString(", ") { it.name }
    }
}

data class Cast(
    @SerializedName("adult") val adult: Boolean,
    @SerializedName("cast_id") val castId: Int,
    @SerializedName("character") val character: String,
    @SerializedName("credit_id") val creditId: String,
    @SerializedName("gender") val gender: Int,
    @SerializedName("id") val id: Int,
    @SerializedName("known_for_department") val knownForDepartment: String,
    @SerializedName("name") val name: String,
    @SerializedName("order") val order: Int,
    @SerializedName("original_name") val originalName: String,
    @SerializedName("popularity") val popularity: Double,
    @SerializedName("profile_path") val profilePath: String
)

data class Crew(
    @SerializedName("adult") val adult: Boolean,
    @SerializedName("credit_id") val creditId: String,
    @SerializedName("department") val department: String,
    @SerializedName("gender") val gender: Int,
    @SerializedName("id") val id: Int,
    @SerializedName("job") val job: String,
    @SerializedName("known_for_department") val knownForDepartment: String,
    @SerializedName("name") val name: String,
    @SerializedName("original_name") val originalName: String,
    @SerializedName("popularity") val popularity: Double,
    @SerializedName("profile_path") val profilePath: String
)

enum class CrewDepartment(val department: String) {
    DIRECTING("Directing")
}

enum class CastDepartment(val department: String) {
    ACTING("Acting")
}
