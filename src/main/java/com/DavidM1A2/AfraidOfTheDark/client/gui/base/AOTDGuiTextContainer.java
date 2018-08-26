package com.DavidM1A2.afraidofthedark.client.gui.base;

import com.DavidM1A2.afraidofthedark.client.gui.fontLibrary.TrueTypeFont;
import org.lwjgl.util.Color;

/**
 * Class containing extra methods used for drawing text
 */
public abstract class AOTDGuiTextContainer extends AOTDGuiContainer
{
	// The font to draw the text with
	private TrueTypeFont font;
	// The text to draw
	private String text = "";
	// The text alignment to draw with
	private TextAlignment textAlignment = TextAlignment.ALIGN_LEFT;
	// The color to draw the text with
	private Color textColor = new Color(255, 255, 255, 255);
	// We draw at a lower scale because otherwise we get blurry text
	private float drawingScale = 0.25f;

	/**
	 * Constructor initializes the bounding box
	 *
	 * @param x      The X location of the top left corner
	 * @param y      The Y location of the top left corner
	 * @param width  The width of the component
	 * @param height The height of the component
	 */
	public AOTDGuiTextContainer(Integer x, Integer y, Integer width, Integer height, TrueTypeFont font)
	{
		super(x, y, width, height);
		this.font = font;
	}

	/**
	 * Draws text with the stored font at the X/Y position
	 *
	 * @param x The X position to draw the text at
	 * @param y The Y position to draw the text at
	 */
	protected void drawText(double x, double y)
	{
		// If we don't have a font, we can't draw anything
		if (this.font == null)
			return;

		// Based on text alignment we re-scale the X value
		switch (this.textAlignment)
		{
			case ALIGN_CENTER:
				x = x + this.getWidthScaled() / 2D - 7;
				break;
			case ALIGN_RIGHT:
				x = x + this.getWidthScaled() - 15;
				break;
			default:
				break;
		}
		// Draw the string at (x, y) with the correct color and scale
		this.font.drawString((float) x, (float) y, this.text, this.getScaleX().floatValue() * drawingScale, this.getScaleY().floatValue() * drawingScale, this.textAlignment, this.textColor.getRed() / 255f, this.textColor.getGreen() / 255f, this.textColor.getBlue() / 255f, this.textColor.getAlpha() / 255f);
	}

	/**
	 * @param font Sets the font to be used while drawing the text
	 */
	public void setFont(TrueTypeFont font)
	{
		this.font = font;
	}

	/**
	 * @return Gets the font to be used while drawing the text
	 */
	public TrueTypeFont getFont()
	{
		return this.font;
	}

	/**
	 * @param text Sets the text to be drawn
	 */
	public void setText(String text)
	{
		this.text = text;
	}

	/**
	 * @return Gets the text to be drawn
	 */
	public String getText()
	{
		return this.text;
	}

	/**
	 * @param textAlignment Sets the text's alignment
	 */
	public void setTextAlignment(TextAlignment textAlignment)
	{
		this.textAlignment = textAlignment;
	}

	/**
	 * @return Gets the text alignment
	 */
	public TextAlignment getTextAlignment()
	{
		return this.textAlignment;
	}

	/**
	 * @param textColor Sets the text's color
	 */
	public void setTextColor(Color textColor)
	{
		this.textColor = textColor;
	}

	/**
	 * @return Gets the text's color
	 */
	public Color getTextColor()
	{
		return this.textColor;
	}

	/**
	 * @return Gets the text drawing scale
	 */
	public float getDrawingScale()
	{
		return this.drawingScale;
	}
}
