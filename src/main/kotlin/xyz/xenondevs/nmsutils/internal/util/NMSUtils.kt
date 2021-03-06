package xyz.xenondevs.nmsutils.internal.util

import io.netty.channel.ChannelFuture
import net.md_5.bungee.api.chat.BaseComponent
import net.md_5.bungee.chat.ComponentSerializer
import net.minecraft.core.Registry
import net.minecraft.network.chat.Component
import net.minecraft.network.protocol.Packet
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.MinecraftServer
import net.minecraft.server.level.ServerPlayer
import net.minecraft.server.network.ServerGamePacketListenerImpl
import net.minecraft.tags.TagKey
import net.minecraft.world.effect.MobEffect
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.item.Item
import net.minecraft.world.item.alchemy.Potion
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import org.bukkit.*
import org.bukkit.craftbukkit.v1_19_R1.CraftServer
import org.bukkit.craftbukkit.v1_19_R1.entity.CraftPlayer
import org.bukkit.craftbukkit.v1_19_R1.inventory.CraftItemStack
import org.bukkit.craftbukkit.v1_19_R1.potion.CraftPotionUtil
import org.bukkit.craftbukkit.v1_19_R1.util.CraftChatMessage
import org.bukkit.craftbukkit.v1_19_R1.util.CraftMagicNumbers
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

internal val DEDICATED_SERVER = (Bukkit.getServer() as CraftServer).server!!

internal val MinecraftServer.channels: List<ChannelFuture>
    get() = ReflectionRegistry.SERVER_CONNECTION_LISTENER_CHANNELS_FIELD.get(this.connection) as List<ChannelFuture>

internal val ItemStack.nmsStack: net.minecraft.world.item.ItemStack
    get() = CraftItemStack.asNMSCopy(this)

internal val net.minecraft.world.item.ItemStack.bukkitStack: ItemStack
    get() = CraftItemStack.asBukkitCopy(this)

internal val NamespacedKey.resourceLocation: ResourceLocation
    get() = ResourceLocation(toString())

internal val String.resourceLocation: ResourceLocation?
    get() = ResourceLocation.tryParse(this)

@Suppress("DEPRECATION")
internal val PotionEffectType.mobEffect: MobEffect
    get() = MobEffect.byId(id)!!

internal val PotionEffect.mobEffectInstance: MobEffectInstance
    get() = CraftPotionUtil.fromBukkit(this)

internal val PotionEffect.potion: Potion
    get() = Potion(mobEffectInstance)

internal val EntityType.nmsType: net.minecraft.world.entity.EntityType<*>
    get() = net.minecraft.world.entity.EntityType.byString(key.toString()).get()

internal val Tag<*>.tagKey: TagKey<*>
    get() = ReflectionRegistry.CRAFT_TAG_TAG_KEY_FIELD.get(this) as TagKey<*>

internal val Material.nmsItem: Item
    get() = CraftMagicNumbers.getItem(this)

internal val Material.nmsBlock: Block
    get() = CraftMagicNumbers.getBlock(this)

internal val World.resourceKey: ResourceKey<Level>
    get() = ResourceKey.create(Registry.DIMENSION_REGISTRY, name.resourceLocation!!)

internal val Player.serverPlayer: ServerPlayer
    get() = (this as CraftPlayer).handle

internal val Player.connection: ServerGamePacketListenerImpl
    get() = serverPlayer.connection

internal fun Player.send(vararg packets: Packet<*>) {
    val connection = connection
    packets.forEach { connection.send(it) }
}

internal fun Component.toBaseComponentArray(): Array<BaseComponent> {
    try {
        return ComponentSerializer.parse(CraftChatMessage.toJSON(this))
    } catch (e: Exception) {
        throw IllegalArgumentException("Could not convert to BaseComponent array: $this", e)
    }
}

internal fun Array<out BaseComponent>.toComponent(): Component {
    if (isEmpty()) return Component.empty()
    
    try {
        return CraftChatMessage.fromJSON(ComponentSerializer.toString(this))
    } catch (e: Exception) {
        throw IllegalArgumentException("Could not convert to Component: ${this.contentToString()}", e)
    }
}