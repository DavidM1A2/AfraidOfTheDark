package com.DavidM1A2.afraidofthedark.client.gui.standardControls;

import com.DavidM1A2.afraidofthedark.client.gui.base.AOTDGuiTextContainer;
import com.DavidM1A2.afraidofthedark.client.gui.fontLibrary.TrueTypeFont;

/**
 * Class representing a label to be drawn on the GUI
 */
public class AOTDGuiLabel extends AOTDGuiTextContainer
{
	// The maximum string length
	private int maxStrLength = Integer.MAX_VALUE;
	// The string to be drawn
	private String actualStringToDraw = "";

	/**
	 * Constructor takes an x and y position as well as a font
	 *
	 * @param x The X coordinate of the label
	 * @param y The Y coordinate of the label
	 * @param font The font to use to draw the label
	 */
	public AOTDGuiLabel(int x, int y, TrueTypeFont font)
	{
		super(x, y, 0, 0, font);
		this.setHeight(Math.toIntExact(Math.round(font.getHeight() * this.getDrawingScale())));
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
			this.drawText(this.getXScaled(), this.getYScaled());
		}
	}

	/**
	 * @param text Sets the text to be drawn
	 */
	@Override
	public void setText(String text)
	{
		// Set the internal text of the label
		super.setText(text);
		// If the internal text is too long, set the actual string to draw's length to be shorter
		if (super.getText().length() > maxStrLength)
			// Shorten the internal string to a shorter representation
			this.actualStringToDraw = super.getText().substring(0, maxStrLength);
		else
			// Just use the internal representation as the text
			this.actualStringToDraw = super.getText();
		this.setWidth(Math.toIntExact(Math.round(this.getFont().getWidth(this.actualStringToDraw) * this.getDrawingScale())));
	}

	/**
	 * @return Gets the text that this label represents
	 */
	@Override
	public String getText()
	{
		return this.actualStringToDraw;
	}

	/**
	 * Sets the maximum string length that a label can contain
	 *
	 * @param maxStrLength The max length of the string
	 */
	public void setMaxStringLength(int maxStrLength)
	{
		// Update the max string length
		this.maxStrLength = maxStrLength;
		// Update the actual string to draw
		if (super.getText().length() > this.maxStrLength)
			this.actualStringToDraw = super.getText().substring(0, this.maxStrLength);
		else
			this.actualStringToDraw = super.getText();
		this.setWidth(Math.toIntExact(Math.round(this.getFont().getWidth(this.actualStringToDraw) * this.getDrawingScale())));
	}
}
