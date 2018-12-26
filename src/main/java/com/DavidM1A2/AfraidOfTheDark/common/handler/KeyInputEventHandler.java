/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.handler;

import com.DavidM1A2.AfraidOfTheDark.common.initializeMod.ModSounds;
import net.minecraft.util.SoundCategory;
import org.lwjgl.input.Keyboard;

import com.DavidM1A2.AfraidOfTheDark.AfraidOfTheDark;
import com.DavidM1A2.AfraidOfTheDark.client.settings.Keybindings;
import com.DavidM1A2.AfraidOfTheDark.common.initializeMod.ModCapabilities;
import com.DavidM1A2.AfraidOfTheDark.common.initializeMod.ModItems;
import com.DavidM1A2.AfraidOfTheDark.common.item.ItemCloakOfAgility;
import com.DavidM1A2.AfraidOfTheDark.common.item.crossbow.ItemWristCrossbow;
import com.DavidM1A2.AfraidOfTheDark.common.packets.FireCrossbowBolt;
import com.DavidM1A2.AfraidOfTheDark.common.packets.SyncKeyPress;
import com.DavidM1A2.AfraidOfTheDark.common.reference.AOTDCrossbowBoltTypes;
import com.DavidM1A2.AfraidOfTheDark.common.reference.ResearchTypes;
import com.DavidM1A2.AfraidOfTheDark.common.utility.Utility;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentString;
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
		if (Keyboard.getEventKeyState() && Minecraft.getMinecraft().player.getCapability(ModCapabilities.PLAYER_DATA, null).getSpellManager().doesKeyMapToSpell(Keyboard.getKeyName(Keyboard.getEventKey())))
		{
			this.spellKeyPressed();
		}
	}

	private void spellKeyPressed()
	{
		AfraidOfTheDark.instance.getPacketHandler().sendToServer(new SyncKeyPress(Keyboard.getKeyName(Keyboard.getEventKey())));
	}

	private void fireWristCrossbow()
	{
		EntityPlayer entityPlayer = Minecraft.getMinecraft().player;
		AOTDCrossbowBoltTypes currentlySelected = AOTDCrossbowBoltTypes.getTypeFromID(entityPlayer.getCapability(ModCapabilities.PLAYER_DATA, null).getSelectedWristCrossbowBolt());
		if (!entityPlayer.isSneaking())
		{
			if (Utility.hasItem(entityPlayer, ModItems.wristCrossbow) && entityPlayer.getCapability(ModCapabilities.PLAYER_DATA, null).isResearched(ResearchTypes.WristCrossbow))
			{
				for (ItemStack itemStack : entityPlayer.inventory.mainInventory)
				{
					if (itemStack != null)
					{
						if (itemStack.getItem() instanceof ItemWristCrossbow)
						{
							ItemWristCrossbow current = (ItemWristCrossbow) itemStack.getItem();
							if (!current.isOnCooldown(itemStack))
							{
								if (Utility.hasItem(entityPlayer, currentlySelected.getMyBoltItem()) || entityPlayer.capabilities.isCreativeMode)
								{
									AfraidOfTheDark.instance.getPacketHandler().sendToServer(new FireCrossbowBolt(currentlySelected));
									current.setOnCooldown(itemStack, entityPlayer);
									entityPlayer.world.playSound(entityPlayer, entityPlayer.posX, entityPlayer.posY, entityPlayer.posZ, ModSounds.crossbowFire, SoundCategory.MASTER, 0.5F, ((entityPlayer.world.rand.nextFloat() * 0.4F) + 0.8F));
									break;
								}
								else
								{
									entityPlayer.sendMessage(new TextComponentString("I'll need at least one " + currentlySelected.formattedString() + "bolt in my inventory to shoot."));
									break;
								}
							}
							else
							{
								entityPlayer.sendMessage(new TextComponentString("Crossbow still reloading..."));
								break;
							}
						}
					}
				}
			}
			else if (!entityPlayer.getCapability(ModCapabilities.PLAYER_DATA, null).isResearched(ResearchTypes.WristCrossbow))
			{
				entityPlayer.sendMessage(new TextComponentString("I don't understand how this works."));
			}
			else if (!Utility.hasItem(entityPlayer, ModItems.wristCrossbow))
			{
				entityPlayer.sendMessage(new TextComponentString("I'll need a Wrist Crossbow in my inventory to use this."));
			}
		}
		else
		{
			entityPlayer.getCapability(ModCapabilities.PLAYER_DATA, null).setSelectedWristCrossbowBolt(AOTDCrossbowBoltTypes.getIDFromType(currentlySelected.next()));
			entityPlayer.getCapability(ModCapabilities.PLAYER_DATA, null).syncSelectedWristCrossbowBolt();
			entityPlayer.sendMessage(new TextComponentString("Crossbow will now fire " + currentlySelected.next().formattedString() + "bolts."));
		}
	}

	private void rollWithCloakOfAgility()
	{
		EntityPlayer entityPlayer = Minecraft.getMinecraft().player;
		boolean willRoll = false;
		if (Utility.hasItem(entityPlayer, ModItems.cloakOfAgility) && entityPlayer.onGround && entityPlayer.getCapability(ModCapabilities.PLAYER_DATA, null).isResearched(ResearchTypes.CloakOfAgility))
		{
			for (ItemStack itemStack : entityPlayer.inventory.mainInventory)
			{
				if (itemStack != null)
				{
					if (itemStack.getItem() instanceof ItemCloakOfAgility)
					{
						ItemCloakOfAgility current = (ItemCloakOfAgility) itemStack.getItem();
						if (!current.isOnCooldown(itemStack))
						{
							willRoll = true;
							current.setOnCooldown(itemStack, entityPlayer);
							break;
						}
					}
				}
			}
			if (willRoll)
			{
				Vec3d lookDirection = entityPlayer.getLookVec();
				double rollVelocity = 15;
				entityPlayer.motionX = entityPlayer.motionX * rollVelocity;
				entityPlayer.motionY = 0.2;
				entityPlayer.motionZ = entityPlayer.motionZ * rollVelocity;
				willRoll = false;
			}
			else
			{
				entityPlayer.sendMessage(new TextComponentString("I'm too tired to roll again."));
			}
		}
		else if (!entityPlayer.getCapability(ModCapabilities.PLAYER_DATA, null).isResearched(ResearchTypes.CloakOfAgility))
		{
			entityPlayer.sendMessage(new TextComponentString("I don't understand how this works."));
		}
		else if (!Utility.hasItem(entityPlayer, ModItems.cloakOfAgility))
		{
			entityPlayer.sendMessage(new TextComponentString("I'll need a Cloak of Agility in my inventory to use this."));
		}
		else if (!entityPlayer.onGround)
		{
			entityPlayer.sendMessage(new TextComponentString("I need to be on the ground to roll."));
		}
	}
}
