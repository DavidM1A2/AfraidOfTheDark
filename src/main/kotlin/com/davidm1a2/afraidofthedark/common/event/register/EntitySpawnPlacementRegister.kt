package com.davidm1a2.afraidofthedark.common.event.register

import com.davidm1a2.afraidofthedark.common.constants.ModEntities
import net.minecraft.entity.EntitySpawnPlacementRegistry
import net.minecraft.entity.monster.MonsterEntity
import net.minecraft.world.gen.Heightmap
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent

class EntitySpawnPlacementRegister {
    @SubscribeEvent
    fun commonSetupEvent(event: FMLCommonSetupEvent) {
        event.enqueueWork {
            EntitySpawnPlacementRegistry.register(
                ModEntities.WEREWOLF,
                EntitySpawnPlacementRegistry.PlacementType.ON_GROUND,
                Heightmap.Type.WORLD_SURFACE,
                MonsterEntity::checkMonsterSpawnRules
            )
        }
    }
}