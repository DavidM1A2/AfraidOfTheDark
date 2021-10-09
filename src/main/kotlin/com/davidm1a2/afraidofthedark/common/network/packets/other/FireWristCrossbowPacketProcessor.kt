package com.davidm1a2.afraidofthedark.common.network.packets.other

import com.davidm1a2.afraidofthedark.common.capabilities.getResearch
import com.davidm1a2.afraidofthedark.common.constants.ModSounds
import com.davidm1a2.afraidofthedark.common.item.core.AOTDBoltItem
import com.davidm1a2.afraidofthedark.common.network.handler.PacketProcessor
import net.minecraft.inventory.ItemStackHelper
import net.minecraft.network.PacketBuffer
import net.minecraft.util.SoundCategory
import net.minecraftforge.fml.network.NetworkDirection
import net.minecraftforge.fml.network.NetworkEvent
import net.minecraftforge.registries.ForgeRegistries

/**
 * Packet that tells the server that the client is ready to fire a wrist crossbow bolt
 */
class FireWristCrossbowPacketProcessor : PacketProcessor<FireWristCrossbowPacket> {
    override fun encode(msg: FireWristCrossbowPacket, buf: PacketBuffer) {
        buf.writeResourceLocation(msg.boltItem.registryName!!)
    }

    override fun decode(buf: PacketBuffer): FireWristCrossbowPacket {
        return FireWristCrossbowPacket(ForgeRegistries.ITEMS.getValue(buf.readResourceLocation())!! as AOTDBoltItem)
    }

    override fun process(msg: FireWristCrossbowPacket, ctx: NetworkEvent.Context) {
        if (ctx.direction == NetworkDirection.PLAY_TO_SERVER) {
            val player = ctx.sender!!
            // Only fire a bolt if the player is in creative or has the right bolt item
            if (player.isCreative || ItemStackHelper.clearOrCountMatchingItems(player.inventory, { it.item == msg.boltItem.item }, 1, false) == 1) {
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
                val bolt = msg.boltItem.createBolt(world)
                val hasResearch = msg.boltItem.requiredResearch?.let { player.getResearch().isResearched(it) } ?: true
                bolt.setShotFromCrossbow(true)
                bolt.initUsingShooter(player, hasResearch)
                world.addFreshEntity(bolt)
            }
        }
    }
}