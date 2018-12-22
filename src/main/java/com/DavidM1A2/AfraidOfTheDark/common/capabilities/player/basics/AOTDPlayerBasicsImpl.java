package com.DavidM1A2.afraidofthedark.common.capabilities.player.basics;

import com.DavidM1A2.afraidofthedark.AfraidOfTheDark;
import com.DavidM1A2.afraidofthedark.common.packets.capabilityPackets.SyncAOTDPlayerBasics;
import com.DavidM1A2.afraidofthedark.common.packets.capabilityPackets.SyncStartedAOTD;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;

/**
 * Default implementation of the AOTD player basics capability
 */
public class AOTDPlayerBasicsImpl implements IAOTDPlayerBasics
{
	// Flag telling us if the user has started the mod or not
	private boolean startedAOTD;

	/**
	 * Constructor initializes default values
	 */
	public AOTDPlayerBasicsImpl()
	{
		this.startedAOTD = false;
	}

	/**
	 * Returns true if the player is on server side or false if not
	 *
	 * @param entityPlayer The player to test
	 * @return true if the player is on server side or false if not
	 */
	private boolean isServerSide(EntityPlayer entityPlayer)
	{
		return !entityPlayer.world.isRemote;
	}

	/**
	 * @return True if the player has started the afraid of the dark mod, false otherwise
	 */
	@Override
	public boolean getStartedAOTD()
	{
		return this.startedAOTD;
	}

	/**
	 * Called to set true if the player has started the mod, false otherwise
	 *
	 * @param startedAOTD True if the player started AOTD, false otherwise
	 */
	@Override
	public void setStartedAOTD(boolean startedAOTD)
	{
		this.startedAOTD = startedAOTD;
	}

	/**
	 * Called to either tell client -> server the current client AOTD status or server -> client based on if it's client or server side
	 *
	 * @param entityPlayer The player to sync
	 */
	@Override
	public void syncStartedAOTD(EntityPlayer entityPlayer)
	{
		if (this.isServerSide(entityPlayer))
			AfraidOfTheDark.INSTANCE.getPacketHandler().sendTo(new SyncStartedAOTD(this.startedAOTD), (EntityPlayerMP) entityPlayer);
		else
			AfraidOfTheDark.INSTANCE.getPacketHandler().sendToServer(new SyncStartedAOTD(this.startedAOTD));
	}

	/**
	 * Syncs all player basic data from server -> client
	 *
	 * @param entityPlayer The player to sync to
	 */
	@Override
	public void syncAll(EntityPlayer entityPlayer)
	{
		// If we're on server side send the player's data
		if (this.isServerSide(entityPlayer))
			AfraidOfTheDark.INSTANCE.getPacketHandler().sendTo(new SyncAOTDPlayerBasics(this), (EntityPlayerMP) entityPlayer);
		// If we're on client side request the server to send us player data
		else
			AfraidOfTheDark.INSTANCE.getPacketHandler().sendToServer(new SyncAOTDPlayerBasics(this));
	}
}
