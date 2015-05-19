package com.DavidM1A2.AfraidOfTheDark.threads;

import net.minecraft.entity.player.EntityPlayer;

import com.DavidM1A2.AfraidOfTheDark.playerData.HasStartedAOTD;

public class DelayedAOTDUpdate extends DelayedUpdate<Boolean>
{
	public DelayedAOTDUpdate(EntityPlayer entityPlayer, boolean data)
	{
		super(entityPlayer, data);
	}

	@Override
	protected void updatePlayer()
	{
		entityPlayer.getEntityData().setBoolean(HasStartedAOTD.PLAYER_STARTED_AOTD, this.data);
	}
}
