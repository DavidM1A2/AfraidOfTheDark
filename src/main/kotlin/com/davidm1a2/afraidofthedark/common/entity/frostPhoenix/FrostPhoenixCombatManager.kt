package com.davidm1a2.afraidofthedark.common.entity.frostPhoenix

import com.davidm1a2.afraidofthedark.AfraidOfTheDark
import com.davidm1a2.afraidofthedark.common.constants.ModDamageSources
import com.davidm1a2.afraidofthedark.common.network.packets.animation.AnimationPacket
import net.minecraft.commands.arguments.EntityAnchorArgument
import net.minecraft.core.BlockPos
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.IntArrayTag
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.effect.MobEffects
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.phys.AABB
import net.minecraft.world.phys.Vec3
import net.minecraftforge.common.util.INBTSerializable
import org.apache.logging.log4j.LogManager

class FrostPhoenixCombatManager(private val phoenix: FrostPhoenixEntity) : INBTSerializable<CompoundTag> {
    private var stormTicksLeft = 0
    private var stormCenter = Vec3(0.0, 0.0, 0.0)
    private var blocksChangedToSnow = mutableSetOf<BlockPos>()

    fun shootFireballAtTarget() {
        val target = phoenix.target!!

        val shootPosition = phoenix.position()
            .add(phoenix.lookAngle.scale(phoenix.boundingBox.zsize))
            .add(0.0, phoenix.eyeHeight.toDouble(), 0.0)

        val targetPosition = target.position()
            .add(0.0, target.eyeHeight.toDouble(), 0.0)

        val direction = targetPosition.subtract(shootPosition).normalize()
        val projectile = FrostPhoenixProjectileEntity(phoenix, direction.x, direction.y, direction.z)
        projectile.setPos(shootPosition.x, shootPosition.y, shootPosition.z)
        phoenix.level.addFreshEntity(projectile)

        AfraidOfTheDark.packetHandler
            .sendToChunk(AnimationPacket(phoenix, "Attack"), phoenix.level.getChunkAt(phoenix.blockPosition()))

        // Play the shoot "fireball" sound effect
        phoenix.level.levelEvent(null, 1018, phoenix.blockPosition(), 0)
    }

    fun isStorming(): Boolean {
        return stormTicksLeft > 0
    }

    fun startStorming() {
        stormTicksLeft = MAX_STORM_TICKS

        stormCenter = phoenix.boundingBox.center
        phoenix.moveControl.setWantedPosition(stormCenter.x, stormCenter.y, stormCenter.z, phoenix.getAttributeValue(Attributes.MOVEMENT_SPEED))

        // Push away nearby entities
        val level = phoenix.level
        val knockbackBox = AABB.unitCubeFromLowerCorner(stormCenter.subtract(-0.5, -0.5, -0.5)).inflate(KNOCKBACK_RADIUS_BLOCKS.toDouble())
        val nearbyEntities = level.getEntitiesOfClass(LivingEntity::class.java, knockbackBox) {
            it != phoenix && it.position().distanceTo(stormCenter) < KNOCKBACK_RADIUS_BLOCKS
        }
        for (entity in nearbyEntities) {
            if (entity is Player && entity.isSpectator) {
                continue
            }
            val awayDir = entity.position().subtract(stormCenter).normalize()
            entity.push(awayDir.x * STORM_KNOCKBACK_STRENGTH, 1.4 * STORM_KNOCKBACK_STRENGTH, awayDir.z * STORM_KNOCKBACK_STRENGTH)
            if (entity is ServerPlayer) {
                entity.connection.send(ClientboundSetEntityMotionPacket(entity))
            }
        }

        // Encase in snow
        val blockPosition = BlockPos(stormCenter)
        for (x in -STORM_RADIUS_BLOCKS..STORM_RADIUS_BLOCKS) {
            // <= 4 to leave the top open
            for (y in -STORM_RADIUS_BLOCKS..3) {
                for (z in -STORM_RADIUS_BLOCKS..STORM_RADIUS_BLOCKS) {
                    val distance = x * x + y * y + z * z
                    if (distance < STORM_RADIUS_BLOCKS * STORM_RADIUS_BLOCKS) {
                        if (distance > (STORM_RADIUS_BLOCKS - 1.5) * (STORM_RADIUS_BLOCKS - 1.5)) {
                            val snowPos = blockPosition.offset(x, y, z)
                            val snowState = level.getBlockState(snowPos)
                            if (snowState.isAir || snowState.block == Blocks.SNOW) {
                                level.setBlockAndUpdate(snowPos, Blocks.SNOW_BLOCK.defaultBlockState())
                                blocksChangedToSnow.add(snowPos)
                            }
                        }
                    }
                }
            }
        }
    }

    fun tickStorming() {
        stormTicksLeft = stormTicksLeft - 1
        phoenix.heal(HEALTH_PER_TICK)
        // Target is null if it died or the player left the game
        phoenix.target?.let { phoenix.lookAt(EntityAnchorArgument.Anchor.EYES, it.getEyePosition(1.0f)) }

        if (stormTicksLeft % 20 == 0) {
            val nearbyLivingEntities = phoenix.level.getEntitiesOfClass(LivingEntity::class.java, phoenix.boundingBox.inflate(STORM_RADIUS_BLOCKS.toDouble())) {
                it != phoenix && it.distanceTo(phoenix) < STORM_RADIUS_BLOCKS
            }
            for (livingEntity in nearbyLivingEntities) {
                livingEntity.hurt(ModDamageSources.getFrostPhoenixStormDamage(phoenix), STORM_DAMAGE_PER_SECOND)
                livingEntity.addEffect(MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 100, 1))
            }
        }
    }

    fun stopStorming() {
        stormTicksLeft = 0
        // Remove snow sphere
        val level = phoenix.level
        for (snowPos in blocksChangedToSnow) {
            if (level.getBlockState(snowPos).block == Blocks.SNOW_BLOCK) {
                level.setBlockAndUpdate(snowPos, Blocks.AIR.defaultBlockState())
            }
        }
        blocksChangedToSnow.clear()
    }

    override fun serializeNBT(): CompoundTag {
        val nbt = CompoundTag()

        nbt.putInt("storm_ticks_left", stormTicksLeft)

        nbt.putDouble("storm_center_x", stormCenter.x)
        nbt.putDouble("storm_center_y", stormCenter.y)
        nbt.putDouble("storm_center_z", stormCenter.z)

        val changedBlocksNbt = IntArrayTag(blocksChangedToSnow.flatMap { sequenceOf(it.x, it.y, it.z) })
        nbt.put("changed_to_snow_positions", changedBlocksNbt)

        return nbt
    }

    override fun deserializeNBT(nbt: CompoundTag) {
        stormTicksLeft = nbt.getInt("storm_ticks_left")

        stormCenter = Vec3(
            nbt.getDouble("storm_center_x"),
            nbt.getDouble("storm_center_y"),
            nbt.getDouble("storm_center_z")
        )

        val changedBlocksNbt = nbt.getIntArray("changed_to_snow_positions")
        if (changedBlocksNbt.size % 3 != 0) {
            LOG.error("Blocks changed to snow positions array had size ${changedBlocksNbt.size} when it should've been a multiple of 3!")
        } else {
            blocksChangedToSnow.clear()
            for (i in changedBlocksNbt.indices step 3) {
                blocksChangedToSnow.add(BlockPos(changedBlocksNbt[i], changedBlocksNbt[i + 1], changedBlocksNbt[i + 2]))
            }
        }
    }

    companion object {
        private val LOG = LogManager.getLogger()

        // 30 seconds
        private const val MAX_STORM_TICKS = 20 * 30

        // 1/5 heart per tick, or 60 hearts over 30 seconds
        private const val HEALTH_PER_TICK = 0.2f

        internal const val STORM_RADIUS_BLOCKS = 13
        private const val KNOCKBACK_RADIUS_BLOCKS = STORM_RADIUS_BLOCKS + 5
        private const val STORM_KNOCKBACK_STRENGTH = 2.0
        private const val STORM_DAMAGE_PER_SECOND = 6f
    }
}