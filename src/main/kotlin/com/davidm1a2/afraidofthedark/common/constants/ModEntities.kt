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
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityType

/**
 * A static class containing all of our entity references for us
 */
object ModEntities {
    // All mod entity static fields
    val ENCHANTED_SKELETON = EntityType.Builder.create(EntityEnchantedSkeleton::class.java) { EntityEnchantedSkeleton(it) }
        .tracker(50, 1, true)
        .build("${Constants.MOD_ID}:enchanted_skeleton")
        .setRegistryNameGeneric("enchanted_skeleton")

    val WEREWOLF = EntityType.Builder.create(EntityWerewolf::class.java) { EntityWerewolf(it) }
        .tracker(50, 1, true)
        .build("${Constants.MOD_ID}:werewolf")
        .setRegistryNameGeneric("werewolf")

    val GHASTLY_ENARIA = EntityType.Builder.create(EntityGhastlyEnaria::class.java) { EntityGhastlyEnaria(it) }
        .tracker(ModCommonConfiguration.blocksBetweenIslands / 2, 1, true)
        .build("${Constants.MOD_ID}:ghastly_enaria")
        .setRegistryNameGeneric("ghastly_enaria")

    val SPLINTER_DRONE = EntityType.Builder.create(EntitySplinterDrone::class.java) { EntitySplinterDrone(it) }
        .tracker(50, 1, true)
        .build("${Constants.MOD_ID}:splinter_drone")
        .setRegistryNameGeneric("splinter_drone")

    val SPLINTER_DRONE_PROJECTILE = EntityType.Builder.create(EntitySplinterDroneProjectile::class.java) { EntitySplinterDroneProjectile(it) }
        .tracker(50, 1, true)
        .build("${Constants.MOD_ID}:splinter_drone_projectile")
        .setRegistryNameGeneric("splinter_drone_projectile")

    val ENARIA = EntityType.Builder.create(EntityEnaria::class.java) { EntityEnaria(it) }
        .tracker(50, 1, true)
        .build("${Constants.MOD_ID}:enaria")
        .setRegistryNameGeneric("enaria")

    val ENCHANTED_FROG = EntityType.Builder.create(EntityEnchantedFrog::class.java) { EntityEnchantedFrog(it) }
        .tracker(50, 1, true)
        .build("${Constants.MOD_ID}:enchanted_frog")
        .setRegistryNameGeneric("enchanted_frog")

    // Spell entities
    val SPELL_PROJECTILE = EntityType.Builder.create(EntitySpellProjectile::class.java) { EntitySpellProjectile(it) }
        .tracker(50, 1, true)
        .build("${Constants.MOD_ID}:spell_projectile")
        .setRegistryNameGeneric("spell_projectile")

    // 5 bolt entities
    val WOODEN_BOLT = EntityType.Builder.create(EntityWoodenBolt::class.java) { EntityWoodenBolt(it) }
        .tracker(50, 1, true)
        .build("${Constants.MOD_ID}:wooden_bolt")
        .setRegistryNameGeneric("wooden_bolt")
    val IRON_BOLT = EntityType.Builder.create(EntityIronBolt::class.java) { EntityIronBolt(it) }
        .tracker(50, 1, true)
        .build("${Constants.MOD_ID}:iron_bolt")
        .setRegistryNameGeneric("iron_bolt")
    val SILVER_BOLT = EntityType.Builder.create(EntitySilverBolt::class.java) { EntitySilverBolt(it) }
        .tracker(50, 1, true)
        .build("${Constants.MOD_ID}:silver_bolt")
        .setRegistryNameGeneric("silver_bolt")
    val IGNEOUS_BOLT = EntityType.Builder.create(EntityIgneousBolt::class.java) { EntityIgneousBolt(it) }
        .tracker(50, 1, true)
        .build("${Constants.MOD_ID}:igneous_bolt")
        .setRegistryNameGeneric("igneous_bolt")
    val STAR_METAL_BOLT = EntityType.Builder.create(EntityStarMetalBolt::class.java) { EntityStarMetalBolt(it) }
        .tracker(50, 1, true)
        .build("${Constants.MOD_ID}:star_metal_bolt")
        .setRegistryNameGeneric("star_metal_bolt")

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

    /**
     * Special version of .setRegistryName that doesn't return EntityType<*> but EntityType<T> instead
     */
    private fun <T : Entity> EntityType<T>.setRegistryNameGeneric(name: String): EntityType<T> {
        this.setRegistryName(Constants.MOD_ID, name)
        return this
    }
}