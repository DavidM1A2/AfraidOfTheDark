/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.client.gui.customControls;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.commons.lang3.StringUtils;
import org.lwjgl.opengl.GL11;

import com.DavidM1A2.AfraidOfTheDark.common.utility.Utility;

// Textbox for simple text painting
public class AOTDGuiTextBox extends AOTDGuiTextComponent
{
	private List<String> textLines = new ArrayList<String>();
	private int maxCharsPerLine = 24;

	public AOTDGuiTextBox()
	{
		super();
	}

	public AOTDGuiTextBox(final int x, final int y, final int width, final int height, TrueTypeFont font)
	{
		super(x, y, width, height, font);
	}

	@Override
	public void setText(String textInput)
	{
		this.textLines.clear();
		StringTokenizer tok = new StringTokenizer(textInput, " ");
		int lineWidth = 0;
		int currentLineIndex = 0;
		while (tok.hasMoreTokens())
		{
			String word = StringUtils.replace(tok.nextToken(), "\t", "   ");
			int nextLineLength = lineWidth + word.length() + 1;

			if (nextLineLength > maxCharsPerLine)
			{
				currentLineIndex = currentLineIndex + 1;
				lineWidth = word.length() + 1;
				nextLineLength = word.length() + 1;
			}

			if (Utility.hasIndex(this.textLines, currentLineIndex))
			{
				this.textLines.set(currentLineIndex, this.textLines.get(currentLineIndex) + word + " ");
			}
			else
			{
				this.textLines.add(word + " ");
			}

			lineWidth = nextLineLength;
		}
	}

	@Override
	public String getText()
	{
		return StringUtils.join(this.textLines.toArray());
	}

	// Draw the text given the width and height as bounds
	public void draw()
	{
		super.draw();
		int x = this.getX();
		int y = this.getY();
		GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
		GL11.glPushMatrix();
		GL11.glScaled(this.getScaleX(), this.getScaleY(), 1.0f);
		for (int i = 0; i < this.textLines.size(); i++)
		{
			String text = this.textLines.get(i);

			this.getFont().drawString(x, y + i * (this.getFont().getFontSize() / 3), text, 0.3f, 0.3f, this.getColor());
		}
		GL11.glPopMatrix();
	}

	public String getOverflowText(String text)
	{
		String toReturn = "";
		StringTokenizer tok = new StringTokenizer(text, " ");
		int lineWidth = 0;
		int currentLineIndex = 0;
		while (tok.hasMoreTokens())
		{
			String word = StringUtils.replace(tok.nextToken(), "\t", "   ");
			int nextLineLength = lineWidth + word.length() + 1;

			if (word.contains("   "))
			{
				nextLineLength = nextLineLength + 2;
			}

			if (nextLineLength > maxCharsPerLine)
			{
				currentLineIndex = currentLineIndex + 1;
				lineWidth = word.length() + 1;
				nextLineLength = word.length() + 1;
			}

			if (currentLineIndex * this.getFont().getHeight() * 0.25 > this.getHeight())
			{
				if (toReturn.isEmpty())
					toReturn = word;
				else
					toReturn = toReturn + " " + word;
			}

			lineWidth = nextLineLength;
		}

		return toReturn;
	}
}
