package com.davidm1a2.afraidofthedark.common.packets.otherPackets

import com.davidm1a2.afraidofthedark.common.constants.ModRegistries
import com.davidm1a2.afraidofthedark.common.constants.ModSounds
import com.davidm1a2.afraidofthedark.common.packets.packetHandler.MessageHandler
import com.davidm1a2.afraidofthedark.common.registry.bolt.BoltEntry
import io.netty.buffer.ByteBuf
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.util.ResourceLocation
import net.minecraft.util.SoundCategory
import net.minecraftforge.fml.common.network.ByteBufUtils
import net.minecraftforge.fml.common.network.simpleimpl.IMessage
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext

/**
 * Packet that tells the server that the client is ready to fire a wrist crossbow bolt
 *
 * @property selectedBolt The bolt to fire
 */
class FireWristCrossbow : IMessage
{
    private lateinit var selectedBolt: BoltEntry

    /**
     * Default constructor is required but not used
     */
    constructor()

    /**
     * Overloaded constructor takes the bolt as an argument
     *
     * @param selectedBolt The bolt to fire
     */
    constructor(selectedBolt: BoltEntry)
    {
        this.selectedBolt = selectedBolt
    }

    /**
     * Reads the bolt type from the buffer
     *
     * @param buf The buffer to read from
     */
    override fun fromBytes(buf: ByteBuf)
    {
        selectedBolt = ModRegistries.BOLTS.getValue(ResourceLocation(ByteBufUtils.readUTF8String(buf)))!!
    }

    /**
     * Writes the bolt type into the buffer
     *
     * @param buf The buffer to write to
     */
    override fun toBytes(buf: ByteBuf)
    {
        ByteBufUtils.writeUTF8String(buf, selectedBolt.registryName.toString())
    }

    /**
     * Handler class for wrist fire crossbow messages sent from the client
     */
    class Handler : MessageHandler.Server<FireWristCrossbow>()
    {
        /**
         * Called when the server receives the packet
         *
         * @param entityPlayer The player that sent the message
         * @param msg          the message received
         * @param ctx          The message's context
         */
        override fun handleServerMessage(entityPlayer: EntityPlayer, msg: FireWristCrossbow, ctx: MessageContext)
        {
            // Only fire a bolt if the player is in creative or has the right bolt item
            if (entityPlayer.capabilities.isCreativeMode || entityPlayer.inventory.clearMatchingItems(msg.selectedBolt.boltItem, -1, 1, null) == 1)
            {
                val world = entityPlayer.world

                // Play a fire sound effect
                world.playSound(null, entityPlayer.position, ModSounds.CROSSBOW_FIRE, SoundCategory.PLAYERS, 0.5f, world.rand.nextFloat() * 0.4f + 0.8f)

                // Instantiate bolt!
                val bolt = msg.selectedBolt.boltEntityFactory.apply(world, entityPlayer)

                // Aim and fire the bolt
                bolt.shoot(entityPlayer, entityPlayer.rotationPitch, entityPlayer.rotationYaw, 0f, 3f, 0f)

                // Push the bolt slightly forward so it does not collide with the player
                bolt.posX = bolt.posX + bolt.motionX * 0.4
                bolt.posY = bolt.posY + bolt.motionY * 0.4
                bolt.posZ = bolt.posZ + bolt.motionZ * 0.4
                world.spawnEntity(bolt)
            }
        }
    }
}