package hu.gyenes.paintball.app.network

import hu.gyenes.paintball.app.model.DTO.ReservationDeleteRequest
import hu.gyenes.paintball.app.model.DTO.ReservationDeleteResponse
import hu.gyenes.paintball.app.model.DTO.ReservationResponse
import hu.gyenes.paintball.app.model.Reservation
import okhttp3.internal.http.hasBody
import retrofit2.Response
import retrofit2.http.*

interface ReservationsApi {
    @GET("reservations")
    suspend fun findReservations(): Response<ReservationResponse>

    @POST("reservations")
    suspend fun insert(@Body reservation: Reservation): Response<Reservation>

    @PATCH("reservations/{reservationId}")
    suspend fun update(
        @Path("reservationId") reservationId: String,
        @Body reservation: Reservation
    ): Response<Reservation>

    @HTTP(method = "DELETE", path = "reservations", hasBody = true)
    suspend fun delete(@Body ids: ReservationDeleteRequest): Response<ReservationDeleteResponse>
}