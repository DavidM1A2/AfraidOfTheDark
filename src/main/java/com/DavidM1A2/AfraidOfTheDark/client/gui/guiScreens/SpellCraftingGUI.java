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

import com.DavidM1A2.AfraidOfTheDark.client.gui.GuiHandler;
import com.DavidM1A2.AfraidOfTheDark.client.gui.baseControls.AOTDGuiButton;
import com.DavidM1A2.AfraidOfTheDark.client.gui.baseControls.AOTDGuiImage;
import com.DavidM1A2.AfraidOfTheDark.client.gui.baseControls.AOTDGuiLabel;
import com.DavidM1A2.AfraidOfTheDark.client.gui.baseControls.AOTDGuiPanel;
import com.DavidM1A2.AfraidOfTheDark.client.gui.baseControls.AOTDGuiScrollBar;
import com.DavidM1A2.AfraidOfTheDark.client.gui.baseControls.AOTDGuiScrollPanel;
import com.DavidM1A2.AfraidOfTheDark.client.gui.baseControls.AOTDGuiTextField;
import com.DavidM1A2.AfraidOfTheDark.client.gui.customControls.AOTDGuiSpellComponent;
import com.DavidM1A2.AfraidOfTheDark.client.gui.customControls.AOTDGuiSpellDeliveryMethod;
import com.DavidM1A2.AfraidOfTheDark.client.gui.customControls.AOTDGuiSpellEffect;
import com.DavidM1A2.AfraidOfTheDark.client.gui.customControls.AOTDGuiSpellPowerSource;
import com.DavidM1A2.AfraidOfTheDark.client.gui.customControls.AOTDGuiSpellStage;
import com.DavidM1A2.AfraidOfTheDark.client.gui.eventListeners.AOTDMouseListener;
import com.DavidM1A2.AfraidOfTheDark.client.gui.events.AOTDMouseEvent;
import com.DavidM1A2.AfraidOfTheDark.client.settings.ClientData;
import com.DavidM1A2.AfraidOfTheDark.common.initializeMod.ModCapabilities;
import com.DavidM1A2.AfraidOfTheDark.common.refrence.Refrence;
import com.DavidM1A2.AfraidOfTheDark.common.spell.ISpellComponentEnum;
import com.DavidM1A2.AfraidOfTheDark.common.spell.Spell;
import com.DavidM1A2.AfraidOfTheDark.common.spell.SpellStage;
import com.DavidM1A2.AfraidOfTheDark.common.spell.deliveryMethods.DeliveryMethods;
import com.DavidM1A2.AfraidOfTheDark.common.spell.effects.Effects;
import com.DavidM1A2.AfraidOfTheDark.common.spell.effects.IEffect;
import com.DavidM1A2.AfraidOfTheDark.common.spell.powerSources.PowerSources;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;

public class SpellCraftingGUI extends AOTDGuiScreen
{
	private AOTDGuiTextField spellName;
	private AOTDGuiScrollBar scrollBar;
	private AOTDGuiScrollPanel scrollPanel;
	private List<AOTDGuiSpellStage> spellStages = new ArrayList<AOTDGuiSpellStage>();
	private AOTDGuiSpellPowerSource powerSource;
	private AOTDGuiLabel spellCost;
	private Spell spell;
	private ISpellComponentEnum selectedComponent;

	public SpellCraftingGUI()
	{
		this.spell = ClientData.spellToBeEdited;
		AOTDGuiPanel tablet = new AOTDGuiPanel(100, (360 - 256) / 2, 192, 256, false);

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
					SpellCraftingGUI.this.saveSpell();
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
					entityPlayer.openGui(Refrence.MOD_ID, GuiHandler.SPELL_SELECTION_ID, entityPlayer.worldObj, (int) entityPlayer.posX, (int) entityPlayer.posY, (int) entityPlayer.posZ);
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
						SpellCraftingGUI.this.selectedComponent = null;
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
		spellCost.setText("Spell Cost: 0");
		tablet.add(this.spellCost);

		this.getContentPane().add(tablet);

		/*
		 * 
		 */

		AOTDGuiPanel effectScroll = new AOTDGuiPanel(340, (360 - 256) / 2, 220, 256, false);
		AOTDGuiImage backgroundScroll = new AOTDGuiImage(0, 0, 200, 256, "afraidofthedark:textures/gui/spellCrafting/effectListScroll.png");
		effectScroll.add(backgroundScroll);

		AOTDGuiScrollBar effectsScrollBar = new AOTDGuiScrollBar(200, 50, 13, 160);
		effectScroll.add(effectsScrollBar);
		AOTDGuiScrollPanel effectsPanel = new AOTDGuiScrollPanel(40, 50, 120, 175, true, effectsScrollBar);
		AOTDMouseListener onClick = new AOTDMouseListener()
		{
			@Override
			public void mouseReleased(AOTDMouseEvent event)
			{
			}

			@Override
			public void mousePressed(AOTDMouseEvent event)
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

			@Override
			public void mouseClicked(AOTDMouseEvent event)
			{
				if (event.getSource().isHovered() && event.getSource() instanceof AOTDGuiSpellComponent)
				{
					AOTDGuiSpellComponent component = (AOTDGuiSpellComponent) event.getSource();
					selectedComponent = component.getType();
				}
			}
		};
		int numEntries = 0;
		for (PowerSources powerSource : PowerSources.values())
		{
			final AOTDGuiSpellPowerSource source = new AOTDGuiSpellPowerSource(5 + 24 * (numEntries % 5), 5 + 24 * (numEntries / 5), 20, 20, powerSource);
			effectsPanel.add(source);
			source.addMouseListener(onClick);
			source.setHoverText("Power Source (" + powerSource.getName() + ")");
			numEntries = numEntries + 1;
		}
		while (numEntries % 5 != 0)
			numEntries++;
		for (Effects effect : Effects.values())
		{
			AOTDGuiSpellEffect source = new AOTDGuiSpellEffect(5 + 24 * (numEntries % 5), 5 + 24 * (numEntries / 5), 20, 20, effect);
			effectsPanel.add(source);
			source.addMouseListener(onClick);
			source.setHoverText("Effect (" + effect.getName() + ")");
			numEntries = numEntries + 1;
		}
		while (numEntries % 5 != 0)
			numEntries++;
		for (DeliveryMethods deliveryMethod : DeliveryMethods.values())
		{
			AOTDGuiSpellDeliveryMethod source = new AOTDGuiSpellDeliveryMethod(5 + 24 * (numEntries % 5), 5 + 24 * (numEntries / 5), 20, 20, deliveryMethod);
			effectsPanel.add(source);
			source.addMouseListener(onClick);
			source.setHoverText("Delivery Method (" + deliveryMethod.getName() + ")");
			numEntries = numEntries + 1;
		}
		int offset = numEntries % 5 - 5;
		offset = offset > 0 ? offset : 0;
		effectsPanel.setMaximumOffset(offset);
		effectScroll.add(effectsPanel);

		this.getContentPane().add(effectScroll);
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
					SpellCraftingGUI.this.addNewSpellStage(new SpellStage(null, new ArrayList<IEffect>()));
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
					SpellCraftingGUI.this.removeSpellStage();
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
		SpellStage[] spellStages = new SpellStage[SpellCraftingGUI.this.spellStages.size()];
		for (int i = 0; i < SpellCraftingGUI.this.spellStages.size(); i++)
		{
			spellStages[i] = SpellCraftingGUI.this.spellStages.get(i).toSpellStage();
		}
		spell.setSpellStages(spellStages);

		// Set the power source
		PowerSources powerSourceEnum = SpellCraftingGUI.this.powerSource.getType();
		spell.setPowerSource(powerSourceEnum == null ? null : powerSourceEnum.newInstance());

		// Sync and state that the spell was saved
		entityPlayer.getCapability(ModCapabilities.PLAYER_DATA, null).syncSpellManager();
		entityPlayer.addChatMessage(new ChatComponentText("Spell " + spell.getName() + " successfully saved."));
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

	public ISpellComponentEnum getSelectedComponent()
	{
		return this.selectedComponent;
	}

	public void setSelectedComponent(ISpellComponentEnum selectedComponent)
	{
		this.selectedComponent = selectedComponent;
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
