package moe.peanutmelonseedbigalmond.kemondl.network

import moe.peanutmelonseedbigalmond.kemondl.network.response.CreatorsResponse
import moe.peanutmelonseedbigalmond.kemondl.network.response.FavouriteUsersResponse
import moe.peanutmelonseedbigalmond.kemondl.network.response.PostContentResponse
import okhttp3.ResponseBody
import org.jsoup.nodes.Document
import retrofit2.http.*

interface Api {
    @POST("account/login")
    @FormUrlEncoded
    suspend fun login(
        @Field("username") username: String,
        @Field("password") password: String,
    ): ResponseBody

    @GET("favorites")
    suspend fun getFavourites(
        @Query("sort") sortBy: String,// updated || faved_seq
        @Query("order") orderBy: String,// desc || asc
    ): List<FavouriteUsersResponse>

    @GET("{site}/user/{id}")
    suspend fun getUserPosts(
        @Path("site") site: String,
        @Path("id") id: String,
        @Query("o") offset: Int
    ): List<PostContentResponse>

    @GET("{site}/user/{uid}/post/{pid}")
    suspend fun getPostContent(
        @Path("site") site: String,
        @Path("uid") uid: String,
        @Path("pid") pid: String
    ): List<PostContentResponse>

    @GET("creators")
    suspend fun getAllCreators():List<CreatorsResponse>

    companion object {
        const val BASE_URL = "https://kemono.party/api/"
        const val BASE_IMAGE_URL = "https://kemono.party/"
    }
}