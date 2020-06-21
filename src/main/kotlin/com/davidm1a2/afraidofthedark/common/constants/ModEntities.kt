package com.davidm1a2.afraidofthedark.common.constants

import com.davidm1a2.afraidofthedark.common.entity.bolt.*
import com.davidm1a2.afraidofthedark.common.entity.enaria.EntityEnaria
import com.davidm1a2.afraidofthedark.common.entity.enaria.EntityGhastlyEnaria
import com.davidm1a2.afraidofthedark.common.entity.enchantedFrog.EntityEnchantedFrog
import com.davidm1a2.afraidofthedark.common.entity.enchantedSkeleton.EntityEnchantedSkeleton
import com.davidm1a2.afraidofthedark.common.entity.spell.projectile.EntitySpellProjectile
import com.davidm1a2.afraidofthedark.common.entity.splinterDrone.EntitySplinterDrone
import com.davidm1a2.afraidofthedark.common.entity.splinterDrone.EntitySplinterDroneProjectile
import com.davidm1a2.afraidofthedark.common.entity.werewolf.EntityWerewolf
import net.minecraft.entity.EntityType

/**
 * A static class containing all of our entity references for us
 */
object ModEntities {
    // All mod entity static fields
    val ENCHANTED_SKELETON = EntityType.Builder.create(EntityEnchantedSkeleton::class.java) { EntityEnchantedSkeleton(it) }
        .tracker(50, 1, true)
        .build("${Constants.MOD_ID}:enchanted_skeleton")
        .setRegistryName(Constants.MOD_ID, "enchanted_skeleton")
    //.egg(0x996600, 0xe69900)

    val WEREWOLF = EntityType.Builder.create(EntityWerewolf::class.java) { EntityWerewolf(it) }
        .tracker(50, 1, true)
        .build("${Constants.MOD_ID}:werewolf")
        .setRegistryName(Constants.MOD_ID, "werewolf")
    //.egg(0x3B170B, 0x181907)
    //.spawn(EnumCreatureType.MONSTER, 25, 1, 4, ModBiomes.EERIE_FOREST) // Weight = 100 is for skeletons, use 1/4 of that

    val GHASTLY_ENARIA = EntityType.Builder.create(EntityGhastlyEnaria::class.java) { EntityGhastlyEnaria(it) }
        .tracker(ModServerConfiguration.blocksBetweenIslands / 2, 1, true)
        .build("${Constants.MOD_ID}:ghastly_enaria")
        .setRegistryName(Constants.MOD_ID, "ghastly_enaria")

    val SPLINTER_DRONE = EntityType.Builder.create(EntitySplinterDrone::class.java) { EntitySplinterDrone(it) }
        .tracker(50, 1, true)
        .build("${Constants.MOD_ID}:splinter_drone")
        .setRegistryName(Constants.MOD_ID, "splinter_drone")
    //.egg(0xcc6600, 0x63300)

    val SPLINTER_DRONE_PROJECTILE = EntityType.Builder.create(EntitySplinterDroneProjectile::class.java) { EntitySplinterDroneProjectile(it) }
        .tracker(50, 1, true)
        .build("${Constants.MOD_ID}:splinter_drone_projectile")
        .setRegistryName(Constants.MOD_ID, "splinter_drone_projectile")

    val ENARIA = EntityType.Builder.create(EntityEnaria::class.java) { EntityEnaria(it) }
        .tracker(50, 1, true)
        .build("${Constants.MOD_ID}:enaria")
        .setRegistryName(Constants.MOD_ID, "enaria")

    val ENCHANTED_FROG = EntityType.Builder.create(EntityEnchantedFrog::class.java) { EntityEnchantedFrog(it) }
        .tracker(50, 1, true)
        .build("${Constants.MOD_ID}:enchanted_frog")
        .setRegistryName(Constants.MOD_ID, "enchanted_frog")
    // .egg(0x92029c, 0xd4028e)

    // Spell entities
    val SPELL_PROJECTILE = EntityType.Builder.create(EntitySpellProjectile::class.java) { EntitySpellProjectile(it) }
        .tracker(50, 1, true)
        .build("${Constants.MOD_ID}:spell_projectile")
        .setRegistryName(Constants.MOD_ID, "spell_projectile")

    // 5 bolt entities
    val WOODEN_BOLT = EntityType.Builder.create(EntityWoodenBolt::class.java) { EntityWoodenBolt(it) }
        .tracker(50, 1, true)
        .build("${Constants.MOD_ID}:wooden_bolt")
        .setRegistryName(Constants.MOD_ID, "wooden_bolt")
    val IRON_BOLT = EntityType.Builder.create(EntityIronBolt::class.java) { EntityIronBolt(it) }
        .tracker(50, 1, true)
        .build("${Constants.MOD_ID}:iron_bolt")
        .setRegistryName(Constants.MOD_ID, "iron_bolt")
    val SILVER_BOLT = EntityType.Builder.create(EntitySilverBolt::class.java) { EntitySilverBolt(it) }
        .tracker(50, 1, true)
        .build("${Constants.MOD_ID}:silver_bolt")
        .setRegistryName(Constants.MOD_ID, "silver_bolt")
    val IGNEOUS_BOLT = EntityType.Builder.create(EntityIgneousBolt::class.java) { EntityIgneousBolt(it) }
        .tracker(50, 1, true)
        .build("${Constants.MOD_ID}:igneous_bolt")
        .setRegistryName(Constants.MOD_ID, "igneous_bolt")
    val STAR_METAL_BOLT = EntityType.Builder.create(EntityStarMetalBolt::class.java) { EntityStarMetalBolt(it) }
        .tracker(50, 1, true)
        .build("${Constants.MOD_ID}:star_metal_bolt")
        .setRegistryName(Constants.MOD_ID, "star_metal_bolt")

    // An array containing a list of entities that AOTD adds
    var ENTITY_LIST = arrayOf(
        ENCHANTED_SKELETON,
        WEREWOLF,
        GHASTLY_ENARIA,
        SPLINTER_DRONE,
        SPLINTER_DRONE_PROJECTILE,
        ENARIA,
        ENCHANTED_FROG,
        SPELL_PROJECTILE,
        WOODEN_BOLT,
        IRON_BOLT,
        SILVER_BOLT,
        IGNEOUS_BOLT,
        STAR_METAL_BOLT
    )
}