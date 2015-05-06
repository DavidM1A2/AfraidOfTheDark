/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.proxy;

import com.DavidM1A2.AfraidOfTheDark.AfraidOfTheDark;
import com.DavidM1A2.AfraidOfTheDark.packets.UpdateAOTDStatus;
import com.DavidM1A2.AfraidOfTheDark.packets.UpdateCrossbow;
import com.DavidM1A2.AfraidOfTheDark.packets.UpdateInsanity;
import com.DavidM1A2.AfraidOfTheDark.refrence.Refrence;

import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.relauncher.Side;

// Common proxy does both Client and Server registrations
public abstract class CommonProxy implements IProxy
{
	// Here we register packets and a channel
	public void registerChannel()
	{
		AfraidOfTheDark.channelNew = NetworkRegistry.INSTANCE.newSimpleChannel("AOTD Packets");
		AfraidOfTheDark.channelNew.registerMessage(UpdateCrossbow.Handler.class, UpdateCrossbow.class, Refrence.PACKET_ID_CROSSBOW, Side.SERVER);
		AfraidOfTheDark.channelNew.registerMessage(UpdateAOTDStatus.HandlerServer.class, UpdateAOTDStatus.class, Refrence.PACKET_ID_HAS_STARTED_AOTD_UPDATE_SERVER, Side.SERVER);
		AfraidOfTheDark.channelNew.registerMessage(UpdateAOTDStatus.HandlerClient.class, UpdateAOTDStatus.class, Refrence.PACKET_ID_HAS_STARTED_AOTD_UPDATE_CLIENT, Side.CLIENT);
		AfraidOfTheDark.channelNew.registerMessage(UpdateInsanity.Handler.class, UpdateInsanity.class, Refrence.PACKET_ID_INSANITY_UPDATE, Side.CLIENT);
	}
}
