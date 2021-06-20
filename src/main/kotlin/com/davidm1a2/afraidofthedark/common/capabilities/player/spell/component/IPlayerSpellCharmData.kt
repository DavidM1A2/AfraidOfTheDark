package com.davidm1a2.afraidofthedark.common.capabilities.player.spell.component

import java.util.*

/**
 * An interface that defines any charm data stored on the player
 *
 * @property charmTicks The number of ticks the player should be charmed
 * @property charmingEntityId The id of the entity that is doing the charming
 */
interface IPlayerSpellCharmData {
    var charmTicks: Int
    var charmingEntityId: UUID?
}