/*
 * Author: David Slovikosky Mod: Afraid of the Dark Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.proxy;

import com.DavidM1A2.AfraidOfTheDark.AfraidOfTheDark;
import com.DavidM1A2.AfraidOfTheDark.common.packets.FireCrossbowBolt;
import com.DavidM1A2.AfraidOfTheDark.common.packets.RotatePlayer;
import com.DavidM1A2.AfraidOfTheDark.common.packets.SpawnMeteor;
import com.DavidM1A2.AfraidOfTheDark.common.packets.SyncAOTDEntityData;
import com.DavidM1A2.AfraidOfTheDark.common.packets.SyncAOTDPlayerData;
import com.DavidM1A2.AfraidOfTheDark.common.packets.SyncAnimation;
import com.DavidM1A2.AfraidOfTheDark.common.packets.SyncFlaskOfSouls;
import com.DavidM1A2.AfraidOfTheDark.common.packets.SyncKeyPress;
import com.DavidM1A2.AfraidOfTheDark.common.packets.SyncParticleFX;
import com.DavidM1A2.AfraidOfTheDark.common.packets.SyncSelectedWristCrossbowBolt;
import com.DavidM1A2.AfraidOfTheDark.common.packets.SyncVoidChest;
import com.DavidM1A2.AfraidOfTheDark.common.packets.UpdateAOTDStatus;
import com.DavidM1A2.AfraidOfTheDark.common.packets.UpdateHasBeatenEnaria;
import com.DavidM1A2.AfraidOfTheDark.common.packets.UpdateInsanity;
import com.DavidM1A2.AfraidOfTheDark.common.packets.UpdateLanternState;
import com.DavidM1A2.AfraidOfTheDark.common.packets.UpdateResearch;
import com.DavidM1A2.AfraidOfTheDark.common.packets.UpdateVitae;

import net.minecraftforge.fml.relauncher.Side;

// Common proxy does both Client and Server registrations
public abstract class CommonProxy implements IProxy
{
	// Here we register packets and a channel
	@Override
	public void registerChannel()
	{
		AfraidOfTheDark.getPacketHandler().registerBidiPacket(UpdateResearch.class, new UpdateResearch.Handler());
		AfraidOfTheDark.getPacketHandler().registerBidiPacket(UpdateAOTDStatus.class, new UpdateAOTDStatus.Handler());
		AfraidOfTheDark.getPacketHandler().registerPacket(UpdateInsanity.class, new UpdateInsanity.Handler(), Side.CLIENT);
		AfraidOfTheDark.getPacketHandler().registerPacket(SpawnMeteor.class, new SpawnMeteor.Handler(), Side.SERVER);
		AfraidOfTheDark.getPacketHandler().registerBidiPacket(UpdateVitae.class, new UpdateVitae.Handler());
		AfraidOfTheDark.getPacketHandler().registerPacket(FireCrossbowBolt.class, new FireCrossbowBolt.Handler(), Side.SERVER);
		AfraidOfTheDark.getPacketHandler().registerPacket(RotatePlayer.class, new RotatePlayer.Handler(), Side.CLIENT);
		AfraidOfTheDark.getPacketHandler().registerPacket(SyncAnimation.class, new SyncAnimation.Handler(), Side.CLIENT);
		AfraidOfTheDark.getPacketHandler().registerBidiPacket(UpdateLanternState.class, new UpdateLanternState.Handler());
		AfraidOfTheDark.getPacketHandler().registerPacket(SyncVoidChest.class, new SyncVoidChest.Handler(), Side.CLIENT);
		AfraidOfTheDark.getPacketHandler().registerPacket(SyncFlaskOfSouls.class, new SyncFlaskOfSouls.Handler(), Side.CLIENT);
		AfraidOfTheDark.getPacketHandler().registerPacket(SyncParticleFX.class, new SyncParticleFX.Handler(), Side.CLIENT);
		AfraidOfTheDark.getPacketHandler().registerBidiPacket(SyncAOTDPlayerData.class, new SyncAOTDPlayerData.Handler());
		AfraidOfTheDark.getPacketHandler().registerBidiPacket(SyncAOTDEntityData.class, new SyncAOTDEntityData.Handler());
		AfraidOfTheDark.getPacketHandler().registerPacket(UpdateHasBeatenEnaria.class, new UpdateHasBeatenEnaria.Handler(), Side.CLIENT);
		AfraidOfTheDark.getPacketHandler().registerPacket(SyncSelectedWristCrossbowBolt.class, new SyncSelectedWristCrossbowBolt.Handler(), Side.SERVER);
		AfraidOfTheDark.getPacketHandler().registerPacket(SyncKeyPress.class, new SyncKeyPress.Handler(), Side.SERVER);
	}
}
