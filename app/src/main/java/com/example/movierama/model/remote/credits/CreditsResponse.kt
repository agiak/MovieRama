package com.example.movierama.model.remote.credits

import com.google.gson.annotations.SerializedName

data class CreditsResponse(
    @SerializedName("cast") val castList: List<Cast>,
    @SerializedName("crew") val crewList: List<Crew>,
    @SerializedName("id") val id: Int
) {
    fun getDirector(): String {
        return crewList
            .filter { it.department == CrewDepartment.DIRECTING.department }
            .joinToString(", ") { it.name }
    }

    fun getCast(): String {
        return castList
            .filter { it.known_for_department == CastDepartment.ACTING.department }
            .joinToString(", ") { it.name }
    }
}

data class Cast(
    @SerializedName("adult") val adult: Boolean,
    @SerializedName("cast_id") val cast_id: Int,
    @SerializedName("character") val character: String,
    @SerializedName("credit_id") val credit_id: String,
    @SerializedName("gender") val gender: Int,
    @SerializedName("id") val id: Int,
    @SerializedName("known_for_department") val known_for_department: String,
    @SerializedName("name") val name: String,
    @SerializedName("order") val order: Int,
    @SerializedName("original_name") val original_name: String,
    @SerializedName("popularity") val popularity: Double,
    @SerializedName("profile_path") val profile_path: String
)

data class Crew(
    @SerializedName("adult") val adult: Boolean,
    @SerializedName("credit_id") val credit_id: String,
    @SerializedName("department") val department: String,
    @SerializedName("gender") val gender: Int,
    @SerializedName("id") val id: Int,
    @SerializedName("job") val job: String,
    @SerializedName("known_for_department") val known_for_department: String,
    @SerializedName("name") val name: String,
    @SerializedName("original_name") val original_name: String,
    @SerializedName("popularity") val popularity: Double,
    @SerializedName("profile_path") val profile_path: String
)

enum class CrewDepartment(val department: String) {
    DIRECTING("Directing")
}

enum class CastDepartment(val department: String) {
    ACTING("Acting")
}