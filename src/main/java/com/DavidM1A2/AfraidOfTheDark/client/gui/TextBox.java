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
	private CustomFont myFontRefrence;

	public TextBox(int x, int y, int width, int height, CustomFont myFont)
	{
		// Given x, y, widht, height, and a font we can draw the textbox
		this.xPosition = x;
		this.yPosition = y;
		this.width = width;
		this.height = height;
		this.myFontRefrence = myFont;
	}

	// Draw the text given the width and height as bounds
	public String drawText(String text)
	{
		String toReturn = "";
		int x = xPosition;
		int y = yPosition;
		int line = 0;
		for (Object o : splitString(text))
		{
			String string = (String) o;
			if (y + line > this.height)
			{
				x = x + width;
				toReturn = toReturn + string;
			}
			else
			{
				myFontRefrence.drawString(string, x, y + line, 0xFF800000);
				line = line + myFontRefrence.getFontSize() / 2;
			}
		}
		return toReturn;
	}

	// Split a string based on page width
	private List<String> splitString(String text)
	{
		List<String> toReturn = new ArrayList<String>();
		float pixelsAcrossPage = this.width;
		int charactersPerPage = 2 * (int) (Math.floor(pixelsAcrossPage / (myFontRefrence.getFontSize())));
		String string = "";
		while (!text.equals(""))
		{
			Iterable iterable = Splitter.fixedLength(charactersPerPage).split(text);
			if (iterable.iterator().hasNext())
			{
				String next = (String) iterable.iterator().next();
				int charIndex = (next.length() > charactersPerPage) ? charactersPerPage : next.length();
				if (next.length() == charactersPerPage)
				{
					while (next.charAt(charIndex - 1) != ' ')
					{
						if (charIndex - 1 <= 0)
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
	public void updateBounds(int x, int y, int width, int height)
	{
		this.xPosition = x;
		this.yPosition = y;
		this.width = width;
		this.height = height;
	}
}
