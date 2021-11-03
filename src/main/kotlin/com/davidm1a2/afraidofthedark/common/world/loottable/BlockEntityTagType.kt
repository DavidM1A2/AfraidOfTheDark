package com.davidm1a2.afraidofthedark.common.world.loottable

import com.davidm1a2.afraidofthedark.common.constants.Constants
import net.minecraft.util.ResourceLocation

class BlockEntityTagType : AOTDLootConditionType(BlockEntityTag.Serializer(), ResourceLocation(Constants.MOD_ID, "block_entity_tag"))