package com.DavidM1A2.afraidofthedark.common.handler;

import com.DavidM1A2.afraidofthedark.common.capabilities.player.basics.AOTDPlayerBasicsImpl;
import com.DavidM1A2.afraidofthedark.common.capabilities.player.basics.AOTDPlayerBasicsProvider;
import com.DavidM1A2.afraidofthedark.common.capabilities.player.basics.AOTDPlayerBasicsStorage;
import com.DavidM1A2.afraidofthedark.common.capabilities.player.basics.IAOTDPlayerBasics;
import com.DavidM1A2.afraidofthedark.common.constants.Constants;
import com.DavidM1A2.afraidofthedark.common.constants.ModCapabilities;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.ChunkGeneratorOverworld;
import net.minecraft.world.gen.ChunkProviderServer;
import net.minecraft.world.gen.NoiseGeneratorPerlin;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.terraingen.InitNoiseGensEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.Sys;

/**
 * Class used to register all of our mod capabilities
 */
public class CapabilityHandler
{
	// Store a flag that ensures if we create multiple capability handlers we only initialize once
	private static boolean wasInitialized = false;

	/**
	 * Called to initialize all of our mod capabilities into the capability manager if it was not already initialized
	 */
	public CapabilityHandler()
	{
		// If the capability manager was not initialized initialize it
		if (!CapabilityHandler.wasInitialized)
		{
			CapabilityManager.INSTANCE.register(IAOTDPlayerBasics.class, new AOTDPlayerBasicsStorage(), AOTDPlayerBasicsImpl::new);

			CapabilityHandler.wasInitialized = true;
		}
	}

	/**
	 * When we get an attach capabilites event we attach our player capabilities
	 *
	 * @param event The attach event that we will add to
	 */
	@SubscribeEvent
	public void onAttachCapabilitiesEntity(AttachCapabilitiesEvent<Entity> event)
	{
		// If the entity is a player then add the player basics capability
		if (event.getObject() instanceof EntityPlayer)
			event.addCapability(new ResourceLocation(Constants.MOD_ID + ":playerBasics"), new AOTDPlayerBasicsProvider());
	}

	/**
	 * When an entity joins the world we perform a capability sync
	 *
	 * @param event The join event we will check
	 */
	@SubscribeEvent
	public void onEntityJoinWorld(EntityJoinWorldEvent event)
	{
		// When the player joins the world
		if (event.getEntity() instanceof EntityPlayer)
		{
			EntityPlayer entityPlayer = (EntityPlayer) event.getEntity();

			// The server will have correct data, the client needs new data
			if (event.getWorld().isRemote)
			{
				entityPlayer.getCapability(ModCapabilities.PLAYER_BASICS, null).syncAll(entityPlayer);
			}
		}
	}

	/**
	 * When the player dies, he is cloned but no capabilities are copied by default, so we need to manually do that here
	 *
	 * @param event The clone event
	 */
	@SubscribeEvent
	public void onClonePlayer(final PlayerEvent.Clone event)
	{
		// Grab new and original player capabilities
		IAOTDPlayerBasics originalPlayerBasics = event.getOriginal().getCapability(ModCapabilities.PLAYER_BASICS, null);
		IAOTDPlayerBasics newPlayerBasics = event.getEntityPlayer().getCapability(ModCapabilities.PLAYER_BASICS, null);

		// Grab the NBT compound off of the original capabilities
		NBTTagCompound originalPlayerBasicsNBT = (NBTTagCompound) ModCapabilities.PLAYER_BASICS.getStorage().writeNBT(ModCapabilities.PLAYER_BASICS, originalPlayerBasics, null);

		// Copy the NBT compound onto the new capabilities
		ModCapabilities.PLAYER_BASICS.getStorage().readNBT(ModCapabilities.PLAYER_BASICS, newPlayerBasics, null, originalPlayerBasicsNBT);
	}
}
