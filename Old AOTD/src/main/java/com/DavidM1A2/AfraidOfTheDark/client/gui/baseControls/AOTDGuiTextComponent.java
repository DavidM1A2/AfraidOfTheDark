/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.client.gui.baseControls;

import java.awt.Color;

import com.DavidM1A2.AfraidOfTheDark.client.trueTypeFont.TrueTypeFont;

public abstract class AOTDGuiTextComponent extends AOTDGuiContainer
{
	private TrueTypeFont font;
	private String text = "";
	private TextAlignment textAlignment = TextAlignment.ALIGN_LEFT;
	private float[] textColor = new float[] { 1.0f, 1.0f, 1.0f, 1.0f };
	protected float textScaleConstant = 0.3f;

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
				x = x + this.getWidthScaled() / 2 - 7;
				break;
			case ALIGN_RIGHT:
				x = x + this.getWidthScaled() - 15;
				break;
			default:
				break;
		}
		this.getFont().drawString(x, y, this.getText(), (float) (textScaleConstant * this.getScaleX()), (float) (textScaleConstant * this.getScaleY()), this.getTextAlignment(), this.getTextColor()[0], this.getTextColor()[1], this.getTextColor()[2], this.getTextColor()[3]);
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
		this.setTextColor(new float[] { color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f, color.getAlpha() / 255f });
	}

	public void setTextColor(float[] color)
	{
		if (color.length != 4)
			return;
		this.textColor = color;
	}

	public float[] getTextColor()
	{
		return this.textColor;
	}
}
