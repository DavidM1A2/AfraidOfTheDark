package com.DavidM1A2.AfraidOfTheDark.client.gui.standardControls;

import com.DavidM1A2.AfraidOfTheDark.client.gui.AOTDGuiUtility;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import org.apache.commons.lang3.ArrayUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.Color;
import org.lwjgl.util.Rectangle;

import java.util.Arrays;
import java.util.Optional;

public abstract class AOTDGuiComponent
{
	private Double scaleX = 1.0;
	private Double scaleY = 1.0;
	private Boolean isHovered = false;
	private Boolean isVisible = true;
	private Rectangle boundingBox;
	private Rectangle scaledBoundingBox = new Rectangle(0, 0, 0, 0);
	private Color tint = new Color(255, 255, 255, 255);
	private String[] hoverText = ArrayUtils.EMPTY_STRING_ARRAY;
	protected final EntityPlayerSP entityPlayer = Minecraft.getMinecraft().player;
	protected final FontRenderer fontRenderer = Minecraft.getMinecraft().fontRenderer;

	public AOTDGuiComponent(Integer x, Integer y, Integer width, Integer height)
	{
		this.boundingBox = new Rectangle(x, y, width, height);
	}

	public void draw()
	{
		GL11.glColor4d(tint.getRed(), tint.getGreen(), tint.getBlue(), tint.getAlpha());
		// this.drawBoundingBox();
	}

	public void drawHoverText()
	{
		if (this.isVisible && this.isHovered)
		{
			Optional<Integer> maxHoverTextLengthOpt = Arrays.stream(this.hoverText).map(fontRenderer::getStringWidth).max(Integer::compareTo);
			if (maxHoverTextLengthOpt.isPresent())
			{
				Integer maxHoverTextLength = maxHoverTextLengthOpt.get();
				Integer mouseX = AOTDGuiUtility.getInstance().getMouseXInMCCoord();
				Integer mouseY = AOTDGuiUtility.getInstance().getMouseYInMCCoord();
				new java.awt.Color(3, 3, 5, 2).hashCode();
				Gui.drawRect(mouseX + 2, mouseY - 2, mouseX + maxHoverTextLength + 7, mouseY + fontRenderer.FONT_HEIGHT * this.hoverText.length, new Color(32, 0, 23, 179).hashCode());
			}
		}
	}
}
