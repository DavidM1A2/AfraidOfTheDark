/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.handler;

import com.DavidM1A2.AfraidOfTheDark.common.initializeMod.ModCapabilities;
import com.DavidM1A2.AfraidOfTheDark.common.reference.AOTDDimensions;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.event.EntityViewRenderEvent.FogColors;
import net.minecraftforge.client.event.EntityViewRenderEvent.FogDensity;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class FogRenderingEvents
{
	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void renderEventFogColor(final FogColors event)
	{
		if (event.getEntity() instanceof EntityPlayer)
		{
			final float insanity = (float) ((EntityPlayer) event.getEntity()).getCapability(ModCapabilities.PLAYER_DATA, null).getPlayerInsanity();

			// If the player is insane, set the fog equal to 1.001^(.5*insanity)
			// - .9989
			if (insanity >= 0.1)
			{
				event.setRed(MathHelper.clamp_float(event.getRed() + (insanity / 100.0F), 0.0F, 1.0F));
			}
		}
	}

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	// see private void setupFog(int p_78468_1_, float partialTicks) in
	// EntityRenderer
	public void renderEventFogDensity(final FogDensity fogDensity)
	{
		float f1;
		float farPlaneDistance = Minecraft.getMinecraft().gameSettings.renderDistanceChunks * 16.0F;
		int someUnknownInt = 0;
		if (Minecraft.getMinecraft().theWorld.provider.getDimension() == AOTDDimensions.Nightmare.getWorldID())
		{
			fogDensity.setDensity(0.1f);
		}
		else
			fogDensity.setCanceled(true);
	}
}
