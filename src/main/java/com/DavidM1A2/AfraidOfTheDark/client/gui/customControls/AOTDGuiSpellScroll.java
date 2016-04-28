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
import com.DavidM1A2.AfraidOfTheDark.client.gui.events.AOTDMouseEvent;
import com.DavidM1A2.AfraidOfTheDark.client.gui.guiScreens.SpellCraftingGUI;
import com.DavidM1A2.AfraidOfTheDark.common.spell.Spell;
import com.DavidM1A2.AfraidOfTheDark.common.spell.deliveryMethods.DeliveryMethods;
import com.DavidM1A2.AfraidOfTheDark.common.spell.effects.Effects;
import com.DavidM1A2.AfraidOfTheDark.common.spell.powerSources.PowerSources;

import net.minecraft.client.Minecraft;

public class AOTDGuiSpellScroll extends AOTDGuiContainer
{
	private Spell spell;
	private SpellCraftingGUI parent;

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
				if (event.getSource().isHovered() && event.getSource() instanceof AOTDGuiSpellComponent)
				{
					AOTDGuiSpellComponent component = (AOTDGuiSpellComponent) event.getSource();
					AOTDGuiSpellScroll.this.parent.getTablet().setSelectedComponent(component.getType());
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

		this.add(effectScroll);
	}
}
