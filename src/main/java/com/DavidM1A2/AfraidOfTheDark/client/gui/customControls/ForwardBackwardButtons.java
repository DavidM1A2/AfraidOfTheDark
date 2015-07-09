package com.DavidM1A2.AfraidOfTheDark.client.gui.customControls;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class ForwardBackwardButtons extends GuiButton
{
	private static final ResourceLocation FORWARD_TEXTURE = new ResourceLocation("afraidofthedark:textures/gui/buttons/forwardButton.png");
	private static final ResourceLocation BACKWARD_TEXTURE = new ResourceLocation("afraidofthedark:textures/gui/buttons/backwardButton.png");
	private static final ResourceLocation FORWARD_DISABLED_TEXTURE = new ResourceLocation("afraidofthedark:textures/gui/buttons/forwardButtonDisabled.png");
	private static final ResourceLocation BACKWARD_DISABLED_TEXTURE = new ResourceLocation("afraidofthedark:textures/gui/buttons/backwardButtonDisabled.png");

	private final boolean isForward;

	public ForwardBackwardButtons(int buttonId, int x, int y, int widthIn, int heightIn, boolean isForward)
	{
		super(buttonId, x, y, widthIn, heightIn, "");
		this.isForward = isForward;
	}

	@Override
	public void drawButton(final Minecraft minecraft, final int mouseX, final int mouseY)
	{
		// Make sure it should be visible
		if (this.visible)
		{
			GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
			GlStateManager.enableBlend();
			GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
			GlStateManager.blendFunc(770, 771);
			this.hovered = (mouseX >= this.xPosition) && (mouseY >= this.yPosition) && (mouseX < (this.xPosition + this.width)) && (mouseY < (this.yPosition + this.height));

			if (isForward)
			{
				minecraft.getTextureManager().bindTexture(this.FORWARD_TEXTURE);
			}
			else
			{
				minecraft.getTextureManager().bindTexture(this.BACKWARD_TEXTURE);
			}

			Gui.drawScaledCustomSizeModalRect(this.xPosition, this.yPosition, 0, 0, 64, 64, this.width, this.height, 64, 64);

		}
	}

	// Update x, y, width, and height of a textbox
	public void updateBounds(final int x, final int y)
	{
		this.xPosition = x;
		this.yPosition = y;
	}
}
