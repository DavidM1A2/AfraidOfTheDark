/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.client.gui.customControls;

import net.minecraft.client.gui.GuiScreen;

public abstract class AOTDGuiScreen extends GuiScreen
{
	private AOTDButtonController buttonController = new AOTDButtonController(this);

	@Override
	public void initGui()
	{
		super.initGui();
		this.buttonList.clear();
		this.buttonList.add(buttonController);
	}

	public AOTDButtonController getButtonController()
	{
		return this.buttonController;
	}

	public void actionPerformed(AOTDGuiButton button)
	{
	}
}
