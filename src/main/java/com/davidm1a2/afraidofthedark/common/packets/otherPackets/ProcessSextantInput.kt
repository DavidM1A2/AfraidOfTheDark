package com.davidm1a2.afraidofthedark.common.packets.otherPackets

import com.davidm1a2.afraidofthedark.AfraidOfTheDark
import com.davidm1a2.afraidofthedark.common.constants.ModBlocks
import com.davidm1a2.afraidofthedark.common.constants.ModCapabilities
import com.davidm1a2.afraidofthedark.common.packets.packetHandler.MessageHandler
import com.davidm1a2.afraidofthedark.common.registry.meteor.MeteorEntry
import io.netty.buffer.ByteBuf
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.Blocks
import net.minecraft.util.math.BlockPos
import net.minecraft.util.text.TextComponentTranslation
import net.minecraft.world.World
import net.minecraftforge.fml.common.network.simpleimpl.IMessage
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext
import org.apache.commons.lang3.RandomUtils
import kotlin.random.Random

/**
 * Packet sent from client to server to tell the server to validate sextant information and tell us where a meteor landed
 *
 * @property dropAngle The first sextant field required
 * @property latitude The second sextant field required
 * @property longitude The third sextant field required
 */
class ProcessSextantInput : IMessage
{
    private var dropAngle: Int
    private var latitude: Int
    private var longitude: Int

    /**
     * Default constructor is required but not used
     */
    constructor()
    {
        dropAngle = -1
        latitude = -1
        longitude = -1
    }

    /**
     * Overloaded constructor that initializes fields
     *
     * @param dropAngle The angle the meteor dropped at
     * @param latitude  The latitude the meteor was dropping to
     * @param longitude The longitude the meteor was dropping to
     */
    constructor(dropAngle: Int, latitude: Int, longitude: Int)
    {
        this.dropAngle = dropAngle
        this.latitude = latitude
        this.longitude = longitude
    }

    /**
     * Converts from raw bytes to structured data
     *
     * @param buf The buffer to read from
     */
    override fun fromBytes(buf: ByteBuf)
    {
        dropAngle = buf.readInt()
        latitude = buf.readInt()
        longitude = buf.readInt()
    }

    /**
     * Converts from structured data to raw bytes
     *
     * @param buf The buffer to write to
     */
    override fun toBytes(buf: ByteBuf)
    {
        buf.writeInt(dropAngle)
        buf.writeInt(latitude)
        buf.writeInt(longitude)
    }

    /**
     * Handler handles ProcessSextantInput packets on the server side
     */
    class Handler : MessageHandler.Server<ProcessSextantInput>()
    {
        /**
         * Handles the message on the server side
         *
         * @param player the player reference (the player who sent the packet)
         * @param msg    the message received
         * @param ctx    The context that the message was sent through
         */
        override fun handleServerMessage(player: EntityPlayer, msg: ProcessSextantInput, ctx: MessageContext)
        {
            // First validate that the player entered the correct values into the sextant
            val playerBasics = player.getCapability(ModCapabilities.PLAYER_BASICS, null)!!
            if (playerBasics.getWatchedMeteorDropAngle() == msg.dropAngle &&
                playerBasics.getWatchedMeteorLatitude() == msg.latitude &&
                playerBasics.getWatchedMeteorLongitude() == msg.longitude
            )
            {
                val meteorEntry = playerBasics.getWatchedMeteor()

                if (meteorEntry != null)
                {
                    // The meteor can drop from 15 - 500 blocks away from the player in both directions
                    var xLocOfDrop = player.position.x +
                            (if (Random.nextBoolean()) -1 else 1) * (Random.nextInt(MAX_METEOR_DISTANCE - 15) + 15)
                    var zLocOfDrop = player.position.z +
                            (if (Random.nextBoolean()) -1 else 1) * (Random.nextInt(MAX_METEOR_DISTANCE - 15) + 15)

                    // Drop the meteor
                    dropMeteor(player.world, meteorEntry, xLocOfDrop, zLocOfDrop)

                    // Print out a message telling the player where the meteor dropped +/- 8 blocks
                    xLocOfDrop =
                        xLocOfDrop + (if (Random.nextBoolean()) -1 else 1) * Random.nextInt(ACCURACY + 1)
                    zLocOfDrop =
                        zLocOfDrop + (if (Random.nextBoolean()) -1 else 1) * Random.nextInt(ACCURACY + 1)

                    player.sendMessage(TextComponentTranslation("aotd.meteor.location", xLocOfDrop, zLocOfDrop))

                    // Clear the player's watched meteors so that the same meteor can't be used twice
                    playerBasics.setWatchedMeteor(null, -1, -1, -1)
                    playerBasics.syncWatchedMeteor(player)
                }
                else
                {
                    AfraidOfTheDark.INSTANCE.logger.error("Player ${player.gameProfile.name} had null meteor entry")
                }

            }
            // The values aren't correct so show an error
            else
            {
                player.sendMessage(TextComponentTranslation("aotd.meteor.process.invalid_vals"))
            }
        }

        /**
         * Spawns a meteor at a given x,y position
         *
         * @param world       The world the meteor spawned in
         * @param meteorEntry The meteor type to spawn
         * @param xPos        The X position the meteor is at
         * @param zPos        The Z position the meteor is at
         */
        private fun dropMeteor(world: World, meteorEntry: MeteorEntry, xPos: Int, zPos: Int)
        {
            // We need to compute the y position, start at the top and work our way down
            var yPos = 255
            // Grab the top block
            var block = world.getBlockState(BlockPos(xPos, yPos, zPos)).block

            // Iterate while we are above bedrock on an air/water/lava block. Work our way down
            while (yPos > 0 && INVALID_TOP_BLOCKS.contains(block))
            {
                yPos--
                block = world.getBlockState(BlockPos(xPos, yPos, zPos)).block
            }

            AfraidOfTheDark.INSTANCE.logger.info("Meteor dropped at $xPos, $yPos, $zPos")

            // If we have a valid y-pos generate the meteor
            if (yPos > 0)
            {
                // Compute the meteor's radius, it will be based on the parameters of the meteor entry
                val radius = RandomUtils.nextInt(meteorEntry.minMeteorRadius, meteorEntry.maxMeteorRadius + 1)
                // Iterate over x, y, and z
                for (x in xPos - radius..xPos + radius)
                {
                    for (y in yPos - radius until yPos + radius)
                    {
                        for (z in zPos - radius..zPos + radius)
                        {
                            // Compute the distance from the center
                            val distanceFromCenter = (xPos - x) * (xPos - x) + (yPos - y) * (yPos - y) + ((zPos - z) * (zPos - z)).toDouble()
                            // If the distance is less than the radius^2 then it's inside the sphere
                            if (distanceFromCenter < radius * radius)
                            {
                                // Grab the block pos at the x,y,z coordinates
                                val blockPos = BlockPos(x, y, z)
                                // Grab the block present at these x,y,z coordinates
                                val existingBlock = world.getBlockState(blockPos).block
                                // If the block is replaceable, replace it
                                if (REPLACEABLE_BLOCKS.contains(existingBlock))
                                {
                                    // It's an exterior block so set it to meteor
                                    if (distanceFromCenter >= (radius - 1) * (radius - 1) || Math.random() > meteorEntry.richnessPercent)
                                    {
                                        world.setBlockState(blockPos, ModBlocks.METEOR.defaultState)
                                    }
                                    else
                                    {
                                        world.setBlockState(blockPos, meteorEntry.interiorBlock.defaultState)
                                    }
                                }
                            }
                        }
                    }
                }
            }
            else
            {
                AfraidOfTheDark.INSTANCE.logger.error("Could not find a suitable y-level for the meteor at $xPos, $yPos")
            }
        }

        companion object
        {
            // The maximum distance a meteor can fall away from the player
            private const val MAX_METEOR_DISTANCE = 500
            // The chance accuracy of the sextant used, the actual meteor will be within this many blocks of the coordinates
            private const val ACCURACY = 16
            // An invalid list of top level blocks that the meteor cant start on
            private val INVALID_TOP_BLOCKS = setOf(
                Blocks.AIR,
                Blocks.WATER,
                Blocks.FLOWING_WATER,
                Blocks.LAVA,
                Blocks.FLOWING_LAVA
            )
            // A list of blocks that the meteor is allowed to remove and spawn in
            private val REPLACEABLE_BLOCKS = setOf(
                ModBlocks.METEOR,
                Blocks.DIRT,
                Blocks.GRASS,
                Blocks.LEAVES,
                Blocks.LEAVES2,
                Blocks.SAND,
                Blocks.LOG,
                Blocks.LOG2,
                Blocks.VINE,
                Blocks.DEADBUSH,
                Blocks.DOUBLE_PLANT,
                Blocks.ICE,
                Blocks.AIR,
                Blocks.STONE,
                Blocks.GRAVEL,
                Blocks.SANDSTONE,
                Blocks.SNOW,
                Blocks.SNOW_LAYER,
                Blocks.HARDENED_CLAY,
                Blocks.CLAY,
                Blocks.WATER,
                Blocks.FLOWING_WATER,
                Blocks.LAVA,
                Blocks.FLOWING_LAVA,
                Blocks.GRASS_PATH
            )
        }
    }
}