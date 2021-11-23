package com.davidm1a2.afraidofthedark.common.entity.enaria.fight.events

import com.davidm1a2.afraidofthedark.common.constants.ModSpellDeliveryMethods
import com.davidm1a2.afraidofthedark.common.constants.ModSpellEffects
import com.davidm1a2.afraidofthedark.common.constants.ModSpellPowerSources
import com.davidm1a2.afraidofthedark.common.entity.enaria.fight.EnariaFight
import com.davidm1a2.afraidofthedark.common.entity.splinterDrone.SplinterDroneEntity
import com.davidm1a2.afraidofthedark.common.spell.Spell
import com.davidm1a2.afraidofthedark.common.spell.SpellStage
import com.davidm1a2.afraidofthedark.common.spell.component.deliveryMethod.base.SpellDeliveryMethodInstance
import com.davidm1a2.afraidofthedark.common.spell.component.effect.base.SpellEffectInstance
import com.davidm1a2.afraidofthedark.common.spell.component.powerSource.base.SpellPowerSourceInstance
import net.minecraft.block.Blocks
import net.minecraft.nbt.CompoundNBT
import net.minecraft.nbt.ListNBT
import net.minecraft.nbt.NBTUtil
import net.minecraft.util.DamageSource
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.vector.Vector3d
import net.minecraft.world.server.ServerWorld
import net.minecraftforge.common.util.Constants
import java.util.UUID
import kotlin.random.Random

class SummonSentinelsFightEvent(fight: EnariaFight) : EnariaFightEvent(fight, EnariaFightEvents.SummonSentinels) {
    private val splinterDroneIds = mutableListOf<UUID>()
    private var enariaStartingHp: Float = 0f
    private var enariaCastPosition: BlockPos = BlockPos.ZERO

    override fun start() {
        // Cast AOE Freeze by Enaria first
        FREEZE_SPELL.attemptToCast(fight.enaria)
        enariaCastPosition = fight.enaria.blockPosition()

        // Summon 8 sentinels around the ice
        val particlePositions = mutableListOf<Vector3d>()
        for (x in -1..1) {
            for (z in -1..1) {
                if (x != 0 || z != 0) {
                    val splinterDrone = SplinterDroneEntity(fight.enaria.level)
                    val position = fight.enaria.blockPosition().offset(x * 5, 0, z * 5)
                    particlePositions.add(Vector3d(position.x + Random.nextDouble(), position.y + Random.nextDouble(), position.z + Random.nextDouble()))
                    splinterDrone.setPos(position.x + 0.5, position.y.toDouble(), position.z + 0.5)
                    fight.enaria.level.addFreshEntity(splinterDrone)
                    splinterDroneIds.add(splinterDrone.uuid)
                }
            }
        }

        spawnEventParticles(particlePositions)

        // Remember Enaria's HP
        enariaStartingHp = fight.enaria.health
    }

    override fun tick() {
    }

    override fun forceStop() {
        killSplinterDronesAndClearIce()
    }

    override fun isOver(): Boolean {
        val healthDifference = enariaStartingHp - fight.enaria.health
        // If enaria took 5 damage the event is over
        if (healthDifference > 5) {
            killSplinterDronesAndClearIce()
            return true
        }
        return false
    }

    private fun killSplinterDronesAndClearIce() {
        // Kill any remaining splinter drones
        val world = fight.enaria.level
        splinterDroneIds.forEach {
            // If we use entity.onKillCommand() they disappear without any animation, if we apply 99999 true damage they play the animation which looks better
            (world as ServerWorld).getEntity(it)?.hurt(DamageSource.OUT_OF_WORLD, 99999f)
        }

        // Clear out the ice
        for (x in -3..3) {
            for (y in -2..4) {
                for (z in -3..3) {
                    val position = enariaCastPosition.offset(x, y, z)
                    if (world.getBlockState(position).block == Blocks.ICE) {
                        world.setBlockAndUpdate(position, Blocks.CAVE_AIR.defaultBlockState())
                    }
                }
            }
        }
    }

    override fun canBasicAttackDuringThis(): Boolean {
        return false
    }

    override fun serializeNBT(): CompoundNBT {
        val nbt = super.serializeNBT()

        nbt.putFloat(NBT_ENARIA_STARTING_HP, enariaStartingHp)
        nbt.put(NBT_ENARIA_CAST_POSITION, NBTUtil.writeBlockPos(enariaCastPosition))

        val splinterDroneIdNbts = ListNBT()
        splinterDroneIds.forEach {
            splinterDroneIdNbts.add(NBTUtil.createUUID(it))
        }
        nbt.put(NBT_SPLINTER_DRONE_IDS, splinterDroneIdNbts)

        return nbt
    }

    override fun deserializeNBT(nbt: CompoundNBT) {
        super.deserializeNBT(nbt)

        enariaStartingHp = nbt.getFloat(NBT_ENARIA_STARTING_HP)
        enariaCastPosition = NBTUtil.readBlockPos(nbt.getCompound(NBT_ENARIA_CAST_POSITION))

        val splinterDroneIdNbts = nbt.getList(NBT_SPLINTER_DRONE_IDS, Constants.NBT.TAG_INT_ARRAY)
        splinterDroneIds.clear()
        splinterDroneIdNbts.forEach {
            splinterDroneIds.add(NBTUtil.loadUUID(it))
        }
    }

    companion object {
        private const val NBT_ENARIA_STARTING_HP = "enaria_starting_hp"
        private const val NBT_ENARIA_CAST_POSITION = "enaria_cast_position"
        private const val NBT_SPLINTER_DRONE_IDS = "splinter_drone_ids"

        private val FREEZE_SPELL = Spell().apply {
            name = "Enaria Sentinels Fight Event"
            powerSource = SpellPowerSourceInstance(ModSpellPowerSources.CREATIVE).apply { setDefaults() }
            spellStages.add(SpellStage().apply {
                deliveryInstance = SpellDeliveryMethodInstance(ModSpellDeliveryMethods.AOE).apply {
                    setDefaults()
                    ModSpellDeliveryMethods.AOE.setRadius(this, 3.0)
                }
                effects[0] = SpellEffectInstance(ModSpellEffects.FREEZE).apply { setDefaults() }
            })
        }
    }
}