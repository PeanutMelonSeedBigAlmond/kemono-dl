package moe.peanutmelonseedbigalmond.kemondl.network.response

import com.google.gson.JsonElement
import com.google.gson.annotations.SerializedName
import java.util.Date

data class PostContentResponse(
    val attachments:List<AttachmentsBean>,
    val content:String,
    @SerializedName("edited")
    val editedTime:Date,
    val embed:JsonElement,
    @SerializedName("file")
    val cover:AttachmentsBean?,
    @SerializedName("id")
    val postId:String,
    @SerializedName("published")
    val publishedTime:Date,
    @SerializedName("service")
    val site:String,
    @SerializedName("shared_file")
    val sharedFile:Boolean,
    val title:String,
    @SerializedName("user")
    val userId:String,
){
    data class AttachmentsBean(
        val name:String?,
        val path:String?,
    )
}