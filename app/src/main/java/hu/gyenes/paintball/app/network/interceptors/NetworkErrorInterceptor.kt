package hu.gyenes.paintball.app.network.interceptors

import android.content.Context
import android.net.ConnectivityManager
import hu.gyenes.paintball.app.utils.PaintballApplication
import okhttp3.Interceptor
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody

class NetworkErrorInterceptor : Interceptor {

    private fun isConnected(): Boolean {
        val connectivityManager
                = (PaintballApplication.context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager)
        val netInfo = connectivityManager.activeNetworkInfo
        return netInfo != null && netInfo.isConnected
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        if (!isConnected())
            return Response.Builder()
                .code(400)
                .body("".toResponseBody(null))
                .protocol(Protocol.HTTP_2)
                .message("No internet connection!")
                .request(chain.request())
                .build()
        return chain.proceed(chain.request())
    }

}