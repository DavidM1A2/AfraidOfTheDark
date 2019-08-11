package com.DavidM1A2.afraidofthedark.common.event;

import com.DavidM1A2.afraidofthedark.AfraidOfTheDark;
import com.DavidM1A2.afraidofthedark.client.sound.BellsRinging;
import com.DavidM1A2.afraidofthedark.client.sound.ErieEcho;
import com.DavidM1A2.afraidofthedark.common.capabilities.player.dimension.IAOTDPlayerNightmareData;
import com.DavidM1A2.afraidofthedark.common.capabilities.player.research.IAOTDPlayerResearch;
import com.DavidM1A2.afraidofthedark.common.constants.*;
import com.DavidM1A2.afraidofthedark.common.dimension.IslandUtility;
import com.DavidM1A2.afraidofthedark.common.entity.enaria.EntityGhastlyEnaria;
import com.DavidM1A2.afraidofthedark.common.worldGeneration.schematic.SchematicGenerator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ClassInheritanceMultiMap;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.WorldServer;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.EntityTravelToDimensionEvent;
import net.minecraftforge.event.entity.player.PlayerSleepInBedEvent;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Random;

/**
 * Class handling events to send players to and from their nightmare realm
 */
public class NightmareHandler
{
    // Constant number of blocks to search for a spawn position
    private static final int VALID_SPAWN_SEARCH_DISTANCE = 6;

    /**
     * Called when the player sleeps in a bed, tests if they're drowsy and if so sends them to the nightmare realm
     *
     * @param event event containing player and world data
     */
    @SubscribeEvent
    public void onPlayerSleepInBedEvent(PlayerSleepInBedEvent event)
    {
        EntityPlayer entityPlayer = event.getEntityPlayer();
        // Only process server side
        if (!entityPlayer.world.isRemote)
        {
            // If the player has a sleeping potion effect on and has the right researches send them to the nightmare
            if (entityPlayer.getActivePotionEffect(ModPotions.SLEEPING_POTION) != null)
            {
                IAOTDPlayerResearch playerResearch = entityPlayer.getCapability(ModCapabilities.PLAYER_RESEARCH, null);
                // If the player can research the nightmare research do so
                if (playerResearch.canResearch(ModResearches.NIGHTMARE))
                {
                    playerResearch.setResearch(ModResearches.NIGHTMARE, true);
                    playerResearch.sync(entityPlayer, true);
                }

                // If the player has the nightmare research send them to the nightmare realm
                if (playerResearch.isResearched(ModResearches.NIGHTMARE))
                {
                    entityPlayer.changeDimension(ModDimensions.NIGHTMARE.getId(), ModDimensions.NOOP_TELEPORTER);
                }
            }
        }
    }

    /**
     * Called when a chunk is unloaded from the world
     *
     * @param event The event containing info about the unload
     */
    @SubscribeEvent
    public void onChunkUnloadEvent(ChunkEvent.Unload event)
    {
        // Server side processing only
        if (!event.getWorld().isRemote)
        {
            // Test if we're in the nightmare dimension
            if (event.getWorld().provider.getDimension() == ModDimensions.NIGHTMARE.getId())
            {
                // Search through the list of entities in the chunk
                for (ClassInheritanceMultiMap<Entity> entityMap : event.getChunk().getEntityLists())
                {
                    // Go through each entity
                    for (Entity entity : entityMap)
                    {
                        // If an entity is enaria, kill her and respawn her closer to the player if possible
                        if (entity instanceof EntityGhastlyEnaria)
                        {
                            // If the enaria entity is dead the player touched her and went back to the overworld
                            if (!entity.isDead)
                            {
                                // Kill any unloaded enaria entities
                                entity.setDead();
                                // Grab the nearby player
                                EntityPlayer entityPlayer = event.getWorld().getClosestPlayerToEntity(entity, AfraidOfTheDark.INSTANCE.getConfigurationHandler().getBlocksBetweenIslands() / 2);
                                // If we have a valid nearby player teleport a new enaria to them. If we don't then just die (the player left the nightmare dimension)
                                if (entityPlayer != null && !entityPlayer.isDead)
                                {
                                    // Compute a random offset in +/- 25-50 in x and z
                                    Random random = entityPlayer.getRNG();
                                    int offsetX = random.nextBoolean() ? random.nextInt(26) - 50 : random.nextInt(26) + 25;
                                    int offsetZ = random.nextBoolean() ? random.nextInt(26) - 50 : random.nextInt(26) + 25;
                                    // Compute enaria's new position
                                    int posX = entityPlayer.getPosition().getX() + offsetX;
                                    int posZ = entityPlayer.getPosition().getZ() + offsetZ;
                                    // Spawn a new enaria
                                    EntityGhastlyEnaria newEnaria = new EntityGhastlyEnaria(event.getWorld());
                                    newEnaria.setBenign(!entityPlayer.getCapability(ModCapabilities.PLAYER_RESEARCH, null).isResearched(ModResearches.ENARIA));
                                    newEnaria.setPosition(posX, entityPlayer.posY, posZ);
                                    event.getWorld().spawnEntity(newEnaria);
                                    // Return out of here, there will only be 1 enaria following the player
                                    return;
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Called when the player dies, teleport them back to the dimension they came from
     *
     * @param event The data of the respawned player
     */
    @SubscribeEvent
    public void onPlayerRespawnEvent(PlayerEvent.PlayerRespawnEvent event)
    {
        // Server side processing only
        if (!event.player.world.isRemote)
        {
            if (event.player.dimension == ModDimensions.NIGHTMARE.getId())
            {
                IAOTDPlayerNightmareData nightmareData = event.player.getCapability(ModCapabilities.PLAYER_NIGHTMARE_DATA, null);
                // Send the player back to their original dimension
                event.player.changeDimension(nightmareData.getPreTeleportDimensionID(), ModDimensions.NOOP_TELEPORTER);
            }
        }
    }

    /**
     * Called when an entity joins a world. Check if a player entered the nightmare and if so play the nightmare
     * bells and echo. Only do this client side since ISound is a client side only class
     *
     * @param event Event containing info about who joined the world
     */
    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void onEntityJoinWorldEvent(EntityJoinWorldEvent event)
    {
        // Client side only, even though this must be true since we're using SideOnly
        if (event.getWorld().isRemote)
        {
            // Test if the player is going to the nightmare
            if (event.getWorld().provider.getDimension() == ModDimensions.NIGHTMARE.getId() && event.getEntity() instanceof EntityPlayer)
            {
                // We need one more check to see if the player's dimension id is nightmare. This is a workaround because
                // when teleporting this callback will get fired twice since the player teleports once for
                // the teleport, once to be spawned into the world
                if (event.getEntity().world.provider.getDimension() == ModDimensions.NIGHTMARE.getId())
                {
                    // Grab the client's sound handler and play the sound if it is not already playing
                    SoundHandler soundHandler = Minecraft.getMinecraft().getSoundHandler();
                    // Play the bell sound after 20 seconds and erie echo after 3
                    soundHandler.playDelayedSound(new BellsRinging(), 20 * 20);
                    soundHandler.playDelayedSound(new ErieEcho(), 3 * 20);
                }
            }
        }
    }

    /**
     * When we want to travel to the
     *
     * @param event The event parameters
     */
    @SubscribeEvent
    public void onPreEntityTravelToDimension(EntityTravelToDimensionEvent event)
    {
        // Server side processing only
        if (!event.getEntity().world.isRemote)
        {
            // Get to and from dimension
            int fromDimension = event.getEntity().dimension;
            int toDimension = event.getDimension();
            // Test if the entity is a player, if so process it
            if (event.getEntity() instanceof EntityPlayerMP)
            {
                EntityPlayerMP entityPlayer = (EntityPlayerMP) event.getEntity();
                // Process the pre-teleport server side, if it returns true then we cancel the TP
                if (this.processPreTeleport(entityPlayer, fromDimension, toDimension))
                {
                    event.setCanceled(true);
                }
            }
        }
    }

    /**
     * Called right before the player is about to teleport
     *
     * @param entityPlayer  The teleporting player
     * @param dimensionFrom The dimension the player is currently in
     * @param dimensionTo   The dimension the player is going to
     * @return True to cancel the teleport, false otherwise
     */
    private boolean processPreTeleport(EntityPlayerMP entityPlayer, int dimensionFrom, int dimensionTo)
    {
        // If we're going to dimension NIGHTMARE then we need to do some preprocesing and tests to ensure the player can continue
        if (dimensionTo == ModDimensions.NIGHTMARE.getId())
        {
            // We can't go from nightmare to nightmare
            if (dimensionFrom == ModDimensions.NIGHTMARE.getId())
            {
                return true;
            }

            // Any other dimension is valid. We can go from any dimension other than the nightmare to the nightmare
            // We need to store off player position data pre-teleport
            IAOTDPlayerNightmareData playerNightmareData = entityPlayer.getCapability(ModCapabilities.PLAYER_NIGHTMARE_DATA, null);
            // Test for a valid spot within ~6 blocks of the player's position. This is used to ensure players do not come back to the overworld and straight into a
            // new portal block. This ensure you don't get stuck in a teleport loop
            // First just test the player's current position, if it's invalid search in a +/- 6 block radius in all directions for a valid position
            if (IslandUtility.isValidSpawnLocation(entityPlayer.world, entityPlayer.getPosition()))
            {
                playerNightmareData.setPreTeleportPosition(entityPlayer.getPosition());
            }
            else
            {
                BlockPos preTeleportPosition = IslandUtility.findValidSpawnLocation(entityPlayer.world, entityPlayer.getPosition(), VALID_SPAWN_SEARCH_DISTANCE);
                // If we didn't find a valid spot around the player's position then throw an error and reject the teleport
                if (preTeleportPosition == null)
                {
                    entityPlayer.sendMessage(new TextComponentTranslation("aotd.dimension.nightmare.no_spawn"));
                    return true;
                }
                else
                {
                    playerNightmareData.setPreTeleportPosition(preTeleportPosition);
                }
            }
            // Set our pre-teleport dimension ID
            playerNightmareData.setPreTeleportDimensionID(dimensionFrom);
            // Write our player's inventory to NBT and save it off
            NBTTagList inventoryNBT = entityPlayer.inventory.writeToNBT(new NBTTagList());
            playerNightmareData.setPreTeleportPlayerInventory(inventoryNBT);
            // Clear the players inventory and sync it with packets
            entityPlayer.inventory.clear();
            entityPlayer.inventoryContainer.detectAndSendChanges();
        }
        return false;
    }

    /**
     * Called when any player is finished changing dimensions
     *
     * @param event The event parameters
     */
    @SubscribeEvent
    public void onPostEntityTravelToDimension(PlayerEvent.PlayerChangedDimensionEvent event)
    {
        // Perform all the important logic server side
        if (!event.player.world.isRemote)
        {
            // Get to and from dimension
            int fromDimension = event.fromDim;
            int toDimension = event.toDim;
            // Get the player teleporting
            EntityPlayerMP entityPlayer = (EntityPlayerMP) event.player;
            // Process the post-teleport server side
            this.processPostTeleport(entityPlayer, fromDimension, toDimension);
        }
    }

    /**
     * Called right after the player teleported
     *
     * @param entityPlayer  The teleporting player
     * @param dimensionFrom The dimension the player was in
     * @param dimensionTo   The dimension the player is now in
     */
    private void processPostTeleport(EntityPlayerMP entityPlayer, int dimensionFrom, int dimensionTo)
    {
        // If the player entered the nightmare dimension then set their position
        IAOTDPlayerNightmareData playerNightmareData = entityPlayer.getCapability(ModCapabilities.PLAYER_NIGHTMARE_DATA, null);
        if (dimensionTo == ModDimensions.NIGHTMARE.getId())
        {
            WorldServer nightmareWorld = entityPlayer.getServer().getWorld(ModDimensions.NIGHTMARE.getId());
            // Compute the player's index to go to
            int indexToGoTo = IslandUtility.getOrAssignPlayerPositionalIndex(nightmareWorld, playerNightmareData);
            // Compute the player's X position based on the index
            int playerXBase = indexToGoTo * AfraidOfTheDark.INSTANCE.getConfigurationHandler().getBlocksBetweenIslands();
            // Set the player's position and rotation for some reason we have to use the connection object to send a packet instead of just using entityplayer#setPosition
            entityPlayer.connection.setPlayerLocation(playerXBase + 21.5, 74, 44.5, 0, 0);
            // Reset the player's stats so that they don't die from low hp in the new dimension
            resetPlayerStats(entityPlayer);
            // Give the player a research journal
            entityPlayer.inventory.addItemStackToInventory(createNamedJournal(entityPlayer));
            // Give the player a hint book to find the researches
            entityPlayer.inventory.addItemStackToInventory(new ItemStack(ModItems.INSANITYS_HEIGHTS));
            // Give the player torches to see
            entityPlayer.inventory.addItemStackToInventory(new ItemStack(Blocks.TORCH, 64));
            // Test if the player needs their spell creation structure generated as an addon to the nightmare island
            testForEnariasAltar(entityPlayer, nightmareWorld, new BlockPos(playerXBase, 0, 0));
        }

        // If the player left the nightmare reset their position
        if (dimensionFrom == ModDimensions.NIGHTMARE.getId())
        {
            // Grab the player's pre-teleport position
            BlockPos preTeleportPosition = playerNightmareData.getPreTeleportPosition();
            // Reset the player's position
            entityPlayer.connection.setPlayerLocation(preTeleportPosition.getX() + 0.5, preTeleportPosition.getY() + 1.5, preTeleportPosition.getZ() + 0.5, 0, 0);
            // Reset the player's stats so that they don't die from low hp in the new dimension
            resetPlayerStats(entityPlayer);
            // Clear the nightmare junk out of the player's inventory
            entityPlayer.inventory.clear();
            // Update the player's inventory with the original things
            entityPlayer.inventory.readFromNBT(playerNightmareData.getPreTeleportPlayerInventory());
            entityPlayer.inventoryContainer.detectAndSendChanges();
        }
    }

    /**
     * Called to reset a player's stats after teleportation
     *
     * @param entityPlayer The player to reset stats for
     */
    private void resetPlayerStats(EntityPlayer entityPlayer)
    {
        entityPlayer.motionX = 0;
        entityPlayer.motionY = 0;
        entityPlayer.motionZ = 0;
        entityPlayer.setHealth(20);
        entityPlayer.getFoodStats().setFoodLevel(20);
        entityPlayer.clearActivePotions();
    }

    /**
     * Creates a blood stained journal that is named after the player
     *
     * @param entityPlayer The player to create a journal for
     * @return The created journal
     */
    private ItemStack createNamedJournal(EntityPlayer entityPlayer)
    {
        ItemStack toReturn = new ItemStack(ModItems.JOURNAL, 1, 0);
        ModItems.JOURNAL.setOwner(toReturn, entityPlayer.getGameProfile().getName());
        return toReturn;
    }

    /**
     * Tests if we need to generate enaria's altar. Do this if the player has beaten enaria and the altar doesn't
     * exist yet
     *
     * @param entityPlayer   The player that killed enaria and is being checked for
     * @param nightmareWorld The nightmare world being tested
     * @param islandPos      The position of this player's island realm
     */
    private void testForEnariasAltar(EntityPlayerMP entityPlayer, WorldServer nightmareWorld, BlockPos islandPos)
    {
        // Grab the player's research, if he has enaria generate the altar if needed
        IAOTDPlayerResearch playerResearch = entityPlayer.getCapability(ModCapabilities.PLAYER_RESEARCH, null);
        if (playerResearch.isResearched(ModResearches.ENARIA))
        {
            // If enaria's alter does not exist generate the schematic
            if (nightmareWorld.getBlockState(islandPos.add(101, 74, 233)).getBlock() != ModBlocks.ENARIAS_ALTAR)
            {
                SchematicGenerator.generateSchematic(ModSchematics.ENARIAS_ALTAR, nightmareWorld, islandPos.add(67, 40, 179));
            }
        }
    }
}
