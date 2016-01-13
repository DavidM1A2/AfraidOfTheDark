/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.client.gui.guiScreens;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;

import com.DavidM1A2.AfraidOfTheDark.client.gui.AOTDActionListener;
import com.DavidM1A2.AfraidOfTheDark.client.gui.baseControls.AOTDGuiButton;
import com.DavidM1A2.AfraidOfTheDark.client.gui.baseControls.AOTDGuiComponent;
import com.DavidM1A2.AfraidOfTheDark.client.gui.baseControls.AOTDGuiImage;
import com.DavidM1A2.AfraidOfTheDark.client.gui.baseControls.AOTDGuiLabel;
import com.DavidM1A2.AfraidOfTheDark.client.gui.baseControls.AOTDGuiPanel;
import com.DavidM1A2.AfraidOfTheDark.client.gui.baseControls.AOTDGuiScreen;
import com.DavidM1A2.AfraidOfTheDark.client.gui.baseControls.AOTDGuiScrollBar;
import com.DavidM1A2.AfraidOfTheDark.client.gui.baseControls.AOTDGuiScrollPanel;
import com.DavidM1A2.AfraidOfTheDark.client.gui.baseControls.AOTDGuiTextField;
import com.DavidM1A2.AfraidOfTheDark.client.gui.customControls.AOTDGuiSpellStage;
import com.DavidM1A2.AfraidOfTheDark.client.settings.ClientData;

public class SpellCraftingGUI extends AOTDGuiScreen
{
	private AOTDGuiTextField spellName;
	private AOTDGuiScrollBar scrollBar;
	private AOTDGuiScrollPanel scrollPanel;
	private List<AOTDGuiSpellStage> spellStages = new ArrayList<AOTDGuiSpellStage>();
	private AOTDGuiLabel spellCost;

	public SpellCraftingGUI()
	{
		AOTDGuiPanel tablet = new AOTDGuiPanel(100, (360 - 256) / 2, 192, 256, false);

		AOTDGuiImage background = new AOTDGuiImage(0, 0, 192, 256, "textures/gui/spellCrafting/tabletBackground.png");
		tablet.add(background);
		spellName = new AOTDGuiTextField(50, 25, 110, 25, ClientData.getTargaMSHandFontSized(35f));
		spellName.setGhostText("Spell Name");
		tablet.add(spellName);
		scrollBar = new AOTDGuiScrollBar(20, 55, 15, 170);
		tablet.add(scrollBar);

		AOTDGuiImage spellCraftingSlotBackground = new AOTDGuiImage(40, 55, 120, 170, "textures/gui/spellCrafting/spellSlotBackground.png");
		tablet.add(spellCraftingSlotBackground);

		this.scrollPanel = new AOTDGuiScrollPanel(40, 55, 120, 170, true, scrollBar);

		this.addNewSpellStage();

		tablet.add(scrollPanel);

		AOTDGuiButton saveButton = new AOTDGuiButton(165, 105, 20, 20, null, "afraidofthedark:textures/gui/spellCrafting/okay.png");
		tablet.add(saveButton);
		AOTDGuiButton closeButton = new AOTDGuiButton(165, 130, 20, 20, null, "afraidofthedark:textures/gui/spellCrafting/delete.png");
		tablet.add(closeButton);
		AOTDGuiImage powerSource = new AOTDGuiImage(165, 155, 20, 20, "textures/gui/spellCrafting/tabletIconHolder.png");
		tablet.add(powerSource);
		AOTDGuiImage helpButton = new AOTDGuiImage(165, 180, 20, 20, "textures/gui/spellCrafting/question.png");
		tablet.add(helpButton);

		this.spellCost = new AOTDGuiLabel(10, 225, ClientData.getTargaMSHandFontSized(40));
		spellCost.setText("Spell Cost: 0");
		tablet.add(this.spellCost);

		this.getContentPane().add(tablet);

		/*
		 * 
		 */

		AOTDGuiPanel effectScroll = new AOTDGuiPanel(340, (360 - 256) / 2, 192, 256, false);
		AOTDGuiImage backgroundScroll = new AOTDGuiImage(0, 0, 192, 256, "textures/gui/spellCrafting/effectListScroll.png");
		effectScroll.add(backgroundScroll);

		AOTDGuiScrollBar effectsScrollBar = new AOTDGuiScrollBar(30, 50, 13, 160);
		effectScroll.add(effectsScrollBar);
		AOTDGuiScrollPanel effectsPanel = new AOTDGuiScrollPanel(10, 10, 150, 200, true, effectsScrollBar);
		effectsPanel.setMaximumOffset(100);
		effectScroll.add(effectsPanel);

		this.getContentPane().add(effectScroll);
	}

	public void addNewSpellStage()
	{
		final AOTDGuiSpellStage nextSpellStage = new AOTDGuiSpellStage(5, (5 + this.spellStages.size() * 35), 110, 45, false);
		nextSpellStage.addActionListenerToNewRow(new AOTDActionListener()
		{
			@Override
			public void actionPerformed(AOTDGuiComponent component, ActionType actionType)
			{
				if (actionType == ActionType.MousePressed)
					if (component.isHovered())
						SpellCraftingGUI.this.addNewSpellStage();
			}
		});
		nextSpellStage.addActionListenerToRemoveRow(new AOTDActionListener()
		{
			@Override
			public void actionPerformed(AOTDGuiComponent component, ActionType actionType)
			{
				if (actionType == ActionType.MousePressed)
					if (component.isHovered())
						SpellCraftingGUI.this.removeSpellStage();
			}
		});
		this.scrollPanel.add(nextSpellStage);
		this.spellStages.add(nextSpellStage);
		if (this.spellStages.size() == 1)
			nextSpellStage.hideMinus();
		else
		{
			for (int i = 0; i < this.spellStages.size() - 1; i++)
			{
				this.spellStages.get(i).hideMinus();
				this.spellStages.get(i).hidePlus();
			}
		}
		this.updateScrollOffset();
	}

	public void removeSpellStage()
	{
		if (this.spellStages.size() == 0)
			return;
		AOTDGuiSpellStage lastStage = this.spellStages.get(this.spellStages.size() - 1);
		this.scrollPanel.remove(lastStage);
		this.spellStages.remove(lastStage);
		if (this.spellStages.size() != 0)
		{
			AOTDGuiSpellStage secondToLastStage = this.spellStages.get(this.spellStages.size() - 1);
			secondToLastStage.showPlus();
			if (this.spellStages.size() != 1)
				secondToLastStage.showMinus();
		}
		this.updateScrollOffset();
	}

	public void updateScrollOffset()
	{
		scrollPanel.setMaximumOffset(this.spellStages.size() > 4 ? (this.spellStages.size() - 4) * 35 : 0);
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
