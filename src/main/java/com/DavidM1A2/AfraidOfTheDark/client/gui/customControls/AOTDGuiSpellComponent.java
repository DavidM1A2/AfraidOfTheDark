/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.client.gui.customControls;

import com.DavidM1A2.AfraidOfTheDark.client.gui.baseControls.AOTDGuiImage;
import com.DavidM1A2.AfraidOfTheDark.client.gui.baseControls.AOTDGuiPanel;
import com.DavidM1A2.AfraidOfTheDark.common.spell.ISpellComponentEnum;

public abstract class AOTDGuiSpellComponent extends AOTDGuiPanel
{
	protected AOTDGuiImage background;
	protected AOTDGuiImage icon;

	public AOTDGuiSpellComponent(int x, int y, int width, int height)
	{
		super(x, y, width, height, false);
		this.background = new AOTDGuiImage(0, 0, width, height, "afraidofthedark:textures/gui/spellCrafting/tabletIconHolder.png");

		this.add(background);

		this.icon = new AOTDGuiImage(0, 0, width, height, "afraidofthedark:textures/gui/spellCrafting/powerSources/none.png");
		this.icon.setVisible(false);

		this.add(this.icon);
	}

	public void setSpellComponent(ISpellComponentEnum source)
	{
		if (source == null)
			this.icon.setVisible(false);
		else
		{
			this.icon.setImageTexture(source.getIcon());
			this.icon.setVisible(true);
		}
	}

	@Override
	public void brightenColor(float amount)
	{
		super.brightenColor(amount);
		this.background.brightenColor(amount);
		this.icon.brightenColor(amount);
	}

	@Override
	public void darkenColor(float amount)
	{
		super.darkenColor(amount);
		this.background.darkenColor(amount);
		this.icon.darkenColor(amount);
	}

	public abstract ISpellComponentEnum getType();

	public abstract String getTypeNameFormatted();

	public abstract void updateHoverText();
}
