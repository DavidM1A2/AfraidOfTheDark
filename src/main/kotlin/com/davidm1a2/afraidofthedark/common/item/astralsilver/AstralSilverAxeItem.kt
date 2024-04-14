package com.davidm1a2.afraidofthedark.common.item.astralsilver

import com.davidm1a2.afraidofthedark.common.constants.ModResearches
import com.davidm1a2.afraidofthedark.common.constants.ModToolMaterials
import com.davidm1a2.afraidofthedark.common.item.core.AOTDResearchRequiredAxeItem
import com.davidm1a2.afraidofthedark.common.utility.damagesource.AOTDDamageType
import com.davidm1a2.afraidofthedark.common.utility.damagesource.IDoesAOTDDamageType

class AstralSilverAxeItem : AOTDResearchRequiredAxeItem("astral_silver_axe", ModToolMaterials.ASTRAL_SILVER, 5.0f, -1.5f, ModResearches.ASTRAL_SILVER, Properties()), IDoesAOTDDamageType {
    override val type: AOTDDamageType = AOTDDamageType.SILVER
}
