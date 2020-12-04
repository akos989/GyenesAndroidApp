package hu.gyenes.paintball.app.network

import hu.gyenes.paintball.app.model.DTO.GamePackageResponse
import hu.gyenes.paintball.app.model.DTO.UserLoginRequest
import hu.gyenes.paintball.app.model.DTO.UserLoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface UsersApi {
    @POST("operators/login")
    suspend fun login(@Body loginData: UserLoginRequest): Response<UserLoginResponse>
}