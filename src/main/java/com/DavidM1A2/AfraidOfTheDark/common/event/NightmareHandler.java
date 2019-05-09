package com.DavidM1A2.afraidofthedark.common.event;

import com.DavidM1A2.afraidofthedark.AfraidOfTheDark;
import com.DavidM1A2.afraidofthedark.client.sound.BellsRinging;
import com.DavidM1A2.afraidofthedark.client.sound.ErieEcho;
import com.DavidM1A2.afraidofthedark.common.capabilities.player.dimension.IAOTDPlayerNightmareData;
import com.DavidM1A2.afraidofthedark.common.capabilities.player.research.IAOTDPlayerResearch;
import com.DavidM1A2.afraidofthedark.common.constants.*;
import com.DavidM1A2.afraidofthedark.common.dimension.IslandUtility;
import com.DavidM1A2.afraidofthedark.common.utility.NBTHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.EntityTravelToDimensionEvent;
import net.minecraftforge.event.entity.player.PlayerSleepInBedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

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
            // Compute the player's index to go to
            int indexToGoTo = IslandUtility.getOrAssignPlayerPositionalIndex(entityPlayer.getServer().getWorld(ModDimensions.NIGHTMARE.getId()), playerNightmareData);
            // Compute the player's X position based on the index
            int playerXBase = indexToGoTo * AfraidOfTheDark.INSTANCE.getConfigurationHandler().getBlocksBetweenIslands();
            // Set the player's position and rotation for some reason we have to use the connection object to send a packet instead of just using entityplayer#setPosition
            entityPlayer.connection.setPlayerLocation(playerXBase + 21.5, 74, 44.5, 0, 0);
            // Reset the player's stats so that they don't die from low hp in the new dimension
            resetPlayerStats(entityPlayer);
            // Give the player a research journal
            entityPlayer.inventory.addItemStackToInventory(createNamedJournal(entityPlayer));
            // Give the player a hint book to find the researches
            entityPlayer.inventory.addItemStackToInventory(createHintBook());
            // Give the player torches to see
            entityPlayer.inventory.addItemStackToInventory(new ItemStack(Blocks.TORCH, 64));
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
        ModItems.JOURNAL.setOwner(toReturn, entityPlayer.getDisplayName().getUnformattedText());
        return toReturn;
    }

    /**
     * Creates a hint book to be used to explore the nightmare by the player
     *
     * @return The itemstack representing the hint book
     */
    private ItemStack createHintBook()
    {
        ItemStack toReturn = new ItemStack(Items.WRITTEN_BOOK, 1, 0);
        NBTHelper.setString(toReturn, "title", "Insanity's Heights");
        NBTHelper.setString(toReturn, "author", "Foul Ole Ron");
        NBTHelper.setBoolean(toReturn, "resolved", true);
        toReturn.getTagCompound().setTag("pages", createPages());
        return toReturn;
    }

    /**
     * Creates a tag list of strings representing pages in the insanity's heights book
     *
     * @return Creates a list of pages to be used by the book
     */
    private NBTTagList createPages()
    {
        NBTTagList pages = new NBTTagList();
        pages.appendTag(new NBTTagString("To whomever finds this: don't stay here. This place is evil. I have been stuck here for longer than I can remember. I can hear the abyss calling to me. It beckons me to jump, calling my name. I've found all of the notes, but I cannot"));
        pages.appendTag(new NBTTagString("leave with them. There are ten scrolls hidden here. Three are in the tallest tower, with two being near the top and one being near the bottom. The saw mill whispers such sweet things to be. The stone tower says that it has two"));
        pages.appendTag(new NBTTagString("gifts for me. What pretty things they have, so many rings. Enaria's bones whisper to me from her grave. I'm sorry; we tried to save you! Her whispers make me want to hide inside of the log. The roof top rooms are hiding something"));
        pages.appendTag(new NBTTagString("from me. They always stay quiet when I am near. I know they are keeping secrets from me! What has it told you? What has the monolith told you to make you stop talking to me? Answer me Enaria! Where have you gone? Have you left me?"));
        pages.appendTag(new NBTTagString("You said we would be together forever!"));
        return pages;
    }
}
