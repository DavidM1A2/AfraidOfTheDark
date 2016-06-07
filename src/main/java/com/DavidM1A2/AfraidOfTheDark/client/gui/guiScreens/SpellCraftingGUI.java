/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.client.gui.guiScreens;

import java.io.IOException;

import org.apache.commons.lang3.SerializationUtils;

import com.DavidM1A2.AfraidOfTheDark.client.gui.GuiHandler;
import com.DavidM1A2.AfraidOfTheDark.client.gui.baseControls.AOTDGuiImage;
import com.DavidM1A2.AfraidOfTheDark.client.gui.customControls.AOTDGuiSpellComponent;
import com.DavidM1A2.AfraidOfTheDark.client.gui.customControls.AOTDGuiSpellScroll;
import com.DavidM1A2.AfraidOfTheDark.client.gui.customControls.AOTDGuiSpellTablet;
import com.DavidM1A2.AfraidOfTheDark.client.gui.eventListeners.AOTDKeyListener;
import com.DavidM1A2.AfraidOfTheDark.client.gui.events.AOTDKeyEvent;
import com.DavidM1A2.AfraidOfTheDark.client.settings.ClientData;
import com.DavidM1A2.AfraidOfTheDark.common.initializeMod.ModCapabilities;
import com.DavidM1A2.AfraidOfTheDark.common.reference.Reference;
import com.DavidM1A2.AfraidOfTheDark.common.spell.Spell;
import com.DavidM1A2.AfraidOfTheDark.common.spell.SpellStage;

import net.minecraft.util.ChatComponentText;

public class SpellCraftingGUI extends AOTDGuiScreen
{
	private Spell spell;
	private AOTDGuiSpellTablet tablet;
	private AOTDGuiSpellScroll scroll;
	private AOTDGuiImage helpScreen;

	public SpellCraftingGUI()
	{
		this.spell = SerializationUtils.<Spell> clone(ClientData.spellToBeEdited);

		// Minimum 1 spell stage
		if (this.spell.getSpellStages().size() == 0)
			this.spell.getSpellStages().add(new SpellStage());

		this.tablet = new AOTDGuiSpellTablet(100, (360 - 256) / 2, 192, 256, this.spell, this);
		this.getContentPane().add(tablet);

		this.scroll = new AOTDGuiSpellScroll(340, (360 - 256) / 2, 220, 256, this.spell, this);
		this.getContentPane().add(scroll);

		this.helpScreen = new AOTDGuiImage(0, 0, 640, 360, "afraidofthedark:textures/gui/spellCrafting/helpScreen.png");
		this.helpScreen.addKeyListener(new AOTDKeyListener()
		{
			@Override
			public void keyTyped(AOTDKeyEvent event)
			{
				if (event.getSource().isVisible())
				{
					event.getSource().setVisible(false);
					event.consume();
				}
			}

			@Override
			public void keyReleased(AOTDKeyEvent event)
			{
				if (event.getSource().isVisible())
				{
					event.getSource().setVisible(false);
					event.consume();
				}
			}

			@Override
			public void keyPressed(AOTDKeyEvent event)
			{
				if (event.getSource().isVisible())
				{
					event.getSource().setVisible(false);
					event.consume();
				}
			}
		});
		this.getContentPane().add(this.helpScreen);
		this.hideHelpScreen();
	}

	public void saveSpell()
	{
		entityPlayer.getCapability(ModCapabilities.PLAYER_DATA, null).getSpellManager().updateSpell(this.spell);
		entityPlayer.getCapability(ModCapabilities.PLAYER_DATA, null).syncSpellManager();
		entityPlayer.addChatMessage(new ChatComponentText("Spell " + spell.getName() + " successfully saved."));
	}

	@Override
	protected void keyTyped(char character, int keyCode) throws IOException
	{
		super.keyTyped(character, keyCode);
		if (this.tablet.closeGuiOnInventory())
		{
			if (keyCode == INVENTORY_KEYCODE)
			{
				AOTDGuiSpellComponent.setSelectedComponent(null);
				entityPlayer.openGui(Reference.MOD_ID, GuiHandler.SPELL_SELECTION_ID, entityPlayer.worldObj, entityPlayer.getPosition().getX(), entityPlayer.getPosition().getY(), entityPlayer.getPosition().getZ());
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

	public void showHelpScreen()
	{
		this.helpScreen.setVisible(true);
	}

	public void hideHelpScreen()
	{
		this.helpScreen.setVisible(false);
	}
}
