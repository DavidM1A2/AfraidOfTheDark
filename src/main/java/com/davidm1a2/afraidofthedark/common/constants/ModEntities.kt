package com.davidm1a2.afraidofthedark.common.constants

import com.davidm1a2.afraidofthedark.AfraidOfTheDark
import com.davidm1a2.afraidofthedark.common.entity.bolt.*
import com.davidm1a2.afraidofthedark.common.entity.enaria.EntityEnaria
import com.davidm1a2.afraidofthedark.common.entity.enaria.EntityGhastlyEnaria
import com.davidm1a2.afraidofthedark.common.entity.enchantedSkeleton.EntityEnchantedSkeleton
import com.davidm1a2.afraidofthedark.common.entity.spell.projectile.EntitySpellProjectile
import com.davidm1a2.afraidofthedark.common.entity.splinterDrone.EntitySplinterDrone
import com.davidm1a2.afraidofthedark.common.entity.splinterDrone.EntitySplinterDroneProjectile
import com.davidm1a2.afraidofthedark.common.entity.werewolf.EntityWerewolf
import net.minecraft.entity.EnumCreatureType
import net.minecraft.util.ResourceLocation
import net.minecraftforge.fml.common.registry.EntityEntryBuilder

/**
 * A static class containing all of our entity references for us
 */
object ModEntities {
    // Various entity IDs
    const val WEREWOLF_ID = 0
    const val IRON_BOLT_ID = 1
    const val SILVER_BOLT_ID = 2
    const val WOODEN_BOLT_ID = 3
    const val DEEE_SYFT_ID = 4
    const val IGNEOUS_BOLT_ID = 5
    const val STAR_METAL_BOLT_ID = 6
    const val ENCHANTED_SKELETON_ID = 7
    const val ENARIA_ID = 8
    const val SPLINTER_DRONE_ID = 9
    const val SPLINTER_DRONE_PROJECTILE_ID = 10
    const val SPELL_PROJECTILE_ID = 11
    const val SPELL_PROJECTILE_DIVE_ID = 12
    const val SPELL_MYSELF_ID = 13
    const val SPELL_AOE_ID = 14
    const val GHASTLY_ENARIA_ID = 15
    const val ARTWORK_ID = 16
    const val SPELL_EXTRA_EFFECTS_ID = 17

    // All mod entity static fields
    val ENCHANTED_SKELETON = EntityEntryBuilder.create<EntityEnchantedSkeleton>()
        .egg(0x996600, 0xe69900)
        .entity(EntityEnchantedSkeleton::class.java)
        .id(ResourceLocation(Constants.MOD_ID, "enchanted_skeleton"), ENCHANTED_SKELETON_ID)
        .name("enchanted_skeleton")
        .tracker(50, 1, true)
        .build()

    val WEREWOLF = EntityEntryBuilder.create<EntityWerewolf>()
        .egg(0x3B170B, 0x181907)
        .entity(EntityWerewolf::class.java)
        .id(ResourceLocation(Constants.MOD_ID, "werewolf"), WEREWOLF_ID)
        .name("werewolf")
        .tracker(50, 1, true)
        .spawn(
            EnumCreatureType.MONSTER,
            25,
            1,
            4,
            ModBiomes.EERIE_FOREST
        ) // Weight = 100 is for skeletons, use 1/4 of that
        .build()

    val GHASTLY_ENARIA = EntityEntryBuilder.create<EntityGhastlyEnaria>()
        .entity(EntityGhastlyEnaria::class.java)
        .id(ResourceLocation(Constants.MOD_ID, "ghastly_enaria"), GHASTLY_ENARIA_ID)
        .name("ghastly_enaria")
        .tracker(AfraidOfTheDark.INSTANCE.configurationHandler.blocksBetweenIslands / 2, 1, true)
        .build()

    val SPLINTER_DRONE = EntityEntryBuilder.create<EntitySplinterDrone>()
        .egg(0xcc6600, 0x63300)
        .entity(EntitySplinterDrone::class.java)
        .id(ResourceLocation(Constants.MOD_ID, "splinter_drone"), SPLINTER_DRONE_ID)
        .name("splinter_drone")
        .tracker(50, 1, true)
        .build()

    val SPLINTER_DRONE_PROJECTILE = EntityEntryBuilder.create<EntitySplinterDroneProjectile>()
        .entity(EntitySplinterDroneProjectile::class.java)
        .id(ResourceLocation(Constants.MOD_ID, "splinter_drone_projectile"), SPLINTER_DRONE_PROJECTILE_ID)
        .name("splinter_drone_projectile")
        .tracker(50, 1, true)
        .build()

    val ENARIA = EntityEntryBuilder.create<EntityEnaria>()
        .entity(EntityEnaria::class.java)
        .id(ResourceLocation(Constants.MOD_ID, "enaria"), ENARIA_ID)
        .name("enaria")
        .tracker(50, 1, true)
        .build()

    // Spell entities
    val SPELL_PROJECTILE = EntityEntryBuilder.create<EntitySpellProjectile>()
        .entity(EntitySpellProjectile::class.java)
        .id(ResourceLocation(Constants.MOD_ID, "spell_projectile"), SPELL_PROJECTILE_ID)
        .name("spell_projectile")
        .tracker(50, 1, true)
        .build()

    // 5 bolt entities
    val WOODEN_BOLT = EntityEntryBuilder.create<EntityWoodenBolt>()
        .entity(EntityWoodenBolt::class.java)
        .id(ResourceLocation(Constants.MOD_ID, "wooden_bolt"), WOODEN_BOLT_ID)
        .name("wooden_bolt")
        .tracker(50, 1, true)
        .build()
    val IRON_BOLT = EntityEntryBuilder.create<EntityIronBolt>()
        .entity(EntityIronBolt::class.java)
        .id(ResourceLocation(Constants.MOD_ID, "iron_bolt"), IRON_BOLT_ID)
        .name("iron_bolt")
        .tracker(50, 1, true)
        .build()
    val SILVER_BOLT = EntityEntryBuilder.create<EntitySilverBolt>()
        .entity(EntitySilverBolt::class.java)
        .id(ResourceLocation(Constants.MOD_ID, "silver_bolt"), SILVER_BOLT_ID)
        .name("silver_bolt")
        .tracker(50, 1, true)
        .build()
    val IGNEOUS_BOLT = EntityEntryBuilder.create<EntityIgneousBolt>()
        .entity(EntityIgneousBolt::class.java)
        .id(ResourceLocation(Constants.MOD_ID, "igneous_bolt"), IGNEOUS_BOLT_ID)
        .name("igneous_bolt")
        .tracker(50, 1, true)
        .build()
    val STAR_METAL_BOLT = EntityEntryBuilder.create<EntityStarMetalBolt>()
        .entity(EntityStarMetalBolt::class.java)
        .id(ResourceLocation(Constants.MOD_ID, "star_metal_bolt"), STAR_METAL_BOLT_ID)
        .name("star_metal_bolt")
        .tracker(50, 1, true)
        .build()

    // An array containing a list of entities that AOTD adds
    var ENTITY_LIST = arrayOf(
        ENCHANTED_SKELETON,
        WEREWOLF,
        GHASTLY_ENARIA,
        SPLINTER_DRONE,
        SPLINTER_DRONE_PROJECTILE,
        ENARIA,
        SPELL_PROJECTILE,
        WOODEN_BOLT,
        IRON_BOLT,
        SILVER_BOLT,
        IGNEOUS_BOLT,
        STAR_METAL_BOLT
    )
}