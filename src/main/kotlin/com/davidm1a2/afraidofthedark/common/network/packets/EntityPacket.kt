package com.davidm1a2.afraidofthedark.common.network.packets

import java.util.*

/**
 * Base packet for entity synchronizing
 */
abstract class EntityPacket(internal val entityUUID: UUID, internal val entityID: Int)