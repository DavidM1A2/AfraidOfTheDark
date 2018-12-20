package com.DavidM1A2.afraidofthedark.client.gui;

import com.DavidM1A2.afraidofthedark.common.constants.Constants;
import com.DavidM1A2.afraidofthedark.common.handler.ConfigurationHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.client.IModGuiFactory;
import net.minecraftforge.fml.client.config.GuiConfig;

import java.util.Collections;
import java.util.Set;

/**
 * Allows for configuration of the mod in the options screen
 */
public class AOTDGuiFactory implements IModGuiFactory
{
	/**
	 * Initialize does nothing
	 *
	 * @param minecraft ignored
	 */
	@Override
	public void initialize(final Minecraft minecraft)
	{
	}

	/**
	 * @return True because we want our mod to be configurable in the settings
	 */
	@Override
	public boolean hasConfigGui()
	{
		return true;
	}

	/**
	 * @param parentScreen The parent GUI screen that this one will live inside of
	 *
	 * @return A GUI screen that lets us edit our mod options
	 */
	@Override
	public GuiScreen createConfigGui(GuiScreen parentScreen)
	{
		ConfigurationHandler configurationHandler = ConfigurationHandler.getInstance();
		return new GuiConfig(parentScreen, configurationHandler.getInGameConfigurableOptions(), Constants.MOD_ID, true, true, configurationHandler.getDisplayTitle());
	}

	/**
	 * @return An empty set for now, not useful for our mod
	 */
	@Override
	public Set<RuntimeOptionCategoryElement> runtimeGuiCategories()
	{
		return Collections.emptySet();
	}
}
