/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.item.spawnEggs;

import net.minecraft.entity.EntityLiving;

import com.DavidM1A2.AfraidOfTheDark.common.item.core.AOTDSpawnEgg;

public class ItemSpawnWerewolf extends AOTDSpawnEgg
{
	// Werewolf spawn egg
	protected String entityToSpawnName = "";
	protected String entityToSpawnNameFull = "";
	protected EntityLiving entityToSpawn = null;

	public ItemSpawnWerewolf()
	{
		// Set various item properties
		super("werewolf");
		this.setUnlocalizedName("spawnWerewolf");
	}
}
