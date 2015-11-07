/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.handler;

import java.util.concurrent.TimeUnit;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLContext;

import com.DavidM1A2.AfraidOfTheDark.client.settings.ClientData;
import com.DavidM1A2.AfraidOfTheDark.common.dimension.nightmare.NightmareTeleporter;
import com.DavidM1A2.AfraidOfTheDark.common.dimension.voidChest.VoidChestTeleporter;
import com.DavidM1A2.AfraidOfTheDark.common.entities.DeeeSyft.EntityDeeeSyft;
import com.DavidM1A2.AfraidOfTheDark.common.entities.Enaria.EntityEnaria;
import com.DavidM1A2.AfraidOfTheDark.common.initializeMod.ModPotionEffects;
import com.DavidM1A2.AfraidOfTheDark.common.item.ItemFlaskOfSouls;
import com.DavidM1A2.AfraidOfTheDark.common.playerData.AOTDEntityData;
import com.DavidM1A2.AfraidOfTheDark.common.playerData.AOTDPlayerData;
import com.DavidM1A2.AfraidOfTheDark.common.refrence.Constants;
import com.DavidM1A2.AfraidOfTheDark.common.refrence.ResearchTypes;
import com.DavidM1A2.AfraidOfTheDark.common.utility.NBTHelper;
import com.DavidM1A2.AfraidOfTheDark.common.utility.Utility;

import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemFlintAndSteel;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.potion.Potion;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.MathHelper;
import net.minecraftforge.client.event.EntityViewRenderEvent.FogColors;
import net.minecraftforge.client.event.EntityViewRenderEvent.FogDensity;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.entity.EntityEvent.EntityConstructing;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.EntityInteractEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerSleepInBedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.ItemCraftedEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class PlayerController
{
	// When the player dies, he is cloned. We move over extended properties such as hasStartedAOTD, insanity, and research
	@SubscribeEvent
	public void onClonePlayer(final PlayerEvent.Clone event)
	{
		NBTTagCompound nbt = new NBTTagCompound();
		AOTDPlayerData.get(event.original).saveNBTData(nbt);
		AOTDEntityData.get(event.original).saveNBTData(nbt);

		AOTDPlayerData.get(event.entityPlayer).loadNBTData(nbt);
		AOTDEntityData.get(event.entityPlayer).loadNBTData(nbt);

		if (event.original.dimension == Constants.NightmareWorld.NIGHTMARE_WORLD_ID)
		{
			Constants.TIMER_FOR_DELAYS.schedule(new Runnable()
			{
				@Override
				public void run()
				{
					Utility.sendPlayerToDimension((EntityPlayerMP) event.entityPlayer, 0, false, NightmareTeleporter.class);
				}
			}, 900, TimeUnit.MILLISECONDS);
		}
		else if (event.original.dimension == Constants.VoidChestWorld.VOID_CHEST_WORLD_ID)
		{
			Constants.TIMER_FOR_DELAYS.schedule(new Runnable()
			{
				@Override
				public void run()
				{
					Utility.sendPlayerToDimension((EntityPlayerMP) event.entityPlayer, 0, false, VoidChestTeleporter.class);
				}
			}, 900, TimeUnit.MILLISECONDS);
		}
	}

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void renderEventFogColor(final FogColors event)
	{
		if (event.entity instanceof EntityPlayer)
		{
			final float insanity = (float) AOTDPlayerData.get((EntityPlayer) event.entity).getPlayerInsanity();

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
			AOTDPlayerData.get((EntityPlayer) event.entity).requestSyncAll();
			AOTDEntityData.get((EntityPlayer) event.entity).requestSyncAll();
		}
		else if (event.entity instanceof EntityEnaria)
		{
			if (!event.entity.getEntityData().getBoolean(EntityEnaria.IS_VALID))
			{
				event.entity.killCommand();
			}
		}
	}

	@SubscribeEvent
	public void onEntityConstructing(final EntityConstructing event)
	{
		if (event.entity instanceof EntityPlayer)
		{
			AOTDPlayerData.register((EntityPlayer) event.entity);
		}

		if (event.entity instanceof EntityLivingBase && !(event.entity instanceof EntityArmorStand))
		{
			AOTDEntityData.register(event.entity);
		}
	}

	@SubscribeEvent
	public void onPlayerSleepInBedEvent(PlayerSleepInBedEvent event)
	{
		if (!event.entityPlayer.worldObj.isRemote)
		{
			if (event.entityPlayer.getActivePotionEffect(ModPotionEffects.sleepingPotion) != null)
			{
				if (AOTDPlayerData.get(event.entityPlayer).canResearch(ResearchTypes.Nightmares))
				{
					AOTDPlayerData.get(event.entityPlayer).unlockResearch(ResearchTypes.Nightmares, true);
				}
				if (AOTDPlayerData.get(event.entityPlayer).isResearched(ResearchTypes.Nightmares))
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
						if (AOTDPlayerData.get(event.entityPlayer).canResearch(ResearchTypes.VitaeDisenchanter))
						{
							AOTDPlayerData.get(event.entityPlayer).unlockResearch(ResearchTypes.VitaeDisenchanter, true);
						}
					}
				}
			}
		}
	}

	@SubscribeEvent
	public void onItemCraftedEvent(ItemCraftedEvent event)
	{
		if (event.crafting.getItem() instanceof ItemFlaskOfSouls)
		{
			if (!event.player.worldObj.isRemote)
			{
				if (AOTDPlayerData.get(event.player).canResearch(ResearchTypes.PhylacteryOfSouls))
				{
					AOTDPlayerData.get(event.player).unlockResearch(ResearchTypes.PhylacteryOfSouls, true);
				}
			}
		}
	}

	@SubscribeEvent
	public void onLivingDeathEvent(LivingDeathEvent event)
	{
		if (event.source.getEntity() instanceof EntityPlayer)
		{
			if (ItemFlaskOfSouls.flaskKillRequirements.containsKey(event.entityLiving.getClass().getSimpleName()))
			{
				EntityPlayer entityPlayer = (EntityPlayer) event.source.getEntity();
				for (int i = 0; i < 9; i++)
				{
					if (entityPlayer.inventory.mainInventory[i] != null)
					{
						ItemStack itemStack = entityPlayer.inventory.mainInventory[i];
						if (itemStack.getItem() instanceof ItemFlaskOfSouls)
						{
							if (AOTDPlayerData.get(entityPlayer).isResearched(ResearchTypes.PhylacteryOfSouls))
							{
								if (NBTHelper.getString(itemStack, ItemFlaskOfSouls.FLASK_TYPE).equals(""))
								{
									NBTHelper.setString(itemStack, ItemFlaskOfSouls.FLASK_TYPE, event.entityLiving.getClass().getSimpleName());
									NBTHelper.setInteger(itemStack, ItemFlaskOfSouls.KILLS, 1);
									break;
								}
								else if (NBTHelper.getString(itemStack, ItemFlaskOfSouls.FLASK_TYPE).equals(event.entityLiving.getClass().getSimpleName()))
								{
									if (itemStack.getItemDamage() == 0)
									{
										int newKills = NBTHelper.getInt(itemStack, ItemFlaskOfSouls.KILLS) + 1;
										if (newKills == ItemFlaskOfSouls.flaskKillRequirements.get(event.entityLiving.getClass().getSimpleName()))
										{
											itemStack.setItemDamage(1);
											NBTHelper.setInteger(itemStack, ItemFlaskOfSouls.KILLS, newKills);
										}
										else
										{
											NBTHelper.setInteger(itemStack, ItemFlaskOfSouls.KILLS, newKills);
										}
										break;
									}
								}
							}
							else
							{
								if (!entityPlayer.worldObj.isRemote)
								{
									entityPlayer.addChatMessage(new ChatComponentText("This flask is trying to interact with the kill I just got but something's wrong."));
								}
								break;
							}
						}
					}
				}
			}
		}
	}
}
