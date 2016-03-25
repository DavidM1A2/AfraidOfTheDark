/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.client.gui.customControls;

import com.DavidM1A2.AfraidOfTheDark.common.spell.powerSources.PowerSources;

public class AOTDGuiSpellPowerSource extends AOTDGuiSpellComponent
{
	private PowerSources powerSource;

	public AOTDGuiSpellPowerSource(int x, int y, int width, int height, PowerSources powerSource)
	{
		super(x, y, width, height);
		this.powerSource = powerSource;

		this.background.setImageTexture("afraidofthedark:textures/gui/spellCrafting/powerSourceHolder.png");

		if (powerSource != null)
		{
			this.icon.setImageTexture(powerSource.getIcon());
			this.icon.setVisible(true);
		}
	}

	@Override
	public PowerSources getType()
	{
		return this.powerSource;
	}

	@Override
	public String getTypeNameFormatted()
	{
		return this.powerSource != null ? this.powerSource.getName() : "Empty";
	}

	public void setType(PowerSources type)
	{
		this.powerSource = type;
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
		this.setHoverText("Power Source (" + this.getTypeNameFormatted() + ")");
	}
}
