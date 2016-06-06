/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.client.gui.customControls;

import com.DavidM1A2.AfraidOfTheDark.common.spell.powerSources.PowerSources;

public class AOTDGuiSpellPowerSource extends AOTDGuiSpellComponent<PowerSources>
{
	public AOTDGuiSpellPowerSource(int x, int y, int width, int height, PowerSources powerSource)
	{
		this(x, y, width, height, powerSource, false);
	}

	public AOTDGuiSpellPowerSource(int x, int y, int width, int height, PowerSources powerSource, boolean showHighlightOnSelected)
	{
		super(x, y, width, height, powerSource, showHighlightOnSelected);
		this.background.setImageTexture("afraidofthedark:textures/gui/spellCrafting/powerSourceHolder.png");
	}

	@Override
	public void updateHoverText()
	{
		this.setHoverText("Power Source (" + this.getTypeNameFormatted() + ")");
	}
}
