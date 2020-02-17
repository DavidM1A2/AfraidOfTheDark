package com.davidm1a2.afraidofthedark.common.registry.meteor

import com.davidm1a2.afraidofthedark.AfraidOfTheDark
import com.davidm1a2.afraidofthedark.common.registry.research.Research
import net.minecraft.block.Block
import net.minecraft.util.ResourceLocation
import net.minecraftforge.registries.IForgeRegistryEntry

/**
 * Base class for all meteor entries, meteor entries are used to define new meteor properties
 *
 * @property icon The resource location containing the icon that this meteor type will use in the telescope
 * @property minMeteorRadius The minimum radius of the meteor
 * @property maxMeteorRadius The maximum radius of the meteor
 * @property richnessPercent What percent of the meteor is ore vs meteor block
 * @property interiorBlock The block that the meteor uses on the inside
 * @property preRequisite The pre-requisite research to be able to see this type of meteor
 */
abstract class MeteorEntry(
        val icon: ResourceLocation,
        val minMeteorRadius: Int,
        val maxMeteorRadius: Int,
        val richnessPercent: Double,
        val interiorBlock: Block,
        val preRequisite: Research
) : IForgeRegistryEntry.Impl<MeteorEntry>()
{
    init
    {
        // Ensure the min/max radii are valid values
        if (minMeteorRadius < 2)
        {
            AfraidOfTheDark.INSTANCE.logger.error("Meteor entries should not have a min radius less than 2!")
        }
        if (minMeteorRadius > maxMeteorRadius)
        {
            AfraidOfTheDark.INSTANCE.logger.error("Meteor entries max-radius should be larger than the min-radius!")
        }
    }

    /**
     * @return The unlocalized name of the meteor entry
     */
    fun getUnlocalizedName(): String
    {
        return "meteor_entry.${registryName!!.resourceDomain}:${registryName!!.resourcePath}"
    }
}