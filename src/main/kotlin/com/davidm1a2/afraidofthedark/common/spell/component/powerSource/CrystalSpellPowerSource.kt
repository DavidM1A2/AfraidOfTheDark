package com.davidm1a2.afraidofthedark.common.spell.component.powerSource

import com.davidm1a2.afraidofthedark.common.constants.Constants
import com.davidm1a2.afraidofthedark.common.constants.ModResearches
import com.davidm1a2.afraidofthedark.common.constants.ModTileEntities
import com.davidm1a2.afraidofthedark.common.spell.Spell
import com.davidm1a2.afraidofthedark.common.spell.component.powerSource.base.AOTDSpellPowerSource
import com.davidm1a2.afraidofthedark.common.spell.component.powerSource.base.CastEnvironment
import com.davidm1a2.afraidofthedark.common.spell.component.powerSource.base.SpellCastResult
import com.davidm1a2.afraidofthedark.common.tileEntity.MagicCrystalTileEntity
import com.davidm1a2.afraidofthedark.common.utility.round
import net.minecraft.entity.Entity
import net.minecraft.util.ResourceLocation
import net.minecraft.util.text.TranslationTextComponent
import net.minecraft.world.World

class CrystalSpellPowerSource : AOTDSpellPowerSource<CrystalSpellPowerSource.CrystalContext>(ResourceLocation(Constants.MOD_ID, "crystal"), ModResearches.ADVANCED_MAGIC) {
    override fun cast(entity: Entity, spell: Spell, environment: CastEnvironment<CrystalContext>): SpellCastResult {
        val x = entity.x
        val y = entity.y
        val z = entity.z

        if (environment.vitaeAvailable < spell.getCost()) {
            return SpellCastResult.failure(TranslationTextComponent("${getUnlocalizedBaseName()}.not_enough_power"))
        }

        val nearestCrystals = environment.context.nearbyCrystals.sortedWith { first, second ->
            val firstDistance = first.blockPos.distSqr(x, y, z, true)
            val secondDistance = second.blockPos.distSqr(x, y, z, true)
            firstDistance.compareTo(secondDistance)
        }

        var vitaeRemaining = spell.getCost()
        for (crystal in nearestCrystals) {
            val vitaeToConsume = crystal.getVitae().coerceAtMost(vitaeRemaining)
            crystal.consumeVitae(vitaeToConsume)
            vitaeRemaining = vitaeRemaining - vitaeToConsume
            if (vitaeRemaining <= 0) {
                break
            }
        }

        return SpellCastResult.success()
    }

    override fun computeCastEnvironment(entity: Entity): CastEnvironment<CrystalContext> {
        val x = entity.x
        val y = entity.y
        val z = entity.z

        val nearbyCrystals = getNearbyCrystals(entity.level, x, y, z)

        return CastEnvironment.withVitae(nearbyCrystals.sumOf { it.getVitae() }, nearbyCrystals.sumOf { it.getMaxVitae() }, CrystalContext(nearbyCrystals))
    }

    private fun getNearbyCrystals(world: World, x: Double, y: Double, z: Double): Sequence<MagicCrystalTileEntity> {
        return world
            .blockEntityList
            .asSequence()
            .filter { it.type == ModTileEntities.MAGIC_CRYSTAL }
            .map { it as MagicCrystalTileEntity }
            .filter { it.isMaster() }
            .filter { it.blockPos.distSqr(x, y, z, false) < MAX_CRYSTAL_RANGE_SQUARED }
    }

    override fun getSourceSpecificCost(vitae: Double): Double {
        return vitae.round(1)
    }

    class CrystalContext(val nearbyCrystals: Sequence<MagicCrystalTileEntity>)

    companion object {
        private const val MAX_CRYSTAL_RANGE_SQUARED = 16 * 16
    }
}