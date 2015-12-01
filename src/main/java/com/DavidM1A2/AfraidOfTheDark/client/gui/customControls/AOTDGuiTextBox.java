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

import com.DavidM1A2.AfraidOfTheDark.common.utility.Utility;

// Textbox for simple text painting
public class AOTDGuiTextBox extends AOTDGuiTextComponent
{
	private List<String> textLines = new ArrayList<String>();

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

			if (lineWidth + word.length() > 20)
			{
				currentLineIndex = currentLineIndex + 1;
				lineWidth = 0;
			}

			if (Utility.hasIndex(this.textLines, currentLineIndex))
			{
				this.textLines.set(currentLineIndex, this.textLines.get(currentLineIndex) + " " + word);
			}
			else
			{
				this.textLines.add(word + " ");
			}

			lineWidth = lineWidth + word.length();
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
		for (int i = 0; i < this.textLines.size(); i++)
		{
			String text = this.textLines.get(i);

			this.getFont().drawString(x, y + i * (this.getFont().getFontSize() / 3), text, 0.3f, 0.3f, this.getColor());
		}
	}

	public String getOverflowText(String text)
	{
		int x = this.getX();
		int y = this.getY();
		int currentLine = 0;

		while (!text.equals(" ") && currentLine + y < this.getHeight())
		{
			String originalTextDuplicate = text;
			while (this.getFont().getWidth(originalTextDuplicate) > this.getWidth())
			{
				originalTextDuplicate = originalTextDuplicate.substring(0, originalTextDuplicate.length() - 1);
			}

			char nextChar = originalTextDuplicate.charAt(originalTextDuplicate.length() - 1);

			while (nextChar != ' ')
			{
				if ((originalTextDuplicate.length() - 1) < 0)
				{
					break;
				}
				originalTextDuplicate = originalTextDuplicate.substring(0, originalTextDuplicate.length() - 1);
				nextChar = originalTextDuplicate.charAt(originalTextDuplicate.length() - 1);
			}
			text = text.substring(originalTextDuplicate.length() - 1, text.length());
			currentLine = currentLine + this.getFont().getFontSize() / 3;
		}

		return text;
	}
}
