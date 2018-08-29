package com.DavidM1A2.afraidofthedark.client.gui.standardControls;

import com.DavidM1A2.afraidofthedark.client.gui.base.AOTDGuiContainer;
import com.DavidM1A2.afraidofthedark.client.gui.base.TextAlignment;
import com.DavidM1A2.afraidofthedark.client.gui.fontLibrary.TrueTypeFont;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.Color;

/**
 * Class representing a GUI button to be used by AOTD
 */
public class AOTDGuiButton extends AOTDGuiContainer
{
	// The background image
	private ResourceLocation icon;
	// The background image when the button is hovered
	private ResourceLocation iconHovered;
	// The image containing the background texture
	private AOTDGuiImage background;
	// The label containing the center text
	private AOTDGuiLabel label;

	/**
	 * Constructor sets the buttons location, scale, font, icon path. No hovered icon is provided
	 *
	 * @param x The X location of the top left corner
	 * @param y The Y location of the top left corner
	 * @param width The width of the component
	 * @param height The height of the component
	 * @param font The font to be used to draw the button's text
	 * @param icon The icon to use for the background of the button
	 */
	public AOTDGuiButton(int x, int y, int width, int height, TrueTypeFont font, String icon)
	{
		// Call the base constructor
		super(x, y, width, height);
		this.icon = new ResourceLocation(icon);

		// Create a background image for the button
		this.background = new AOTDGuiImage(0, 0, width, height, icon);
		this.add(background);

		// Create a label to cover the button
		this.label = new AOTDGuiLabel(0, 0, width, height, font);
		this.add(this.label);
	}

	/**
	 * Constructor sets the buttons location, scale, font, icon and hovered icon paths
	 *
	 * @param x The X location of the top left corner
	 * @param y The Y location of the top left corner
	 * @param width The width of the component
	 * @param height The height of the component
	 * @param font The font to be used to draw the button's text
	 * @param icon The icon to use for the background of the button
	 * @param iconHovered The icon to use for the background of the button when the button is hovered
	 */
	public AOTDGuiButton(int x, int y, int width, int height, TrueTypeFont font, String icon, String iconHovered)
	{
		// Set the base properties and icon
		this(x, y, width, height, font, icon);
		this.iconHovered = new ResourceLocation(iconHovered);
	}

	/**
	 * Draw function that gets called every frame. Draw the button and i's label
	 */
	@Override
	public void draw()
	{
		if (this.isVisible() && this.icon != null)
		{
			this.background.setImageTexture(this.isHovered() && this.iconHovered != null ? this.iconHovered : this.icon);
			super.draw();
		}
	}

	public void setText(String text)
	{
		this.label.setText(text);
	}

	public void setTextColor(Color textColor)
	{
		this.label.setTextColor(textColor);
	}

	public void setTextAlignment(TextAlignment textAlignment)
	{
		this.label.setTextAlignment(textAlignment);
	}
}
