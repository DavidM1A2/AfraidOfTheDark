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
import com.DavidM1A2.AfraidOfTheDark.client.gui.eventListeners.AOTDMouseListener;
import com.DavidM1A2.AfraidOfTheDark.client.gui.events.AOTDMouseEvent;
import com.DavidM1A2.AfraidOfTheDark.client.settings.ClientData;
import com.DavidM1A2.AfraidOfTheDark.common.initializeMod.ModCapabilities;
import com.DavidM1A2.AfraidOfTheDark.common.reference.Reference;
import com.DavidM1A2.AfraidOfTheDark.common.spell.ISpellComponentEnum;
import com.DavidM1A2.AfraidOfTheDark.common.spell.Spell;
import com.DavidM1A2.AfraidOfTheDark.common.spell.SpellStage;
import com.DavidM1A2.AfraidOfTheDark.common.spell.effects.IEffect;
import com.DavidM1A2.AfraidOfTheDark.common.spell.powerSources.PowerSources;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;

public class AOTDGuiSpellTablet extends AOTDGuiContainer
{
	private AOTDGuiTextField spellName;
	private AOTDGuiScrollBar scrollBar;
	private AOTDGuiScrollPanel scrollPanel;
	private Spell spell;
	private List<AOTDGuiSpellStage> spellStages = new ArrayList<AOTDGuiSpellStage>();
	private AOTDGuiSpellPowerSource powerSource;
	private AOTDGuiLabel spellCost;
	private ISpellComponentEnum selectedComponent;

	public AOTDGuiSpellTablet(int x, int y, int width, int height, Spell spell)
	{
		super(x, y, width, height);

		this.spell = spell;

		AOTDGuiPanel tablet = new AOTDGuiPanel(0, 0, 192, 256, false);

		AOTDGuiImage background = new AOTDGuiImage(0, 0, 192, 256, "afraidofthedark:textures/gui/spellCrafting/tabletBackground.png");
		tablet.add(background);
		spellName = new AOTDGuiTextField(60, 30, 85, 25, ClientData.getTargaMSHandFontSized(35f));
		spellName.setGhostText("Spell Name");
		spellName.setText(spell.getName());
		tablet.add(spellName);
		scrollBar = new AOTDGuiScrollBar(10, 75, 15, 170);
		tablet.add(scrollBar);

		AOTDGuiImage spellCraftingSlotBackground = new AOTDGuiImage(30, 55, 120, 170, "afraidofthedark:textures/gui/spellCrafting/spellSlotBackground.png");
		tablet.add(spellCraftingSlotBackground);

		this.scrollPanel = new AOTDGuiScrollPanel(30, 55, 120, 170, true, scrollBar);

		for (SpellStage spellStage : spell.getSpellStages())
			this.addNewSpellStage(spellStage);

		tablet.add(scrollPanel);

		AOTDGuiButton saveButton = new AOTDGuiButton(152, 105, 20, 20, null, "afraidofthedark:textures/gui/spellCrafting/okay.png");
		saveButton.setHoverText("Save Spell");
		saveButton.addMouseListener(new AOTDMouseListener()
		{
			@Override
			public void mouseClicked(AOTDMouseEvent event)
			{
			}

			@Override
			public void mousePressed(AOTDMouseEvent event)
			{
				if (event.getSource().isHovered())
					AOTDGuiSpellTablet.this.saveSpell();
			}

			@Override
			public void mouseReleased(AOTDMouseEvent event)
			{
			}

			@Override
			public void mouseEntered(AOTDMouseEvent event)
			{
				event.getSource().darkenColor(0.1f);
				Minecraft.getMinecraft().thePlayer.playSound("afraidofthedark:spellCraftingButtonHover", 0.6f, 1.7f);
			}

			@Override
			public void mouseExited(AOTDMouseEvent event)
			{
				event.getSource().brightenColor(0.1f);
			}
		});
		tablet.add(saveButton);
		AOTDGuiButton closeButton = new AOTDGuiButton(152, 130, 20, 20, null, "afraidofthedark:textures/gui/spellCrafting/delete.png");
		closeButton.setHoverText("Exit without saving");
		closeButton.addMouseListener(new AOTDMouseListener()
		{
			@Override
			public void mouseClicked(AOTDMouseEvent event)
			{
			}

			@Override
			public void mousePressed(AOTDMouseEvent event)
			{
				if (event.getSource().isHovered())
					entityPlayer.openGui(Reference.MOD_ID, GuiHandler.SPELL_SELECTION_ID, entityPlayer.worldObj, (int) entityPlayer.posX, (int) entityPlayer.posY, (int) entityPlayer.posZ);
			}

			@Override
			public void mouseReleased(AOTDMouseEvent event)
			{
			}

			@Override
			public void mouseEntered(AOTDMouseEvent event)
			{
				event.getSource().darkenColor(0.1f);
				Minecraft.getMinecraft().thePlayer.playSound("afraidofthedark:spellCraftingButtonHover", 0.6f, 1.7f);
			}

			@Override
			public void mouseExited(AOTDMouseEvent event)
			{
				event.getSource().brightenColor(0.1f);
			}
		});
		tablet.add(closeButton);
		this.powerSource = new AOTDGuiSpellPowerSource(152, 155, 20, 20, spell.getPowerSource() != null ? spell.getPowerSource().getType() : null);
		this.powerSource.updateHoverText();
		this.powerSource.addMouseListener(new AOTDMouseListener()
		{
			@Override
			public void mouseClicked(AOTDMouseEvent event)
			{
			}

			@Override
			public void mousePressed(AOTDMouseEvent event)
			{
			}

			@Override
			public void mouseReleased(AOTDMouseEvent event)
			{
				if (event.getSource().isHovered())
				{
					if (getSelectedComponent() instanceof PowerSources)
					{
						powerSource.setType((PowerSources) getSelectedComponent());
						powerSource.updateHoverText();
						AOTDGuiSpellTablet.this.selectedComponent = null;
						event.getSource().darkenColor(0.1f);
					}
					else if (getSelectedComponent() == null)
					{
						powerSource.setType(null);
						powerSource.updateHoverText();
					}
				}
			}

			@Override
			public void mouseEntered(AOTDMouseEvent event)
			{
				event.getSource().darkenColor(0.1f);
				Minecraft.getMinecraft().thePlayer.playSound("afraidofthedark:spellCraftingButtonHover", 0.6f, 1.7f);
			}

			@Override
			public void mouseExited(AOTDMouseEvent event)
			{
				event.getSource().brightenColor(0.1f);
			}
		});
		tablet.add(powerSource);
		AOTDGuiImage helpButton = new AOTDGuiImage(152, 180, 20, 20, "afraidofthedark:textures/gui/spellCrafting/question.png");
		helpButton.setHoverText("Help");
		helpButton.addMouseListener(new AOTDMouseListener()
		{
			@Override
			public void mouseClicked(AOTDMouseEvent event)
			{
			}

			@Override
			public void mousePressed(AOTDMouseEvent event)
			{
			}

			@Override
			public void mouseReleased(AOTDMouseEvent event)
			{
			}

			@Override
			public void mouseEntered(AOTDMouseEvent event)
			{
				event.getSource().darkenColor(0.1f);
				Minecraft.getMinecraft().thePlayer.playSound("afraidofthedark:spellCraftingButtonHover", 0.6f, 1.7f);
			}

			@Override
			public void mouseExited(AOTDMouseEvent event)
			{
				event.getSource().brightenColor(0.1f);
			}
		});
		tablet.add(helpButton);

		this.spellCost = new AOTDGuiLabel(25, 225, ClientData.getTargaMSHandFontSized(40));
		spellCost.setText("Spell Cost: " + this.calculateCost());
		tablet.add(this.spellCost);

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
				if (event.getSource().isHovered() && event.getSource().isVisible())
					AOTDGuiSpellTablet.this.addNewSpellStage(new SpellStage(null, new ArrayList<IEffect>()));
			}

			@Override
			public void mousePressed(AOTDMouseEvent event)
			{
			}

			@Override
			public void mouseReleased(AOTDMouseEvent event)
			{
			}

			@Override
			public void mouseEntered(AOTDMouseEvent event)
			{
			}

			@Override
			public void mouseExited(AOTDMouseEvent event)
			{
			}
		});
		nextSpellStage.addMouseListenerToRemoveRow(new AOTDMouseListener()
		{
			@Override
			public void mouseClicked(AOTDMouseEvent event)
			{
				if (event.getSource().isHovered() && event.getSource().isVisible())
					AOTDGuiSpellTablet.this.removeSpellStage();
			}

			@Override
			public void mousePressed(AOTDMouseEvent event)
			{
			}

			@Override
			public void mouseReleased(AOTDMouseEvent event)
			{
			}

			@Override
			public void mouseEntered(AOTDMouseEvent event)
			{
			}

			@Override
			public void mouseExited(AOTDMouseEvent event)
			{
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

	private void saveSpell()
	{
		// Set the spell name
		spell.setName(spellName.getText());

		// Set the spell stages
		SpellStage[] spellStages = new SpellStage[this.spellStages.size()];
		for (int i = 0; i < this.spellStages.size(); i++)
		{
			spellStages[i] = this.spellStages.get(i).toSpellStage();
		}
		spell.setSpellStages(spellStages);

		// Set the power source
		PowerSources powerSourceEnum = this.powerSource.getType();
		spell.setPowerSource(powerSourceEnum == null ? null : powerSourceEnum.newInstance());

		// Sync and state that the spell was saved
		entityPlayer.getCapability(ModCapabilities.PLAYER_DATA, null).syncSpellManager();
		entityPlayer.addChatMessage(new ChatComponentText("Spell " + spell.getName() + " successfully saved."));
	}

	public double calculateCost()
	{
		double cost = 0;
		for (AOTDGuiSpellStage spellStageGui : this.spellStages)
		{
			SpellStage spellStage = spellStageGui.toSpellStage();
			cost = cost + spellStage.getCost();
		}
		return cost;
	}

	public ISpellComponentEnum getSelectedComponent()
	{
		return this.selectedComponent;
	}

	public void setSelectedComponent(ISpellComponentEnum selectedComponent)
	{
		this.selectedComponent = selectedComponent;
	}

	public boolean closeGuiOnInventory()
	{
		return !this.spellName.isFocused();
	}
}
