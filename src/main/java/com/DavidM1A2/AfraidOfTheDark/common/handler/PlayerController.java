/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.handler;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLContext;

import com.DavidM1A2.AfraidOfTheDark.AfraidOfTheDark;
import com.DavidM1A2.AfraidOfTheDark.client.settings.ClientData;
import com.DavidM1A2.AfraidOfTheDark.common.dimension.nightmare.NightmareTeleporter;
import com.DavidM1A2.AfraidOfTheDark.common.dimension.voidChest.VoidChestTeleporter;
import com.DavidM1A2.AfraidOfTheDark.common.entities.DeeeSyft.EntityDeeeSyft;
import com.DavidM1A2.AfraidOfTheDark.common.initializeMod.ModPotionEffects;
import com.DavidM1A2.AfraidOfTheDark.common.packets.UpdateAOTDStatus;
import com.DavidM1A2.AfraidOfTheDark.common.packets.UpdateInsanity;
import com.DavidM1A2.AfraidOfTheDark.common.packets.UpdateResearch;
import com.DavidM1A2.AfraidOfTheDark.common.playerData.HasStartedAOTD;
import com.DavidM1A2.AfraidOfTheDark.common.playerData.Insanity;
import com.DavidM1A2.AfraidOfTheDark.common.playerData.InventorySaver;
import com.DavidM1A2.AfraidOfTheDark.common.playerData.Research;
import com.DavidM1A2.AfraidOfTheDark.common.playerData.Vitae;
import com.DavidM1A2.AfraidOfTheDark.common.playerData.VoidChestLocation;
import com.DavidM1A2.AfraidOfTheDark.common.refrence.Constants;
import com.DavidM1A2.AfraidOfTheDark.common.refrence.ResearchTypes;
import com.DavidM1A2.AfraidOfTheDark.common.threads.delayed.DelayedAOTDUpdate;
import com.DavidM1A2.AfraidOfTheDark.common.threads.delayed.DelayedInsanityUpdate;
import com.DavidM1A2.AfraidOfTheDark.common.threads.delayed.DelayedResearchUpdate;
import com.DavidM1A2.AfraidOfTheDark.common.threads.delayed.DelayedTeleport;
import com.DavidM1A2.AfraidOfTheDark.common.threads.delayed.DelayedVitaeUpdate;
import com.DavidM1A2.AfraidOfTheDark.common.utility.Utility;

import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemFlintAndSteel;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.potion.Potion;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraftforge.client.event.EntityViewRenderEvent.FogColors;
import net.minecraftforge.client.event.EntityViewRenderEvent.FogDensity;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.entity.EntityEvent.EntityConstructing;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.player.EntityInteractEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerSleepInBedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class PlayerController
{

	// When the player dies, he is cloned. We move over extended properties such as hasStartedAOTD, insanity, and research
	@SubscribeEvent
	public void onClonePlayer(final PlayerEvent.Clone event)
	{
		final boolean hasStartedAOTD = HasStartedAOTD.get(event.original);
		HasStartedAOTD.set(event.entityPlayer, hasStartedAOTD, Side.SERVER);
		final double insanity = Insanity.get(event.original);
		Insanity.addInsanity(insanity, event.entityPlayer);
		final NBTTagCompound research = Research.get(event.original);
		Research.set(event.entityPlayer, research);
		final int vitaeLevel = Vitae.get(event.original);
		Vitae.set(event.entityPlayer, vitaeLevel, Side.SERVER);
		if (event.original.dimension == Constants.NightmareWorld.NIGHTMARE_WORLD_ID)
		{
			(new DelayedTeleport(1000, event.entityPlayer, 0, NightmareTeleporter.class)).start();
		}
		else if (event.original.dimension == Constants.VoidChestWorld.VOID_CHEST_WORLD_ID)
		{
			(new DelayedTeleport(1000, event.entityPlayer, 0, VoidChestTeleporter.class)).start();
		}
		InventorySaver.set(event.entityPlayer, InventorySaver.getInventory(event.original), InventorySaver.getPlayerLocationOverworld(event.original), InventorySaver.getPlayerLocationNightmare(event.original));
		final BlockPos overworldVoidChestLocation = VoidChestLocation.getOverworldLocation(event.original);
		final int voidChestIndex = VoidChestLocation.getVoidChestLocation(event.original);
		VoidChestLocation.setOverworldLocation(event.entityPlayer, new int[]
		{ overworldVoidChestLocation.getX(), overworldVoidChestLocation.getY(), overworldVoidChestLocation.getZ() });
		VoidChestLocation.setVoidChestLocation(event.entityPlayer, voidChestIndex);
		// When the player gets new research we will wait 500ms before updating because otherwise the event.original player
		// will get the new data
		(new DelayedAOTDUpdate(600, event.entityPlayer, hasStartedAOTD)).start();
		(new DelayedInsanityUpdate(700, event.entityPlayer, insanity)).start();
		(new DelayedResearchUpdate(800, event.entityPlayer, research)).start();
		(new DelayedVitaeUpdate(900, event.entityPlayer, vitaeLevel)).start();
	}

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void renderEventFogColor(final FogColors event)
	{
		if (event.entity instanceof EntityPlayer)
		{
			final float insanity = (float) Insanity.get((EntityPlayer) event.entity);

			// If the player is insane, set the fog equal to 1.001^(.5*insanity) - .9989
			if (insanity >= 0.1)
			{
				event.red = MathHelper.clamp_float(event.red + (insanity / 100.0F), 0.0F, 1.0F);
			}
		}
	}

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void renderEventFogDensity(final FogDensity fogDensity)
	{
		float f1;
		float farPlaneDistance = Minecraft.getMinecraft().gameSettings.renderDistanceChunks * 16.0F;
		int someUnknownInt = 0;
		if (Minecraft.getMinecraft().theWorld.provider.getDimensionId() == Constants.NightmareWorld.NIGHTMARE_WORLD_ID)
		{
			GlStateManager.setFog(2048);
			//GlStateManager.setFogDensity(0.1F);
			fogDensity.density = 0.1f;
		}
		else if (Minecraft.getMinecraft().thePlayer.isPotionActive(Potion.blindness))
		{
			f1 = 5.0F;
			int j = Minecraft.getMinecraft().thePlayer.getActivePotionEffect(Potion.blindness).getDuration();

			if (j < 20)
			{
				f1 = 5.0F + (farPlaneDistance - 5.0F) * (1.0F - (float) j / 20.0F);
			}

			GlStateManager.setFog(9729);

			if (someUnknownInt == -1)
			{
				GlStateManager.setFogStart(0.0F);
				GlStateManager.setFogEnd(f1 * 0.8F);
			}
			else
			{
				GlStateManager.setFogStart(f1 * 0.25F);
				GlStateManager.setFogEnd(f1);
			}

			if (GLContext.getCapabilities().GL_NV_fog_distance)
			{
				GL11.glFogi(34138, 34139);
			}
		}
		//		else if (this.cloudFog)
		//		{
		//			GlStateManager.setFog(2048);
		//			GlStateManager.setFogDensity(0.1F);
		//		}
		else if (fogDensity.block.getMaterial() == Material.water)
		{
			GlStateManager.setFog(2048);

			if (Minecraft.getMinecraft().thePlayer.isPotionActive(Potion.waterBreathing))
			{
				fogDensity.density = 0.01F;
			}
			else
			{
				fogDensity.density = 0.1F - (float) EnchantmentHelper.func_180319_a(Minecraft.getMinecraft().thePlayer) * 0.03F;
			}
		}
		else if (fogDensity.block.getMaterial() == Material.lava)
		{
			GlStateManager.setFog(2048);
			fogDensity.density = 2.0F;
		}
		else
		{
			f1 = farPlaneDistance;
			GlStateManager.setFog(9729);

			if (someUnknownInt == -1)
			{
				GlStateManager.setFogStart(0.0F);
				GlStateManager.setFogEnd(f1);
			}
			else
			{
				GlStateManager.setFogStart(f1 * 0.75F);
				GlStateManager.setFogEnd(f1);
			}

			if (GLContext.getCapabilities().GL_NV_fog_distance)
			{
				GL11.glFogi(34138, 34139);
			}

			if (Minecraft.getMinecraft().theWorld.provider.doesXZShowFog((int) Minecraft.getMinecraft().thePlayer.posX, (int) Minecraft.getMinecraft().thePlayer.posZ))
			{
				GlStateManager.setFogStart(f1 * 0.05F);
				GlStateManager.setFogEnd(Math.min(f1, 192.0F) * 0.5F);
			}
		}
		fogDensity.setCanceled(true);
	}

	@SubscribeEvent
	public void onEntityJoinWorld(final EntityJoinWorldEvent event)
	{
		// When the player joins the world
		if (event.entity instanceof EntityPlayer)
		{
			/*
			 * Sync player research, insanity, and AOTDStart status
			 */
			if (!event.world.isRemote)
			{
				final EntityPlayer entityPlayer = (EntityPlayer) event.entity;

				AfraidOfTheDark.getSimpleNetworkWrapper().sendTo(new UpdateInsanity(Insanity.get(entityPlayer)), (EntityPlayerMP) entityPlayer);

				AfraidOfTheDark.getSimpleNetworkWrapper().sendTo(new UpdateAOTDStatus(HasStartedAOTD.get(entityPlayer)), (EntityPlayerMP) entityPlayer);

				AfraidOfTheDark.getSimpleNetworkWrapper().sendTo(new UpdateResearch(Research.get(entityPlayer), false), (EntityPlayerMP) entityPlayer);
			}
		}
	}

	@SubscribeEvent
	public void onEntityConstructing(final EntityConstructing event)
	{
		if (event.entity instanceof EntityPlayer)
		{
			final EntityPlayer entityPlayer = (EntityPlayer) event.entity;

			Research.register(entityPlayer);
			HasStartedAOTD.register(entityPlayer);
			Insanity.register(entityPlayer);
			InventorySaver.register(entityPlayer);
			VoidChestLocation.register(entityPlayer);
		}

		if (event.entity instanceof EntityLivingBase)
		{
			EntityLivingBase entityLivingBase = (EntityLivingBase) event.entity;
			Vitae.register(entityLivingBase);
		}
	}

	@SubscribeEvent
	public void onPlayerSleepInBedEvent(PlayerSleepInBedEvent event)
	{
		if (!event.entityPlayer.worldObj.isRemote)
		{
			if (event.entityPlayer.getActivePotionEffect(ModPotionEffects.sleepingPotion) != null)
			{
				if (Research.canResearch(event.entityPlayer, ResearchTypes.Nightmares))
				{
					Research.unlockResearchSynced(event.entityPlayer, ResearchTypes.Nightmares, Side.SERVER, true);
				}
				if (Research.isResearched(event.entityPlayer, ResearchTypes.Nightmares))
				{
					Utility.sendPlayerToDimension((EntityPlayerMP) event.entityPlayer, Constants.NightmareWorld.NIGHTMARE_WORLD_ID, false, NightmareTeleporter.class);
				}
			}
		}
	}

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onRenderGameOverlayEventChat(final RenderGameOverlayEvent.Chat event)
	{
		if (ClientData.researchAchievedOverlay != null)
		{
			ClientData.researchAchievedOverlay.updateResearchAchievedWindow();
		}
	}

	@SubscribeEvent
	public void onEntityInteractEvent(final EntityInteractEvent event)
	{
		if (event.target instanceof EntityDeeeSyft)
		{
			if (event.entityPlayer.inventory.getCurrentItem() != null)
			{
				if (event.entityPlayer.inventory.getCurrentItem().getItem() instanceof ItemFlintAndSteel)
				{
					event.target.setFire(1);
				}
			}
		}
	}

	@SubscribeEvent
	public void onItemTooltipEvent(ItemTooltipEvent event)
	{
		if (event.itemStack.isItemEnchanted())
		{
			NBTTagList enchantments = event.itemStack.getEnchantmentTagList();
			for (int i = 0; i < enchantments.tagCount(); i++)
			{
				if (enchantments.get(i) instanceof NBTTagCompound)
				{
					Integer enchantment = ((NBTTagCompound) enchantments.get(i)).getInteger("id");
					if (enchantment == 1 || enchantment == 3 || enchantment == 4 || enchantment == 17 || enchantment == 18)
					{
						if (Research.canResearch(event.entityPlayer, ResearchTypes.VitaeDisenchanter))
						{
							Research.unlockResearchSynced(event.entityPlayer, ResearchTypes.VitaeDisenchanter, Side.CLIENT, true);
						}
					}
				}
			}
		}
	}
}
