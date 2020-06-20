package com.davidm1a2.afraidofthedark.common.tileEntity.core

import net.minecraft.tileentity.TileEntity
import net.minecraft.tileentity.TileEntityType

/**
 * Base class for all AOTD tile entities
 *
 * @constructor initializes the tile entity fields
 * @param tileEntityType The tile entity's type
 */
abstract class AOTDTileEntity(tileEntityType: TileEntityType<*>) : TileEntity(tileEntityType)