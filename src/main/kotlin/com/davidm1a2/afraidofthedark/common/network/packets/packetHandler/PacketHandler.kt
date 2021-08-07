package com.davidm1a2.afraidofthedark.common.network.packets.packetHandler

import com.davidm1a2.afraidofthedark.common.constants.Constants
import net.minecraft.entity.Entity
import net.minecraft.entity.player.ServerPlayerEntity
import net.minecraft.util.RegistryKey
import net.minecraft.util.ResourceLocation
import net.minecraft.world.World
import net.minecraftforge.fml.network.NetworkRegistry
import net.minecraftforge.fml.network.PacketDistributor

/**
 * Miner's basic packet handler updated
 *
 * @constructor Instantiates a new packet handler.
 * @property nextPacketID The ID for the next packet registration
 * @property wrapper The internal network wrapper.
 */
class PacketHandler {
    private var nextPacketID = 0
    private val wrapper = NetworkRegistry.newSimpleChannel(
        ResourceLocation(Constants.MOD_ID, "main"),
        { PROTOCOL_VERSION },
        { it == PROTOCOL_VERSION },
        { it == PROTOCOL_VERSION }
    )

    /**
     * Register an IMessage packet with it's corresponding message handler.
     *
     * @param packetClass    the packet's class that should be registered.
     * @param packetHandler the message handler for this packet type.
     */
    fun <C, H : PacketProcessor<C>> registerPacket(packetClass: Class<C>, packetHandler: H) {
        @Suppress("INACCESSIBLE_TYPE")
        wrapper.registerMessage(
            nextPacketID++,
            packetClass,
            { msg, buf -> packetHandler.encode(msg, buf) },
            { packetHandler.decode(it) },
            { msg, ctx ->
                val context = ctx.get()
                if (packetHandler.processAsync()) {
                    context.enqueueWork {
                        packetHandler.process(msg, context)
                    }
                } else {
                    packetHandler.process(msg, context)
                }
                context.packetHandled = true
            }
        )
    }

    /**
     * Sends the given packet to every client.
     *
     * @param packet the packet to send.
     */
    fun <C> sendToAll(packet: C) {
        sendRaw(packet, PacketDistributor.ALL.noArg())
    }

    /**
     * Sends the given packet to the given player.
     *
     * @param packet the packet to send.
     * @param player  the player to send the packet to.
     */
    fun <C> sendTo(packet: C, player: ServerPlayerEntity) {
        sendRaw(packet, PacketDistributor.PLAYER.with { player })
    }

    /**
     * Sends the given packet to all players around the given target point.
     *
     * @param packet the packet to send.
     * @param point   the target point.
     */
    fun <C> sendToAllAround(packet: C, point: PacketDistributor.TargetPoint) {
        sendRaw(packet, PacketDistributor.NEAR.with { point })
    }

    /**
     * Sends the given packet to all players within the radius around the given coordinates.
     *
     * @param packet   the packet to send.
     * @param dimension the dimension.
     * @param x         the x coordinate.
     * @param y         the y coordinate.
     * @param z         the z coordinate.
     * @param range     the radius.
     */
    fun <C> sendToAllAround(packet: C, dimension: RegistryKey<World>, x: Double, y: Double, z: Double, range: Double) {
        this.sendToAllAround(packet, PacketDistributor.TargetPoint(x, y, z, range, dimension))
    }

    /**
     * Sends the given packet to all players within the radius around the given entity.
     *
     * @param packet the packet to send.
     * @param entity  the entity.
     * @param range   the radius.
     */
    fun <C> sendToAllAround(packet: C, entity: Entity, range: Double) {
        this.sendToAllAround(packet, PacketDistributor.TargetPoint(entity.x, entity.y, entity.z, range, entity.level.dimension()))
    }

    /**
     * Sends the given packet to every player in the given dimension.
     *
     * @param packet     the packet to send.
     * @param dimensionType the dimension to send the packet to.
     */
    fun <C> sendToDimension(packet: C, dimensionType: RegistryKey<World>) {
        sendRaw(packet, PacketDistributor.DIMENSION.with { dimensionType })
    }

    /**
     * Sends the given packet to the server.
     *
     * @param packet the packet to send.
     */
    fun <C> sendToServer(packet: C) {
        wrapper.sendToServer(packet)
    }

    /**
     * Sends the given packet
     *
     * @param packet The packet to send
     * @param packetDistributor The distributor to use
     */
    fun <C> sendRaw(packet: C, packetDistributor: PacketDistributor.PacketTarget) {
        wrapper.send(packetDistributor, packet)
    }

    companion object {
        private const val PROTOCOL_VERSION = "1"
    }
}