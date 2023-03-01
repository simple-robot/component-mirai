/*
 *  Copyright (c) 2022-2023 ForteScarlet <ForteScarlet@163.com>
 *
 *  本文件是 simbot-component-mirai 的一部分。
 *
 *  simbot-component-mirai 是自由软件：你可以再分发之和/或依照由自由软件基金会发布的 GNU Affero通用公共许可证修改之，无论是版本 3 许可证，还是（按你的决定）任何以后版都可以。
 *
 *  发布 simbot-component-mirai 是希望它能有用，但是并无保障;甚至连可销售和符合某个特定的目的都不保证。请参看 GNU Affero通用公共许可证，了解详情。
 *
 *  你应该随程序获得一份 GNU Affero通用公共许可证的复本。如果没有，请看 <https://www.gnu.org/licenses/>。
 *
 *
 */

package love.forte.simbot.component.mirai.message

import love.forte.simbot.ID
import love.forte.simbot.component.mirai.ID
import love.forte.simbot.component.mirai.fullSerialID
import love.forte.simbot.message.MessageContent
import love.forte.simbot.message.Messages
import love.forte.simbot.message.toMessages
import love.forte.simbot.randomID
import net.mamoe.mirai.message.data.MessageChain
import net.mamoe.mirai.message.data.MessageSource
import net.mamoe.mirai.message.data.SingleMessage
import net.mamoe.mirai.message.data.sourceOrNull

/**
 *
 * 直接将一个 [MessageChain] 作为一个简单的 [MessageContent] 对象实现。
 *
 * @author ForteScarlet
 */
public open class MiraiMessageChainContent(
    final override val originalMessageChain: MessageChain,
    public val messageSourceOrNull: MessageSource? = originalMessageChain.sourceOrNull,
) : MessageContent(),
    MiraiMessageContent,
    MiraiMessageChainContainer {

    /**
     * 当前消息的ID。
     * 当 [originalMessageChain] 中的 [MessageSource] 存在时, 使用 [MessageSource.ID] 计算ID，
     * 当 [originalMessageChain] 中的 [MessageSource] 不存在时会使用 [randomID] 作为消息ID.
     */
    override val messageId: ID by lazy(LazyThreadSafetyMode.PUBLICATION) { messageSourceOrNull?.ID ?: randomID() }


    /**
     * 当前消息的完整ID。
     * 当 [originalMessageChain] 中的 [MessageSource] 存在时, 使用 [MessageSource.ID] 计算ID，
     * 当 [originalMessageChain] 中的 [MessageSource] 不存在时与 [messageId] 一致。
     */
    override val fullMessageId: ID by lazy(LazyThreadSafetyMode.PUBLICATION) { messageSourceOrNull?.fullSerialID ?: messageId }

    /**
     * 消息链。
     *
     * 消息链中不追加source(如果存在的话)。
     */
    override val messages: Messages by lazy(
        LazyThreadSafetyMode.PUBLICATION,
        originalMessageChain.filter { it !is MessageSource }.map(SingleMessage::asSimbotMessage)::toMessages
    )
}
