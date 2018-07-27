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
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;

public abstract class PlayerFollowingSound extends MovingSound
{
	protected EntityPlayer entityPlayer;

	PlayerFollowingSound(SoundEvent sound)
	{
		// SoundEvent.registerSound?
		super(sound, SoundCategory.PLAYERS);
		this.attenuationType = AttenuationType.NONE;
	}

	@Override
	public void update()
	{
		if (this.entityPlayer == null)
			this.entityPlayer = Minecraft.getMinecraft().player;
		this.xPosF = (float) this.entityPlayer.posX;
		this.yPosF = (float) this.entityPlayer.posY;
		this.zPosF = (float) this.entityPlayer.posZ;
	}
}
