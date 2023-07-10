package com.example.movierama.data

import com.example.movierama.data.network.credits.Cast
import com.example.movierama.data.network.credits.CreditsResponse
import com.example.movierama.data.network.credits.Crew
import com.google.common.truth.Truth.assertThat

import org.junit.Test


class CreditsResponseTest {

    @Test
    fun `test get Director method`() {
        // Given
        val response = CreditsResponse(
            id = 1,
            castList = getFakeCastList(),
            crewList = getFakeCrewList()
        )

        // When
        val expectedResult = "Name 1, Name 2, Name 3"

        // When
        assertThat(expectedResult).isEqualTo(response.getDirector())
    }

    @Test
    fun `test get Cast method`() {
        // Given
        val response = CreditsResponse(
            id = 1,
            castList = getFakeCastList(),
            crewList = getFakeCrewList()
        )

        // When
        val expectedResult = "Name 1, Name 2, Name 3"

        // When
        assertThat(expectedResult).isEqualTo(response.getCast())
    }

    private fun getFakeCastList() =
        listOf(
            Cast(
                false,
                123,
                "Character 1",
                "credit_id_1",
                0,
                456,
                "Acting",
                "Name 1",
                1,
                "Original Name 1",
                7.8,
                "/path1.jpg"
            ), Cast(
                true,
                234,
                "Character 2",
                "credit_id_2",
                1,
                567,
                "Acting",
                "Name 2",
                2,
                "Original Name 2",
                6.5,
                "/path2.jpg"
            ),
            Cast(
                false,
                345,
                "Character 3",
                "credit_id_3",
                0,
                678,
                "Acting",
                "Name 3",
                3,
                "Original Name 3",
                8.2,
                "/path3.jpg"
            ), Cast(
                true,
                456,
                "Character 4",
                "credit_id_4",
                1,
                789,
                "Department 2",
                "Name 4",
                4,
                "Original Name 4",
                7.1,
                "/path4.jpg"
            ), Cast(
                false,
                567,
                "Character 5",
                "credit_id_5",
                0,
                890,
                "Department 1",
                "Name 5",
                5,
                "Original Name 5",
                6.9,
                "/path5.jpg"
            )
        )


    private fun getFakeCrewList() =
        listOf(
            Crew(
                false,
                "credit_id_1",
                "Directing",
                0,
                123,
                "Job 1",
                "Known Department 1",
                "Name 1",
                "Original Name 1",
                7.8,
                "/path1.jpg"
            ),
            Crew(
                true,
                "credit_id_2",
                "Directing",
                1,
                234,
                "Job 2",
                "Known Department 2",
                "Name 2",
                "Original Name 2",
                6.5,
                "/path2.jpg"
            ),
            Crew(
                false,
                "credit_id_3",
                "Directing",
                0,
                345,
                "Job 3",
                "Known Department 1",
                "Name 3",
                "Original Name 3",
                8.2,
                "/path3.jpg"
            ),
            Crew(
                true,
                "credit_id_4",
                "Department 2",
                1,
                456,
                "Job 4",
                "Known Department 2",
                "Name 4",
                "Original Name 4",
                7.1,
                "/path4.jpg"
            ),
            Crew(
                false,
                "credit_id_5",
                "Department 1",
                0,
                567,
                "Job 5",
                "Known Department 1",
                "Name 5",
                "Original Name 5",
                6.9,
                "/path5.jpg"
            )
        )
}
