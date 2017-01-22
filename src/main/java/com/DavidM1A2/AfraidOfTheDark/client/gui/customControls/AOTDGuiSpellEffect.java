/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.client.gui.customControls;

import com.DavidM1A2.AfraidOfTheDark.common.spell.effects.Effects;

public class AOTDGuiSpellEffect extends AOTDGuiSpellComponent<Effects>
{
	private Effects effect;
	private int effectIndex = 0;

	public AOTDGuiSpellEffect(int x, int y, int width, int height, Effects effect)
	{
		this(x, y, width, height, effect, false);
	}

	public AOTDGuiSpellEffect(int x, int y, int width, int height, Effects effect, boolean showHighlightOnSelected)
	{
		super(x, y, width, height, effect, showHighlightOnSelected);
		this.background.setImageTexture("afraidofthedark:textures/gui/spell_crafting/effect_holder.png");
		if (this.type != null)
			this.background.setColor(this.type.getAffinity().getRed(), this.type.getAffinity().getGreen(), this.type.getAffinity().getBlue(), 1.0f);
	}

	@Override
	public void updateHoverText()
	{
		this.setHoverText("Effect (" + this.getTypeNameFormatted() + ")\nAffinity: " + (type == null ? "None" : ("\n" + this.type.getAffinity().toString())));
	}

	@Override
	public void setType(Effects source)
	{
		super.setType(source);
		if (source != null)
			this.background.setColor(this.type.getAffinity().getRed(), this.type.getAffinity().getGreen(), this.type.getAffinity().getBlue(), 1.0f);
		else
			this.background.setColor(1.0f, 1.0f, 1.0f, 1.0f);
	}

	public void setEffectIndex(int index)
	{
		this.effectIndex = index;
	}

	public int getEffectIndex()
	{
		return this.effectIndex;
	}
}
