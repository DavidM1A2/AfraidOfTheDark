/*
 * Author: David Slovikosky Mod: Afraid of the Dark Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.proxy;

import com.DavidM1A2.AfraidOfTheDark.AfraidOfTheDark;
import com.DavidM1A2.AfraidOfTheDark.common.packets.FireCrossbowBolt;
import com.DavidM1A2.AfraidOfTheDark.common.packets.PlaySoundAtPlayer;
import com.DavidM1A2.AfraidOfTheDark.common.packets.SpawnMeteor;
import com.DavidM1A2.AfraidOfTheDark.common.packets.SyncAOTDEntityData;
import com.DavidM1A2.AfraidOfTheDark.common.packets.SyncAOTDPlayerData;
import com.DavidM1A2.AfraidOfTheDark.common.packets.SyncAnimation;
import com.DavidM1A2.AfraidOfTheDark.common.packets.SyncItemWithCooldown;
import com.DavidM1A2.AfraidOfTheDark.common.packets.SyncHasEnariasAltar;
import com.DavidM1A2.AfraidOfTheDark.common.packets.SyncKeyPress;
import com.DavidM1A2.AfraidOfTheDark.common.packets.SyncParticleFX;
import com.DavidM1A2.AfraidOfTheDark.common.packets.SyncSelectedWristCrossbowBolt;
import com.DavidM1A2.AfraidOfTheDark.common.packets.SyncSpellManager;
import com.DavidM1A2.AfraidOfTheDark.common.packets.SyncVoidChest;
import com.DavidM1A2.AfraidOfTheDark.common.packets.UpdateAOTDStatus;
import com.DavidM1A2.AfraidOfTheDark.common.packets.UpdateInsanity;
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
		AfraidOfTheDark.INSTANCE.getPacketHandler().registerBidiPacket(UpdateResearch.class, new UpdateResearch.Handler());
		AfraidOfTheDark.INSTANCE.getPacketHandler().registerBidiPacket(UpdateAOTDStatus.class, new UpdateAOTDStatus.Handler());
		AfraidOfTheDark.INSTANCE.getPacketHandler().registerPacket(UpdateInsanity.class, new UpdateInsanity.Handler(), Side.CLIENT);
		AfraidOfTheDark.INSTANCE.getPacketHandler().registerPacket(SpawnMeteor.class, new SpawnMeteor.Handler(), Side.SERVER);
		AfraidOfTheDark.INSTANCE.getPacketHandler().registerBidiPacket(UpdateVitae.class, new UpdateVitae.Handler());
		AfraidOfTheDark.INSTANCE.getPacketHandler().registerPacket(FireCrossbowBolt.class, new FireCrossbowBolt.Handler(), Side.SERVER);
		AfraidOfTheDark.INSTANCE.getPacketHandler().registerPacket(SyncAnimation.class, new SyncAnimation.Handler(), Side.CLIENT);
		AfraidOfTheDark.INSTANCE.getPacketHandler().registerPacket(SyncVoidChest.class, new SyncVoidChest.Handler(), Side.CLIENT);
		AfraidOfTheDark.INSTANCE.getPacketHandler().registerPacket(SyncItemWithCooldown.class, new SyncItemWithCooldown.Handler(), Side.CLIENT);
		AfraidOfTheDark.INSTANCE.getPacketHandler().registerPacket(SyncParticleFX.class, new SyncParticleFX.Handler(), Side.CLIENT);
		AfraidOfTheDark.INSTANCE.getPacketHandler().registerBidiPacket(SyncAOTDPlayerData.class, new SyncAOTDPlayerData.Handler());
		AfraidOfTheDark.INSTANCE.getPacketHandler().registerBidiPacket(SyncAOTDEntityData.class, new SyncAOTDEntityData.Handler());
		AfraidOfTheDark.INSTANCE.getPacketHandler().registerPacket(SyncSelectedWristCrossbowBolt.class, new SyncSelectedWristCrossbowBolt.Handler(), Side.SERVER);
		AfraidOfTheDark.INSTANCE.getPacketHandler().registerPacket(SyncKeyPress.class, new SyncKeyPress.Handler(), Side.SERVER);
		AfraidOfTheDark.INSTANCE.getPacketHandler().registerBidiPacket(SyncSpellManager.class, new SyncSpellManager.Handler());
		AfraidOfTheDark.INSTANCE.getPacketHandler().registerPacket(PlaySoundAtPlayer.class, new PlaySoundAtPlayer.Handler(), Side.CLIENT);
		AfraidOfTheDark.INSTANCE.getPacketHandler().registerPacket(SyncHasEnariasAltar.class, new SyncHasEnariasAltar.Handler(), Side.CLIENT);
	}
}
