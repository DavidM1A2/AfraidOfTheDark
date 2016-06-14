/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.client.gui.customControls;

import com.DavidM1A2.AfraidOfTheDark.client.gui.baseControls.AOTDGuiButton;
import com.DavidM1A2.AfraidOfTheDark.client.gui.baseControls.AOTDGuiImage;
import com.DavidM1A2.AfraidOfTheDark.client.gui.baseControls.AOTDGuiPanel;
import com.DavidM1A2.AfraidOfTheDark.client.gui.eventListeners.AOTDMouseListener;
import com.DavidM1A2.AfraidOfTheDark.client.gui.events.AOTDMouseEvent;
import com.DavidM1A2.AfraidOfTheDark.client.gui.events.AOTDMouseEvent.MouseButtonClicked;
import com.DavidM1A2.AfraidOfTheDark.common.spell.ISpellComponentEnum;
import com.DavidM1A2.AfraidOfTheDark.common.spell.SpellStage;
import com.DavidM1A2.AfraidOfTheDark.common.spell.deliveryMethods.DeliveryMethods;
import com.DavidM1A2.AfraidOfTheDark.common.spell.effects.Effects;
import com.DavidM1A2.AfraidOfTheDark.common.spell.effects.IEffect;

import net.minecraft.client.Minecraft;

public class AOTDGuiSpellStage extends AOTDGuiPanel
{
	private AOTDGuiButton addNewRow;
	private AOTDGuiButton removeRow;
	private AOTDGuiSpellDeliveryMethod deliveryMethod;
	private AOTDGuiSpellEffect effect1;
	private AOTDGuiSpellEffect effect2;
	private AOTDGuiSpellEffect effect3;
	private AOTDGuiSpellEffect effect4;
	private AOTDGuiSpellTablet parent;
	private SpellStage spellStage;

	public AOTDGuiSpellStage(int x, int y, int width, int height, boolean scissorEnabled, SpellStage spellStage, AOTDGuiSpellTablet parent)
	{
		super(x, y, width, height, scissorEnabled);

		this.parent = parent;

		this.spellStage = spellStage;

		AOTDGuiImage background = new AOTDGuiImage(0, 0, width, height - 14, "afraidofthedark:textures/gui/spellCrafting/tabletSpellModule2.png");
		this.add(background);

		this.deliveryMethod = new AOTDGuiSpellDeliveryMethod(5, 5, height - 25, height - 25, null);
		this.deliveryMethod.addMouseListener(new AOTDMouseListener()
		{
			@Override
			public void mouseReleased(AOTDMouseEvent event)
			{
				if (event.getSource().isHovered() && event.getClickedButton() == MouseButtonClicked.Left)
				{
					ISpellComponentEnum selectedComponent = AOTDGuiSpellComponent.getSelectedComponent();
					if (selectedComponent instanceof DeliveryMethods)
					{
						AOTDGuiSpellStage.this.spellStage.setDeliveryMethod(((DeliveryMethods) selectedComponent).newInstance());
						event.getSource().darkenColor(0.1f);
						AOTDGuiSpellComponent.setSelectedComponent(null);
					}
					else if (selectedComponent == null)
						AOTDGuiSpellStage.this.spellStage.setDeliveryMethod(null);
					AOTDGuiSpellStage.this.parent.refresh();
				}
			}

			@Override
			public void mouseExited(AOTDMouseEvent event)
			{
				event.getSource().brightenColor(0.1f);
			}

			@Override
			public void mouseEntered(AOTDMouseEvent event)
			{
				event.getSource().darkenColor(0.1f);
				Minecraft.getMinecraft().thePlayer.playSound("afraidofthedark:spellCraftingButtonHover", 0.6f, 1.7f);
			}
		});
		this.add(deliveryMethod);

		AOTDMouseListener onEffectClick = new AOTDMouseListener()
		{
			@Override
			public void mouseReleased(AOTDMouseEvent event)
			{
				if (event.getSource().isHovered() && event.getClickedButton() == MouseButtonClicked.Left)
				{
					ISpellComponentEnum selectedComponent = AOTDGuiSpellComponent.getSelectedComponent();
					AOTDGuiSpellEffect effect = ((AOTDGuiSpellEffect) event.getSource());
					if (selectedComponent instanceof Effects)
					{
						AOTDGuiSpellStage.this.spellStage.getEffects()[effect.getEffectIndex()] = (selectedComponent == null ? null : ((Effects) selectedComponent).newInstance());
						event.getSource().darkenColor(0.1f);
						AOTDGuiSpellComponent.setSelectedComponent(null);
					}
					else if (selectedComponent == null)
						AOTDGuiSpellStage.this.spellStage.getEffects()[effect.getEffectIndex()] = null;
					AOTDGuiSpellStage.this.parent.refresh();
				}
			}
		};
		AOTDMouseListener effectHover = new AOTDMouseListener()
		{
			@Override
			public void mouseEntered(AOTDMouseEvent event)
			{
				event.getSource().darkenColor(0.1f);
				if (event.getSource().isVisible())
					Minecraft.getMinecraft().thePlayer.playSound("afraidofthedark:spellCraftingButtonHover", 0.6f, 1.7f);
			}

			@Override
			public void mouseExited(AOTDMouseEvent event)
			{
				event.getSource().brightenColor(0.1f);
			}
		};

		this.effect1 = new AOTDGuiSpellEffect(25, 5, height - 25, height - 25, null);
		this.effect1.addMouseListener(effectHover);
		this.effect1.addMouseListener(onEffectClick);
		this.effect1.setEffectIndex(0);
		this.effect2 = new AOTDGuiSpellEffect(45, 5, height - 25, height - 25, null);
		this.effect2.addMouseListener(effectHover);
		this.effect2.addMouseListener(onEffectClick);
		this.effect2.setEffectIndex(1);
		this.effect3 = new AOTDGuiSpellEffect(65, 5, height - 25, height - 25, null);
		this.effect3.addMouseListener(effectHover);
		this.effect3.addMouseListener(onEffectClick);
		this.effect3.setEffectIndex(2);
		this.effect4 = new AOTDGuiSpellEffect(85, 5, height - 25, height - 25, null);
		this.effect4.addMouseListener(effectHover);
		this.effect4.addMouseListener(onEffectClick);
		this.effect4.setEffectIndex(3);
		this.add(this.effect1);
		this.add(this.effect2);
		this.add(this.effect3);
		this.add(this.effect4);

		this.addNewRow = new AOTDGuiButton(0, height - 15, 15, 15, null, "afraidofthedark:textures/gui/spellCrafting/add.png");
		this.addNewRow.setHoverText("Add new spell stage");
		this.addNewRow.addMouseListener(effectHover);
		this.add(this.addNewRow);
		this.removeRow = new AOTDGuiButton(15, height - 15, 15, 15, null, "afraidofthedark:textures/gui/spellCrafting/delete.png");
		this.removeRow.setHoverText("Remove spell stage");
		this.removeRow.addMouseListener(effectHover);
		this.add(this.removeRow);
	}

	public void showPlus()
	{
		this.addNewRow.setVisible(true);
	}

	public void showMinus()
	{
		this.removeRow.setVisible(true);
	}

	public void hidePlus()
	{
		this.addNewRow.setVisible(false);
	}

	public void hideMinus()
	{
		this.removeRow.setVisible(false);
	}

	public void addMouseListenerToNewRow(AOTDMouseListener mouseListener)
	{
		this.addNewRow.addMouseListener(mouseListener);
	}

	public void addMouseListenerToRemoveRow(AOTDMouseListener mouseListener)
	{
		this.removeRow.addMouseListener(mouseListener);
	}

	public void refresh()
	{
		this.deliveryMethod.setType(this.spellStage.getDeliveryMethod() != null ? this.spellStage.getDeliveryMethod().getType() : null);
		IEffect[] effects = this.spellStage.getEffects();
		this.effect1.setType(effects[0] != null ? effects[0].getType() : null);
		this.effect2.setType(effects[1] != null ? effects[1].getType() : null);
		this.effect3.setType(effects[2] != null ? effects[2].getType() : null);
		this.effect4.setType(effects[3] != null ? effects[3].getType() : null);
	}
}
