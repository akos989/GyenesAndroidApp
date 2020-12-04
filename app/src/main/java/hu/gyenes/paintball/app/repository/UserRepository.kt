package hu.gyenes.paintball.app.repository

import hu.gyenes.paintball.app.db.GyenesPaintballDatabase
import hu.gyenes.paintball.app.model.DTO.UserLoginRequest
import hu.gyenes.paintball.app.network.RetrofitInstance

class UserRepository(val db: GyenesPaintballDatabase) {
    suspend fun login(email: String, password: String)
            = RetrofitInstance.userApi.login(UserLoginRequest(email, password))
}