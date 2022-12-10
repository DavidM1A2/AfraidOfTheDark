package com.davidm1a2.afraidofthedark.client.event

import com.davidm1a2.afraidofthedark.client.entity.bolt.AstralSilverBoltRenderer
import com.davidm1a2.afraidofthedark.client.entity.bolt.EldritchMetalBoltRenderer
import com.davidm1a2.afraidofthedark.client.entity.bolt.IgneousBoltRenderer
import com.davidm1a2.afraidofthedark.client.entity.bolt.IronBoltRenderer
import com.davidm1a2.afraidofthedark.client.entity.bolt.StarMetalBoltRenderer
import com.davidm1a2.afraidofthedark.client.entity.bolt.WoodenBoltRenderer
import com.davidm1a2.afraidofthedark.client.entity.enaria.EnariaRenderer
import com.davidm1a2.afraidofthedark.client.entity.enaria.GhastlyEnariaRenderer
import com.davidm1a2.afraidofthedark.client.entity.enchantedFrog.EnchantedFrogRenderer
import com.davidm1a2.afraidofthedark.client.entity.enchantedSkeleton.EnchantedSkeletonRenderer
import com.davidm1a2.afraidofthedark.client.entity.frostPhoenix.FrostPhoenixProjectileRenderer
import com.davidm1a2.afraidofthedark.client.entity.frostPhoenix.FrostPhoenixRenderer
import com.davidm1a2.afraidofthedark.client.entity.spell.SpellAOERenderer
import com.davidm1a2.afraidofthedark.client.entity.spell.SpellLaserRenderer
import com.davidm1a2.afraidofthedark.client.entity.spell.SpellProjectileRenderer
import com.davidm1a2.afraidofthedark.client.entity.splinterDrone.SplinterDroneProjectileRenderer
import com.davidm1a2.afraidofthedark.client.entity.splinterDrone.SplinterDroneRenderer
import com.davidm1a2.afraidofthedark.client.entity.werewolf.WerewolfRenderer
import com.davidm1a2.afraidofthedark.common.constants.ModEntities
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.client.registry.RenderingRegistry
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent

class EntityRendererRegister {
    @SubscribeEvent
    fun fmlClientSetupEvent(event: FMLClientSetupEvent) {
        // Register all of our renderers
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.ENCHANTED_SKELETON) { EnchantedSkeletonRenderer(it) }
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.WEREWOLF) { WerewolfRenderer(it) }
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.GHASTLY_ENARIA) { GhastlyEnariaRenderer(it) }
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.SPLINTER_DRONE) { SplinterDroneRenderer(it) }
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.SPLINTER_DRONE_PROJECTILE) { SplinterDroneProjectileRenderer(it) }
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.ENARIA) { EnariaRenderer(it) }
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.WOODEN_BOLT) { WoodenBoltRenderer(it) }
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.IRON_BOLT) { IronBoltRenderer(it) }
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.ASTRAL_SILVER_BOLT) { AstralSilverBoltRenderer(it) }
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.IGNEOUS_BOLT) { IgneousBoltRenderer(it) }
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.STAR_METAL_BOLT) { StarMetalBoltRenderer(it) }
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.ELDRITCH_METAL_BOLT) { EldritchMetalBoltRenderer(it) }
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.SPELL_PROJECTILE) { SpellProjectileRenderer(it) }
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.SPELL_LASER) { SpellLaserRenderer(it) }
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.SPELL_AOE) { SpellAOERenderer(it) }
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.ENCHANTED_FROG) { EnchantedFrogRenderer(it) }
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.FROST_PHOENIX) { FrostPhoenixRenderer(it) }
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.FROST_PHOENIX_PROJECTILE) { FrostPhoenixProjectileRenderer(it) }
    }
}