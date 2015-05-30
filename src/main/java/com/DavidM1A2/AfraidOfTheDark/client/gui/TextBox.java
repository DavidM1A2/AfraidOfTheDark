/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.client.gui;

import java.util.ArrayList;
import java.util.List;

import com.DavidM1A2.AfraidOfTheDark.refrence.CustomFont;
import com.google.common.base.Splitter;

// Textbox for simple text painting
public class TextBox
{
	private int xPosition;
	private int yPosition;
	private int width;
	private int height;
	private final CustomFont myFontRefrence;

	public TextBox(final int x, final int y, final int width, final int height, final CustomFont myFont)
	{
		// Given x, y, widht, height, and a font we can draw the textbox
		this.xPosition = x;
		this.yPosition = y;
		this.width = width;
		this.height = height;
		this.myFontRefrence = myFont;
	}

	// Draw the text given the width and height as bounds
	public String drawText(final String text)
	{
		String toReturn = "";
		int x = this.xPosition;
		final int y = this.yPosition;
		int line = 0;
		for (final Object o : this.splitString(text))
		{
			final String string = (String) o;
			if ((y + line) > this.height)
			{
				x = x + this.width;
				toReturn = toReturn + string;
			}
			else
			{
				this.myFontRefrence.drawString(string, x, y + line, 0xFF800000);
				line = line + (this.myFontRefrence.getFontSize() / 2);
			}
		}
		return toReturn;
	}

	// Split a string based on page width
	private List<String> splitString(String text)
	{
		final List<String> toReturn = new ArrayList<String>();
		final float pixelsAcrossPage = this.width;
		final int charactersPerPage = 2 * (int) (Math.floor(pixelsAcrossPage / (this.myFontRefrence.getFontSize())));
		while (!text.equals(""))
		{
			final Iterable iterable = Splitter.fixedLength(charactersPerPage).split(text);
			if (iterable.iterator().hasNext())
			{
				String next = (String) iterable.iterator().next();
				int charIndex = (next.length() > charactersPerPage) ? charactersPerPage : next.length();
				if (next.length() == charactersPerPage)
				{
					while (next.charAt(charIndex - 1) != ' ')
					{
						if ((charIndex - 1) <= 0)
						{
							break;
						}
						next = next.substring(0, charIndex - 1);
						charIndex = charIndex - 1;
					}
				}
				text = text.substring(charIndex);
				toReturn.add(next);
			}
		}

		return toReturn;
	}

	// Update x, y, width, and height of a textbox
	public void updateBounds(final int x, final int y, final int width, final int height)
	{
		this.xPosition = x;
		this.yPosition = y;
		this.width = width;
		this.height = height;
	}
}
