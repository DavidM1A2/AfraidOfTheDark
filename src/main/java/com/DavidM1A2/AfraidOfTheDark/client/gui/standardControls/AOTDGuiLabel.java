package com.DavidM1A2.afraidofthedark.client.gui.standardControls;

import com.DavidM1A2.afraidofthedark.client.gui.base.AOTDGuiContainer;
import com.DavidM1A2.afraidofthedark.client.gui.base.TextAlignment;
import com.DavidM1A2.afraidofthedark.client.gui.fontLibrary.TrueTypeFont;
import org.apache.commons.lang3.StringUtils;
import org.lwjgl.util.Color;

/**
 * Class representing a label to be drawn on the GUI
 */
public class AOTDGuiLabel extends AOTDGuiContainer
{
	// The maximum string length
	private int maxStrLength = Integer.MAX_VALUE;
	// The raw text we are supposed to draw
	private String originalText = "";
	// The text to draw
	private String text = StringUtils.EMPTY;
	// The font to draw the text with
	private TrueTypeFont font;
	// The color to draw the text with
	private Color textColor = new Color(255, 255, 255, 255);
	// Text alignment
	private TextAlignment textAlignment = TextAlignment.ALIGN_LEFT;
	// We draw at a lower scale because otherwise we get blurry text
	private float drawingScale = 0.25f;

	/**
	 * Constructor takes an x and y position as well as a font
	 *
	 * @param x The X coordinate of the label
	 * @param y The Y coordinate of the label
	 * @param font The font to use to draw the label
	 */
	public AOTDGuiLabel(int x, int y, TrueTypeFont font)
	{
		super(x, y, 0, 0);
		this.font = font;
		this.setHeight(Math.toIntExact(Math.round(font.getHeight() * this.drawingScale)));
	}

	/**
	 * Draw function that gets called every frame. Draw the text
	 */
	@Override
	public void draw()
	{
		// If the label is visible, draw it
		if (this.isVisible())
		{
			super.draw();

			// If we don't have a font, we can't draw anything
			if (this.font != null)
			{
				// Draw the string at (x, y) with the correct color and scale
				this.font.drawString(
						(float) this.getXScaled(),
						(float) this.getYScaled(),
						this.text,
						this.getScaleX().floatValue() * drawingScale,
						this.getScaleY().floatValue() * drawingScale,
						textAlignment,
						this.textColor.getRed() / 255f, this.textColor.getGreen() / 255f, this.textColor.getBlue() / 255f, this.textColor.getAlpha() / 255f);
			}
		}
	}

	/**
	 * @param text Sets the text to be drawn
	 */
	public void setText(String text)
	{
		// Set the internal text of the label
		this.originalText = text;
		// If the internal text is too long, set the actual string to draw's length to be shorter
		if (this.originalText.length() > maxStrLength)
			// Shorten the internal string to a shorter representation
			this.text = this.originalText.substring(0, maxStrLength);
		else
			// Just use the internal representation as the text
			this.text = this.originalText;
		this.setWidth(Math.toIntExact(Math.round(this.font.getWidth(this.text) * this.drawingScale)));
	}

	/**
	 * @return Gets the text that this label represents
	 */
	public String getText()
	{
		return this.originalText;
	}

	/**
	 * Sets the maximum string length that a label can contain
	 *
	 * @param maxStrLength The max length of the string
	 */
	/*
	public void setMaxStringLength(int maxStrLength)
	{
		// Update the max string length
		this.maxStrLength = maxStrLength;
		// If the internal text is too long, set the actual string to draw's length to be shorter
		if (this.originalText.length() > maxStrLength)
			// Shorten the internal string to a shorter representation
			this.textRenderer.setText(this.originalText.substring(0, maxStrLength));
		else
			// Just use the internal representation as the text
			this.textRenderer.setText(this.originalText);
		this.setWidth(Math.toIntExact(Math.round(this.textRenderer.getFont().getWidth(this.textRenderer.getText()) * this.textRenderer.getDrawingScale())));
	}
	*/

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
	 * @param textAlignment Text alignment used by the label
	 */
	public void setTextAlignment(TextAlignment textAlignment)
	{
		this.textAlignment = textAlignment;
	}

	/**
	 * @return Get the text alignment used by the label
	 */
	public TextAlignment getTextAlignment()
	{
		return this.textAlignment;
	}

	/**
	 * @param drawingScale Sets the scale to draw the text at
	 */
	public void setDrawingScale(float drawingScale)
	{
		this.drawingScale = drawingScale;
	}

	/**
	 * @return Gets the text drawing scale
	 */
	public float getDrawingScale()
	{
		return this.drawingScale;
	}
}
