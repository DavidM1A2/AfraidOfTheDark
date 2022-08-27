package com.davidm1a2.afraidofthedark.common.integration.jei

import com.davidm1a2.afraidofthedark.common.constants.Constants
import com.davidm1a2.afraidofthedark.common.crafting.ResearchRequiredRecipeBase
import mezz.jei.api.IModPlugin
import mezz.jei.api.JeiPlugin
import mezz.jei.api.registration.IVanillaCategoryExtensionRegistration
import net.minecraft.util.ResourceLocation

@JeiPlugin
class JeiPlugin : IModPlugin {
    override fun getPluginUid(): ResourceLocation {
        return ResourceLocation(Constants.MOD_ID, PLUGIN_NAME)
    }

    override fun registerVanillaCategoryExtensions(registration: IVanillaCategoryExtensionRegistration) {
        val craftingCategory = registration.craftingCategory
        craftingCategory.addCategoryExtension(ResearchRequiredRecipeBase::class.java, ::CraftingCategoryExtension)
    }

    companion object {
        private const val PLUGIN_NAME = "plugin"
    }
}