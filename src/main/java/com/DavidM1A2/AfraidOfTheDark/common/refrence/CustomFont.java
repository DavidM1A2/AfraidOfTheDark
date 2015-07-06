/*
 * Author: TheObliterator
 * Mod: Afraid of the Dark
 */
package com.DavidM1A2.AfraidOfTheDark.common.refrence;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.InputStream;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.texture.DynamicTexture;

import org.lwjgl.opengl.GL11;

/**
 *
 * @author TheObliterator
 *
 *         A class to create and draw true type fonts onto the Minecraft game engine.
 */
public class CustomFont
{

	private FontMetrics metrics;
	private Font myFont;
	private final int eachCharImageDimension = 256;
	private DynamicTexture[] textures;

	// NEW
	private int[] images;
	private char[] characters;
	private int fontSize;

	/**
	 * Instantiates the font, filling in default start and end character parameters.
	 *
	 * 'new CustomFont(ModLoader.getMinecraftInstance(), "Arial", 12);
	 *
	 * @param mc
	 *            The Minecraft instance for the font to be bound to.
	 * @param fontName
	 *            The name of the font to be drawn.
	 * @param size
	 *            The size of the font to be drawn.
	 */
	public CustomFont(final Object font, final int size)
	{
		this(font, size, 32, 126);
	}

	/**
	 * Instantiates the font, pre-rendering a sprite font image by using a true type font on a bitmap. Then allocating that bitmap to the Minecraft
	 * rendering engine for later use.
	 *
	 * 'new CustomFont(ModLoader.getMinecraftInstance(), "Arial", 12, 32, 126);'
	 *
	 * @param mc
	 *            The Minecraft instance for the font to be bound to.
	 * @param fontName
	 *            The name of the font to be drawn.
	 * @param size
	 *            The size of the font to be drawn.
	 * @param startChar
	 *            The starting ASCII character id to be drawable. (Default 32)
	 * @param endChar
	 *            The ending ASCII character id to be drawable. (Default 126)
	 */
	public CustomFont(final Object font, final int size, final int startChar, final int endChar)
	{
		try
		{
			if ((font instanceof InputStream) && (this.myFont == null))
			{
				this.myFont = Font.createFont(Font.TRUETYPE_FONT, (InputStream) font);
			}
		}
		catch (final Exception e)
		{
			e.printStackTrace();
		}

		this.setFontSize(size, startChar, endChar, true);
	}

	/**
	 * Draws a given string with an automatically calculated shadow below it.
	 *
	 * @param gui
	 *            The gui/subclass to be drawn on
	 * @param text
	 *            The string to be drawn
	 * @param x
	 *            The x position to start drawing
	 * @param y
	 *            The y position to start drawing
	 * @param color
	 *            The color of the non-shadowed text (Hex)
	 */
	public void drawStringS(final String text, final int x, final int y, final int color)
	{
		final int l = color & 0xff000000;
		int shade = (color & 0xfcfcfc) >> 2;
		shade += l;
		this.drawString(text, x + 1, y + 1, shade);
		this.drawString(text, x, y, color);
	}

	/**
	 * Draws a given string onto a gui/subclass.
	 *
	 * @param gui
	 *            The gui/subclass to be drawn on
	 * @param text
	 *            The string to be drawn
	 * @param x
	 *            The x position to start drawing
	 * @param y
	 *            The y position to start drawing
	 * @param color
	 *            The color of the non-shadowed text (Hex)
	 */
	public void drawString(final String text, int x, int y, final int color)
	{
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		final float red = ((color >> 16) & 0xff) / 255F;
		final float green = ((color >> 8) & 0xff) / 255F;
		final float blue = (color & 0xff) / 255F;
		final float alpha = ((color >> 24) & 0xff) / 255F;
		GL11.glColor4f(red, green, blue, alpha);
		final int startX = x;

		for (int i = 0; i < text.length(); i++)
		{
			final char c = text.charAt(i);

			for (int j = 0; j < this.characters.length; j++)
			{
				if (c == this.characters[j])
				{
					GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.images[j]);
				}
			}

			if (c == '\\')
			{
				final char type = text.charAt(i + 1);
				if (type == 'n')
				{
					y += this.metrics.getAscent() + 2;
					x = startX;
				}
				i++;
				continue;
			}
			this.drawChar(c, x, y);
			x += this.metrics.getStringBounds("" + c, null).getWidth() / (this.eachCharImageDimension / this.fontSize);
		}

		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
	}

	/**
	 * Returns the created FontMetrics which is used to retrive various information about the True Type Font
	 *
	 * @return FontMetrics of the created font.
	 */
	public FontMetrics getMetrics()
	{
		return this.metrics;
	}

	/**
	 * Gets the drawing width of a given string of string.
	 *
	 * @param text
	 *            The string to be measured
	 * @return The width of the given string.
	 */
	public int getStringWidth(final String text)
	{
		return (int) this.getBounds(text).getWidth();
	}

	/**
	 * Gets the drawing height of a given string of string.
	 *
	 * @param text
	 *            The string to be measured
	 * @return The height of the given string.
	 */
	public int getStringHeight(final String text)
	{
		return (int) this.getBounds(text).getHeight();
	}

	/**
	 * A method that returns a Rectangle that contains the width and height demensions of the given string.
	 *
	 * @param text
	 *            The string to be measured
	 * @return Rectangle containing width and height that the text will consume when drawn.
	 */
	private Rectangle getBounds(final String text)
	{
		int w = 0;
		int h = 0;
		int tw = 0;
		for (int i = 0; i < text.length(); i++)
		{
			final char c = text.charAt(i);
			if (c == '\\')
			{
				final char type = text.charAt(i + 1);

				if (type == 'n')
				{
					h += this.metrics.getAscent() + 2;
					if (tw > w)
					{
						w = tw;
					}
					tw = 0;
				}
				i++;
				continue;
			}
			tw += this.metrics.stringWidth("" + c);
		}
		if (tw > w)
		{
			w = tw;
		}
		h += this.metrics.getAscent();
		return new Rectangle(0, 0, w, h);
	}

	/**
	 * Private drawing method used within other drawing methods.
	 */
	private void drawChar(final char c, final int x, final int y)
	{
		Gui.drawScaledCustomSizeModalRect(x, y, 0, 0, this.eachCharImageDimension, this.eachCharImageDimension, this.fontSize, this.fontSize, this.eachCharImageDimension, this.eachCharImageDimension);
	}

	public void setFontSize(int size, final int startChar, final int endChar, final boolean firstLaunch)
	{
		if (!firstLaunch)
		{
			for (final DynamicTexture texture : this.textures)
			{
				texture.deleteGlTexture();
			}
		}

		this.fontSize = size;
		size = 100;
		this.images = new int[endChar - startChar];
		this.characters = new char[endChar - startChar];
		this.textures = new DynamicTexture[endChar - startChar];

		// Create a bitmap and fill it with a transparent color as well
		// as obtain a Graphics instance which can be drawn on.
		// NOTE: It is CRUICIAL that the size of the image is 256x256, if
		// it is not the Minecraft engine will not draw it properly.
		BufferedImage img = new BufferedImage(this.eachCharImageDimension, this.eachCharImageDimension, BufferedImage.TYPE_INT_ARGB);
		Graphics g = img.getGraphics();
		g.setFont(this.myFont.deriveFont((float) size));

		g.setColor(new Color(255, 255, 255, 0));
		g.fillRect(0, 0, this.eachCharImageDimension, this.eachCharImageDimension);
		g.setColor(Color.white);
		this.metrics = g.getFontMetrics();

		// Draw the specified range of characters onto
		// the new bitmap, spacing according to the font
		// widths. Also allocating positions of characters
		// on the bitmap to two arrays which will be used
		// later when drawing.
		final int x = 2;
		final int y = 2;

		for (int i = startChar; i < endChar; i++)
		{
			img = new BufferedImage(this.eachCharImageDimension, this.eachCharImageDimension, BufferedImage.TYPE_INT_ARGB);
			g = img.getGraphics();
			g.setFont(this.myFont.deriveFont((float) size).deriveFont(Font.BOLD));

			g.setColor(new Color(255, 255, 255, 0));
			g.fillRect(0, 0, 64, 64);
			g.setColor(Color.white);
			this.metrics = g.getFontMetrics();

			g.drawString("" + ((char) i), x, y + g.getFontMetrics().getAscent());
			this.textures[i - startChar] = new DynamicTexture(img);
			this.images[i - startChar] = this.textures[i - startChar].getGlTextureId();

			this.characters[i - startChar] = ((char) i);

			img.flush();
			g.dispose();
		}

	}

	public int getFontSize()
	{
		return this.fontSize;
	}
}