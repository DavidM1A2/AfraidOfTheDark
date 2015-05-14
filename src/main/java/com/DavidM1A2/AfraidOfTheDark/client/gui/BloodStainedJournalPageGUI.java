package com.DavidM1A2.AfraidOfTheDark.client.gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import com.DavidM1A2.AfraidOfTheDark.refrence.Refrence;
import com.google.common.base.Splitter;

public class BloodStainedJournalPageGUI extends GuiScreen
{
	private final String text;
	private final String title;

	private int xCornerOfPage = 0;
	private int yCornerOfPage = 0;
	private int journalWidth = 0;

	public BloodStainedJournalPageGUI(String text, String title)
	{
		super();
		this.text = text;
		this.title = title;
	}

	@Override
	public void initGui()
	{
		super.initGui();
		buttonList.clear();
	}

	// Opening a research book DOES NOT pause the game (unlike escape)
	@Override
	public boolean doesGuiPauseGame()
	{
		return false;
	}

	// To draw the screen we first draw the default GUI background, then the
	// background images. Then we add buttons and a frame.
	@Override
	public void drawScreen(int i, int j, float f)
	{
		drawDefaultBackground();

		double scale;

		GL11.glColor4f(1, 1, 1, 1);
		ResourceLocation texture = new ResourceLocation("afraidofthedark:textures/gui/bloodStainedJournalPage.png");
		mc.renderEngine.bindTexture(texture);

		scale = this.width / 640.0;
		this.xCornerOfPage = (int) ((this.width - 330 * scale) / 2);
		this.yCornerOfPage = (int) ((this.height - 330 * scale) / 2);
		this.journalWidth = (int) (330 * scale);

		this.drawScaledCustomSizeModalRect(xCornerOfPage, yCornerOfPage, 0, 0, journalWidth, journalWidth, journalWidth, journalWidth, journalWidth, journalWidth);

		this.drawText(xCornerOfPage + (int) (25 * scale), yCornerOfPage + (int) (20 * scale), scale);

		super.drawScreen(i, j, f);
	}

	// If E is typed we close the GUI screen
	@Override
	protected void keyTyped(char character, int iDontKnowWhatThisDoes) throws IOException
	{
		if (character == 'e' || character == 'E')
		{
			Minecraft.getMinecraft().thePlayer.closeScreen();
			GL11.glFlush();
		}
		super.keyTyped(character, iDontKnowWhatThisDoes);
	}

	private void drawText(int x, int y, double scale)
	{
		Refrence.journalTitleFont.setFontSize((int) (scale * 10), 32, 126);
		Refrence.journalTitleFont.drawString(this, this.title, x, y, 0xFF800000);

		int line = 25;
		Refrence.journalFont.setFontSize((int) (scale * 10), 32, 126);
		for (Object o : splitString(text, scale))
		{
			String string = (String) o;
			if (y + line > this.height - 60)
			{
				x = (int) (x + getSinglePageWidth() - 20);
				line = 25;
			}
			Refrence.journalFont.drawString(this, string, x, y + line, 0xFF800000);
			line = line + 10;
		}
	}

	private List<String> splitString(String text, double scale)
	{
		List<String> toReturn = new ArrayList<String>();
		float pixelsAcrossPage = getSinglePageWidth();
		int charactersPerPage = (int) (Math.floor(pixelsAcrossPage / (Refrence.journalFont.getFontSize() - 5 * scale)));
		String string = "";
		while (!text.equals(""))
		{
			Iterable iterator = Splitter.fixedLength(charactersPerPage).split(text);
			if (iterator.iterator().hasNext())
			{
				String next = (String) iterator.iterator().next();
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

	private float getSinglePageWidth()
	{
		return (float) (journalWidth / 2.0);
	}
}
