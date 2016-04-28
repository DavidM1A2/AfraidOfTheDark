/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.client.gui.guiScreens;

import java.io.IOException;

import org.lwjgl.opengl.GL11;

import com.DavidM1A2.AfraidOfTheDark.client.gui.customControls.AOTDGuiSpellScroll;
import com.DavidM1A2.AfraidOfTheDark.client.gui.customControls.AOTDGuiSpellTablet;
import com.DavidM1A2.AfraidOfTheDark.client.settings.ClientData;
import com.DavidM1A2.AfraidOfTheDark.common.spell.Spell;

public class SpellCraftingGUI extends AOTDGuiScreen
{
	private Spell spell;
	private AOTDGuiSpellTablet tablet;
	private AOTDGuiSpellScroll scroll;

	public SpellCraftingGUI()
	{
		this.spell = ClientData.spellToBeEdited;

		this.tablet = new AOTDGuiSpellTablet(100, (360 - 256) / 2, 192, 256, this.spell);
		this.getContentPane().add(tablet);

		this.scroll = new AOTDGuiSpellScroll(340, (360 - 256) / 2, 220, 256, this.spell, this);
		this.getContentPane().add(scroll);
	}

	@Override
	protected void keyTyped(char character, int keyCode) throws IOException
	{
		super.keyTyped(character, keyCode);
		if (this.tablet.closeGuiOnInventory())
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

	public AOTDGuiSpellTablet getTablet()
	{
		return this.tablet;
	}

	public AOTDGuiSpellScroll getScroll()
	{
		return this.scroll;
	}
}
