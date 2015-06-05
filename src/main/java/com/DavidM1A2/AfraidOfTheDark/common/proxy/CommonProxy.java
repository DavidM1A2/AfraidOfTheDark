/*
 * Author: David Slovikosky Mod: Afraid of the Dark Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.proxy;

import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.relauncher.Side;

import com.DavidM1A2.AfraidOfTheDark.AfraidOfTheDark;
import com.DavidM1A2.AfraidOfTheDark.common.packets.RotatePlayer;
import com.DavidM1A2.AfraidOfTheDark.common.packets.TellServerToCreateMeteor;
import com.DavidM1A2.AfraidOfTheDark.common.packets.UpdateAOTDStatus;
import com.DavidM1A2.AfraidOfTheDark.common.packets.UpdateCrossbow;
import com.DavidM1A2.AfraidOfTheDark.common.packets.UpdateInsanity;
import com.DavidM1A2.AfraidOfTheDark.common.packets.UpdateResearch;
import com.DavidM1A2.AfraidOfTheDark.common.packets.UpdateVitae;
import com.DavidM1A2.AfraidOfTheDark.common.refrence.Refrence;

// Common proxy does both Client and Server registrations
public abstract class CommonProxy implements IProxy
{
	// Here we register packets and a channel
	@Override
	public void registerChannel()
	{
		AfraidOfTheDark.setSimpleNetworkWrapper(NetworkRegistry.INSTANCE.newSimpleChannel("AOTD Packets"));
		AfraidOfTheDark.getSimpleNetworkWrapper().registerMessage(UpdateResearch.HandlerServer.class, UpdateResearch.class, Refrence.PACKET_ID_RESEARCH_UPDATE, Side.SERVER);
		AfraidOfTheDark.getSimpleNetworkWrapper().registerMessage(UpdateCrossbow.Handler.class, UpdateCrossbow.class, Refrence.PACKET_ID_CROSSBOW, Side.SERVER);
		AfraidOfTheDark.getSimpleNetworkWrapper().registerMessage(UpdateAOTDStatus.HandlerServer.class, UpdateAOTDStatus.class, Refrence.PACKET_ID_HAS_STARTED_AOTD_UPDATE, Side.SERVER);
		AfraidOfTheDark.getSimpleNetworkWrapper().registerMessage(UpdateInsanity.Handler.class, UpdateInsanity.class, Refrence.PACKET_ID_INSANITY_UPDATE, Side.SERVER);
		AfraidOfTheDark.getSimpleNetworkWrapper().registerMessage(TellServerToCreateMeteor.HandlerServer.class, TellServerToCreateMeteor.class, Refrence.PACKET_ID_CREATE_METEOR, Side.SERVER);
		AfraidOfTheDark.getSimpleNetworkWrapper().registerMessage(UpdateVitae.HandlerServer.class, UpdateVitae.class, Refrence.PACKET_ID_VITAE_UPDATE, Side.SERVER);
		AfraidOfTheDark.getSimpleNetworkWrapper().registerMessage(RotatePlayer.HandlerClient.class, RotatePlayer.class, Refrence.PACKET_ID_ROTATE_PLAYER_UPDATE, Side.CLIENT);
	}
}