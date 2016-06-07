/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.client.gui.customControls;

import com.DavidM1A2.AfraidOfTheDark.client.gui.baseControls.AOTDGuiContainer;
import com.DavidM1A2.AfraidOfTheDark.client.gui.baseControls.AOTDGuiImage;
import com.DavidM1A2.AfraidOfTheDark.client.gui.baseControls.AOTDGuiPanel;
import com.DavidM1A2.AfraidOfTheDark.client.gui.baseControls.AOTDGuiScrollBar;
import com.DavidM1A2.AfraidOfTheDark.client.gui.baseControls.AOTDGuiScrollPanel;
import com.DavidM1A2.AfraidOfTheDark.client.gui.eventListeners.AOTDMouseListener;
import com.DavidM1A2.AfraidOfTheDark.client.gui.eventListeners.AOTDMouseMoveListener;
import com.DavidM1A2.AfraidOfTheDark.client.gui.events.AOTDMouseEvent;
import com.DavidM1A2.AfraidOfTheDark.client.gui.events.AOTDMouseEvent.MouseButtonClicked;
import com.DavidM1A2.AfraidOfTheDark.client.gui.guiScreens.SpellCraftingGUI;
import com.DavidM1A2.AfraidOfTheDark.common.spell.ISpellComponentEnum;
import com.DavidM1A2.AfraidOfTheDark.common.spell.Spell;
import com.DavidM1A2.AfraidOfTheDark.common.spell.deliveryMethods.DeliveryMethods;
import com.DavidM1A2.AfraidOfTheDark.common.spell.effects.Effects;
import com.DavidM1A2.AfraidOfTheDark.common.spell.powerSources.PowerSources;
import com.DavidM1A2.AfraidOfTheDark.common.utility.LogHelper;

import net.minecraft.client.Minecraft;

public class AOTDGuiSpellScroll extends AOTDGuiContainer
{
	private Spell spell;
	private SpellCraftingGUI parent;
	private AOTDGuiImage hoveredIcon;

	public AOTDGuiSpellScroll(int x, int y, int width, int height, Spell spell, SpellCraftingGUI parent)
	{
		super(x, y, width, height);
		this.spell = spell;
		this.parent = parent;

		AOTDGuiPanel effectScroll = new AOTDGuiPanel(0, 0, 220, 256, false);
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
				if (event.getSource().isHovered() && event.getSource() instanceof AOTDGuiSpellComponent && event.getClickedButton() == MouseButtonClicked.Left)
				{
					AOTDGuiSpellComponent component = (AOTDGuiSpellComponent) event.getSource();
					AOTDGuiSpellComponent.setSelectedComponent(component.getType());
				}
			}
		};

		int numEntries = 0;

		// Add the power source heading, then move down a row (add 5 icons)
		AOTDGuiImage powerSourceHeading = new AOTDGuiImage(5 + 24 * (numEntries % 5), 5 + 24 * (numEntries / 5), 80, 20, "afraidofthedark:textures/gui/spellcrafting/powerSources.png");
		effectsPanel.add(powerSourceHeading);
		numEntries = numEntries + 5;

		// Add each power source icon
		for (PowerSources powerSource : PowerSources.values())
		{
			final AOTDGuiSpellPowerSource source = new AOTDGuiSpellPowerSource(5 + 24 * (numEntries % 5), 5 + 24 * (numEntries / 5), 20, 20, powerSource, true);
			effectsPanel.add(source);
			source.addMouseListener(onClick);
			source.updateHoverText();
			numEntries = numEntries + 1;
		}
		// Ensure that we're on the next row now
		while (numEntries % 5 != 0)
			numEntries++;

		// Add the effect heading, then move down a row (add 5 icons)
		AOTDGuiImage effectHeading = new AOTDGuiImage(5 + 24 * (numEntries % 5), 5 + 24 * (numEntries / 5), 80, 20, "afraidofthedark:textures/gui/spellcrafting/effects.png");
		effectsPanel.add(effectHeading);
		numEntries = numEntries + 5;

		// Add each effect icon
		for (Effects effect : Effects.values())
		{
			AOTDGuiSpellEffect source = new AOTDGuiSpellEffect(5 + 24 * (numEntries % 5), 5 + 24 * (numEntries / 5), 20, 20, effect, true);
			effectsPanel.add(source);
			source.addMouseListener(onClick);
			source.updateHoverText();
			numEntries = numEntries + 1;
		}
		// Ensure that we're on the next row now
		while (numEntries % 5 != 0)
			numEntries++;

		// Add the delivery method heading, then move down a row (add 5 icons)
		AOTDGuiImage deliveryMethodHeading = new AOTDGuiImage(5 + 24 * (numEntries % 5), 5 + 24 * (numEntries / 5), 80, 20, "afraidofthedark:textures/gui/spellcrafting/deliveryMethods.png");
		effectsPanel.add(deliveryMethodHeading);
		numEntries = numEntries + 5;

		// Add each delivery method icon
		for (DeliveryMethods deliveryMethod : DeliveryMethods.values())
		{
			AOTDGuiSpellDeliveryMethod source = new AOTDGuiSpellDeliveryMethod(5 + 24 * (numEntries % 5), 5 + 24 * (numEntries / 5), 20, 20, deliveryMethod, true);
			effectsPanel.add(source);
			source.addMouseListener(onClick);
			source.updateHoverText();
			numEntries = numEntries + 1;
		}

		// Update the offset accordingly
		int offset = numEntries / 6 - 5;
		LogHelper.info(offset);
		offset = offset > 0 ? offset : 0;
		effectsPanel.setMaximumOffset(offset * 30);
		effectScroll.add(effectsPanel);

		this.add(effectScroll);

		// Add the hover icon

		this.hoveredIcon = new AOTDGuiImage(0, 0, 20, 20, "afraidofthedark:textures/gui/spellCrafting/blank.png");
		this.hoveredIcon.addMouseMoveListener(new AOTDMouseMoveListener()
		{
			@Override
			public void mouseMoved(AOTDMouseEvent event)
			{
				event.getSource().setX((int) (event.getMouseX() / AOTDGuiSpellScroll.this.getScaleX()) - event.getSource().getWidthScaled() / 2);
				event.getSource().setY((int) (event.getMouseY() / AOTDGuiSpellScroll.this.getScaleY()) - event.getSource().getHeightScaled() / 2);
			}

			@Override
			public void mouseDragged(AOTDMouseEvent event)
			{
			}
		});
		this.hoveredIcon.addMouseListener(new AOTDMouseListener()
		{
			@Override
			public void mouseReleased(AOTDMouseEvent event)
			{
			}

			@Override
			public void mousePressed(AOTDMouseEvent event)
			{
				if (event.getClickedButton() == MouseButtonClicked.Right)
					AOTDGuiSpellComponent.setSelectedComponent(null);

				ISpellComponentEnum currentlySelected = AOTDGuiSpellComponent.getSelectedComponent();
				((AOTDGuiImage) event.getSource()).setImageTexture(currentlySelected == null ? "afraidofthedark:textures/gui/spellCrafting/blank.png" : currentlySelected.getIcon());
			}

			@Override
			public void mouseExited(AOTDMouseEvent event)
			{
			}

			@Override
			public void mouseEntered(AOTDMouseEvent event)
			{
			}

			@Override
			public void mouseClicked(AOTDMouseEvent event)
			{
			}
		});
		this.add(this.hoveredIcon);
	}
}
