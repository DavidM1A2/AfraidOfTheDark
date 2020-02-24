package com.davidm1a2.afraidofthedark.common.registry.meteor

import com.davidm1a2.afraidofthedark.common.constants.Constants
import com.davidm1a2.afraidofthedark.common.registry.research.Research
import net.minecraft.block.Block
import net.minecraft.util.ResourceLocation

/**
 * Base class for all AOTD meteor entries
 *
 * @constructor just initializes fields
 * @param name            The name of the meteor entry
 * @param icon            The resource location containing the icon that this meteor type will use in the telescope
 * @param minMeteorRadius The minimum radius of the meteor
 * @param maxMeteorRadius The maximum radius of the meteor
 * @param richnessPercent What percent of the meteor is ore vs meteor block
 * @param interiorBlock   The block that the meteor uses on the inside
 * @param preRequisite    The pre-requisite research to be able to see this type of meteor
 */
class AOTDMeteorEntry(
    name: String,
    icon: ResourceLocation,
    minMeteorRadius: Int,
    maxMeteorRadius: Int,
    richnessPercent: Double,
    interiorBlock: Block,
    preRequisite: Research
) : MeteorEntry(icon, minMeteorRadius, maxMeteorRadius, richnessPercent, interiorBlock, preRequisite) {
    init {
        registryName = ResourceLocation(Constants.MOD_ID, name)
    }
}