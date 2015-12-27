/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.client.gui.baseControls;

public abstract class AOTDGuiTextComponent extends AOTDGuiContainer
{
	private TrueTypeFont font;
	private String text = "";
	private TextAlignment textAlignment = TextAlignment.LEFT;

	public AOTDGuiTextComponent(int x, int y, int width, int height, TrueTypeFont font)
	{
		super(x, y, width, height);
		this.font = font;
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
}
