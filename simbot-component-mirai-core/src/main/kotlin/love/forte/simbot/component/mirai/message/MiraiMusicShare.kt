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

import kotlinx.serialization.*
import love.forte.simbot.*
import love.forte.simbot.component.mirai.*
import love.forte.simbot.message.*
import love.forte.simbot.message.Message
import net.mamoe.mirai.message.data.*
import kotlin.reflect.*

/**
 * Mirai中的音乐分享模板实例。
 * 仅用于发送，不会出现在接收的消息中。
 *
 * 你可以直接使用 [MusicShare] 对象作为参数，
 * 或者通过其他两个参数与 [MusicShare] 的构造完全一致的构造来构造这个对象。
 *
 * 其他注意事项请以 [MusicShare] 说明为准。
 *
 * @see MusicShare
 *
 * @author ForteScarlet
 */
@SerialName("mirai.musicShare")
@Serializable
public data class MiraiMusicShare(
    override val originalMiraiMessage: MusicShare
) : MiraiSendOnlySimbotMessage<MiraiMusicShare>,
    OriginalMiraiDirectlySimbotMessage<MusicShare, MiraiMusicShare> {

    override val key: Message.Key<MiraiMusicShare> get() = Key

    /**
     * 参数完全来自 [MusicShare] 的 **主构造**，具体使用请参考 [MusicShare].
     *
     *
     * @param kind 音乐应用类型
     * @param title 消息卡片标题. 例如 `"ファッション"`
     * @param summary 消息卡片内容. 例如 `"rinahamu/Yunomi"`
     * @param jumpUrl 点击卡片跳转网页 URL. 例如 `"http://music.163.com/song/1338728297/?userid=324076307"`
     * @param pictureUrl 消息卡片图片 URL. 例如 `"http://p2.music.126.net/y19E5SadGUmSR8SZxkrNtw==/109951163785855539.jpg"`
     * @param musicUrl 音乐文件 URL. 例如 `"http://music.163.com/song/media/outer/url?id=1338728297&userid=324076307"`
     * @param brief 在消息列表显示. 例如 `"[分享]ファッション"`
     */
    @Suppress("KDocUnresolvedReference")
    public constructor(
        kind: MusicKind, // 'type' is reserved by serialization
        title: String,
        summary: String,
        jumpUrl: String,
        pictureUrl: String,
        musicUrl: String,
        brief: String,
    ) : this(
        MusicShare(kind, title, summary, jumpUrl, pictureUrl, musicUrl, brief)
    )

    /**
     * 参数完全来自 [MusicShare] 构造，具体使用请参考 [MusicShare].
     *
     *
     * @param kind 音乐应用类型
     * @param title 消息卡片标题. 例如 `"ファッション"`
     * @param summary 消息卡片内容. 例如 `"rinahamu/Yunomi"`
     * @param jumpUrl 点击卡片跳转网页 URL. 例如 `"http://music.163.com/song/1338728297/?userid=324076307"`
     * @param pictureUrl 消息卡片图片 URL. 例如 `"http://p2.music.126.net/y19E5SadGUmSR8SZxkrNtw==/109951163785855539.jpg"`
     * @param musicUrl 音乐文件 URL. 例如 `"http://music.163.com/song/media/outer/url?id=1338728297&userid=324076307"`
     */
    public constructor(
        kind: MusicKind,
        title: String,
        summary: String,
        jumpUrl: String,
        pictureUrl: String,
        musicUrl: String,
    ) : this(
        MusicShare(kind, title, summary, jumpUrl, pictureUrl, musicUrl)
    )

    public companion object Key : Message.Key<MiraiMusicShare> {
        override fun safeCast(value: Any): MiraiMusicShare? = doSafeCast(value)
    }
}
