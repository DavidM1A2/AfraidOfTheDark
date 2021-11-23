package com.davidm1a2.afraidofthedark.common.capabilities.player.spell.component

import java.util.UUID

/**
 * Default implementation of the AOTD spell charm data class
 *
 * @property charmTicks The number of charm ticks remaining
 * @property charmingEntityId The id of the entity that is doing the charming
 */
class PlayerSpellCharmData : IPlayerSpellCharmData {
    override var charmTicks = 0
    override var charmingEntityId: UUID? = null
}