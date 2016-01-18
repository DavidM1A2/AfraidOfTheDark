/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.handler;

import org.lwjgl.input.Keyboard;

import com.DavidM1A2.AfraidOfTheDark.AfraidOfTheDark;
import com.DavidM1A2.AfraidOfTheDark.client.gui.GuiHandler;
import com.DavidM1A2.AfraidOfTheDark.client.settings.Keybindings;
import com.DavidM1A2.AfraidOfTheDark.common.initializeMod.ModItems;
import com.DavidM1A2.AfraidOfTheDark.common.item.ItemCloakOfAgility;
import com.DavidM1A2.AfraidOfTheDark.common.item.ItemVitaeLantern;
import com.DavidM1A2.AfraidOfTheDark.common.item.crossbow.ItemWristCrossbow;
import com.DavidM1A2.AfraidOfTheDark.common.packets.FireCrossbowBolt;
import com.DavidM1A2.AfraidOfTheDark.common.packets.SyncKeyPress;
import com.DavidM1A2.AfraidOfTheDark.common.refrence.AOTDCrossbowBoltTypes;
import com.DavidM1A2.AfraidOfTheDark.common.refrence.Refrence;
import com.DavidM1A2.AfraidOfTheDark.common.refrence.ResearchTypes;
import com.DavidM1A2.AfraidOfTheDark.common.savedData.AOTDPlayerData;
import com.DavidM1A2.AfraidOfTheDark.common.utility.NBTHelper;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.Vec3;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;

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
		if (AOTDPlayerData.get(Minecraft.getMinecraft().thePlayer).getSpellManager().doesKeyMapToSpell(Keyboard.getEventCharacter()))
		{
			this.spellKeyPressed();
		}
	}

	private void spellKeyPressed()
	{
		AfraidOfTheDark.getPacketHandler().sendToServer(new SyncKeyPress(Keyboard.getEventCharacter(), Keyboard.getEventKey()));
	}

	private void changeLanternMode()
	{
		EntityPlayer entityPlayer = Minecraft.getMinecraft().thePlayer;

		if (AOTDPlayerData.get(entityPlayer).isResearched(ResearchTypes.VitaeLanternI))
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
		AOTDCrossbowBoltTypes currentlySelected = AOTDCrossbowBoltTypes.getTypeFromID(AOTDPlayerData.get(entityPlayer).getSelectedWristCrossbowBolt());
		if (!entityPlayer.isSneaking())
		{
			if (entityPlayer.inventory.hasItem(ModItems.wristCrossbow) && AOTDPlayerData.get(entityPlayer).isResearched(ResearchTypes.WristCrossbow))
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
								if (entityPlayer.inventory.hasItem(currentlySelected.getMyBoltItem()) || entityPlayer.capabilities.isCreativeMode)
								{
									AfraidOfTheDark.getPacketHandler().sendToServer(new FireCrossbowBolt(currentlySelected));
									current.setOnCooldown();
									break;
								}
								else
								{
									entityPlayer.addChatMessage(new ChatComponentText("I'll need at least one " + currentlySelected.formattedString() + "bolt in my inventory to shoot."));
								}
							}
							else
							{
								entityPlayer.addChatMessage(new ChatComponentText("Crossbow still reloading..."));
							}
						}
					}
				}
			}
			else if (!AOTDPlayerData.get(entityPlayer).isResearched(ResearchTypes.WristCrossbow))
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
			AOTDPlayerData.get(entityPlayer).setSelectedWristCrossbowBolt(AOTDCrossbowBoltTypes.getIDFromType(currentlySelected.next()));
			AOTDPlayerData.get(entityPlayer).syncSelectedWristCrossbowBolt();
			entityPlayer.addChatMessage(new ChatComponentText("Crossbow will now fire " + currentlySelected.next().formattedString() + "bolts."));
		}
	}

	private void rollWithCloakOfAgility()
	{
		EntityPlayer entityPlayer = Minecraft.getMinecraft().thePlayer;
		boolean willRoll = false;
		if (entityPlayer.inventory.hasItem(ModItems.cloakOfAgility) && entityPlayer.onGround && AOTDPlayerData.get(entityPlayer).isResearched(ResearchTypes.CloakOfAgility))
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
		else if (!AOTDPlayerData.get(entityPlayer).isResearched(ResearchTypes.CloakOfAgility))
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
