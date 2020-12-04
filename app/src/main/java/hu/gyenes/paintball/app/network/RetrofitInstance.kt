package hu.gyenes.paintball.app.network

import hu.gyenes.paintball.app.model.CurrentUser
import hu.gyenes.paintball.app.network.interceptors.NetworkErrorInterceptor
import hu.gyenes.paintball.app.utils.Constants.Companion.BASE_URL
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitInstance {
    companion object {
        private val headerInterceptor = object: Interceptor {
            override fun intercept(chain: Interceptor.Chain): Response {
                var request = chain.request()
                request = request.newBuilder()
                    .addHeader("Authorization", "Bearer ${CurrentUser.token}")
                    .build()
                return chain.proceed(request)
            }
        }

        private val retrofit by lazy {
            val logging = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.BODY
            val client = OkHttpClient.Builder()
                .addInterceptor(NetworkErrorInterceptor())
                .addInterceptor(headerInterceptor)
                .addInterceptor(logging)
                .build()

            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
        }

        val userApi by lazy {
            retrofit.create(UsersApi::class.java)
        }
        val gamePackagesApi by lazy {
            retrofit.create(GamePackagesApi::class.java)
        }
        val noDateApi by lazy {
            retrofit.create(NoDatesApi::class.java)
        }
        val reservationApi by lazy {
            retrofit.create(ReservationsApi::class.java)
        }
    }
}