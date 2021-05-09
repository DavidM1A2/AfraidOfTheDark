package com.davidm1a2.afraidofthedark.common.spell.component

import com.davidm1a2.afraidofthedark.common.spell.Spell
import com.davidm1a2.afraidofthedark.common.spell.SpellStage
import net.minecraft.entity.Entity
import net.minecraft.nbt.CompoundNBT
import net.minecraft.nbt.NBTUtil
import net.minecraft.util.ResourceLocation
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World
import net.minecraft.world.dimension.DimensionType
import net.minecraft.world.server.ServerWorld
import net.minecraftforge.fml.server.ServerLifecycleHooks
import java.util.*

/**
 * Class representing a spell in the transition state. This object is immutable
 *
 * @property spell The spell that is transitioning
 * @property stageIndex The index of the spell stage that is transitioning
 * @property lazyWorld A lazy initialized version of world. Used in deserialization to postpone the computation of the world object
 * @property world The world that the transition is happening in
 * @property position The exact double vector position the transition occurred
 * @property blockPosition A rounded for of getPosition() that estimates the block the transition occurred in
 * @property direction A normalized vector pointing in the direction the transition was in
 * @property casterEntityId The entity that cast the spell originally
 * @property entityId The entity being delivered to, can be null
 * @property deliveryEntityId The entity delivering the spell, can be null
 */
class DeliveryTransitionState {
    val spell: Spell
    val stageIndex: Int
    private val lazyWorld: Lazy<ServerWorld>
    val world: ServerWorld
        get() = lazyWorld.value
    val position: Vec3d
    val blockPosition: BlockPos
    val direction: Vec3d
    private val casterEntityId: UUID?
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
     * @param casterEntity   The entity that cast the spell initially
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
        casterEntity: Entity?,
        entity: Entity?,
        deliveryEntity: Entity?
    ) {
        this.spell = spell
        this.stageIndex = stageIndex
        // This should only be created server side, so we can cast safely
        this.lazyWorld = lazyOf(world as ServerWorld)
        this.position = position
        blockPosition = blockPos
        this.direction = direction
        casterEntityId = casterEntity?.uniqueID
        entityId = entity?.uniqueID
        deliveryEntityId = deliveryEntity?.uniqueID
    }

    /**
     * Initializes the state from NBT
     *
     * @param nbt The NBT containing the delivery state
     */
    constructor(nbt: CompoundNBT) {
        spell = Spell(nbt.getCompound(NBT_SPELL))
        stageIndex = nbt.getInt(NBT_STAGE_INDEX)
        lazyWorld = lazy {
            // This call will fail during deserialization, but is valid before first use, so lazily initialize it
            ServerLifecycleHooks.getCurrentServer().getWorld(DimensionType.byName(ResourceLocation(nbt.getString(NBT_WORLD)))!!)
        }
        position = Vec3d(
            nbt.getDouble(NBT_POSITION + "_x"),
            nbt.getDouble(NBT_POSITION + "_y"),
            nbt.getDouble(NBT_POSITION + "_z")
        )
        blockPosition = NBTUtil.readBlockPos(nbt.getCompound(NBT_BLOCK_POSITION))
        direction = Vec3d(
            nbt.getDouble(NBT_DIRECTION + "_x"),
            nbt.getDouble(NBT_DIRECTION + "_y"),
            nbt.getDouble(NBT_DIRECTION + "_z")
        )
        casterEntityId = if (nbt.contains(NBT_CASTER_ENTITY_ID)) {
            NBTUtil.readUniqueId(nbt.getCompound(NBT_CASTER_ENTITY_ID))
        } else {
            null
        }
        entityId = if (nbt.contains(NBT_ENTITY_ID)) {
            NBTUtil.readUniqueId(nbt.getCompound(NBT_ENTITY_ID))
        } else {
            null
        }
        deliveryEntityId = if (nbt.contains(NBT_DELIVERY_ENTITY_ID)) {
            NBTUtil.readUniqueId(nbt.getCompound(NBT_DELIVERY_ENTITY_ID))
        } else {
            null
        }
    }

    /**
     * @return The transition state serialized as NBT
     */
    fun writeToNbt(): CompoundNBT {
        val nbt = CompoundNBT()
        nbt.put(NBT_SPELL, spell.serializeNBT())
        nbt.putInt(NBT_STAGE_INDEX, stageIndex)
        nbt.putString(NBT_WORLD, DimensionType.getKey(world.dimension.type).toString())
        nbt.putDouble(NBT_POSITION + "_x", position.x)
        nbt.putDouble(NBT_POSITION + "_y", position.y)
        nbt.putDouble(NBT_POSITION + "_z", position.z)
        nbt.put(NBT_BLOCK_POSITION, NBTUtil.writeBlockPos(blockPosition))
        nbt.putDouble(NBT_DIRECTION + "_x", direction.x)
        nbt.putDouble(NBT_DIRECTION + "_y", direction.y)
        nbt.putDouble(NBT_DIRECTION + "_z", direction.z)
        if (casterEntityId != null) {
            nbt.put(NBT_CASTER_ENTITY_ID, NBTUtil.writeUniqueId(casterEntityId))
        }
        if (entityId != null) {
            nbt.put(NBT_ENTITY_ID, NBTUtil.writeUniqueId(entityId))
        }
        if (deliveryEntityId != null) {
            nbt.put(NBT_DELIVERY_ENTITY_ID, NBTUtil.writeUniqueId(deliveryEntityId))
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
     * @return The entity that cast this spell originally
     */
    fun getCasterEntity(): Entity? {
        // If the entity is non null we get the entity in the world, otherwise we return null
        return this.casterEntityId?.let { world.getEntityByUuid(this.casterEntityId) }
    }

    /**
     * @return The entity that this state was transitioning through
     */
    fun getEntity(): Entity? {
        // If the entity is non null we get the entity in the world, otherwise we return null
        return this.entityId?.let { world.getEntityByUuid(it) }
    }

    /**
     * @return The entity that performed the delivery and transition
     */
    fun getDeliveryEntity(): Entity? {
        // If the entity is non null we get the entity in the world, otherwise we return null
        return deliveryEntityId?.let { world.getEntityByUuid(it) }
    }

    companion object {
        // Constants used for NBT serialization / deserialization
        private const val NBT_SPELL = "spell"
        private const val NBT_STAGE_INDEX = "stage_index"
        private const val NBT_WORLD = "world"
        private const val NBT_POSITION = "position"
        private const val NBT_BLOCK_POSITION = "block_position"
        private const val NBT_DIRECTION = "direction"
        private const val NBT_CASTER_ENTITY_ID = "caster_entity_id"
        private const val NBT_ENTITY_ID = "entity_id"
        private const val NBT_DELIVERY_ENTITY_ID = "delivery_entity_id"
    }
}