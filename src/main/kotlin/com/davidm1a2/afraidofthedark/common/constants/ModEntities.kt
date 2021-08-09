package com.davidm1a2.afraidofthedark.common.constants

import com.davidm1a2.afraidofthedark.common.entity.bolt.IgneousBoltEntity
import com.davidm1a2.afraidofthedark.common.entity.bolt.IronBoltEntity
import com.davidm1a2.afraidofthedark.common.entity.bolt.SilverBoltEntity
import com.davidm1a2.afraidofthedark.common.entity.bolt.StarMetalBoltEntity
import com.davidm1a2.afraidofthedark.common.entity.bolt.WoodenBoltEntity
import com.davidm1a2.afraidofthedark.common.entity.enaria.EnariaEntity
import com.davidm1a2.afraidofthedark.common.entity.enaria.GhastlyEnariaEntity
import com.davidm1a2.afraidofthedark.common.entity.enchantedFrog.EnchantedFrogEntity
import com.davidm1a2.afraidofthedark.common.entity.enchantedSkeleton.EnchantedSkeletonEntity
import com.davidm1a2.afraidofthedark.common.entity.spell.projectile.SpellProjectileEntity
import com.davidm1a2.afraidofthedark.common.entity.splinterDrone.SplinterDroneEntity
import com.davidm1a2.afraidofthedark.common.entity.splinterDrone.SplinterDroneProjectileEntity
import com.davidm1a2.afraidofthedark.common.entity.werewolf.WerewolfEntity
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityClassification
import net.minecraft.entity.EntityType
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.ai.attributes.AttributeModifierMap

/**
 * A static class containing all of our entity references for us
 */
object ModEntities {
    // All mod entity static fields
    val ENCHANTED_SKELETON = EntityType.Builder.of(::EnchantedSkeletonEntity, EntityClassification.MONSTER)
        .setTrackingRange(50)
        .setUpdateInterval(1)
        .setShouldReceiveVelocityUpdates(true)
        .sized(0.8f, 2.0f)
        .build("${Constants.MOD_ID}:enchanted_skeleton")
        .setRegistryNameGeneric("enchanted_skeleton")

    val WEREWOLF = EntityType.Builder.of(::WerewolfEntity, EntityClassification.MONSTER)
        .setTrackingRange(50)
        .setUpdateInterval(1)
        .setShouldReceiveVelocityUpdates(true)
        .sized(1.1f, 1.4f)
        .build("${Constants.MOD_ID}:werewolf")
        .setRegistryNameGeneric("werewolf")

    val GHASTLY_ENARIA = EntityType.Builder.of(::GhastlyEnariaEntity, EntityClassification.MONSTER)
        .setTrackingRange(Constants.DISTANCE_BETWEEN_ISLANDS)
        .setUpdateInterval(1)
        .setShouldReceiveVelocityUpdates(true)
        .sized(0.8f, 1.8f)
        .fireImmune()
        .noSummon()
        .build("${Constants.MOD_ID}:ghastly_enaria")
        .setRegistryNameGeneric("ghastly_enaria")

    val SPLINTER_DRONE = EntityType.Builder.of(::SplinterDroneEntity, EntityClassification.MONSTER)
        .setTrackingRange(50)
        .setUpdateInterval(1)
        .setShouldReceiveVelocityUpdates(true)
        .sized(0.8f, 3.0f)
        .fireImmune()
        .build("${Constants.MOD_ID}:splinter_drone")
        .setRegistryNameGeneric("splinter_drone")

    val SPLINTER_DRONE_PROJECTILE = EntityType.Builder.of(::SplinterDroneProjectileEntity, EntityClassification.MISC)
        .setTrackingRange(50)
        .setUpdateInterval(1)
        .setShouldReceiveVelocityUpdates(true)
        .sized(0.4f, 0.4f)
        .fireImmune()
        .build("${Constants.MOD_ID}:splinter_drone_projectile")
        .setRegistryNameGeneric("splinter_drone_projectile")

    val ENARIA = EntityType.Builder.of(::EnariaEntity, EntityClassification.MONSTER)
        .setTrackingRange(50)
        .setUpdateInterval(1)
        .setShouldReceiveVelocityUpdates(true)
        .sized(0.8f, 1.8f)
        .fireImmune()
        .noSummon()
        .build("${Constants.MOD_ID}:enaria")
        .setRegistryNameGeneric("enaria")

    val ENCHANTED_FROG = EntityType.Builder.of(::EnchantedFrogEntity, EntityClassification.CREATURE)
        .setTrackingRange(50)
        .setUpdateInterval(1)
        .setShouldReceiveVelocityUpdates(true)
        .sized(0.7f, 0.4f)
        .build("${Constants.MOD_ID}:enchanted_frog")
        .setRegistryNameGeneric("enchanted_frog")

    // Spell entities
    val SPELL_PROJECTILE = EntityType.Builder.of(::SpellProjectileEntity, EntityClassification.MISC)
        .setTrackingRange(50)
        .setUpdateInterval(1)
        .setShouldReceiveVelocityUpdates(true)
        .sized(0.4f, 0.4f)
        .build("${Constants.MOD_ID}:spell_projectile")
        .setRegistryNameGeneric("spell_projectile")

    // 5 bolt entities
    val WOODEN_BOLT = EntityType.Builder.of(::WoodenBoltEntity, EntityClassification.MISC)
        .setTrackingRange(50)
        .setUpdateInterval(1)
        .setShouldReceiveVelocityUpdates(true)
        .sized(0.5f, 0.5f)
        .build("${Constants.MOD_ID}:wooden_bolt")
        .setRegistryNameGeneric("wooden_bolt")
    val IRON_BOLT = EntityType.Builder.of(::IronBoltEntity, EntityClassification.MISC)
        .setTrackingRange(50)
        .setUpdateInterval(1)
        .setShouldReceiveVelocityUpdates(true)
        .sized(0.5f, 0.5f)
        .build("${Constants.MOD_ID}:iron_bolt")
        .setRegistryNameGeneric("iron_bolt")
    val SILVER_BOLT = EntityType.Builder.of(::SilverBoltEntity, EntityClassification.MISC)
        .setTrackingRange(50)
        .setUpdateInterval(1)
        .setShouldReceiveVelocityUpdates(true)
        .sized(0.5f, 0.5f)
        .build("${Constants.MOD_ID}:silver_bolt")
        .setRegistryNameGeneric("silver_bolt")
    val IGNEOUS_BOLT = EntityType.Builder.of(::IgneousBoltEntity, EntityClassification.MISC)
        .setTrackingRange(50)
        .setUpdateInterval(1)
        .setShouldReceiveVelocityUpdates(true)
        .sized(0.5f, 0.5f)
        .build("${Constants.MOD_ID}:igneous_bolt")
        .setRegistryNameGeneric("igneous_bolt")
    val STAR_METAL_BOLT = EntityType.Builder.of(::StarMetalBoltEntity, EntityClassification.MISC)
        .setTrackingRange(50)
        .setUpdateInterval(1)
        .setShouldReceiveVelocityUpdates(true)
        .sized(0.5f, 0.5f)
        .build("${Constants.MOD_ID}:star_metal_bolt")
        .setRegistryNameGeneric("star_metal_bolt")

    // An array containing a list of entities that AOTD adds
    val ENTITY_LIST = arrayOf(
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

    // This is not ready to be initialized at the time ENTITY_LIST is, so delay it using a lazy initializer
    val ENTITY_ATTRIBUTES: Array<Pair<EntityType<out LivingEntity>, AttributeModifierMap>> by lazy {
        arrayOf(
            ENARIA to EnariaEntity.buildAttributeModifiers().build(),
            GHASTLY_ENARIA to GhastlyEnariaEntity.buildAttributeModifiers().build(),
            ENCHANTED_FROG to EnchantedFrogEntity.buildAttributeModifiers().build(),
            ENCHANTED_SKELETON to EnchantedSkeletonEntity.buildAttributeModifiers().build(),
            SPLINTER_DRONE to SplinterDroneEntity.buildAttributeModifiers().build(),
            WEREWOLF to WerewolfEntity.buildAttributeModifiers().build()
        )
    }

    /**
     * Special version of .setRegistryName that doesn't return EntityType<*> but EntityType<T> instead
     */
    private fun <T : Entity> EntityType<T>.setRegistryNameGeneric(name: String): EntityType<T> {
        this.setRegistryName(Constants.MOD_ID, name)
        return this
    }
}