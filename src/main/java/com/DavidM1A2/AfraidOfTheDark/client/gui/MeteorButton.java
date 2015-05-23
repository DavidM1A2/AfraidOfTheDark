package com.DavidM1A2.AfraidOfTheDark.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import com.DavidM1A2.AfraidOfTheDark.refrence.MeteorTypes;

public class MeteorButton extends GuiButton
{
	private final ResourceLocation METEOR_TEXTURE;
	private final int ORIGINAL_X_POSITION;
	private final int ORIGINAL_Y_POSITION;

	private final MeteorTypes myType;

	public MeteorButton(int buttonId, int x, int y, int widthIn, int heightIn, MeteorTypes myType)
	{
		super(buttonId, x, y, widthIn, heightIn, "");
		this.ORIGINAL_X_POSITION = xPosition;
		this.ORIGINAL_Y_POSITION = yPosition;
		this.myType = myType;
		if (myType == MeteorTypes.silver)
		{
			METEOR_TEXTURE = new ResourceLocation("afraidofthedark:textures/blocks/meteoricSilver.png");
		}
		else if (myType == MeteorTypes.starMetal)
		{
			METEOR_TEXTURE = new ResourceLocation("afraidofthedark:textures/blocks/starMetal.png");
		}
		else
		{
			METEOR_TEXTURE = new ResourceLocation("afraidofthedark:textures/blocks/sunstone.png");
		}
	}

	// Set the position of this node
	public void setPosition(int xPos, int yPos)
	{
		this.xPosition = ORIGINAL_X_POSITION - xPos;
		this.yPosition = ORIGINAL_Y_POSITION - yPos;
	}

	public MeteorTypes getMyType()
	{
		return this.myType;
	}

	// Draw button draws the button using OpenGL
	@Override
	public void drawButton(Minecraft minecraft, int mouseX, int mouseY)
	{
		// Make sure it should be visible
		if (this.visible)
		{
			this.hovered = mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;

			minecraft.getTextureManager().bindTexture(METEOR_TEXTURE);
			GL11.glEnable(GL11.GL_BLEND);
			OpenGlHelper.glBlendFunc(770, 771, 1, 0);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			this.drawScaledCustomSizeModalRect(this.xPosition, this.yPosition, 0, 0, 64, 64, this.width, this.height, 64, 64);

		}
	}

}
