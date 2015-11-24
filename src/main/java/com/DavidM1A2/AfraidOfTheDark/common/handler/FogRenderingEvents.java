/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.handler;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLContext;

import com.DavidM1A2.AfraidOfTheDark.common.refrence.Constants;
import com.DavidM1A2.AfraidOfTheDark.common.savedData.AOTDPlayerData;

import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.util.MathHelper;
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
		if (event.entity instanceof EntityPlayer)
		{
			final float insanity = (float) AOTDPlayerData.get((EntityPlayer) event.entity).getPlayerInsanity();

			// If the player is insane, set the fog equal to 1.001^(.5*insanity) - .9989
			if (insanity >= 0.1)
			{
				event.red = MathHelper.clamp_float(event.red + (insanity / 100.0F), 0.0F, 1.0F);
			}
		}
	}

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void renderEventFogDensity(final FogDensity fogDensity)
	{
		float f1;
		float farPlaneDistance = Minecraft.getMinecraft().gameSettings.renderDistanceChunks * 16.0F;
		int someUnknownInt = 0;
		if (Minecraft.getMinecraft().theWorld.provider.getDimensionId() == Constants.NightmareWorld.NIGHTMARE_WORLD_ID)
		{
			GlStateManager.setFog(2048);
			//GlStateManager.setFogDensity(0.1F);
			fogDensity.density = 0.1f;
		}
		else if (Minecraft.getMinecraft().thePlayer.isPotionActive(Potion.blindness))
		{
			f1 = 5.0F;
			int j = Minecraft.getMinecraft().thePlayer.getActivePotionEffect(Potion.blindness).getDuration();

			if (j < 20)
			{
				f1 = 5.0F + (farPlaneDistance - 5.0F) * (1.0F - (float) j / 20.0F);
			}

			GlStateManager.setFog(9729);

			if (someUnknownInt == -1)
			{
				GlStateManager.setFogStart(0.0F);
				GlStateManager.setFogEnd(f1 * 0.8F);
			}
			else
			{
				GlStateManager.setFogStart(f1 * 0.25F);
				GlStateManager.setFogEnd(f1);
			}

			if (GLContext.getCapabilities().GL_NV_fog_distance)
			{
				GL11.glFogi(34138, 34139);
			}
		}
		//		else if (this.cloudFog)
		//		{
		//			GlStateManager.setFog(2048);
		//			GlStateManager.setFogDensity(0.1F);
		//		}
		else if (fogDensity.block.getMaterial() == Material.water)
		{
			GlStateManager.setFog(2048);

			if (Minecraft.getMinecraft().thePlayer.isPotionActive(Potion.waterBreathing))
			{
				fogDensity.density = 0.01F;
			}
			else
			{
				fogDensity.density = 0.1F - (float) EnchantmentHelper.getRespiration(Minecraft.getMinecraft().thePlayer) * 0.03F;
			}
		}
		else if (fogDensity.block.getMaterial() == Material.lava)
		{
			GlStateManager.setFog(2048);
			fogDensity.density = 2.0F;
		}
		else
		{
			f1 = farPlaneDistance;
			GlStateManager.setFog(9729);

			if (someUnknownInt == -1)
			{
				GlStateManager.setFogStart(0.0F);
				GlStateManager.setFogEnd(f1);
			}
			else
			{
				GlStateManager.setFogStart(f1 * 0.75F);
				GlStateManager.setFogEnd(f1);
			}

			if (GLContext.getCapabilities().GL_NV_fog_distance)
			{
				GL11.glFogi(34138, 34139);
			}

			if (Minecraft.getMinecraft().theWorld.provider.doesXZShowFog((int) Minecraft.getMinecraft().thePlayer.posX, (int) Minecraft.getMinecraft().thePlayer.posZ))
			{
				GlStateManager.setFogStart(f1 * 0.05F);
				GlStateManager.setFogEnd(Math.min(f1, 192.0F) * 0.5F);
			}
		}
		fogDensity.setCanceled(true);
	}
}
