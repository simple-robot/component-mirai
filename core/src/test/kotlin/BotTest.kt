import love.forte.simbot.component.mirai.miraiBotManager
import love.forte.simbot.core.event.coreListenerManager


suspend fun main() {
    // LoggerFactory.getLogger("a").info("a")
    // LoggerFactory.getLogger("a").debug("b")
     val manager = coreListenerManager {  }
     val botManager = miraiBotManager(manager)
     val bot = botManager.register(2370606773, "LiChengYang9983.")

     bot.start()

     bot.friends()

     bot.join()
}