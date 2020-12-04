package hu.gyenes.paintball.app.network

import hu.gyenes.paintball.app.model.DTO.NoDateResponse
import retrofit2.Response
import retrofit2.http.GET

interface NoDatesApi {
    @GET("no_dates")
    suspend fun findNoDates(): Response<NoDateResponse>
}