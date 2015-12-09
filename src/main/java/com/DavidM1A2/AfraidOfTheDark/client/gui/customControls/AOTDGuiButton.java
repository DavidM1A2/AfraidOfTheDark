/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.client.gui.customControls;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class AOTDGuiButton extends AOTDGuiTextComponent
{
	private ResourceLocation icon;

	public AOTDGuiButton()
	{
		super();
		this.setVisible(false);
		this.icon = null;
	}

	public AOTDGuiButton(int x, int y, int width, int height, TrueTypeFont font, String icon)
	{
		super(x, y, width, height, font);
		this.setVisible(true);
		if (icon == null || icon.isEmpty())
			this.icon = null;
		else
			this.icon = new ResourceLocation(icon);
	}

	@Override
	public void draw()
	{
		if (icon != null)
		{
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			GlStateManager.enableBlend();
			GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
			GlStateManager.blendFunc(770, 771);
			if (this.isVisible())
			{
				Minecraft.getMinecraft().getTextureManager().bindTexture(this.icon);
				Gui.drawScaledCustomSizeModalRect(this.getXScaled(), this.getYScaled(), 0, 0, this.getWidth(), this.getWidth(), this.getWidthScaled(), this.getHeightScaled(), this.getWidth(), this.getWidth());
			}
		}
	}
}
