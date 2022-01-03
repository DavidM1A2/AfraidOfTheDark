package com.davidm1a2.afraidofthedark.common.spell.component.powerSource

import com.davidm1a2.afraidofthedark.common.constants.Constants
import com.davidm1a2.afraidofthedark.common.constants.ModResearches
import com.davidm1a2.afraidofthedark.common.constants.ModTileEntities
import com.davidm1a2.afraidofthedark.common.spell.Spell
import com.davidm1a2.afraidofthedark.common.spell.component.powerSource.base.AOTDSpellPowerSource
import com.davidm1a2.afraidofthedark.common.tileEntity.MagicCrystalTileEntity
import com.davidm1a2.afraidofthedark.common.utility.round
import net.minecraft.entity.Entity
import net.minecraft.util.ResourceLocation
import net.minecraft.world.World

class CrystalSpellPowerSource : AOTDSpellPowerSource(ResourceLocation(Constants.MOD_ID, "crystal"), ModResearches.ADVANCED_MAGIC) {
    override fun canCast(entity: Entity, spell: Spell): Boolean {
        val totalNearbyVitae = getNearbyCrystals(entity.level, entity.x, entity.y, entity.z).sumOf { it.getVitae() }
        return totalNearbyVitae >= spell.getCost()
    }

    override fun consumePowerToCast(entity: Entity, spell: Spell) {
        val x = entity.x
        val y = entity.y
        val z = entity.z
        var vitaeRemaining = spell.getCost()
        val nearestCrystals = getNearbyCrystals(entity.level, x, y, z)
            .sortedWith { first, second ->
                val firstDistance = first.blockPos.distSqr(x, y, z, true)
                val secondDistance = second.blockPos.distSqr(x, y, z, true)
                firstDistance.compareTo(secondDistance)
            }

        for (crystal in nearestCrystals) {
            val vitaeToConsume = crystal.getVitae().coerceAtMost(vitaeRemaining)
            crystal.consumeVitae(vitaeToConsume)
            vitaeRemaining = vitaeRemaining - vitaeToConsume
            if (vitaeRemaining <= 0) {
                break
            }
        }
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

    override fun getSourceSpecificCost(rawCost: Double): Double {
        return rawCost.round(1)
    }

    companion object {
        private const val MAX_CRYSTAL_RANGE_SQUARED = 16 * 16
    }
}