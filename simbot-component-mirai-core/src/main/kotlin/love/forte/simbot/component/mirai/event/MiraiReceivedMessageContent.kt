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

package love.forte.simbot.component.mirai.event

import love.forte.simbot.ID
import love.forte.simbot.SimbotIllegalStateException
import love.forte.simbot.component.mirai.message.MiraiMessageChainContainer
import love.forte.simbot.component.mirai.message.MiraiMessageChainContent
import love.forte.simbot.component.mirai.message.MiraiMessageContent
import love.forte.simbot.message.Messages
import love.forte.simbot.message.ReceivedMessageContent
import net.mamoe.mirai.contact.PermissionDeniedException
import net.mamoe.mirai.event.events.MessagePostSendEvent
import net.mamoe.mirai.message.data.MessageChain
import net.mamoe.mirai.message.data.MessageSource
import net.mamoe.mirai.message.data.MessageSource.Key.recall
import net.mamoe.mirai.message.data.sourceOrNull
import net.mamoe.mirai.event.events.MessageEvent as OriginalMiraiMessageEvent


/**
 * 通过 [MessageChain] 将Mirai的消息链解析为simbot的 [ReceivedMessageContent].
 */
@Suppress("MemberVisibilityCanBePrivate")
public open class MiraiReceivedMessageContent internal constructor(
    originalMessageChain: MessageChain,
    
    /**
     * 消息源，即此消息的Mirai原生对象。
     *
     * 当此消息对象为通过 [MessagePostSendEvent] 事件而得到的时候，此属性有概率不存在。
     * 当此属性不存在时，当前消息将无法被 [删除][MiraiReceivedMessageContent.delete].
     */
    messageSourceOrNull: MessageSource?,
) : ReceivedMessageContent(), MiraiMessageContent, MiraiMessageChainContainer {
    private val delegateContent = MiraiMessageChainContent(originalMessageChain, messageSourceOrNull)
    
    override val originalMessageChain: MessageChain
        get() = delegateContent.originalMessageChain
    
    /**
     * 当前消息中的消息源。
     */
    public val messageSourceOrNull: MessageSource?
        get() = delegateContent.messageSourceOrNull
    
    /**
     * 此消息中的 [MessageSource]. 不一定100%能够获取：当此消息对象为通过 [MessagePostSendEvent] 事件而得到的时候，
     * [messageSource] 有概率不存在。当 [messageSource] 不存在的时候获取此属性将会抛出 [IllegalStateException].
     *
     * 如果你想在正确处理 `null` 的情况下使用，请使用 [messageSourceOrNull].
     *
     * @throws IllegalStateException 当消息内的 [messageSource] 实际不存在的时候。
     *
     */
    public val messageSource: MessageSource
        get() = delegateContent.messageSourceOrNull
            ?: throw SimbotIllegalStateException("No 'MessageSource' in current message content.")
    
    /**
     * 消息链。
     *
     * 消息链中不追加source. 如果需要, 使用 [originalMessageChain] 或者 [messageSource]。
     */
    override val messages: Messages
        get() = delegateContent.messages
    
    /**
     * mirai接收到的消息的ID。
     *
     * @see MiraiMessageChainContent.messageId
     */
    override val messageId: ID
        get() = delegateContent.messageId

    /**
     * mirai接收到的消息的完整ID。
     *
     * @see MiraiMessageChainContent.fullMessageId
     */
    override val fullMessageId: ID
        get() = delegateContent.fullMessageId

    /**
     * 尝试撤回此消息。
     *
     * @throws PermissionDeniedException see [MessageSource.recall]
     * @throws IllegalStateException see [messageSource]、[MessageSource.recall]
     *
     */
    override suspend fun delete(): Boolean {
        messageSource.recall()
        return true
    }
    
    override fun toString(): String =
        "MiraiReceivedMessageContent(content=$originalMessageChain, messageSource=$messageSourceOrNull)"
}

internal fun MessageChain.toSimbotMessageContent(messageSource: MessageSource? = this.sourceOrNull): MiraiReceivedMessageContent =
    MiraiReceivedMessageContent(this, messageSource)

internal fun OriginalMiraiMessageEvent.toSimbotMessageContent(): MiraiReceivedMessageContent =
    this.message.toSimbotMessageContent()






