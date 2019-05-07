package com.DavidM1A2.afraidofthedark.common.packets.otherPackets;

import com.DavidM1A2.afraidofthedark.AfraidOfTheDark;
import com.DavidM1A2.afraidofthedark.common.capabilities.player.basics.IAOTDPlayerBasics;
import com.DavidM1A2.afraidofthedark.common.constants.ModBlocks;
import com.DavidM1A2.afraidofthedark.common.constants.ModCapabilities;
import com.DavidM1A2.afraidofthedark.common.packets.packetHandler.MessageHandler;
import com.DavidM1A2.afraidofthedark.common.registry.meteor.MeteorEntry;
import com.google.common.collect.ImmutableSet;
import io.netty.buffer.ByteBuf;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import org.apache.commons.lang3.RandomUtils;

import java.util.Random;
import java.util.Set;

/**
 * Packet sent from client to server to tell the server to validate sextant information and tell us where a meteor landed
 */
public class ProcessSextantInput implements IMessage
{
    // The 3 fields that we are tracking
    private int dropAngle;
    private int latitude;
    private int longitude;

    /**
     * Default constructor is required but not used
     */
    public ProcessSextantInput()
    {
        this.dropAngle = -1;
        this.latitude = -1;
        this.longitude = -1;
    }

    /**
     * Overloaded constructor that initializes fields
     *
     * @param dropAngle The angle the meteor dropped at
     * @param latitude  The latitude the meteor was dropping to
     * @param longitude The longitude the meteor was dropping to
     */
    public ProcessSextantInput(int dropAngle, int latitude, int longitude)
    {
        this.dropAngle = dropAngle;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    /**
     * Converts from raw bytes to structured data
     *
     * @param buf The buffer to read from
     */
    @Override
    public void fromBytes(ByteBuf buf)
    {
        this.dropAngle = buf.readInt();
        this.latitude = buf.readInt();
        this.longitude = buf.readInt();
    }

    /**
     * Converts from structured data to raw bytes
     *
     * @param buf The buffer to write to
     */
    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeInt(this.dropAngle);
        buf.writeInt(this.latitude);
        buf.writeInt(this.longitude);
    }

    /**
     * Handler handles ProcessSextantInput packets on the server side
     */
    public static class Handler extends MessageHandler.Server<ProcessSextantInput>
    {
        // The maximum distance a meteor can fall away from the player
        private static final int MAX_METEOR_DISTANCE = 500;
        // The chance accuracy of the sextant used, the actual meteor will be within this many blocks of the coordinates
        private static final int ACCURACY = 16;
        // An invalid list of top level blocks that the meteor cant start on
        private static final Set<Block> INVALID_TOP_BLOCKS = ImmutableSet.of(
                Blocks.AIR,
                Blocks.WATER,
                Blocks.FLOWING_WATER,
                Blocks.LAVA,
                Blocks.FLOWING_LAVA
        );
        // A list of blocks that the meteor is allowed to remove and spawn in
        private static final Set<Block> REPLACEABLE_BLOCKS = ImmutableSet.of(
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
                Blocks.GRASS_PATH);

        /**
         * Handles the message on the server side
         *
         * @param player the player reference (the player who sent the packet)
         * @param msg    the message received
         * @param ctx    The context that the message was sent through
         */
        @Override
        public void handleServerMessage(EntityPlayer player, ProcessSextantInput msg, MessageContext ctx)
        {
            // First validate that the player entered the correct values into the sextant
            IAOTDPlayerBasics playerBasics = player.getCapability(ModCapabilities.PLAYER_BASICS, null);
            if (playerBasics.getWatchedMeteorDropAngle() == msg.dropAngle &&
                    playerBasics.getWatchedMeteorLatitude() == msg.latitude &&
                    playerBasics.getWatchedMeteorLongitude() == msg.longitude)
            {
                MeteorEntry meteorEntry = playerBasics.getWatchedMeteor();
                Random random = player.getRNG();

                // The meteor can drop from 15 - 500 blocks away from the player in both directions
                int xLocOfDrop = player.getPosition().getX() +
                        (random.nextBoolean() ? -1 : 1) * (random.nextInt(MAX_METEOR_DISTANCE - 15) + 15);
                int zLocOfDrop = player.getPosition().getZ() +
                        (random.nextBoolean() ? -1 : 1) * (random.nextInt(MAX_METEOR_DISTANCE - 15) + 15);

                // Drop the meteor
                this.dropMeteor(player.world, meteorEntry, xLocOfDrop, zLocOfDrop);
                // Print out a message telling the player where the meteor dropped +/- 8 blocks
                xLocOfDrop = xLocOfDrop + (random.nextBoolean() ? -1 : 1) * random.nextInt(ACCURACY + 1);
                zLocOfDrop = zLocOfDrop + (random.nextBoolean() ? -1 : 1) * random.nextInt(ACCURACY + 1);
                player.sendMessage(new TextComponentString("Based off of this information the meteor fell around " + xLocOfDrop + ", " + zLocOfDrop));

                // Clear the player's watched meteors so that the same meteor can't be used twice
                playerBasics.setWatchedMeteor(null, -1, -1, -1);
                playerBasics.syncWatchedMeteor(player);
            }
            else
            // The values aren't correct so show an error
            {
                player.sendMessage(new TextComponentString("The values entered do not make sense. I should try to re-enter the meteor's data or observe a new meteor."));
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
        private void dropMeteor(World world, MeteorEntry meteorEntry, int xPos, int zPos)
        {
            // We need to compute the y position, start at the top and work our way down
            int yPos = 255;
            // Grab the top block
            Block block = world.getBlockState(new BlockPos(xPos, yPos, zPos)).getBlock();
            // Iterate while we are above bedrock on an air/water/lava block. Work our way down
            while (yPos > 0 && INVALID_TOP_BLOCKS.contains(block))
            {
                yPos--;
                block = world.getBlockState(new BlockPos(xPos, yPos, zPos)).getBlock();
            }
            AfraidOfTheDark.INSTANCE.getLogger().info("Meteor dropped at " + xPos + ", " + yPos + ", " + zPos);
            // If we have a valid y-pos generate the meteor
            if (yPos > 0)
            {
                // Compute the meteor's radius, it will be based on the parameters of the meteor entry
                int radius = RandomUtils.nextInt(meteorEntry.getMinMeteorRadius(), meteorEntry.getMaxMeteorRadius() + 1);
                // Iterate over x, y, and z
                for (int x = xPos - radius; x <= xPos + radius; x++)
                    for (int y = yPos - radius; y < yPos + radius; y++)
                        for (int z = zPos - radius; z <= zPos + radius; z++)
                        {
                            // Compute the distance from the center
                            double distanceFromCenter = (xPos - x) * (xPos - x) + (yPos - y) * (yPos - y) + (zPos - z) * (zPos - z);
                            // If the distance is less than the radius^2 then it's inside the sphere
                            if (distanceFromCenter < radius * radius)
                            {
                                // Grab the block pos at the x,y,z coordinates
                                BlockPos blockPos = new BlockPos(x, y, z);
                                // Grab the block present at these x,y,z coordinates
                                Block existingBlock = world.getBlockState(blockPos).getBlock();
                                // If the block is replaceable, replace it
                                if (REPLACEABLE_BLOCKS.contains(existingBlock))
                                {
                                    // It's an exterior block so set it to meteor
                                    if (distanceFromCenter >= (radius - 1) * (radius - 1) || Math.random() > meteorEntry.getRichnessPercent())
                                    {
                                        world.setBlockState(blockPos, ModBlocks.METEOR.getDefaultState());
                                    }
                                    // It's an interior block so set it to the interior block
                                    else
                                    {
                                        world.setBlockState(blockPos, meteorEntry.getInteriorBlock().getDefaultState());
                                    }
                                }
                            }

                        }
            }
            // Log an error, this should never happen
            else
            {
                AfraidOfTheDark.INSTANCE.getLogger().error("Could not find a suitable y-level for the meteor at " + xPos + ", " + yPos);
            }
        }
    }
}
