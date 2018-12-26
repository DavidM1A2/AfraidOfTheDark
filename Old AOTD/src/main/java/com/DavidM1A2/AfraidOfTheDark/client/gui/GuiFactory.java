/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.client.gui;

import java.util.Set;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.client.IModGuiFactory;

/*
 * Allows for configuration of the mod in the options screen
 */
public class GuiFactory implements IModGuiFactory
{
	/*
	 * GUI factory class?
	 */
	@Override
	public void initialize(final Minecraft minecraftInstance)
	{
	}

	@Override
	public boolean hasConfigGui()
	{
		return false;
	}

	@Override
	public GuiScreen createConfigGui(GuiScreen parentScreen)
	{
		return new ModGuiConfig(parentScreen);
	}

	@Override
	public Set<RuntimeOptionCategoryElement> runtimeGuiCategories()
	{
		return null;
	}
}
