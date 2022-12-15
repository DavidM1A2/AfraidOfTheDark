package com.davidm1a2.afraidofthedark.common.entity.enaria.fight.events

import com.davidm1a2.afraidofthedark.common.constants.ModItems
import com.davidm1a2.afraidofthedark.common.constants.ModSpellDeliveryMethods
import com.davidm1a2.afraidofthedark.common.constants.ModSpellEffects
import com.davidm1a2.afraidofthedark.common.entity.enaria.fight.EnariaFight
import com.davidm1a2.afraidofthedark.common.spell.Spell
import com.davidm1a2.afraidofthedark.common.spell.SpellStage
import com.davidm1a2.afraidofthedark.common.spell.component.deliveryMethod.base.SpellDeliveryMethodInstance
import com.davidm1a2.afraidofthedark.common.spell.component.effect.base.SpellEffectInstance
import net.minecraft.block.Blocks
import net.minecraft.block.FlowerBlock
import net.minecraft.command.arguments.EntityAnchorArgument
import net.minecraft.entity.item.ItemEntity
import net.minecraft.item.ItemStack
import net.minecraft.nbt.CompoundNBT
import net.minecraft.util.math.vector.Vector3d
import java.awt.Color
import kotlin.random.Random

class GrassSkeletonFightEvent(fight: EnariaFight) : EnariaFightEvent(fight, EnariaFightEvents.GrassSkeleton) {
    private var ticksUntilEnd: Int = 0
    private var numberGrowSpellsLeft: Int = 0

    override fun start() {
        ticksUntilEnd = Random.nextInt(20 * MIN_EVENT_TIME_SEC, 20 * MAX_EVENT_TIME_SEC)
        numberGrowSpellsLeft = START_NUMBER_GROW_SPELLS

        val world = fight.enaria.level
        val cornerOne = relativeToAbsolutePosition(-30, -1, -3)
        val cornerTwo = relativeToAbsolutePosition(30, -1, 79)
        iterateOverRegion(cornerOne, cornerTwo) {
            if (world.isEmptyBlock(it)) {
                world.setBlockAndUpdate(it, Blocks.GRASS_BLOCK.defaultBlockState())
            }
        }
    }

    override fun tick() {
        ticksUntilEnd = ticksUntilEnd - 1

        // Every 5 ticks try to randomly cast a grow projectile spell
        if (ticksUntilEnd % 5 == 0 && numberGrowSpellsLeft > 0) {
            numberGrowSpellsLeft = numberGrowSpellsLeft - 1
            val hitPos = Vector3d.atCenterOf(relativeToAbsolutePosition(Random.nextInt(-30, 30), -1, Random.nextInt(-3, 79)))
            fight.enaria.lookAt(EntityAnchorArgument.Type.EYES, hitPos)
            GROW_SPELL.attemptToCast(fight.enaria)

            // Also spawn 4-12 enchanted skeleton bones
            fight.enaria.level.addFreshEntity(
                ItemEntity(
                    fight.enaria.level,
                    hitPos.x,
                    hitPos.y,
                    hitPos.z,
                    ItemStack(ModItems.ENCHANTED_SKELETON_BONE, POSSIBLE_ENCHANTED_SKELE_BONE_COUNTS.random())
                )
            )
        }

        if (ticksUntilEnd == 0) {
            clearArenaGrass()
        }
    }

    override fun forceStop() {
        ticksUntilEnd = 0
        clearArenaGrass()
    }

    private fun clearArenaGrass() {
        val world = fight.enaria.level
        iterateOverRegion(relativeToAbsolutePosition(-30, -1, -3), relativeToAbsolutePosition(30, 2, 79)) {
            val block = world.getBlockState(it).block
            if (VALID_BLOCKS_TO_REMOVE.contains(block) || block is FlowerBlock) {
                world.setBlock(it, Blocks.AIR.defaultBlockState(), 2 or 32)
            }
        }
    }

    override fun isOver(): Boolean {
        return ticksUntilEnd <= 0
    }

    override fun canBasicAttackDuringThis(): Boolean {
        return numberGrowSpellsLeft == 0
    }

    override fun serializeNBT(): CompoundNBT {
        val nbt = super.serializeNBT()

        nbt.putInt(NBT_TICKS_UNTIL_END, ticksUntilEnd)
        nbt.putInt(NBT_NUMBER_GROW_SPELLS_LEFT, numberGrowSpellsLeft)

        return nbt
    }

    override fun deserializeNBT(nbt: CompoundNBT) {
        super.deserializeNBT(nbt)

        ticksUntilEnd = nbt.getInt(NBT_TICKS_UNTIL_END)
        numberGrowSpellsLeft = nbt.getInt(NBT_NUMBER_GROW_SPELLS_LEFT)
    }

    companion object {
        private const val NBT_TICKS_UNTIL_END = "ticks_until_end"
        private const val NBT_NUMBER_GROW_SPELLS_LEFT = "number_grow_spells_left"

        private val GROW_SPELL = Spell().apply {
            name = "Enaria Grass Skeleton Event"
            spellStages.add(SpellStage().apply {
                deliveryInstance = SpellDeliveryMethodInstance(ModSpellDeliveryMethods.PROJECTILE).apply {
                    setDefaults()
                    ModSpellDeliveryMethods.PROJECTILE.setRange(this, 100.0)
                    ModSpellDeliveryMethods.PROJECTILE.setColor(this, Color(0, 200, 0))
                }
            })
            spellStages.add(SpellStage().apply {
                deliveryInstance = SpellDeliveryMethodInstance(ModSpellDeliveryMethods.AOE).apply {
                    setDefaults()
                    ModSpellDeliveryMethods.AOE.setRadius(this, 5.0)
                    ModSpellDeliveryMethods.AOE.setColor(this, Color.GREEN)
                }
                effects[0] = SpellEffectInstance(ModSpellEffects.GROW).apply { setDefaults() }
            })
        }

        private const val MIN_EVENT_TIME_SEC = 30
        private const val MAX_EVENT_TIME_SEC = 40
        private const val START_NUMBER_GROW_SPELLS = 20
        private val POSSIBLE_ENCHANTED_SKELE_BONE_COUNTS = listOf(4, 8, 12)
        private val VALID_BLOCKS_TO_REMOVE = setOf(
            Blocks.GRASS,
            Blocks.DIRT,
            Blocks.TALL_GRASS,
            Blocks.GRASS_BLOCK
        )
    }
}