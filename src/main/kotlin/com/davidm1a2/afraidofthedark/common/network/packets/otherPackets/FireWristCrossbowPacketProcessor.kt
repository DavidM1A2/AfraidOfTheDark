package com.davidm1a2.afraidofthedark.common.network.packets.otherPackets

import com.davidm1a2.afraidofthedark.common.constants.ModRegistries
import com.davidm1a2.afraidofthedark.common.constants.ModSounds
import com.davidm1a2.afraidofthedark.common.network.packets.packetHandler.PacketProcessor
import net.minecraft.inventory.ItemStackHelper
import net.minecraft.network.PacketBuffer
import net.minecraft.util.SoundCategory
import net.minecraftforge.fml.network.NetworkDirection
import net.minecraftforge.fml.network.NetworkEvent

/**
 * Packet that tells the server that the client is ready to fire a wrist crossbow bolt
 */
class FireWristCrossbowPacketProcessor : PacketProcessor<FireWristCrossbowPacket> {
    override fun encode(msg: FireWristCrossbowPacket, buf: PacketBuffer) {
        buf.writeResourceLocation(msg.selectedBolt.registryName!!)
    }

    override fun decode(buf: PacketBuffer): FireWristCrossbowPacket {
        return FireWristCrossbowPacket(ModRegistries.BOLTS.getValue(buf.readResourceLocation())!!)
    }

    override fun process(msg: FireWristCrossbowPacket, ctx: NetworkEvent.Context) {
        if (ctx.direction == NetworkDirection.PLAY_TO_SERVER) {
            val player = ctx.sender!!
            // Only fire a bolt if the player is in creative or has the right bolt item
            if (player.isCreative || ItemStackHelper.clearOrCountMatchingItems(player.inventory, { it.item == msg.selectedBolt.item }, 1, false) == 1) {
                val world = player.level

                // Play a fire sound effect
                world.playSound(
                    null,
                    player.blockPosition(),
                    ModSounds.CROSSBOW_FIRE,
                    SoundCategory.PLAYERS,
                    0.5f,
                    world.random.nextFloat() * 0.4f + 0.8f
                )

                // Instantiate bolt!
                val bolt = msg.selectedBolt.boltEntityFactory(world)
                bolt.setShotFrom(player)
                world.addFreshEntity(bolt)
            }
        }
    }
}