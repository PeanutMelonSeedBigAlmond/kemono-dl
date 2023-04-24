package moe.peanutmelonseedbigalmond.aria2

interface Aria2Callback {
    fun onDownloadStart(taskInfo: Aria2TaskInfo)
    fun onDownloadComplete(taskInfo: Aria2TaskInfo)
    fun onDownloadError(taskInfo: Aria2TaskInfo)
    fun onDownloadStop(taskInfo: Aria2TaskInfo)
}