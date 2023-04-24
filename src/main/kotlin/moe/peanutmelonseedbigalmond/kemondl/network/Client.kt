package moe.peanutmelonseedbigalmond.kemondl.network

import com.google.gson.GsonBuilder
import moe.peanutmelonseedbigalmond.kemondl.App
import moe.peanutmelonseedbigalmond.kemondl.component.DateAdapter
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.net.InetSocketAddress
import java.net.Proxy
import java.util.Date

object Client {
    private val session =
        App.config.session
    private val client = OkHttpClient.Builder()
        .addInterceptor {
            val request = it.request()
                .newBuilder()
                .header("Cookie", "session=$session")
                .header("referer", "https://kemono.party/")
                .build()

            return@addInterceptor it.proceed(request)
        }
        .build()
    private val service = Retrofit.Builder()
        .baseUrl(Api.BASE_URL)
        .client(client)
        .addConverterFactory(ScalarsConverterFactory.create())
        .addConverterFactory(
            GsonConverterFactory.create(
                GsonBuilder()
                    .registerTypeAdapter(Date::class.java,DateAdapter)
                    .create()
            )
        )
        .build()
        .create(Api::class.java)

    suspend fun getFavourites(sortBy: String = "updated", orderBy: String = "desc") =
        service.getFavourites(sortBy, orderBy)

    suspend fun getUserPosts(site: String, id: String, offset: Int = 0) = service.getUserPosts(site, id, offset)

    suspend fun getPostContent(site: String, userId: String, postId: String) =
        service.getPostContent(site, userId, postId)[0]

    suspend fun getAllCreators()= service.getAllCreators()
}