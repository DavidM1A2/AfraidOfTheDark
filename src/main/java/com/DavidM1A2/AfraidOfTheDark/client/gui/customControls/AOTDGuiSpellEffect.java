/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.client.gui.customControls;

import com.DavidM1A2.AfraidOfTheDark.common.spell.effects.Effects;

public class AOTDGuiSpellEffect extends AOTDGuiSpellComponent
{
	private Effects effect;

	public AOTDGuiSpellEffect(int x, int y, int width, int height, Effects effect)
	{
		super(x, y, width, height);
		this.effect = effect;

		this.background.setImageTexture("afraidofthedark:textures/gui/spellCrafting/effectHolder.png");

		if (effect != null)
		{
			this.icon.setImageTexture(effect.getIcon());
			this.icon.setVisible(true);
		}
	}

	@Override
	public Effects getType()
	{
		return this.effect;
	}

	@Override
	public String getTypeNameFormatted()
	{
		return this.effect != null ? this.effect.getName() : "Empty";
	}

	public void setType(Effects type)
	{
		this.effect = type;
		if (type != null)
		{
			this.icon.setImageTexture(type.getIcon());
			this.icon.setVisible(true);
		}
		else
		{
			this.icon.setVisible(false);
		}
	}

	@Override
	public void updateHoverText()
	{
		this.setHoverText("Effect (" + this.getTypeNameFormatted() + ")");
	}
}
