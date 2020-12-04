package hu.gyenes.paintball.app.model.DTO

data class UserLoginRequest(
    val email: String,
    val password: String
)