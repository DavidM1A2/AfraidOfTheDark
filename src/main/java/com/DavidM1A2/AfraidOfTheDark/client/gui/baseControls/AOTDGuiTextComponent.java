/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.client.gui.baseControls;

import java.awt.Color;

import com.DavidM1A2.AfraidOfTheDark.client.gui.AOTDGuiUtility;
import com.DavidM1A2.AfraidOfTheDark.client.trueTypeFont.TrueTypeFont;

public abstract class AOTDGuiTextComponent extends AOTDGuiContainer
{
	private TrueTypeFont font;
	private String text = "";
	private TextAlignment textAlignment = TextAlignment.ALIGN_LEFT;
	private Color textColor = Color.WHITE;

	public AOTDGuiTextComponent(int x, int y, int width, int height, TrueTypeFont font)
	{
		super(x, y, width, height);
		this.font = font;
	}
	
	protected void drawText(float x, float y)
	{
		if (this.getFont() == null)
			return;
		switch (this.getTextAlignment()) 
		{
			case ALIGN_CENTER:
				x = x + this.getWidthScaled() / 2 - AOTDGuiUtility.realToMcCoord((int) this.getFont().getWidth(this.getText())) / 2;
				break;
			case ALIGN_RIGHT:
				x = x + this.getWidthScaled();
				break;
			default:
				break;
		}
		this.getFont().drawString(x, y, this.getText(), 0.3f, 0.3f, TextAlignment.ALIGN_LEFT, this.textColor.getRed() / 255.0f, this.textColor.getGreen() / 255.0f, this.textColor.getBlue() / 255.0f, this.textColor.getAlpha() / 255.0f);
	}

	public TrueTypeFont getFont()
	{
		return this.font;
	}

	public void setFont(TrueTypeFont font)
	{
		this.font = font;
	}

	public void setText(String text)
	{
		this.text = text;
	}

	public String getText()
	{
		return this.text;
	}

	public void setTextAlignment(TextAlignment textAlignment)
	{
		this.textAlignment = textAlignment;
	}

	public TextAlignment getTextAlignment()
	{
		return this.textAlignment;
	}
	
	public void setTextColor(Color color)
	{
		this.textColor = color;
	}
	
	public Color getTextColor()
	{
		return this.textColor;
	}
}
