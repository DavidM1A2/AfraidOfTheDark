/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.client.gui.customControls;

import java.util.ArrayList;
import java.util.List;

import com.DavidM1A2.AfraidOfTheDark.client.gui.GuiHandler;
import com.DavidM1A2.AfraidOfTheDark.client.gui.baseControls.AOTDGuiButton;
import com.DavidM1A2.AfraidOfTheDark.client.gui.baseControls.AOTDGuiContainer;
import com.DavidM1A2.AfraidOfTheDark.client.gui.baseControls.AOTDGuiImage;
import com.DavidM1A2.AfraidOfTheDark.client.gui.baseControls.AOTDGuiLabel;
import com.DavidM1A2.AfraidOfTheDark.client.gui.baseControls.AOTDGuiPanel;
import com.DavidM1A2.AfraidOfTheDark.client.gui.baseControls.AOTDGuiScrollBar;
import com.DavidM1A2.AfraidOfTheDark.client.gui.baseControls.AOTDGuiScrollPanel;
import com.DavidM1A2.AfraidOfTheDark.client.gui.baseControls.AOTDGuiTextField;
import com.DavidM1A2.AfraidOfTheDark.client.gui.eventListeners.AOTDKeyListener;
import com.DavidM1A2.AfraidOfTheDark.client.gui.eventListeners.AOTDMouseListener;
import com.DavidM1A2.AfraidOfTheDark.client.gui.events.AOTDKeyEvent;
import com.DavidM1A2.AfraidOfTheDark.client.gui.events.AOTDMouseEvent;
import com.DavidM1A2.AfraidOfTheDark.client.gui.events.AOTDMouseEvent.MouseButtonClicked;
import com.DavidM1A2.AfraidOfTheDark.client.gui.guiScreens.SpellCraftingGUI;
import com.DavidM1A2.AfraidOfTheDark.client.settings.ClientData;
import com.DavidM1A2.AfraidOfTheDark.common.initializeMod.ModSounds;
import com.DavidM1A2.AfraidOfTheDark.common.reference.Reference;
import com.DavidM1A2.AfraidOfTheDark.common.spell.ISpellComponentEnum;
import com.DavidM1A2.AfraidOfTheDark.common.spell.Spell;
import com.DavidM1A2.AfraidOfTheDark.common.spell.SpellStage;
import com.DavidM1A2.AfraidOfTheDark.common.spell.powerSources.PowerSources;

import net.minecraft.client.Minecraft;

public class AOTDGuiSpellTablet extends AOTDGuiContainer
{
	private AOTDGuiTextField spellName;
	private AOTDGuiScrollBar scrollBar;
	private AOTDGuiScrollPanel scrollPanel;
	private Spell spell;
	private List<AOTDGuiSpellStage> spellStages = new ArrayList<AOTDGuiSpellStage>();
	private AOTDGuiSpellPowerSource powerSource;
	private AOTDGuiLabel spellCost;
	private SpellCraftingGUI parent;

	public AOTDGuiSpellTablet(int x, int y, int width, int height, Spell spell, SpellCraftingGUI parent)
	{
		super(x, y, width, height);

		this.spell = spell;

		this.parent = parent;

		AOTDGuiPanel tablet = new AOTDGuiPanel(0, 0, 192, 256, false);

		AOTDGuiImage background = new AOTDGuiImage(0, 0, 192, 256, "afraidofthedark:textures/gui/spell_crafting/tablet_background.png");
		tablet.add(background);
		this.spellName = new AOTDGuiTextField(60, 30, 85, 25, ClientData.getTargaMSHandFontSized(35f));
		this.spellName.addKeyListener(new AOTDKeyListener()
		{
			@Override
			public void keyTyped(AOTDKeyEvent event)
			{
				AOTDGuiSpellTablet.this.spell.setName(((AOTDGuiTextField) event.getSource()).getText());
			}
		});
		tablet.add(this.spellName);
		scrollBar = new AOTDGuiScrollBar(10, 75, 15, 170);
		tablet.add(scrollBar);

		AOTDGuiImage spellCraftingSlotBackground = new AOTDGuiImage(30, 55, 120, 170, "afraidofthedark:textures/gui/spell_crafting/spell_slot_background.png");
		tablet.add(spellCraftingSlotBackground);

		this.scrollPanel = new AOTDGuiScrollPanel(30, 55, 120, 170, true, scrollBar);

		tablet.add(scrollPanel);

		AOTDGuiButton saveButton = new AOTDGuiButton(152, 105, 20, 20, null, "afraidofthedark:textures/gui/spell_crafting/okay.png");
		saveButton.setHoverText("Save Spell");
		saveButton.addMouseListener(new AOTDMouseListener()
		{
			@Override
			public void mousePressed(AOTDMouseEvent event)
			{
				if (event.getSource().isHovered() && event.getClickedButton() == MouseButtonClicked.Left)
					AOTDGuiSpellTablet.this.parent.saveSpell();
			}

			@Override
			public void mouseEntered(AOTDMouseEvent event)
			{
				event.getSource().darkenColor(0.1f);
				Minecraft.getMinecraft().thePlayer.playSound(ModSounds.spellCraftingButtonHover, 0.6f, 1.7f);
			}

			@Override
			public void mouseExited(AOTDMouseEvent event)
			{
				event.getSource().brightenColor(0.1f);
			}
		});
		tablet.add(saveButton);
		AOTDGuiButton closeButton = new AOTDGuiButton(152, 130, 20, 20, null, "afraidofthedark:textures/gui/spell_crafting/delete.png");
		closeButton.setHoverText("Exit without saving");
		closeButton.addMouseListener(new AOTDMouseListener()
		{
			@Override
			public void mousePressed(AOTDMouseEvent event)
			{
				if (event.getSource().isHovered() && event.getClickedButton() == MouseButtonClicked.Left)
					entityPlayer.openGui(Reference.MOD_ID, GuiHandler.SPELL_SELECTION_ID, entityPlayer.worldObj, (int) entityPlayer.posX, (int) entityPlayer.posY, (int) entityPlayer.posZ);
			}

			@Override
			public void mouseEntered(AOTDMouseEvent event)
			{
				event.getSource().darkenColor(0.1f);
				Minecraft.getMinecraft().thePlayer.playSound(ModSounds.spellCraftingButtonHover, 0.6f, 1.7f);
			}

			@Override
			public void mouseExited(AOTDMouseEvent event)
			{
				event.getSource().brightenColor(0.1f);
			}
		});
		tablet.add(closeButton);
		this.powerSource = new AOTDGuiSpellPowerSource(152, 155, 20, 20, spell.getPowerSource() != null ? spell.getPowerSource().getType() : null);
		this.powerSource.addMouseListener(new AOTDMouseListener()
		{
			@Override
			public void mouseReleased(AOTDMouseEvent event)
			{
				if (event.getSource().isHovered() && event.getClickedButton() == MouseButtonClicked.Left)
				{
					ISpellComponentEnum currentlySelected = AOTDGuiSpellComponent.getSelectedComponent();
					if (currentlySelected instanceof PowerSources)
					{
						event.getSource().darkenColor(0.1f);
						AOTDGuiSpellTablet.this.spell.setPowerSource(((PowerSources) currentlySelected).newInstance());
						AOTDGuiSpellComponent.setSelectedComponent(null);
					}
					else if (currentlySelected == null)
						AOTDGuiSpellTablet.this.spell.setPowerSource(null);
					AOTDGuiSpellTablet.this.refresh();
				}
			}

			@Override
			public void mouseEntered(AOTDMouseEvent event)
			{
				event.getSource().darkenColor(0.1f);
				Minecraft.getMinecraft().thePlayer.playSound(ModSounds.spellCraftingButtonHover, 0.6f, 1.7f);
			}

			@Override
			public void mouseExited(AOTDMouseEvent event)
			{
				event.getSource().brightenColor(0.1f);
			}
		});
		tablet.add(powerSource);
		AOTDGuiImage helpButton = new AOTDGuiImage(152, 180, 20, 20, "afraidofthedark:textures/gui/spell_crafting/question.png");
		helpButton.setHoverText("Help");
		helpButton.addMouseListener(new AOTDMouseListener()
		{
			@Override
			public void mousePressed(AOTDMouseEvent event)
			{
				if (event.getSource().isHovered() && event.getSource().isVisible())
				{
					AOTDGuiSpellTablet.this.parent.showHelpScreen();
					event.consume();
				}
			}

			@Override
			public void mouseEntered(AOTDMouseEvent event)
			{
				event.getSource().darkenColor(0.1f);
				Minecraft.getMinecraft().thePlayer.playSound(ModSounds.spellCraftingButtonHover, 0.6f, 1.7f);
			}

			@Override
			public void mouseExited(AOTDMouseEvent event)
			{
				event.getSource().brightenColor(0.1f);
			}
		});
		tablet.add(helpButton);

		this.spellCost = new AOTDGuiLabel(25, 225, ClientData.getTargaMSHandFontSized(40));
		tablet.add(this.spellCost);

		this.refresh();

		this.add(tablet);
	}

	public void addNewSpellStage(SpellStage spellStage)
	{
		final AOTDGuiSpellStage nextSpellStage = new AOTDGuiSpellStage(5, (5 + this.spellStages.size() * 35), 110, 45, false, spellStage, this);
		nextSpellStage.addMouseListenerToNewRow(new AOTDMouseListener()
		{
			@Override
			public void mouseClicked(AOTDMouseEvent event)
			{
				if (event.getSource().isHovered() && event.getSource().isVisible() && event.getClickedButton() == MouseButtonClicked.Left)
				{
					AOTDGuiSpellTablet.this.spell.getSpellStages().add(new SpellStage());
					AOTDGuiSpellTablet.this.refresh();
				}
			}
		});
		nextSpellStage.addMouseListenerToRemoveRow(new AOTDMouseListener()
		{
			@Override
			public void mouseClicked(AOTDMouseEvent event)
			{
				if (event.getSource().isHovered() && event.getSource().isVisible() && event.getClickedButton() == MouseButtonClicked.Left)
				{
					AOTDGuiSpellTablet.this.spell.getSpellStages().remove(AOTDGuiSpellTablet.this.spell.getSpellStages().size() - 1);
					AOTDGuiSpellTablet.this.refresh();
				}
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
	}

	public void refresh()
	{
		if (this.spellCost != null)
			this.spellCost.setText("Spell Cost: " + Math.round(this.spell.getCost() * 100.0) / 100.0);
		if (this.powerSource != null)
			this.powerSource.setType(this.spell.getPowerSource() != null ? this.spell.getPowerSource().getType() : null);

		spellName.setGhostText("Spell Name");
		spellName.setText(spell.getName());
		while (!this.spellStages.isEmpty())
			this.removeSpellStage();
		for (SpellStage spellStage : spell.getSpellStages())
			this.addNewSpellStage(spellStage);
		for (AOTDGuiSpellStage spellStage : this.spellStages)
			spellStage.refresh();
		this.updateScrollOffset();
	}

	public void updateScrollOffset()
	{
		scrollPanel.setMaximumOffset(this.spellStages.size() > 4 ? (this.spellStages.size() - 4) * 35 : 0);
	}

	public boolean closeGuiOnInventory()
	{
		return !this.spellName.isFocused();
	}
}
