package com.davidm1a2.afraidofthedark.common.loot.loottable

import com.davidm1a2.afraidofthedark.common.constants.Constants
import net.minecraft.resources.ResourceLocation

class BlockEntityTagType : AOTDLootConditionType(BlockEntityTag.Serializer(), ResourceLocation(Constants.MOD_ID, "block_entity_tag"))