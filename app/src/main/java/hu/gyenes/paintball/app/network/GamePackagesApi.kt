package hu.gyenes.paintball.app.network

import hu.gyenes.paintball.app.model.DTO.GamePackageResponse
import retrofit2.Response
import retrofit2.http.GET

interface GamePackagesApi {
    @GET("packages")
    suspend fun findPackages(): Response<GamePackageResponse>
}