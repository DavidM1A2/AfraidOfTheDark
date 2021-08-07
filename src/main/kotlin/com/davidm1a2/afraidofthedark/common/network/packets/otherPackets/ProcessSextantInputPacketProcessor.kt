package com.davidm1a2.afraidofthedark.common.network.packets.otherPackets

import com.davidm1a2.afraidofthedark.common.capabilities.getBasics
import com.davidm1a2.afraidofthedark.common.constants.ModBlocks
import com.davidm1a2.afraidofthedark.common.network.packets.packetHandler.PacketProcessor
import com.davidm1a2.afraidofthedark.common.registry.meteor.MeteorEntry
import net.minecraft.block.Blocks
import net.minecraft.network.PacketBuffer
import net.minecraft.util.math.BlockPos
import net.minecraft.util.text.TranslationTextComponent
import net.minecraft.world.World
import net.minecraftforge.fml.network.NetworkDirection
import net.minecraftforge.fml.network.NetworkEvent
import org.apache.logging.log4j.LogManager
import kotlin.math.roundToInt
import kotlin.random.Random

/**
 * Packet sent from client to server to tell the server to validate sextant information and tell us where a meteor landed
 */
class ProcessSextantInputPacketProcessor : PacketProcessor<ProcessSextantInputPacket> {
    override fun encode(msg: ProcessSextantInputPacket, buf: PacketBuffer) {
        buf.writeInt(msg.dropAngle)
        buf.writeInt(msg.latitude)
        buf.writeInt(msg.longitude)
    }

    override fun decode(buf: PacketBuffer): ProcessSextantInputPacket {
        return ProcessSextantInputPacket(
            buf.readInt(),
            buf.readInt(),
            buf.readInt()
        )
    }

    override fun process(msg: ProcessSextantInputPacket, ctx: NetworkEvent.Context) {
        if (ctx.direction == NetworkDirection.PLAY_TO_SERVER) {
            val player = ctx.sender!!
            // First validate that the player entered the correct values into the sextant
            val playerBasics = player.getBasics()
            if (playerBasics.getWatchedMeteorDropAngle() == msg.dropAngle &&
                playerBasics.getWatchedMeteorLatitude() == msg.latitude &&
                playerBasics.getWatchedMeteorLongitude() == msg.longitude
            ) {
                val meteorEntry = playerBasics.getWatchedMeteor()

                if (meteorEntry != null) {
                    // The meteor can drop from 15 - 500 blocks away from the player in both directions
                    var xLocOfDrop = player.x +
                            (if (Random.nextBoolean()) -1 else 1) * (Random.nextInt(MAX_METEOR_DISTANCE - 15) + 15)
                    var zLocOfDrop = player.z +
                            (if (Random.nextBoolean()) -1 else 1) * (Random.nextInt(MAX_METEOR_DISTANCE - 15) + 15)

                    // Drop the meteor
                    dropMeteor(player.level, meteorEntry, xLocOfDrop.roundToInt(), zLocOfDrop.roundToInt())

                    val accuracy = playerBasics.getWatchedMeteorAccuracy()

                    // Print out a message telling the player where the meteor dropped +/- 8 blocks
                    xLocOfDrop =
                        xLocOfDrop + (if (Random.nextBoolean()) -1 else 1) * Random.nextInt(accuracy + 1)
                    zLocOfDrop =
                        zLocOfDrop + (if (Random.nextBoolean()) -1 else 1) * Random.nextInt(accuracy + 1)

                    player.sendMessage(TranslationTextComponent("message.afraidofthedark.meteor.location", xLocOfDrop, zLocOfDrop), player.uuid)

                    // Clear the player's watched meteors so that the same meteor can't be used twice
                    playerBasics.setWatchedMeteor(null, 0, -1, -1, -1)
                    playerBasics.syncWatchedMeteor(player)
                } else {
                    logger.error("Player ${player.gameProfile.name} had null meteor entry")
                }

            }
            // The values aren't correct so show an error
            else {
                player.sendMessage(TranslationTextComponent("message.afraidofthedark.meteor.process.invalid_vals"), player.uuid)
            }
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
    private fun dropMeteor(world: World, meteorEntry: MeteorEntry, xPos: Int, zPos: Int) {
        // We need to compute the y position, start at the top and work our way down
        var yPos = 255
        // Grab the top block
        var block = world.getBlockState(BlockPos(xPos, yPos, zPos)).block

        // Iterate while we are above bedrock on an air/water/lava block. Work our way down
        while (yPos > 0 && INVALID_TOP_BLOCKS.contains(block)) {
            yPos--
            block = world.getBlockState(BlockPos(xPos, yPos, zPos)).block
        }

        logger.info("Meteor dropped at $xPos, $yPos, $zPos")

        // If we have a valid y-pos generate the meteor
        if (yPos > 0) {
            // Compute the meteor's radius, it will be based on the parameters of the meteor entry
            val radius = Random.nextInt(meteorEntry.minMeteorRadius, meteorEntry.maxMeteorRadius + 1)
            // Iterate over x, y, and z
            for (x in xPos - radius..xPos + radius) {
                for (y in yPos - radius until yPos + radius) {
                    for (z in zPos - radius..zPos + radius) {
                        // Compute the distance from the center
                        val distanceFromCenter =
                            (xPos - x) * (xPos - x) + (yPos - y) * (yPos - y) + ((zPos - z) * (zPos - z)).toDouble()
                        // If the distance is less than the radius^2 then it's inside the sphere
                        if (distanceFromCenter < radius * radius) {
                            // Grab the block pos at the x,y,z coordinates
                            val blockPos = BlockPos(x, y, z)

                            // Grab the block present at these x,y,z coordinates
                            val existingBlock = world.getBlockState(blockPos).block

                            // If the block is replaceable, replace it
                            if (REPLACEABLE_BLOCKS.contains(existingBlock)) {
                                // It's an exterior block so set it to meteor
                                if (distanceFromCenter >= (radius - 1) * (radius - 1) || Math.random() > meteorEntry.richnessPercent) {
                                    world.setBlockAndUpdate(blockPos, ModBlocks.METEOR.defaultBlockState())
                                } else {
                                    world.setBlockAndUpdate(blockPos, meteorEntry.interiorBlock.defaultBlockState())
                                }
                            }
                        }
                    }
                }
            }
        } else {
            logger.error("Could not find a suitable y-level for the meteor at $xPos, $yPos")
        }
    }

    companion object {
        private val logger = LogManager.getLogger()

        // The maximum distance a meteor can fall away from the player
        private const val MAX_METEOR_DISTANCE = 500

        // An invalid list of top level blocks that the meteor cant start on
        private val INVALID_TOP_BLOCKS = setOf(
            Blocks.AIR,
            Blocks.WATER,
            Blocks.LAVA
        )

        // A list of blocks that the meteor is allowed to remove and spawn in
        private val REPLACEABLE_BLOCKS = setOf(
            ModBlocks.METEOR,
            Blocks.DIRT,
            Blocks.GRASS,
            Blocks.GRASS_BLOCK,
            Blocks.SPRUCE_LEAVES,
            Blocks.OAK_LEAVES,
            Blocks.JUNGLE_LEAVES,
            Blocks.DARK_OAK_LEAVES,
            Blocks.BIRCH_LEAVES,
            Blocks.ACACIA_LEAVES,
            Blocks.SAND,
            Blocks.ACACIA_LOG,
            Blocks.BIRCH_LOG,
            Blocks.DARK_OAK_LOG,
            Blocks.JUNGLE_LOG,
            Blocks.OAK_LOG,
            Blocks.SPRUCE_LOG,
            Blocks.VINE,
            Blocks.DEAD_BUSH,
            Blocks.TALL_GRASS,
            Blocks.ICE,
            Blocks.AIR,
            Blocks.STONE,
            Blocks.GRAVEL,
            Blocks.SANDSTONE,
            Blocks.SNOW_BLOCK,
            Blocks.SNOW,
            Blocks.TERRACOTTA,
            Blocks.CLAY,
            Blocks.WATER,
            Blocks.LAVA,
            Blocks.GRASS_PATH
        )
    }
}