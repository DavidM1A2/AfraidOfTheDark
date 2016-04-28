/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.client.gui.customControls;

import java.util.ArrayList;
import java.util.List;

import com.DavidM1A2.AfraidOfTheDark.client.gui.baseControls.AOTDGuiButton;
import com.DavidM1A2.AfraidOfTheDark.client.gui.baseControls.AOTDGuiImage;
import com.DavidM1A2.AfraidOfTheDark.client.gui.baseControls.AOTDGuiPanel;
import com.DavidM1A2.AfraidOfTheDark.client.gui.eventListeners.AOTDMouseListener;
import com.DavidM1A2.AfraidOfTheDark.client.gui.events.AOTDMouseEvent;
import com.DavidM1A2.AfraidOfTheDark.common.spell.ISpellComponentEnum;
import com.DavidM1A2.AfraidOfTheDark.common.spell.SpellStage;
import com.DavidM1A2.AfraidOfTheDark.common.spell.deliveryMethods.DeliveryMethods;
import com.DavidM1A2.AfraidOfTheDark.common.spell.deliveryMethods.IDeliveryMethod;
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

	public AOTDGuiSpellStage(int x, int y, int width, int height, boolean scissorEnabled, SpellStage spellStage, AOTDGuiSpellTablet parent)
	{
		super(x, y, width, height, scissorEnabled);

		this.parent = parent;

		AOTDGuiImage background = new AOTDGuiImage(0, 0, width, height - 14, "afraidofthedark:textures/gui/spellCrafting/tabletSpellModule2.png");
		this.add(background);

		this.deliveryMethod = new AOTDGuiSpellDeliveryMethod(5, 5, height - 25, height - 25, spellStage.getDeliveryMethod() != null ? spellStage.getDeliveryMethod().getType() : null);
		this.deliveryMethod.updateHoverText();
		this.deliveryMethod.addMouseListener(new AOTDMouseListener()
		{
			@Override
			public void mouseReleased(AOTDMouseEvent event)
			{
				if (event.getSource().isHovered())
				{
					ISpellComponentEnum selectedComponent = AOTDGuiSpellStage.this.parent.getSelectedComponent();
					if (selectedComponent instanceof DeliveryMethods)
					{
						deliveryMethod.setType((DeliveryMethods) selectedComponent);
						deliveryMethod.updateHoverText();
						AOTDGuiSpellStage.this.parent.setSelectedComponent(null);
						event.getSource().darkenColor(0.1f);
					}
					else if (selectedComponent == null)
					{
						deliveryMethod.setType(null);
						deliveryMethod.updateHoverText();
					}
				}
			}

			@Override
			public void mousePressed(AOTDMouseEvent event)
			{
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

			@Override
			public void mouseClicked(AOTDMouseEvent event)
			{
			}
		});
		this.add(deliveryMethod);

		AOTDMouseListener onEffectClick = new AOTDMouseListener()
		{
			@Override
			public void mouseReleased(AOTDMouseEvent event)
			{
				if (event.getSource().isHovered())
				{
					ISpellComponentEnum selectedComponent = AOTDGuiSpellStage.this.parent.getSelectedComponent();
					AOTDGuiSpellEffect effect = ((AOTDGuiSpellEffect) event.getSource());
					if (selectedComponent instanceof Effects)
					{
						effect.setType((Effects) selectedComponent);
						effect.updateHoverText();
						AOTDGuiSpellStage.this.parent.setSelectedComponent(null);
						event.getSource().darkenColor(0.1f);
					}
					else if (selectedComponent == null)
					{
						effect.setType(null);
						effect.updateHoverText();
					}
				}
			}

			@Override
			public void mousePressed(AOTDMouseEvent event)
			{
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
		};
		AOTDMouseListener effectHover = new AOTDMouseListener()
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
				if (event.getSource().isVisible())
					Minecraft.getMinecraft().thePlayer.playSound("afraidofthedark:spellCraftingButtonHover", 0.6f, 1.7f);
			}

			@Override
			public void mouseExited(AOTDMouseEvent event)
			{
				event.getSource().brightenColor(0.1f);
			}
		};

		List<IEffect> effects = spellStage.getEffects();

		this.effect1 = new AOTDGuiSpellEffect(25, 5, height - 25, height - 25, effects.size() > 0 ? effects.get(0).getType() : null);
		this.effect1.addMouseListener(effectHover);
		this.effect1.addMouseListener(onEffectClick);
		this.effect1.updateHoverText();
		this.effect2 = new AOTDGuiSpellEffect(45, 5, height - 25, height - 25, effects.size() > 1 ? effects.get(1).getType() : null);
		this.effect2.addMouseListener(effectHover);
		this.effect2.addMouseListener(onEffectClick);
		this.effect2.updateHoverText();
		this.effect3 = new AOTDGuiSpellEffect(65, 5, height - 25, height - 25, effects.size() > 2 ? effects.get(2).getType() : null);
		this.effect3.addMouseListener(effectHover);
		this.effect3.addMouseListener(onEffectClick);
		this.effect3.updateHoverText();
		this.effect4 = new AOTDGuiSpellEffect(85, 5, height - 25, height - 25, effects.size() > 3 ? effects.get(3).getType() : null);
		this.effect4.addMouseListener(effectHover);
		this.effect4.addMouseListener(onEffectClick);
		this.effect4.updateHoverText();
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

	public SpellStage toSpellStage()
	{
		DeliveryMethods deliveryMethodEnum = this.deliveryMethod.getType();
		IDeliveryMethod deliveryToReturn = deliveryMethodEnum == null ? null : deliveryMethodEnum.newInstance();

		Effects effectEnum = this.effect1.getType();
		IEffect effect1ToReturn = effectEnum == null ? null : effectEnum.newInstance();
		effectEnum = this.effect2.getType();
		IEffect effect2ToReturn = effectEnum == null ? null : effectEnum.newInstance();
		effectEnum = this.effect3.getType();
		IEffect effect3ToReturn = effectEnum == null ? null : effectEnum.newInstance();
		effectEnum = this.effect4.getType();
		IEffect effect4ToReturn = effectEnum == null ? null : effectEnum.newInstance();
		List<IEffect> effectsToReturn = new ArrayList<IEffect>();
		if (effect1ToReturn != null)
			effectsToReturn.add(effect1ToReturn);
		if (effect2ToReturn != null)
			effectsToReturn.add(effect2ToReturn);
		if (effect3ToReturn != null)
			effectsToReturn.add(effect3ToReturn);
		if (effect4ToReturn != null)
			effectsToReturn.add(effect4ToReturn);

		SpellStage toReturn = new SpellStage(deliveryToReturn, effectsToReturn);
		return toReturn;
	}
}
