package com.davidm1a2.afraidofthedark.common.item.astralsilver

import com.davidm1a2.afraidofthedark.common.constants.ModResearches
import com.davidm1a2.afraidofthedark.common.constants.ModToolMaterials
import com.davidm1a2.afraidofthedark.common.item.core.AOTDResearchRequiredHoeItem
import com.davidm1a2.afraidofthedark.common.utility.damagesource.AOTDDamageType
import com.davidm1a2.afraidofthedark.common.utility.damagesource.IDoesAOTDDamageType

class AstralSilverHoeItem : AOTDResearchRequiredHoeItem("astral_silver_hoe", ModToolMaterials.ASTRAL_SILVER, -3, 0.0f, ModResearches.ASTRAL_SILVER, Properties()), IDoesAOTDDamageType {
    override val type: AOTDDamageType = AOTDDamageType.SILVER
}

