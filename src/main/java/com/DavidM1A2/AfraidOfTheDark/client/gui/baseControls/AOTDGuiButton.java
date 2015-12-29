/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.client.gui.baseControls;

import java.awt.Color;

import com.DavidM1A2.AfraidOfTheDark.client.trueTypeFont.TrueTypeFont;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class AOTDGuiButton extends AOTDGuiTextComponent
{
	private ResourceLocation icon; 
	private ResourceLocation iconHovered;

	public AOTDGuiButton(int x, int y, int width, int height, TrueTypeFont font, String icon)
	{
		super(x, y, width, height, font);
		if (icon == null || icon.isEmpty())
			this.icon = null;
		else
			this.icon = new ResourceLocation(icon);
		this.iconHovered = null;
		this.setTextAlignment(TextAlignment.ALIGN_CENTER);
	}

	public AOTDGuiButton(int x, int y, int width, int height, TrueTypeFont font, String icon, String iconHovered)
	{
		this(x, y, width, height, font, icon);
		if (iconHovered == null || iconHovered.isEmpty())
			this.iconHovered = null;
		else
			this.iconHovered = new ResourceLocation(iconHovered);
	}

	@Override
	public void draw()
	{
		if (icon != null)
		{
			if (this.isVisible())
			{
				super.draw();
				GlStateManager.enableBlend();
				GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
				GlStateManager.blendFunc(770, 771);
				Minecraft.getMinecraft().getTextureManager().bindTexture(this.isHovered() && this.iconHovered != null ? this.iconHovered : this.icon);
				Gui.drawScaledCustomSizeModalRect(this.getXScaled(), this.getYScaled(), 0, 0, this.getWidth(), this.getHeight(), this.getWidthScaled(), this.getHeightScaled(), this.getWidth(), this.getHeight());
				this.drawText(this.getXScaled(), this.getYScaled());
			}
		}
	}
}
