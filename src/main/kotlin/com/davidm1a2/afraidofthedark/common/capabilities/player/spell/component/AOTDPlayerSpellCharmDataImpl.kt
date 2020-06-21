package com.davidm1a2.afraidofthedark.common.capabilities.player.spell.component

import java.util.*

/**
 * Default implementation of the AOTD spell charm data class
 *
 * @property charmTicks The number of charm ticks remaining
 * @property charmingEntityId The id of the entity that is doing the charming
 */
class AOTDPlayerSpellCharmDataImpl : IAOTDPlayerSpellCharmData {
    override var charmTicks = 0
    override var charmingEntityId: UUID? = null
}