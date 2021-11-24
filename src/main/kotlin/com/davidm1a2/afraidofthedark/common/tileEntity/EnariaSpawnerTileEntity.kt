package com.davidm1a2.afraidofthedark.common.tileEntity

import com.davidm1a2.afraidofthedark.common.capabilities.getResearch
import com.davidm1a2.afraidofthedark.common.constants.ModResearches
import com.davidm1a2.afraidofthedark.common.constants.ModTileEntities
import com.davidm1a2.afraidofthedark.common.entity.enaria.EnariaEntity
import com.davidm1a2.afraidofthedark.common.tileEntity.core.AOTDZoneTileEntity
import com.davidm1a2.afraidofthedark.common.utility.toRotation
import net.minecraft.block.BlockState
import net.minecraft.block.HorizontalFaceBlock
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.nbt.CompoundNBT
import net.minecraft.util.math.AxisAlignedBB
import net.minecraft.util.math.BlockPos

/**
 * Tile entity that spawns enaria in the forbidden city
 *
 * @constructor just sets the enaria spawner block
 */
class EnariaSpawnerTileEntity : AOTDZoneTileEntity(ModTileEntities.ENARIA_SPAWNER) {
    // Unfortunately world.getBlockState() only works right before the first tick, so lazily initialize this
    private val enariaFightStartTriggerBox: AxisAlignedBB by lazy {
        val direction = level!!.getBlockState(blockPos).getValue(HorizontalFaceBlock.FACING)
        val rotation = direction.toRotation()
        val enariaFightStartTriggerBoxNegCorner = BlockPos(-11, -2, -2).rotate(rotation).offset(blockPos)
        val enariaFightStartTriggerBoxPosCorner = BlockPos(11, 11, 20).rotate(rotation).offset(blockPos)
        AxisAlignedBB(enariaFightStartTriggerBoxNegCorner, enariaFightStartTriggerBoxPosCorner)
    }
    override val zone: AxisAlignedBB by lazy {
        val direction = level!!.getBlockState(blockPos).getValue(HorizontalFaceBlock.FACING)
        val rotation = direction.toRotation()
        val enariaArenaBoxNegCorner = BlockPos(-30, -2, -3).rotate(rotation).offset(blockPos)
        val enariaArenaBoxPosCorner = BlockPos(30, 12, 79).rotate(rotation).offset(blockPos)
        AxisAlignedBB(enariaArenaBoxNegCorner, enariaArenaBoxPosCorner)
    }

    private var fightCooldownTime: Long = 0L
    private var fightIsActive = false

    override fun tick() {
        super.tick()
        // Only process server side
        if (!level!!.isClientSide) {
            // If no fight is active...
            if (!fightIsActive) {
                // And the fight is not on cooldown...
                if (ticksExisted >= this.fightCooldownTime) {
                    // And a player that qualifies for the fight is nearby...
                    val playersEligibleToFight = level!!.getEntitiesOfClass(PlayerEntity::class.java, enariaFightStartTriggerBox) {
                        it.getResearch().canResearch(ModResearches.ARCH_SORCERESS) || it.getResearch().canResearch(ModResearches.INFERNO)
                    }
                    if (playersEligibleToFight.isNotEmpty()) {
                        // Start the fight
                        startFight()
                    }
                }
            }
        }
    }

    private fun startFight() {
        fightIsActive = true
        summonEnaria()
    }

    fun endFight() {
        fightIsActive = false
        fightCooldownTime = ticksExisted + POST_FIGHT_COOLDOWN_TICKS
    }

    private fun summonEnaria() {
        val enaria = EnariaEntity(level!!, blockPos)
        enaria.setPos(blockPos.x + 0.5, blockPos.y + 7.0, blockPos.z + 0.5)
        level!!.addFreshEntity(enaria)
    }

    override fun load(blockState: BlockState, compound: CompoundNBT) {
        super.load(blockState, compound)
        fightIsActive = compound.getBoolean(NBT_FIGHT_IS_ACTIVE)
        fightCooldownTime = compound.getLong(NBT_POST_FIGHT_COOLDOWN)
    }

    override fun save(compound: CompoundNBT): CompoundNBT {
        super.save(compound)
        compound.putBoolean(NBT_FIGHT_IS_ACTIVE, fightIsActive)
        compound.putLong(NBT_POST_FIGHT_COOLDOWN, fightCooldownTime)
        return compound
    }

    companion object {
        private const val NBT_FIGHT_IS_ACTIVE = "fight_is_active"
        private const val NBT_POST_FIGHT_COOLDOWN = "post_fight_cooldown"

        private const val POST_FIGHT_COOLDOWN_TICKS = 20 * 60 * 1 // 1 minute before she can respawn
    }
}