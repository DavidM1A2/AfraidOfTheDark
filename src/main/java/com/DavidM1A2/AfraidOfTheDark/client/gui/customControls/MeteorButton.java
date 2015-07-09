/*
 * Author: David Slovikosky 
 * Mod: Afraid of the Dark 
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.client.gui.customControls;

import com.DavidM1A2.AfraidOfTheDark.common.refrence.MeteorTypes;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class MeteorButton extends GuiButton
{
	private final ResourceLocation METEOR_TEXTURE;
	private final int ORIGINAL_X_POSITION;
	private final int ORIGINAL_Y_POSITION;

	private final MeteorTypes myType;

	public MeteorButton(final int buttonId, final int x, final int y, final int widthIn, final int heightIn, final MeteorTypes myType)
	{
		super(buttonId, x, y, widthIn, heightIn, "");
		this.ORIGINAL_X_POSITION = this.xPosition;
		this.ORIGINAL_Y_POSITION = this.yPosition;
		this.myType = myType;
		if (myType == MeteorTypes.silver)
		{
			this.METEOR_TEXTURE = new ResourceLocation("afraidofthedark:textures/gui/astralSilverMeteor.png");
		}
		else if (myType == MeteorTypes.starMetal)
		{
			this.METEOR_TEXTURE = new ResourceLocation("afraidofthedark:textures/gui/starMetalMeteor.png");
		}
		else
		{
			this.METEOR_TEXTURE = new ResourceLocation("afraidofthedark:textures/gui/sunstoneMeteor.png");
		}
	}

	// Set the position of this node
	public void setPosition(final int xPos, final int yPos)
	{
		this.xPosition = this.ORIGINAL_X_POSITION - xPos;
		this.yPosition = this.ORIGINAL_Y_POSITION - yPos;
	}

	public MeteorTypes getMyType()
	{
		return this.myType;
	}

	// Draw button draws the button using OpenGL
	@Override
	public void drawButton(final Minecraft minecraft, final int mouseX, final int mouseY)
	{
		// Make sure it should be visible
		if (this.visible)
		{
			this.hovered = (mouseX >= this.xPosition) && (mouseY >= this.yPosition) && (mouseX < (this.xPosition + this.width)) && (mouseY < (this.yPosition + this.height));

			minecraft.getTextureManager().bindTexture(this.METEOR_TEXTURE);
			GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
			GlStateManager.enableBlend();
			GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
			GlStateManager.blendFunc(770, 771);
			Gui.drawScaledCustomSizeModalRect(this.xPosition, this.yPosition, 0, 0, 64, 64, this.width, this.height, 64, 64);

		}
	}

}
