package com.DavidM1A2.afraidofthedark.client.gui.standardControls;

import com.DavidM1A2.afraidofthedark.client.gui.base.AOTDGuiContainer;
import com.DavidM1A2.afraidofthedark.client.gui.base.TextAlignment;
import com.DavidM1A2.afraidofthedark.client.gui.fontLibrary.TrueTypeFont;
import com.DavidM1A2.afraidofthedark.common.constants.Constants;
import net.minecraft.util.math.MathHelper;
import org.apache.commons.lang3.StringUtils;
import org.lwjgl.util.Color;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * A text box control that will have multiple lines of text like a label
 */
public class AOTDGuiTextBox extends AOTDGuiContainer
{
	// A computed list of text lines
	private List<String> textLines = new ArrayList<>();
	// The font to draw the text with
	private final TrueTypeFont font;
	// The color to draw the text with
	private Color textColor = new Color(255, 255, 255, 255);
	// The overflow text that doesn't fit inside this text box
	private String overflowText = StringUtils.EMPTY;

	/**
	 * Constructor initializes the bounding box
	 *
	 * @param x The X location of the top left corner
	 * @param y The Y location of the top left corner
	 * @param width  The width of the component
	 * @param height The height of the component
	 * @param font The font to draw text with
	 */
	public AOTDGuiTextBox(Integer x, Integer y, Integer width, Integer height, TrueTypeFont font)
	{
		super(x, y, width, height);
		this.font = font;
	}

	/**
	 * Draw the text given the width and height as bounds
	 */
	@Override
	public void draw()
	{
		// Only draw the text if it's visible
		if (this.isVisible())
		{
			super.draw();
			// Draw each string in the text lines list one at at time
			for (int i = 0; i < this.textLines.size(); i++)
				this.font.drawString(
					this.getXScaled().floatValue(),
					this.getYScaled().floatValue() + i * this.font.getFontSize() * Constants.TEXT_SCALE_FACTOR * this.getScaleY().floatValue(),
					this.textLines.get(i),
					this.getScaleX().floatValue() * Constants.TEXT_SCALE_FACTOR,
					this.getScaleY().floatValue() * Constants.TEXT_SCALE_FACTOR,
					TextAlignment.ALIGN_LEFT,
		 			this.textColor.getRed() / 255f, this.textColor.getGreen() / 255f, this.textColor.getBlue() / 255f, this.textColor.getAlpha() / 255f
				);
		}
	}

	/**
	 * Sets the text inside the text box
	 *
	 * @param text The text to use
	 */
	public void setText(String text)
	{
		// Clear the original text
		this.textLines.clear();
		// Split the text into words
		StringTokenizer words = new StringTokenizer(text, " ");

		// The current line text
		String currentLineText = "";

		// Iterate over all words
		while (words.hasMoreTokens())
		{
			// Grab the first word
			String word = words.nextToken();
			// Replace tab characters with spaces since tab characters are buggy to render
			word = word.replace( "\t", "   ");

			// If the line is too long for the current text move to the next line
			if (this.font.getWidth(currentLineText + " " + word) * Constants.TEXT_SCALE_FACTOR > this.getWidth())
			{
				// Store the current line and move on
				this.textLines.add(currentLineText);

				// Store the word as the beginning of the next line
				currentLineText = word;
			}
			// Else append to the current line
			else
			{
				currentLineText = currentLineText + " " + word;
			}
		}

		// Compute the maximum number of lines that fit vertically inside the text box
		int maxLines = MathHelper.floor(this.getHeight() / this.font.getHeight() * Constants.TEXT_SCALE_FACTOR);
		// If the number of lines we have is less than or equal to the max we're OK
		if (textLines.size() <= maxLines)
		{
			this.overflowText = StringUtils.EMPTY;
		}
		// If the number of lines is greater than the max then we partition the lines into actual text lines and overflow text
		else
		{
			List<String> actualText = this.textLines.subList(0, maxLines);
			List<String> spareText = this.textLines.subList(maxLines, this.textLines.size());
			this.textLines = actualText;
			this.overflowText = String.join(" ", spareText);
		}
	}

	/**
	 * @return Returns the text found inside this text box
	 */
	public String getText()
	{
		return String.join("", this.textLines);
	}

	/**
	 * @return Returns any text that was not able to fit inside the text box
	 */
	public String getOverflowText()
	{
		return this.overflowText;
	}
}
