/*
 *  Copyright (c) 2021-2021 ForteScarlet <https://github.com/ForteScarlet>
 *
 *  根据 Apache License 2.0 获得许可；
 *  除非遵守许可，否则您不得使用此文件。
 *  您可以在以下网址获取许可证副本：
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 *   有关许可证下的权限和限制的具体语言，请参见许可证。
 */


abstract class SimbotProject {
    abstract val group: String
    abstract val version: String
}


/**
 * Project versions.
 */
sealed class P : SimbotProject() {
    object Simbot {
        const val GROUP = "love.forte.simbot"
        const val VERSION = "3.0.0.preview.1.0"
    }

    object Simboot {
        const val GROUP = "love.forte.simbot.boot"
        const val VERSION = Simbot.VERSION
    }

    object ComponentMirai {
        const val GROUP = "${Simbot.GROUP}.component" // love.forte.simbot.component
        val VERSION = "${Simbot.VERSION}-${V.Mirai.VERSION_SIM}.0.1"

        const val API = ":simbot-component-mirai-api"
        const val CORE = ":simbot-component-mirai-core"
        const val BOOT = ":simbot-component-mirai-boot"
    }

}



