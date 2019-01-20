package com.DavidM1A2.afraidofthedark.client.gui.standardControls;

import com.DavidM1A2.afraidofthedark.client.gui.base.AOTDGuiContainer;
import com.DavidM1A2.afraidofthedark.client.gui.base.TextAlignment;
import com.DavidM1A2.afraidofthedark.client.gui.fontLibrary.TrueTypeFont;
import net.minecraft.util.ResourceLocation;
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
		this.background.setColor(new Color(255, 255, 255));
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
			super.draw();
			this.background.setImageTexture(this.isHovered() && this.iconHovered != null ? this.iconHovered : this.icon);
		}
	}

	/**
	 * Sets the text of the button
	 *
	 * @param text The text to draw over the button
	 */
	public void setText(String text)
	{
		this.label.setText(text);
	}

	/**
	 * Sets the color of the background image
	 *
	 * @param tint The color that this component should be
	 */
	@Override
	public void setColor(Color tint)
	{
		super.setColor(tint);
		this.background.setColor(tint);
	}

	/**
	 * Sets the color of the text
	 *
	 * @param textColor The text color
	 */
	public void setTextColor(Color textColor)
	{
		this.label.setTextColor(textColor);
	}

	/**
	 * Sets the text alignment for this button
	 *
	 * @param textAlignment The button's text alignment
	 */
	public void setTextAlignment(TextAlignment textAlignment)
	{
		this.label.setTextAlignment(textAlignment);
	}
}
