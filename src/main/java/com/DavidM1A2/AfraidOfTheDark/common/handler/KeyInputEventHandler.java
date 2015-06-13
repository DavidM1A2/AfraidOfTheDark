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
import com.DavidM1A2.AfraidOfTheDark.client.settings.Keybindings;
import com.DavidM1A2.AfraidOfTheDark.common.initializeMod.ModItems;
import com.DavidM1A2.AfraidOfTheDark.common.item.ItemCloakOfAgility;
import com.DavidM1A2.AfraidOfTheDark.common.item.crossbow.ItemWristCrossbow;
import com.DavidM1A2.AfraidOfTheDark.common.packets.FireCrossbowBolt;
import com.DavidM1A2.AfraidOfTheDark.common.playerData.LoadResearchData;
import com.DavidM1A2.AfraidOfTheDark.common.refrence.ClientData;
import com.DavidM1A2.AfraidOfTheDark.common.refrence.ResearchTypes;

public class KeyInputEventHandler
{
	@SubscribeEvent
	public void handleKeyInputEvent(final InputEvent.KeyInputEvent event)
	{
		if (Keybindings.rollWithCloakOfAgility.isPressed())
		{
			this.rollWithCloakOfAgility();
		}
		else if (Keybindings.fireWristCrossbow.isPressed())
		{
			this.fireWristCrossbow();
		}
	}

	private void fireWristCrossbow()
	{
		EntityPlayer entityPlayer = Minecraft.getMinecraft().thePlayer;
		if (!entityPlayer.isSneaking())
		{
			boolean willFire = false;
			if (entityPlayer.inventory.hasItem(ModItems.wristCrossbow) && LoadResearchData.isResearched(entityPlayer, ResearchTypes.Crossbow))
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
								willFire = true;
								current.setOnCooldown();
								break;
							}
						}
					}
				}
				if (willFire)
				{
					AfraidOfTheDark.getSimpleNetworkWrapper().sendToServer(new FireCrossbowBolt(ClientData.currentlySelectedBolt));
					willFire = false;
				}
				else
				{
					entityPlayer.addChatMessage(new ChatComponentText("Crossbow still reloading..."));
				}
			}
			else if (!LoadResearchData.isResearched(entityPlayer, ResearchTypes.Crossbow))
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
