/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.client.gui.baseControls;

import java.awt.Color;

import org.lwjgl.opengl.GL11;

import com.DavidM1A2.AfraidOfTheDark.client.gui.AOTDGuiUtility;
import com.DavidM1A2.AfraidOfTheDark.client.trueTypeFont.FontHelper;
import com.DavidM1A2.AfraidOfTheDark.client.trueTypeFont.TrueTypeFont;

import net.minecraft.world.gen.FlatGeneratorInfo;

public abstract class AOTDGuiTextComponent extends AOTDGuiContainer
{
	private TrueTypeFont font;
	private String text = "";
	private TextAlignment textAlignment = TextAlignment.ALIGN_LEFT;
	private float[] textColor = new float[] {1.0f, 1.0f, 1.0f, 1.0f};
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
				x = x + this.getWidth() / 2 - 7;
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
		this.textColor[0] = color.getRed() / 255.0f;
		this.textColor[1] = color.getGreen() / 255.0f;
		this.textColor[2] = color.getBlue() / 255.0f;
		this.textColor[3] = color.getAlpha() / 255.0f;
	}
	
	public void setTextColor(float[] color)
	{
		this.textColor = color;
	}
	
	public float[] getTextColor()
	{
		return this.textColor;
	}
}
