package com.davidm1a2.afraidofthedark.common.item.astralsilver

import com.davidm1a2.afraidofthedark.common.constants.ModResearches
import com.davidm1a2.afraidofthedark.common.constants.ModToolMaterials
import com.davidm1a2.afraidofthedark.common.item.core.AOTDResearchRequiredSwordItem
import com.davidm1a2.afraidofthedark.common.utility.damagesource.AOTDDamageType
import com.davidm1a2.afraidofthedark.common.utility.damagesource.IDoesAOTDDamageType

class AstralSilverSwordItem : AOTDResearchRequiredSwordItem("astral_silver_sword", ModToolMaterials.ASTRAL_SILVER, 3, -1.2f, ModResearches.ASTRAL_SILVER, Properties()), IDoesAOTDDamageType {
    override val type: AOTDDamageType = AOTDDamageType.SILVER
}