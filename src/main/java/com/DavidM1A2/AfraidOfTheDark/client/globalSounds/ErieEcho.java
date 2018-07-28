/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.client.globalSounds;

import com.DavidM1A2.AfraidOfTheDark.common.initializeMod.ModSounds;
import com.DavidM1A2.AfraidOfTheDark.common.reference.AOTDDimensions;

import net.minecraft.util.ResourceLocation;

public class ErieEcho extends PlayerFollowingSound
{
	public ErieEcho()
	{
		super(ModSounds.erieEchos);
	}

	@Override
	public void update()
	{
		super.update();
		if (this.entityPlayer.isDead || this.entityPlayer.dimension != AOTDDimensions.Nightmare.getWorldID())
			this.donePlaying = true;
	}
}
