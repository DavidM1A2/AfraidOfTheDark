package com.davidm1a2.afraidofthedark.client.event

import com.davidm1a2.afraidofthedark.client.entity.bolt.AOTDBoltRenderer
import com.davidm1a2.afraidofthedark.client.entity.enaria.EnariaRenderer
import com.davidm1a2.afraidofthedark.client.entity.enaria.GhastlyEnariaRenderer
import com.davidm1a2.afraidofthedark.client.entity.enchantedFrog.EnchantedFrogRenderer
import com.davidm1a2.afraidofthedark.client.entity.enchantedSkeleton.EnchantedSkeletonRenderer
import com.davidm1a2.afraidofthedark.client.entity.frostPhoenix.FrostPhoenixProjectileRenderer
import com.davidm1a2.afraidofthedark.client.entity.frostPhoenix.FrostPhoenixRenderer
import com.davidm1a2.afraidofthedark.client.entity.spell.*
import com.davidm1a2.afraidofthedark.client.entity.splinterDrone.SplinterDroneProjectileRenderer
import com.davidm1a2.afraidofthedark.client.entity.splinterDrone.SplinterDroneRenderer
import com.davidm1a2.afraidofthedark.client.entity.werewolf.WerewolfRenderer
import com.davidm1a2.afraidofthedark.common.constants.ModEntities
import net.minecraftforge.client.event.EntityRenderersEvent.RegisterRenderers
import net.minecraftforge.eventbus.api.SubscribeEvent

class EntityRendererRegister {
    @SubscribeEvent
    fun registerRenderersEvent(event: RegisterRenderers) {
        // Register all of our renderers
        event.registerEntityRenderer(ModEntities.ENCHANTED_SKELETON, ::EnchantedSkeletonRenderer)
        event.registerEntityRenderer(ModEntities.WEREWOLF, ::WerewolfRenderer)
        event.registerEntityRenderer(ModEntities.GHASTLY_ENARIA, ::GhastlyEnariaRenderer)
        event.registerEntityRenderer(ModEntities.SPLINTER_DRONE, ::SplinterDroneRenderer)
        event.registerEntityRenderer(ModEntities.SPLINTER_DRONE_PROJECTILE, ::SplinterDroneProjectileRenderer)
        event.registerEntityRenderer(ModEntities.ENARIA, ::EnariaRenderer)
        event.registerEntityRenderer(ModEntities.WOODEN_BOLT) { AOTDBoltRenderer("wooden_bolt", it) }
        event.registerEntityRenderer(ModEntities.IRON_BOLT) { AOTDBoltRenderer("iron_bolt", it) }
        event.registerEntityRenderer(ModEntities.ASTRAL_SILVER_BOLT) { AOTDBoltRenderer("astral_silver_bolt", it) }
        event.registerEntityRenderer(ModEntities.IGNEOUS_BOLT) { AOTDBoltRenderer("igneous_bolt", it) }
        event.registerEntityRenderer(ModEntities.STAR_METAL_BOLT) { AOTDBoltRenderer("star_metal_bolt", it) }
        event.registerEntityRenderer(ModEntities.ELDRITCH_METAL_BOLT) { AOTDBoltRenderer("eldritch_metal_bolt", it) }
        event.registerEntityRenderer(ModEntities.AMORPHOUS_METAL_BOLT) { AOTDBoltRenderer("amorphous_metal_bolt", it) }
        event.registerEntityRenderer(ModEntities.VOID_BOLT) { AOTDBoltRenderer("void_bolt", it) }
        event.registerEntityRenderer(ModEntities.SPELL_PROJECTILE, ::SpellProjectileRenderer)
        event.registerEntityRenderer(ModEntities.SPELL_LASER, ::SpellLaserRenderer)
        event.registerEntityRenderer(ModEntities.SPELL_AOE, ::SpellAOERenderer)
        event.registerEntityRenderer(ModEntities.SPELL_WALL, ::SpellWallRenderer)
        event.registerEntityRenderer(ModEntities.SPELL_CONE, ::SpellConeRenderer)
        event.registerEntityRenderer(ModEntities.SPELL_CHAIN, ::SpellChainRenderer)
        event.registerEntityRenderer(ModEntities.ENCHANTED_FROG, ::EnchantedFrogRenderer)
        event.registerEntityRenderer(ModEntities.FROST_PHOENIX, ::FrostPhoenixRenderer)
        event.registerEntityRenderer(ModEntities.FROST_PHOENIX_PROJECTILE, ::FrostPhoenixProjectileRenderer)
    }
}