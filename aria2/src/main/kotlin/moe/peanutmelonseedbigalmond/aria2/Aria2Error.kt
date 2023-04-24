package moe.peanutmelonseedbigalmond.aria2

object Aria2Error {
    @JvmStatic
    private val errors= mapOf(
        0 to "下载完成.",
        1 to "未知错误.",
        2 to "操作超时.",
        3 to "无法找到指定资源.",
        4 to "无法找到指定资源. 参见 --max-file-not-found option 参数.",
        5 to "由于下载速度过慢, 下载已经终止. 参见 --lowest-speed-limit option 参数.",
        6 to "网络问题.",
        8 to "服务器不支持断点续传.",
        9 to "可用磁盘空间不足.",
        10 to "分片大小与 .aria2 控制文件中的不同. 参见 --allow-piece-length-change 参数.",
        11 to "aria2 已经下载了另一个相同文件.",
        12 to "aria2 已经下载了另一个相同哈希的种子文件.",
        13 to "文件已经存在. 参见 --allow-overwrite 参数. ",
        14 to "文件重命名失败. 参见 --auto-file-renaming 参数.",
        15 to "文件打开失败.",
        16 to "文件创建或删除已有文件失败.",
        17 to "文件系统出错.",
        18 to "无法创建指定目录.",
        19 to "域名解析失败.",
        20 to "解析 Metalink 文件失败.",
        21 to "FTP 命令执行失败.",
        22 to "HTTP 返回头无效或无法识别.",
        23 to "指定地址重定向过多.",
        24 to "HTTP 认证失败.",
        25 to "解析种子文件失败.",
        26 to "指定 \".torrent\" 种子文件已经损坏或缺少 aria2 需要的信息.",
        27 to "指定磁链地址无效.",
        28 to "设置错误.",
        29 to "远程服务器繁忙, 无法处理当前请求.",
        30 to "处理 RPC 请求失败.",
        32 to "文件校验失败.",
    )

    @JvmStatic
    fun getError(code: Int): String {
        return errors[code] ?: "未知错误."
    }
}