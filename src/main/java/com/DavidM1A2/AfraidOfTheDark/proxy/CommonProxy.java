/*
 * Author: David Slovikosky Mod: Afraid of the Dark Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.proxy;

import com.DavidM1A2.AfraidOfTheDark.AfraidOfTheDark;
import com.DavidM1A2.AfraidOfTheDark.common.packets.FireCrossbowBolt;
import com.DavidM1A2.AfraidOfTheDark.common.packets.FlaskOfSoulsUpdate;
import com.DavidM1A2.AfraidOfTheDark.common.packets.RotatePlayer;
import com.DavidM1A2.AfraidOfTheDark.common.packets.SyncParticleFX;
import com.DavidM1A2.AfraidOfTheDark.common.packets.SyncVoidChest;
import com.DavidM1A2.AfraidOfTheDark.common.packets.TellClientToPlayAnimation;
import com.DavidM1A2.AfraidOfTheDark.common.packets.TellServerToCreateMeteor;
import com.DavidM1A2.AfraidOfTheDark.common.packets.UpdateAOTDStatus;
import com.DavidM1A2.AfraidOfTheDark.common.packets.UpdateCrossbow;
import com.DavidM1A2.AfraidOfTheDark.common.packets.UpdateInsanity;
import com.DavidM1A2.AfraidOfTheDark.common.packets.UpdateLanternState;
import com.DavidM1A2.AfraidOfTheDark.common.packets.UpdateResearch;
import com.DavidM1A2.AfraidOfTheDark.common.packets.UpdateVitae;
import com.DavidM1A2.AfraidOfTheDark.common.refrence.Constants;

import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.relauncher.Side;

// Common proxy does both Client and Server registrations
public abstract class CommonProxy implements IProxy
{
	// Here we register packets and a channel
	@Override
	public void registerChannel()
	{
		AfraidOfTheDark.setSimpleNetworkWrapper(NetworkRegistry.INSTANCE.newSimpleChannel("AOTD Packets"));
		AfraidOfTheDark.getSimpleNetworkWrapper().registerMessage(UpdateResearch.HandlerServer.class, UpdateResearch.class, Constants.Packets.PACKET_ID_RESEARCH_UPDATE, Side.SERVER);
		AfraidOfTheDark.getSimpleNetworkWrapper().registerMessage(UpdateCrossbow.Handler.class, UpdateCrossbow.class, Constants.Packets.PACKET_ID_CROSSBOW, Side.SERVER);
		AfraidOfTheDark.getSimpleNetworkWrapper().registerMessage(UpdateAOTDStatus.HandlerServer.class, UpdateAOTDStatus.class, Constants.Packets.PACKET_ID_HAS_STARTED_AOTD_UPDATE, Side.SERVER);
		AfraidOfTheDark.getSimpleNetworkWrapper().registerMessage(UpdateInsanity.Handler.class, UpdateInsanity.class, Constants.Packets.PACKET_ID_INSANITY_UPDATE, Side.SERVER);
		AfraidOfTheDark.getSimpleNetworkWrapper().registerMessage(TellServerToCreateMeteor.HandlerServer.class, TellServerToCreateMeteor.class, Constants.Packets.PACKET_ID_CREATE_METEOR, Side.SERVER);
		AfraidOfTheDark.getSimpleNetworkWrapper().registerMessage(UpdateVitae.HandlerServer.class, UpdateVitae.class, Constants.Packets.PACKET_ID_VITAE_UPDATE, Side.SERVER);
		AfraidOfTheDark.getSimpleNetworkWrapper().registerMessage(FireCrossbowBolt.HandlerServer.class, FireCrossbowBolt.class, Constants.Packets.PACKET_ID_FIRE_BOLT, Side.SERVER);
		AfraidOfTheDark.getSimpleNetworkWrapper().registerMessage(RotatePlayer.HandlerClient.class, RotatePlayer.class, Constants.Packets.PACKET_ID_ROTATE_PLAYER_UPDATE, Side.CLIENT);
		AfraidOfTheDark.getSimpleNetworkWrapper().registerMessage(TellClientToPlayAnimation.HandlerClient.class, TellClientToPlayAnimation.class, Constants.Packets.PACKET_ID_PLAY_ANIMATION, Side.CLIENT);
		AfraidOfTheDark.getSimpleNetworkWrapper().registerMessage(UpdateLanternState.HandlerServer.class, UpdateLanternState.class, Constants.Packets.PACKET_ID_UPDATE_LANTERN_STATE, Side.SERVER);
		AfraidOfTheDark.getSimpleNetworkWrapper().registerMessage(SyncVoidChest.HandlerClient.class, SyncVoidChest.class, Constants.Packets.PACKET_ID_OPEN_CHEST, Side.CLIENT);
		AfraidOfTheDark.getSimpleNetworkWrapper().registerMessage(FlaskOfSoulsUpdate.HandlerClient.class, FlaskOfSoulsUpdate.class, Constants.Packets.PACKET_ID_UPDATE_FLASK, Side.CLIENT);
		AfraidOfTheDark.getSimpleNetworkWrapper().registerMessage(SyncParticleFX.HandlerClient.class, SyncParticleFX.class, Constants.Packets.PACKET_ID_SYNC_PARTICLEFX, Side.CLIENT);
	}
}
