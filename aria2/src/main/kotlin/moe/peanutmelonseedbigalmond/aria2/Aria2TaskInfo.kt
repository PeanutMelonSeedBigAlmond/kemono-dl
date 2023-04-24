package moe.peanutmelonseedbigalmond.aria2

data class Aria2TaskInfo(
    val completedLength:Long,
    val path:String,
    val errorCode:Int,
    val gid:String,
    val totalLength:Long,
)