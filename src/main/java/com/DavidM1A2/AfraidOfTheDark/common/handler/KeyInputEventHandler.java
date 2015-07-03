/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.handler;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.Vec3;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;

import com.DavidM1A2.AfraidOfTheDark.AfraidOfTheDark;
import com.DavidM1A2.AfraidOfTheDark.client.gui.GuiHandler;
import com.DavidM1A2.AfraidOfTheDark.client.settings.ClientData;
import com.DavidM1A2.AfraidOfTheDark.client.settings.Keybindings;
import com.DavidM1A2.AfraidOfTheDark.common.initializeMod.ModItems;
import com.DavidM1A2.AfraidOfTheDark.common.item.ItemCloakOfAgility;
import com.DavidM1A2.AfraidOfTheDark.common.item.ItemVitaeLantern;
import com.DavidM1A2.AfraidOfTheDark.common.item.crossbow.ItemWristCrossbow;
import com.DavidM1A2.AfraidOfTheDark.common.packets.FireCrossbowBolt;
import com.DavidM1A2.AfraidOfTheDark.common.playerData.LoadResearchData;
import com.DavidM1A2.AfraidOfTheDark.common.refrence.Refrence;
import com.DavidM1A2.AfraidOfTheDark.common.refrence.ResearchTypes;
import com.DavidM1A2.AfraidOfTheDark.common.utility.NBTHelper;

public class KeyInputEventHandler
{
	public boolean changeLanternModeActive = false;

	@SubscribeEvent
	public void handleKeyInputEvent(final InputEvent.KeyInputEvent event)
	{
		if (Keybindings.rollWithCloakOfAgility.isPressed())
		{
			this.rollWithCloakOfAgility();
		}
		if (Keybindings.fireWristCrossbow.isPressed())
		{
			this.fireWristCrossbow();
		}
		if (Keybindings.changeLanternMode.isPressed())
		{
			this.changeLanternMode();
		}
	}

	private void changeLanternMode()
	{
		EntityPlayer entityPlayer = Minecraft.getMinecraft().thePlayer;

		if (LoadResearchData.isResearched(entityPlayer, ResearchTypes.VitaeLanternI))
		{
			boolean hasLantern = false;
			for (ItemStack itemStack : entityPlayer.inventory.mainInventory)
			{
				if (itemStack != null)
				{
					if (itemStack.getItem() instanceof ItemVitaeLantern)
					{
						if (NBTHelper.getBoolean(itemStack, "isActive"))
						{
							entityPlayer.openGui(Refrence.MOD_ID, GuiHandler.VITAE_LANTERN_ID, entityPlayer.worldObj, entityPlayer.getPosition().getX(), entityPlayer.getPosition().getY(), entityPlayer.getPosition().getZ());
							hasLantern = true;
							break;
						}
					}
				}
			}
			if (!hasLantern)
			{
				entityPlayer.addChatMessage(new ChatComponentText("I'll need an active vitae lantern in my inventory to use this."));
			}
		}
		else
		{
			entityPlayer.addChatMessage(new ChatComponentText("I don't know what this could be useful for."));
		}
	}

	private void fireWristCrossbow()
	{
		EntityPlayer entityPlayer = Minecraft.getMinecraft().thePlayer;
		if (!entityPlayer.isSneaking())
		{
			boolean willFire = false;
			if (entityPlayer.inventory.hasItem(ModItems.wristCrossbow) && LoadResearchData.isResearched(entityPlayer, ResearchTypes.WristCrossbow))
			{
				for (ItemStack itemStack : entityPlayer.inventory.mainInventory)
				{
					if (itemStack != null)
					{
						if (itemStack.getItem() instanceof ItemWristCrossbow)
						{
							ItemWristCrossbow current = (ItemWristCrossbow) itemStack.getItem();
							if (!current.isOnCooldown())
							{
								if (entityPlayer.inventory.hasItem(ClientData.currentlySelectedBolt.getMyBoltItem()) || entityPlayer.capabilities.isCreativeMode)
								{
									willFire = true;
									current.setOnCooldown();
									break;
								}
								else
								{
									entityPlayer.addChatMessage(new ChatComponentText("No bolts of type " + ClientData.currentlySelectedBolt + " found."));
								}
							}
							else
							{
								entityPlayer.addChatMessage(new ChatComponentText("Crossbow still reloading..."));
							}
						}
					}
				}
				if (willFire)
				{
					AfraidOfTheDark.getSimpleNetworkWrapper().sendToServer(new FireCrossbowBolt(ClientData.currentlySelectedBolt));
				}
			}
			else if (!LoadResearchData.isResearched(entityPlayer, ResearchTypes.WristCrossbow))
			{
				entityPlayer.addChatMessage(new ChatComponentText("I don't understand how this works."));
			}
			else if (!entityPlayer.inventory.hasItem(ModItems.wristCrossbow))
			{
				entityPlayer.addChatMessage(new ChatComponentText("I'll need a Wrist Crossbow in my inventory to use this."));
			}
		}
		else
		{
			ClientData.currentlySelectedBolt = ClientData.currentlySelectedBolt.next();
			entityPlayer.addChatMessage(new ChatComponentText("Crossbow will now fire " + ClientData.currentlySelectedBolt + " bolts."));
		}
	}

	private void rollWithCloakOfAgility()
	{
		EntityPlayer entityPlayer = Minecraft.getMinecraft().thePlayer;
		boolean willRoll = false;
		if (entityPlayer.inventory.hasItem(ModItems.cloakOfAgility) && entityPlayer.onGround && LoadResearchData.isResearched(entityPlayer, ResearchTypes.CloakOfAgility))
		{
			for (ItemStack itemStack : entityPlayer.inventory.mainInventory)
			{
				if (itemStack != null)
				{
					if (itemStack.getItem() instanceof ItemCloakOfAgility)
					{
						ItemCloakOfAgility current = (ItemCloakOfAgility) itemStack.getItem();
						if (!current.isOnCooldown())
						{
							willRoll = true;
							current.setOnCooldown();
							break;
						}
					}
				}
			}
			if (willRoll)
			{
				Vec3 lookDirection = entityPlayer.getLookVec();
				double rollVelocity = 15;
				entityPlayer.motionX = entityPlayer.motionX * rollVelocity;
				entityPlayer.motionY = 0.2;
				entityPlayer.motionZ = entityPlayer.motionZ * rollVelocity;
				willRoll = false;
			}
			else
			{
				entityPlayer.addChatMessage(new ChatComponentText("I'm too tired to roll again."));
			}
		}
		else if (!LoadResearchData.isResearched(entityPlayer, ResearchTypes.CloakOfAgility))
		{
			entityPlayer.addChatMessage(new ChatComponentText("I don't understand how this works."));
		}
		else if (!entityPlayer.inventory.hasItem(ModItems.cloakOfAgility))
		{
			entityPlayer.addChatMessage(new ChatComponentText("I'll need a Cloak of Agility in my inventory to use this."));
		}
		else if (!entityPlayer.onGround)
		{
			entityPlayer.addChatMessage(new ChatComponentText("I need to be on the ground to roll."));
		}
	}
}
