package com.davidm1a2.afraidofthedark.common.spell.component

import com.davidm1a2.afraidofthedark.common.spell.Spell
import com.davidm1a2.afraidofthedark.common.spell.SpellStage
import net.minecraft.entity.Entity
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.nbt.NBTUtil
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World
import net.minecraft.world.WorldServer
import net.minecraftforge.common.DimensionManager
import java.util.*

/**
 * Class representing a spell in the transition state. This object is immutable
 *
 * @property spell The spell that is transitioning
 * @property stageIndex The index of the spell stage that is transitioning
 * @property world The world that the transition is happening in
 * @property position The exact double vector position the transition occurred
 * @property blockPosition A rounded for of getPosition() that estimates the block the transition occurred in
 * @property direction A normalized vector pointing in the direction the transition was in
 * @property entityId The entity being delivered to, can be null
 * @property deliveryEntityId The entity delivering the spell, can be null
 */
class DeliveryTransitionState {
    val spell: Spell
    val stageIndex: Int
    val world: WorldServer
    val position: Vec3d
    val blockPosition: BlockPos
    val direction: Vec3d
    private val entityId: UUID?
    private val deliveryEntityId: UUID?

    /**
     * Constructor initializes all final fields
     *
     * @param spell          The spell being transitioned
     * @param stageIndex     The current stage that finished firing. The next stage to fire is stageIndex + 1
     * @param world          The world that the spell is transitioning in
     * @param position       The position the transition is happening at
     * @param direction      The direction the transition is toward
     * @param entity         The entity being transitioned through
     * @param deliveryEntity The entity that caused the transition
     */
    internal constructor(
        spell: Spell,
        stageIndex: Int,
        world: World,
        position: Vec3d,
        blockPos: BlockPos,
        direction: Vec3d,
        entity: Entity?,
        deliveryEntity: Entity?
    ) {
        this.spell = spell
        this.stageIndex = stageIndex
        // This should only be created server side, so we can cast safely
        this.world = world as WorldServer
        this.position = position
        blockPosition = blockPos
        this.direction = direction
        entityId = entity?.persistentID
        deliveryEntityId = deliveryEntity?.persistentID
    }

    /**
     * Initializes the state from NBT
     *
     * @param nbt The NBT containing the delivery state
     */
    constructor(nbt: NBTTagCompound) {
        spell = Spell(nbt.getCompoundTag(NBT_SPELL))
        stageIndex = nbt.getInteger(NBT_STAGE_INDEX)
        world = DimensionManager.getWorld(nbt.getInteger(NBT_WORLD_ID))
        position = Vec3d(
            nbt.getDouble(NBT_POSITION + "_x"),
            nbt.getDouble(NBT_POSITION + "_y"),
            nbt.getDouble(NBT_POSITION + "_z")
        )
        blockPosition = NBTUtil.getPosFromTag(nbt.getCompoundTag(NBT_BLOCK_POSITION))
        direction = Vec3d(
            nbt.getDouble(NBT_DIRECTION + "_x"),
            nbt.getDouble(NBT_DIRECTION + "_y"),
            nbt.getDouble(NBT_DIRECTION + "_z")
        )
        entityId = if (nbt.hasKey(NBT_ENTITY_ID)) {
            NBTUtil.getUUIDFromTag(nbt.getCompoundTag(NBT_ENTITY_ID))
        } else {
            null
        }
        deliveryEntityId = if (nbt.hasKey(NBT_DELIVERY_ENTITY_ID)) {
            NBTUtil.getUUIDFromTag(nbt.getCompoundTag(NBT_DELIVERY_ENTITY_ID))
        } else {
            null
        }
    }

    /**
     * @return The transition state serialized as NBT
     */
    fun writeToNbt(): NBTTagCompound {
        val nbt = NBTTagCompound()
        nbt.setTag(NBT_SPELL, spell.serializeNBT())
        nbt.setInteger(NBT_STAGE_INDEX, stageIndex)
        nbt.setInteger(NBT_WORLD_ID, world.provider.dimension)
        nbt.setDouble(NBT_POSITION + "_x", position.x)
        nbt.setDouble(NBT_POSITION + "_y", position.y)
        nbt.setDouble(NBT_POSITION + "_z", position.z)
        nbt.setTag(NBT_BLOCK_POSITION, NBTUtil.createPosTag(blockPosition))
        nbt.setDouble(NBT_DIRECTION + "_x", direction.x)
        nbt.setDouble(NBT_DIRECTION + "_y", direction.y)
        nbt.setDouble(NBT_DIRECTION + "_z", direction.z)
        if (entityId != null) {
            nbt.setTag(NBT_ENTITY_ID, NBTUtil.createUUIDTag(entityId))
        }
        if (deliveryEntityId != null) {
            nbt.setTag(NBT_DELIVERY_ENTITY_ID, NBTUtil.createUUIDTag(deliveryEntityId))
        }
        return nbt
    }

    /**
     * Utility method to get the current spell stage from the spell and index
     *
     * @return The current spell stage
     */
    fun getCurrentStage(): SpellStage {
        return this.spell.getStage(this.stageIndex)
            ?: throw IllegalArgumentException("Current spell state is null, that shouldn't be possible. Spell: \n${spell.name}")
    }

    /**
     * @return The entity that this state was transitioning through
     */
    fun getEntity(): Entity? {
        // If the entity is non null we get the entity in the world, otherwise we return null
        return this.entityId?.let { world.getEntityFromUuid(it) }
    }

    /**
     * @return The entity that performed the delivery and transition
     */
    fun getDeliveryEntity(): Entity? {
        // If the entity is non null we get the entity in the world, otherwise we return null
        return deliveryEntityId?.let { world.getEntityFromUuid(it) }
    }

    companion object {
        // Constants used for NBT serialization / deserialization
        private const val NBT_SPELL = "spell"
        private const val NBT_STAGE_INDEX = "stage_index"
        private const val NBT_WORLD_ID = "world_id"
        private const val NBT_POSITION = "position"
        private const val NBT_BLOCK_POSITION = "block_position"
        private const val NBT_DIRECTION = "direction"
        private const val NBT_ENTITY_ID = "entity_id"
        private const val NBT_DELIVERY_ENTITY_ID = "delivery_entity_id"
    }
}