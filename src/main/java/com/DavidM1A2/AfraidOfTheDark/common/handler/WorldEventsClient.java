/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.handler;

import com.DavidM1A2.AfraidOfTheDark.common.savedData.AOTDPlayerData;

import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent.ClientDisconnectionFromServerEvent;

public class WorldEventsClient
{
	@SubscribeEvent
	public void onPlayerLoggedOutEvent(ClientDisconnectionFromServerEvent event)
	{
		AOTDPlayerData.get(Minecraft.getMinecraft().thePlayer).syncSelectedWristCrossbowBolt();
	}
}
