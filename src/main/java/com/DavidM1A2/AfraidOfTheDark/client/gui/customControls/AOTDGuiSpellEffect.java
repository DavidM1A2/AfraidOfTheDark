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

		this.setType(effect);
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
			this.background.setColor(effect.getAffinity().getRed(), effect.getAffinity().getGreen(), effect.getAffinity().getBlue(), 1.0f);
		}
		else
		{
			this.icon.setVisible(false);
			this.background.setColor(1.0f, 1.0f, 1.0f, 1.0f);
		}
	}

	@Override
	public void updateHoverText()
	{
		this.setHoverText("Effect (" + this.getTypeNameFormatted() + ")");
	}
}
