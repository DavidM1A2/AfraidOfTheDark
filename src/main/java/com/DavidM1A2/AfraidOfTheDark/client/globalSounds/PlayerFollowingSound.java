/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.client.globalSounds;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.MovingSound;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

public abstract class PlayerFollowingSound extends MovingSound
{
	protected EntityPlayer entityPlayer;

	protected PlayerFollowingSound(ResourceLocation sound)
	{
		super(sound);
		this.attenuationType = AttenuationType.NONE;
	}

	@Override
	public void update()
	{
		if (this.entityPlayer == null)
			this.entityPlayer = Minecraft.getMinecraft().thePlayer;
		this.xPosF = (float) this.entityPlayer.posX;
		this.yPosF = (float) this.entityPlayer.posY;
		this.zPosF = (float) this.entityPlayer.posZ;
	}
}
