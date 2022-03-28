package top.imaikuai

import net.mamoe.mirai.console.data.AutoSavePluginConfig
import net.mamoe.mirai.console.data.value
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescription
import net.mamoe.mirai.console.plugin.jvm.KotlinPlugin
import net.mamoe.mirai.event.GlobalEventChannel
import net.mamoe.mirai.event.events.GroupMessageEvent
import net.mamoe.mirai.message.data.At
import net.mamoe.mirai.utils.info
import tax.cute.minecraftserverping.MCPing


object ServerInfo : KotlinPlugin(
    JvmPluginDescription(
        id = "top.imaikuai.ServerInfo",
        name = "Minecraft Server Info",
        version = "1.0-SNAPSHOT",
    )
) {
    object ServerInfo : AutoSavePluginConfig("ServerInfo"){
        val host by value("127.0.0.1")
        val port by value(25565)
        val groupid by value("123456789")
        val command by value("服务器信息")
    }
    override fun onEnable() {
        logger.info { "Minecraft Server Info loaded" }
        ServerInfo.reload()
        GlobalEventChannel.subscribeAlways<GroupMessageEvent> { event ->
            if (sender.group.id.toString() == ServerInfo.groupid && message.contentToString() == ServerInfo.command) {
                val host = ServerInfo.host
                val port = ServerInfo.port
                val ping: MCPing = MCPing.getMotd(host, port)
                val at = At(event.sender.id)
                subject.sendMessage(at + ("服务器的在线人数为：" + ping.online_players + "/" + ping.max_players))
            }
        }
    }
}
