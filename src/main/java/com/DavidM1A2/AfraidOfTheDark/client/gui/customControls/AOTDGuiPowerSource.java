/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.client.gui.customControls;

import com.DavidM1A2.AfraidOfTheDark.client.gui.baseControls.AOTDGuiImage;
import com.DavidM1A2.AfraidOfTheDark.client.gui.baseControls.AOTDGuiPanel;
import com.DavidM1A2.AfraidOfTheDark.common.spell.powerSources.PowerSources;

public class AOTDGuiPowerSource extends AOTDGuiPanel
{
	private AOTDGuiImage background;
	private AOTDGuiImage powerSource;

	public AOTDGuiPowerSource(int x, int y, int width, int height, PowerSources powerSource)
	{
		super(x, y, width, height, false);

		this.background = new AOTDGuiImage(0, 0, width, height, "afraidofthedark:textures/gui/spellCrafting/tabletIconHolder.png");
		this.add(background);

		if (powerSource == null)
		{
			this.powerSource = new AOTDGuiImage(0, 0, width, height, "afraidofthedark:textures/gui/spellCrafting/powerSources/none.png");
			this.powerSource.setVisible(false);
		}
		else
		{
			this.powerSource = new AOTDGuiImage(0, 0, width, height, powerSource.getIcon());
			this.powerSource.setVisible(true);
		}
		this.add(this.powerSource);
	}

	public void setPowerSource(PowerSources powerSource)
	{
		if (powerSource == null)
			this.powerSource.setVisible(false);
		else
		{
			this.powerSource.setImageTexture(powerSource.getIcon());
			this.powerSource.setVisible(true);
		}
	}

	@Override
	public void brightenColor(float amount)
	{
		super.brightenColor(amount);
		this.background.brightenColor(amount);
		this.powerSource.brightenColor(amount);
	}

	@Override
	public void darkenColor(float amount)
	{
		super.darkenColor(amount);
		this.background.darkenColor(amount);
		this.powerSource.darkenColor(amount);
	}
}
