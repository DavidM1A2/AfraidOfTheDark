package com.davidm1a2.afraidofthedark.common.spell.component.powerSource

import com.davidm1a2.afraidofthedark.common.constants.ModBlocks
import com.davidm1a2.afraidofthedark.common.constants.ModCommonConfiguration
import com.davidm1a2.afraidofthedark.common.constants.ModEntities
import com.davidm1a2.afraidofthedark.common.constants.ModResearches
import com.davidm1a2.afraidofthedark.common.spell.Spell
import com.davidm1a2.afraidofthedark.common.spell.component.powerSource.base.AOTDSpellPowerSource
import com.davidm1a2.afraidofthedark.common.spell.component.powerSource.base.CastEnvironment
import com.davidm1a2.afraidofthedark.common.spell.component.powerSource.base.SpellCastResult
import com.davidm1a2.afraidofthedark.common.utility.round
import net.minecraft.block.Blocks
import net.minecraft.world.entity.Entity
import net.minecraft.entity.EntityType
import net.minecraft.entity.LivingEntity
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.util.math.BlockPos
import net.minecraft.util.text.TranslatableComponent
import org.apache.logging.log4j.LogManager
import kotlin.math.min

class LeechSpellPowerSource : AOTDSpellPowerSource<LeechSpellPowerSource.LeechContext>("leech", ModResearches.BLOODBATH) {
    override fun cast(entity: Entity, spell: Spell, environment: CastEnvironment<LeechContext>): SpellCastResult {
        if (environment.vitaeAvailable < spell.getCost()) {
            return SpellCastResult.failure(TranslatableComponent("${getUnlocalizedBaseName()}.not_enough_power"))
        }

        var vitaeToDistribute = spell.getCost()

        // Destroy all leeched blocks
        val context = environment.context
        val blocksToDestroy = context.nearbyBlockToVitae.keys.shuffled()
        for (blockToDestroy in blocksToDestroy) {
            if (vitaeToDistribute <= 0) {
                break
            }

            vitaeToDistribute = vitaeToDistribute - context.nearbyBlockToVitae[blockToDestroy]!!
            if (context.grassPositions.contains(blockToDestroy)) {
                entity.level.setBlockAndUpdate(blockToDestroy, Blocks.DIRT.defaultBlockState())
            } else {
                entity.level.setBlockAndUpdate(blockToDestroy, Blocks.AIR.defaultBlockState())
            }
        }

        // Damage all leeched entities
        for ((damagedEntity, damage) in distributeDamageOver(context.leechableEntities, vitaeToDistribute / VITAE_PER_HP)) {
            damagedEntity.hurt(DamageSource.OUT_OF_WORLD, damage.toFloat())
        }

        return SpellCastResult.success()
    }

    override fun computeCastEnvironment(entity: Entity): CastEnvironment<LeechContext> {
        val nearbyBlockToVitae = mutableMapOf<BlockPos, Double>()
        val grassPositions = mutableSetOf<BlockPos>()
        var totalBlockVitae = 0.0

        // Find all nearby blocks that have vitae potential
        for (x in -LEECH_RANGE_BLOCKS..LEECH_RANGE_BLOCKS) {
            for (y in -LEECH_RANGE_BLOCKS..LEECH_RANGE_BLOCKS) {
                for (z in -LEECH_RANGE_BLOCKS..LEECH_RANGE_BLOCKS) {
                    if (x * x + y * y + z * z <= LEECH_RANGE_BLOCKS * LEECH_RANGE_BLOCKS) {
                        val blockPos = entity.blockPosition().offset(x, y, z)
                        val block = entity.level.getBlockState(blockPos).block
                        if (VITAE_PER_BLOCK.containsKey(block)) {
                            val blockVitae = VITAE_PER_BLOCK[block]!!
                            totalBlockVitae = totalBlockVitae + blockVitae
                            nearbyBlockToVitae[blockPos] = blockVitae
                            if (GRASS_BLOCK_VARIANTS.contains(block)) {
                                grassPositions.add(blockPos)
                            }
                        }
                    }
                }
            }
        }

        // Find all nearby entities that have vitae potential. Skip this if we already have enough vitae from blocks
        val leechableEntities = getLeechableEntities(entity)

        val availableHealth = leechableEntities.sumOf { it.health.toDouble() / 2 }
        val maxHealth = leechableEntities.sumOf { it.maxHealth.toDouble() / 2 }

        return CastEnvironment.withVitae(
            totalBlockVitae + availableHealth * VITAE_PER_HP,
            totalBlockVitae + maxHealth * VITAE_PER_HP,
            LeechContext(nearbyBlockToVitae, grassPositions, leechableEntities)
        )
    }

    private fun getLeechableEntities(entity: Entity): List<LivingEntity> {
        return entity.level.getEntitiesOfClass(LivingEntity::class.java, entity.boundingBox.inflate(LEECH_RANGE_BLOCKS.toDouble())) {
            // Only leech leechable entities
            if (it.type.registryName !in ModCommonConfiguration.leechPowerSourceEntities) {
                return@getEntitiesOfClass false
            }
            // Don't leech ourselves
            if (it == entity) {
                return@getEntitiesOfClass false
            }
            // Don't leech entities that are currently being hurt. This avoids players getting "free" leeches without doing damage
            if (it.hurtTime > 0) {
                return@getEntitiesOfClass false
            }
            // Don't leech entities through walls
            if (!it.canSee(entity)) {
                return@getEntitiesOfClass false
            }
            true
        }
    }

    private fun distributeDamageOver(entities: List<LivingEntity>, damage: Double): Map<LivingEntity, Double> {
        val entityToDamage = mutableMapOf<LivingEntity, Double>()
        var currentEntityToDamage = 0
        var damageToDistribute = damage
        // Safety measure to avoid infinite loops
        var didDamageInLastIteration = false
        while (damageToDistribute > 0) {
            val affectedEntity = entities[currentEntityToDamage]
            entityToDamage.compute(affectedEntity) { _, mapDamage ->
                val currentDamage = mapDamage ?: 0.0
                val damageToAdd = min(min(damageToDistribute, 1.0), affectedEntity.health / 2.0 - currentDamage)
                if (damageToAdd > 0) {
                    didDamageInLastIteration = true
                }
                damageToDistribute = damageToDistribute - damageToAdd
                currentDamage + damageToAdd
            }
            currentEntityToDamage = (currentEntityToDamage + 1) % entities.size
            if (currentEntityToDamage == 0 && !didDamageInLastIteration) {
                LOG.error("Attempted to leech more damage than possible? Remaining damage: $damageToDistribute")
                break
            }
        }
        return entityToDamage
    }

    override fun getSourceSpecificCost(vitae: Double): Number {
        return vitae.round(1)
    }

    class LeechContext(val nearbyBlockToVitae: Map<BlockPos, Double>, val grassPositions: Set<BlockPos>, val leechableEntities: List<LivingEntity>)

    companion object {
        private val LOG = LogManager.getLogger()

        // The number of units each hp supplies
        private const val VITAE_PER_HP = 4.0
        private const val LEECH_RANGE_BLOCKS = 12

        private val GRASS_BLOCK_VARIANTS = setOf(
            Blocks.GRASS_BLOCK, Blocks.PODZOL, Blocks.MYCELIUM
        )

        private val VITAE_PER_BLOCK = mapOf(
            Blocks.ACACIA_SAPLING to 4.0,
            Blocks.BIRCH_SAPLING to 4.0,
            Blocks.JUNGLE_SAPLING to 4.0,
            Blocks.DARK_OAK_SAPLING to 4.0,
            Blocks.OAK_SAPLING to 4.0,
            Blocks.SPRUCE_SAPLING to 4.0,
            ModBlocks.GRAVEWOOD_SAPLING to 4.0,
            ModBlocks.MANGROVE_SAPLING to 4.0,
            ModBlocks.SACRED_MANGROVE_SAPLING to 40.0,
            Blocks.ACACIA_LEAVES to 1.0,
            Blocks.BIRCH_LEAVES to 1.0,
            Blocks.JUNGLE_LEAVES to 1.0,
            Blocks.DARK_OAK_LEAVES to 1.0,
            Blocks.OAK_LEAVES to 1.0,
            Blocks.SPRUCE_LEAVES to 1.0,
            ModBlocks.GRAVEWOOD_LEAVES to 1.0,
            ModBlocks.MANGROVE_LEAVES to 1.0,
            ModBlocks.SACRED_MANGROVE_LEAVES to 10.0,
            Blocks.GRASS to 1.0,
            Blocks.FERN to 1.0,
            Blocks.DANDELION to 2.0,
            Blocks.POPPY to 2.0,
            Blocks.BLUE_ORCHID to 2.0,
            Blocks.ALLIUM to 2.0,
            Blocks.AZURE_BLUET to 8.0,
            Blocks.RED_TULIP to 2.0,
            Blocks.ORANGE_TULIP to 2.0,
            Blocks.WHITE_TULIP to 2.0,
            Blocks.PINK_TULIP to 2.0,
            Blocks.OXEYE_DAISY to 2.0,
            Blocks.CORNFLOWER to 2.0,
            Blocks.SUNFLOWER to 2.0,
            Blocks.LILAC to 2.0,
            Blocks.ROSE_BUSH to 2.0,
            Blocks.PEONY to 2.0,
            Blocks.TALL_GRASS to 1.0,
            Blocks.LARGE_FERN to 1.0,
            Blocks.LILY_OF_THE_VALLEY to 8.0,
            Blocks.LILY_PAD to 1.0,
            Blocks.VINE to 1.0,
            Blocks.MELON to 8.0,
            Blocks.PUMPKIN to 8.0,
            Blocks.CARROTS to 4.0,
            Blocks.POTATOES to 4.0,
            Blocks.BEETROOTS to 4.0,
            Blocks.SWEET_BERRY_BUSH to 4.0,
            Blocks.GRASS_BLOCK to 1.0,
            Blocks.PODZOL to 1.0,
            Blocks.MYCELIUM to 1.0
        )

        val LEECH_ENTITIES = listOf(
            EntityType.BAT,
            EntityType.BEE,
            EntityType.BLAZE,
            EntityType.CAT,
            EntityType.CAVE_SPIDER,
            EntityType.CHICKEN,
            EntityType.COD,
            EntityType.COW,
            EntityType.CREEPER,
            EntityType.DOLPHIN,
            EntityType.DONKEY,
            EntityType.DROWNED,
            EntityType.ENDERMITE,
            EntityType.FOX,
            EntityType.HORSE,
            EntityType.HUSK,
            EntityType.MAGMA_CUBE,
            EntityType.MOOSHROOM,
            EntityType.OCELOT,
            EntityType.PANDA,
            EntityType.PARROT,
            EntityType.PHANTOM,
            EntityType.PIG,
            EntityType.POLAR_BEAR,
            EntityType.PUFFERFISH,
            EntityType.RABBIT,
            EntityType.SALMON,
            EntityType.SHEEP,
            EntityType.SILVERFISH,
            EntityType.SKELETON,
            EntityType.SKELETON_HORSE,
            EntityType.SLIME,
            EntityType.SNOW_GOLEM,
            EntityType.SPIDER,
            EntityType.SQUID,
            EntityType.STRAY,
            EntityType.TROPICAL_FISH,
            EntityType.TURTLE,
            EntityType.VEX,
            EntityType.WOLF,
            EntityType.ZOMBIE,
            EntityType.ZOMBIE_HORSE,
            ModEntities.ENCHANTED_SKELETON,
            ModEntities.ENCHANTED_FROG
        ).map { it.registryName!!.toString() }
    }
}