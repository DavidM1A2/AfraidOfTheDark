package com.DavidM1A2.AfraidOfTheDark.common.item.spawnEggs;

import net.minecraft.entity.EntityLiving;

import com.DavidM1A2.AfraidOfTheDark.common.item.core.AOTDSpawnEgg;

public class ItemSpawnDeeeSyft extends AOTDSpawnEgg
{
	// Deee Syft spawn egg
	protected String entityToSpawnName = "";
	protected String entityToSpawnNameFull = "";
	protected EntityLiving entityToSpawn = null;

	public ItemSpawnDeeeSyft()
	{
		// Set various item properties
		super("deeeSyft");
		this.setUnlocalizedName("spawnDeeeSyft");
	}
}
