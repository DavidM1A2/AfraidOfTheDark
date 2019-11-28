package com.davidm1a2.afraidofthedark.client.gui

import com.davidm1a2.afraidofthedark.AfraidOfTheDark
import com.davidm1a2.afraidofthedark.common.constants.Constants
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiScreen
import net.minecraftforge.fml.client.IModGuiFactory
import net.minecraftforge.fml.client.IModGuiFactory.RuntimeOptionCategoryElement
import net.minecraftforge.fml.client.config.GuiConfig

/**
 * Allows for configuration of the mod in the options screen
 */
class AOTDGuiFactory : IModGuiFactory
{
    /**
     * Initialize does nothing
     *
     * @param minecraft ignored
     */
    override fun initialize(minecraft: Minecraft)
    {
    }

    /**
     * @return True because we want our mod to be configurable in the settings
     */
    override fun hasConfigGui(): Boolean
    {
        return true
    }

    /**
     * @param parentScreen The parent GUI screen that this one will live inside of
     * @return A GUI screen that lets us edit our mod options
     */
    override fun createConfigGui(parentScreen: GuiScreen): GuiScreen
    {
        val configurationHandler = AfraidOfTheDark.INSTANCE.configurationHandler
        return GuiConfig(
            parentScreen,
            configurationHandler.inGameConfigurableOptions,
            Constants.MOD_ID,
            true,
            true,
            configurationHandler.displayTitle
        )
    }

    /**
     * @return An empty set for now, not useful for our mod
     */
    override fun runtimeGuiCategories(): Set<RuntimeOptionCategoryElement>
    {
        return emptySet()
    }
}