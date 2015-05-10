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
 * No Clue who this class does...
 */
public class GuiFactory implements IModGuiFactory
{
	/*
	 * GUI factory class?
	 */
	@Override
	public void initialize(Minecraft minecraftInstance)
	{

	}

	@Override
	public Class<? extends GuiScreen> mainConfigGuiClass()
	{
		return ModGuiConfig.class;
	}

	@Override
	public Set<RuntimeOptionCategoryElement> runtimeGuiCategories()
	{
		return null;
	}

	@Override
	public RuntimeOptionGuiHandler getHandlerFor(RuntimeOptionCategoryElement element)
	{
		return null;
	}

}
