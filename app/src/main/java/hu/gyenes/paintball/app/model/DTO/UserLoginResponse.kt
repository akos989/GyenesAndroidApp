package hu.gyenes.paintball.app.model.DTO

data class UserLoginResponse(
    val token: String,
    val email: String,
    val expiresIn: Int,
    val localId: String,
    val newReservations: List<String>
)