/*
 *  Copyright (c) 2022-2022 ForteScarlet <ForteScarlet@163.com>
 *
 *  本文件是 simbot-component-mirai 的一部分。
 *
 *  simbot-component-mirai 是自由软件：你可以再分发之和/或依照由自由软件基金会发布的 GNU 通用公共许可证修改之，无论是版本 3 许可证，还是（按你的决定）任何以后版都可以。
 *
 *  发布 simbot-component-mirai 是希望它能有用，但是并无保障;甚至连可销售和符合某个特定的目的都不保证。请参看 GNU 通用公共许可证，了解详情。
 *
 *  你应该随程序获得一份 GNU 通用公共许可证的复本。如果没有，请看:
 *  https://www.gnu.org/licenses
 *  https://www.gnu.org/licenses/gpl-3.0-standalone.html
 *  https://www.gnu.org/licenses/lgpl-3.0-standalone.html
 *
 */

package love.forte.simbot.component.mirai.message

import love.forte.plugin.suspendtrans.annotation.JvmAsync
import love.forte.plugin.suspendtrans.annotation.JvmBlocking
import love.forte.simbot.ID
import love.forte.simbot.component.mirai.MiraiContactContainer
import love.forte.simbot.component.mirai.bot.MiraiBot
import love.forte.simbot.message.Image
import love.forte.simbot.message.Message
import love.forte.simbot.message.doSafeCast
import love.forte.simbot.resources.Resource
import love.forte.simbot.resources.Resource.Companion.toResource
import net.mamoe.mirai.contact.Contact
import net.mamoe.mirai.contact.Contact.Companion.uploadImage
import net.mamoe.mirai.message.data.Image.Key.queryUrl
import net.mamoe.mirai.message.data.ImageType
import net.mamoe.mirai.message.data.flash
import java.io.IOException
import java.net.URL
import net.mamoe.mirai.message.data.FlashImage as OriginalMiraiFlashImage
import net.mamoe.mirai.message.data.Image as OriginalMiraiImage


/**
 * 一个仅用于发送的临时 [OriginalMiraiImage] 类型，通过 [MiraiBot.uploadImage] 有可能会得到。
 * 不建议对其进行长久的序列化，因为其内部的 [resource] 中保存的内容很有可能是 **临时** 内容。
 *
 * [MiraiSendOnlyImage] 是通过 [Resource] 作为上传资源使用的，
 * 如果你希望仅通过一个ID来获取图片，[MiraiBot.idImage]。
 *
 * [MiraiSendOnlyImage] 是一个预处理对象，每次使用都会进行一次图片上传，其内部不会保存任何**具体的**图片消息对象。
 * 可以通过 [upload] 将当前图片消息上传为一个具体的图片对象。
 *
 * @see Resource
 * @see MiraiBot.uploadImage
 *
 */
public interface MiraiSendOnlyImage :
    MiraiSendOnlyComputableMessage<MiraiSendOnlyImage>,
    Image<MiraiSendOnlyImage> {
    
    /**
     * 作为仅用于发送的图片时的类型，[id] 的值为其内部资源的name而并非具体的图片ID。
     * 如果希望得到具体的图片信息，使用 [upload]。
     */
    override val id: ID
    
    /**
     * 是否作为一个闪照。
     */
    public val isFlash: Boolean
    
    /**
     * 图片发送所使用的资源对象。
     */
    @JvmSynthetic
    override suspend fun resource(): Resource
    
    /**
     * 将当前_预处理_图片消息对象通过 [contact] 上传为一个**具体**的图片消息对象。
     *
     * @see MiraiImage
     */
    @JvmBlocking
    @JvmAsync
    public suspend fun upload(contact: Contact): MiraiImage
    
    /**
     * 将当前的 [MiraiSendOnlyImage] 消息对象通过 [contactContainer.contact][MiraiContactContainer.originalContact] 上传为一个**具体**的图片消息对象。
     *
     * @see MiraiContactContainer
     * @see MiraiImage
     */
    @JvmBlocking
    @JvmAsync
    public suspend fun upload(contactContainer: MiraiContactContainer): MiraiImage =
        upload(contactContainer.originalContact)
    
    public companion object Key : Message.Key<MiraiSendOnlyImage> {
        override fun safeCast(value: Any): MiraiSendOnlyImage? = doSafeCast(value)
    
        /**
         * 通过 [resource] 得到一个 [MiraiSendOnlyImage] 实例。
         */
        @JvmStatic
        @JvmOverloads
        public fun of(resource: Resource, isFlash: Boolean = false): MiraiSendOnlyImage =
            MiraiSendOnlyImageImpl(resource, isFlash)
    }
}

/**
 * 将一个 [OriginalMiraiImage] 作为 simbot的 [love.forte.simbot.message.Image] 进行使用。
 *
 */
public interface MiraiImage :
    OriginalMiraiDirectlySimbotMessage<OriginalMiraiImage, MiraiImage>,
    Image<MiraiImage> {
    
    /**
     * 得到Mirai的原生图片类型 [OriginalMiraiImage].
     */
    public val originalImage: OriginalMiraiImage
    
    /**
     * 得到Mirai的原生图片类型 [OriginalMiraiImage]。同 [originalImage]。
     */
    override val originalMiraiMessage: OriginalMiraiImage
        get() = originalImage
    
    /**
     * 此图片是否为一个 `闪照`。
     * @see OriginalMiraiFlashImage
     *
     */
    public val isFlash: Boolean
    
    /**
     * 此图片的 `imageId`
     */
    override val id: ID
    
    /**
     * 图片的宽度 (px), 当无法获取时为 0
     * @see OriginalMiraiImage.width
     */
    public val width: Int get() = originalImage.width
    
    /**
     * 图片的高度 (px), 当无法获取时为 0
     * @see OriginalMiraiImage.height
     */
    public val height: Int get() = originalImage.height
    
    /**
     * 图片的大小（字节）, 当无法获取时为 0
     * @see OriginalMiraiImage.size
     */
    public val size: Long get() = originalImage.size
    
    /**
     * 图片的类型, 当无法获取时为未知 [ImageType.UNKNOWN]
     * @see OriginalMiraiImage.imageType
     */
    public val imageType: ImageType get() = originalImage.imageType
    
    /**
     * 判断该图片是否为 `动画表情`
     * @see OriginalMiraiImage.isEmoji
     */
    public val isEmoji: Boolean get() = originalImage.isEmoji
    
    /**
     * 图片文件 MD5.
     *
     * @see OriginalMiraiImage.md5
     */
    public val md5: ByteArray get() = originalImage.md5
    
    /**
     * 查询原图下载链接.
     * @see OriginalMiraiImage.queryUrl
     */
    @JvmBlocking
    @JvmAsync
    public suspend fun queryUrl(): String = originalImage.queryUrl()
    
    /**
     * 通过 [queryUrl] 查询并得到 [Resource] 对象。
     */
    @JvmSynthetic
    override suspend fun resource(): Resource = URL(queryUrl()).toResource()
    
    
    public companion object Key : Message.Key<MiraiImage> {
        
        @JvmStatic
        @JvmOverloads
        public fun of(nativeImage: OriginalMiraiImage, isFlash: Boolean = false): MiraiImage {
            return MiraiImageImpl(nativeImage, isFlash)
        }
        
        @JvmStatic
        @JvmOverloads
        public fun of(nativeImage: OriginalMiraiFlashImage, isFlash: Boolean = true): MiraiImage {
            return MiraiImageImpl(nativeImage.image, isFlash)
        }
        
        override fun safeCast(value: Any): MiraiImage? = doSafeCast(value)
    }
    
}

/**
 * 将 [Resource] 作为资源读取并上传到 [contact], 并得到一个图片结果。
 */
internal suspend fun Resource.uploadToImage(contact: Contact): net.mamoe.mirai.message.data.Image {
    return openStream().use { contact.uploadImage(it) }
}

/**
 * 将 [Resource] 作为资源读取并上传到 [contact], 并得到一个图片结果。
 */
@Throws(IOException::class)
internal suspend fun Resource.uploadToImage(contact: Contact, isFlash: Boolean): net.mamoe.mirai.message.data.Message {
    return uploadToImage(contact).let { image ->
        if (isFlash) image.flash() else image
    }
}


public fun OriginalMiraiImage.asSimbot(isFlash: Boolean = false): MiraiImage = MiraiImage.of(this, isFlash)
public fun OriginalMiraiFlashImage.asSimbot(isFlash: Boolean = true): MiraiImage = MiraiImage.of(this, isFlash)