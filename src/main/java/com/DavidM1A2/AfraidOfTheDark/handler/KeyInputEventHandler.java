/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.handler;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.Vec3;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;

import com.DavidM1A2.AfraidOfTheDark.client.settings.Keybindings;
import com.DavidM1A2.AfraidOfTheDark.initializeMod.ModItems;
import com.DavidM1A2.AfraidOfTheDark.playerData.LoadResearchData;
import com.DavidM1A2.AfraidOfTheDark.refrence.ResearchTypes;

public class KeyInputEventHandler
{
	@SubscribeEvent
	public void handleKeyInputEvent(final InputEvent.KeyInputEvent event)
	{
		if (Keybindings.rollWithCloakOfAgility.isPressed())
		{
			EntityPlayer entityPlayer = Minecraft.getMinecraft().thePlayer;
			if (entityPlayer.inventory.hasItem(ModItems.cloakOfAgility) && entityPlayer.onGround && LoadResearchData.isResearched(entityPlayer, ResearchTypes.CloakOfAgility))
			{
				Vec3 lookDirection = entityPlayer.getLookVec();
				double rollVelocity = 15;
				entityPlayer.motionX = entityPlayer.motionX * rollVelocity;
				entityPlayer.motionY = 0.2;
				entityPlayer.motionZ = entityPlayer.motionZ * rollVelocity;
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
}
