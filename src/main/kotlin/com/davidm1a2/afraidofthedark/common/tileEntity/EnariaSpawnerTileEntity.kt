package com.davidm1a2.afraidofthedark.common.tileEntity

import com.davidm1a2.afraidofthedark.common.capabilities.getResearch
import com.davidm1a2.afraidofthedark.common.constants.ModResearches
import com.davidm1a2.afraidofthedark.common.constants.ModTileEntities
import com.davidm1a2.afraidofthedark.common.entity.enaria.EnariaEntity
import com.davidm1a2.afraidofthedark.common.tileEntity.core.AOTDZoneTileEntity
import com.davidm1a2.afraidofthedark.common.utility.toRotation
import net.minecraft.core.BlockPos
import net.minecraft.nbt.CompoundTag
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.HorizontalDirectionalBlock
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.AABB

/**
 * Tile entity that spawns enaria in the forbidden city
 *
 * @constructor just sets the enaria spawner block
 */
class EnariaSpawnerTileEntity(blockPos: BlockPos, blockState: BlockState) : AOTDZoneTileEntity(ModTileEntities.ENARIA_SPAWNER, blockPos, blockState) {
    // Unfortunately world.getBlockState() only works right before the first tick, so lazily initialize this
    private val enariaFightStartTriggerBox: AABB by lazy {
        val direction = level!!.getBlockState(blockPos).getValue(HorizontalDirectionalBlock.FACING)
        val rotation = direction.toRotation()
        val enariaFightStartTriggerBoxNegCorner = BlockPos(-11, -2, -2).rotate(rotation).offset(blockPos)
        val enariaFightStartTriggerBoxPosCorner = BlockPos(11, 11, 20).rotate(rotation).offset(blockPos)
        AABB(enariaFightStartTriggerBoxNegCorner, enariaFightStartTriggerBoxPosCorner)
    }
    override val zone: AABB by lazy {
        val direction = level!!.getBlockState(blockPos).getValue(HorizontalDirectionalBlock.FACING)
        val rotation = direction.toRotation()
        val enariaArenaBoxNegCorner = BlockPos(-30, -2, -3).rotate(rotation).offset(blockPos)
        val enariaArenaBoxPosCorner = BlockPos(30, 12, 79).rotate(rotation).offset(blockPos)
        AABB(enariaArenaBoxNegCorner, enariaArenaBoxPosCorner)
    }

    private var fightCooldownTime: Long = 0L
    private var fightIsActive = false

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

    override fun load(compound: CompoundTag) {
        super.load(compound)
        fightIsActive = compound.getBoolean(NBT_FIGHT_IS_ACTIVE)
        fightCooldownTime = compound.getLong(NBT_POST_FIGHT_COOLDOWN)
    }

    override fun save(compound: CompoundTag): CompoundTag {
        super.save(compound)
        compound.putBoolean(NBT_FIGHT_IS_ACTIVE, fightIsActive)
        compound.putLong(NBT_POST_FIGHT_COOLDOWN, fightCooldownTime)
        return compound
    }

    companion object {
        private const val NBT_FIGHT_IS_ACTIVE = "fight_is_active"
        private const val NBT_POST_FIGHT_COOLDOWN = "post_fight_cooldown"

        private const val POST_FIGHT_COOLDOWN_TICKS = 20 * 60 * 1 // 1 minute before she can respawn

        fun serverTick(level: Level, blockPos: BlockPos, blockState: BlockState, blockEntity: EnariaSpawnerTileEntity) {
            AOTDZoneTileEntity.serverTick(level, blockPos, blockState, blockEntity)

            // If no fight is active...
            if (!blockEntity.fightIsActive) {
                // And the fight is not on cooldown...
                if (blockEntity.ticksExisted >= blockEntity.fightCooldownTime) {
                    // And a player that qualifies for the fight is nearby...
                    val playersEligibleToFight = level.getEntitiesOfClass(Player::class.java, blockEntity.enariaFightStartTriggerBox) {
                        it.getResearch().canResearch(ModResearches.ARCH_SORCERESS) || it.getResearch().canResearch(ModResearches.INFERNO)
                    }
                    if (playersEligibleToFight.isNotEmpty()) {
                        // Start the fight
                        blockEntity.startFight()
                    }
                }
            }
        }
    }
}