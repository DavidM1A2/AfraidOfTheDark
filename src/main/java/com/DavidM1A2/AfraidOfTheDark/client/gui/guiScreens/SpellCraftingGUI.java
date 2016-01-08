/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.client.gui.guiScreens;

import java.io.IOException;

import org.lwjgl.opengl.GL11;

import com.DavidM1A2.AfraidOfTheDark.client.gui.baseControls.AOTDGuiImage;
import com.DavidM1A2.AfraidOfTheDark.client.gui.baseControls.AOTDGuiPanel;
import com.DavidM1A2.AfraidOfTheDark.client.gui.baseControls.AOTDGuiScreen;
import com.DavidM1A2.AfraidOfTheDark.client.gui.baseControls.AOTDGuiScrollBar;
import com.DavidM1A2.AfraidOfTheDark.client.gui.baseControls.AOTDGuiScrollPanel;
import com.DavidM1A2.AfraidOfTheDark.client.gui.baseControls.AOTDGuiTextField;
import com.DavidM1A2.AfraidOfTheDark.client.trueTypeFont.FontLoader;
import com.DavidM1A2.AfraidOfTheDark.client.trueTypeFont.TrueTypeFont;

import net.minecraft.util.ResourceLocation;

public class SpellCraftingGUI extends AOTDGuiScreen
{
	private static final TrueTypeFont SPELL_NAME_FONT = FontLoader.createFont(new ResourceLocation("afraidofthedark:fonts/Targa MS Hand.ttf"), 40f, true);
	private AOTDGuiTextField spellName;
	private AOTDGuiScrollBar scrollBar;

	public SpellCraftingGUI()
	{
		AOTDGuiPanel tablet = new AOTDGuiPanel((640 - 192) / 2, (360 - 256) / 2, 192, 256, false);

		AOTDGuiImage background = new AOTDGuiImage(0, 0, 192, 256, "textures/gui/spellCrafting/tabletBackground.png");
		tablet.add(background);
		spellName = new AOTDGuiTextField(50, 25, 110, 25, SPELL_NAME_FONT);
		spellName.setGhostText("Spell Name");
		tablet.add(spellName);
		scrollBar = new AOTDGuiScrollBar(20, 55, 15, 190);
		tablet.add(scrollBar);

		AOTDGuiScrollPanel scrollPanel = new AOTDGuiScrollPanel(40, 55, 120, 170, true, scrollBar);
		scrollPanel.setMaximumOffset(200);
		AOTDGuiImage spellCraftingSlotBackground = new AOTDGuiImage(0, 0, 120, 170, "textures/gui/spellCrafting/spellSlotBackground.png");
		scrollPanel.add(spellCraftingSlotBackground);

		tablet.add(scrollPanel);

		this.getContentPane().add(tablet);
	}

	@Override
	protected void keyTyped(char character, int keyCode) throws IOException
	{
		super.keyTyped(character, keyCode);
		if (!this.spellName.isFocused())
		{
			if (keyCode == INVENTORY_KEYCODE)
			{
				entityPlayer.closeScreen();
				GL11.glFlush();
			}
		}
	}

	@Override
	public boolean inventoryToCloseGuiScreen()
	{
		return false;
	}

	@Override
	public boolean drawGradientBackground()
	{
		return true;
	}
}
