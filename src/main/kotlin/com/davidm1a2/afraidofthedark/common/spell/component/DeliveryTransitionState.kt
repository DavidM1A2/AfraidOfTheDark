package com.davidm1a2.afraidofthedark.common.spell.component

import com.davidm1a2.afraidofthedark.common.spell.Spell
import com.davidm1a2.afraidofthedark.common.spell.SpellStage
import net.minecraft.core.BlockPos
import net.minecraft.entity.Entity
import net.minecraft.nbt.CompoundNBT
import net.minecraft.nbt.NBTUtil
import net.minecraft.util.RegistryKey
import net.minecraft.util.ResourceLocation
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.vector.Vector3d
import net.minecraft.util.registry.Registry
import net.minecraft.world.level.Level
import net.minecraft.world.entity.Entity
import net.minecraft.world.server.ServerWorld
import net.minecraftforge.fml.server.ServerLifecycleHooks
import java.util.UUID

class DeliveryTransitionState {
    val spell: Spell
    val stageIndex: Int
    val position: Vector3d
    val blockPosition: BlockPos
    val direction: Vector3d
    val normal: Vector3d

    private val lazyWorld: Lazy<ServerWorld>
    val world: ServerWorld
        get() = lazyWorld.value

    private val casterEntityId: UUID?
    val casterEntity: Entity?
        get() = casterEntityId?.let { world.getEntity(it) }

    private val entityId: UUID?
    val entity: Entity?
        get() = entityId?.let { world.getEntity(it) }

    private val deliveryEntityId: UUID?
    val deliveryEntity: Entity?
        get() = deliveryEntityId?.let { world.getEntity(it) }

    constructor(
        spell: Spell,
        stageIndex: Int,
        world: Level,
        position: Vector3d,
        blockPosition: BlockPos,
        direction: Vector3d,
        normal: Vector3d,
        casterEntity: Entity? = null,
        entity: Entity? = null,
        deliveryEntity: Entity? = null
    ) {
        this.spell = spell
        this.stageIndex = stageIndex
        this.position = position
        this.blockPosition = blockPosition
        this.direction = direction
        this.normal = normal
        // This should only be created server side, so we can cast safely
        this.lazyWorld = lazyOf(world as ServerWorld)
        casterEntityId = casterEntity?.uuid
        entityId = entity?.uuid
        deliveryEntityId = deliveryEntity?.uuid
    }

    constructor(nbt: CompoundNBT) {
        spell = Spell(nbt.getCompound(NBT_SPELL))
        stageIndex = nbt.getInt(NBT_STAGE_INDEX)
        lazyWorld = lazy {
            // This call will fail during deserialization, but is valid before first use, so lazily initialize it
            ServerLifecycleHooks.getCurrentServer().getLevel(RegistryKey.create(Registry.DIMENSION_REGISTRY, ResourceLocation(nbt.getString(NBT_WORLD))))!!
        }
        position = Vector3d(
            nbt.getDouble(NBT_POSITION + "_x"),
            nbt.getDouble(NBT_POSITION + "_y"),
            nbt.getDouble(NBT_POSITION + "_z")
        )
        normal = Vector3d(
            nbt.getDouble(NBT_NORMAL + "_x"),
            nbt.getDouble(NBT_NORMAL + "_y"),
            nbt.getDouble(NBT_NORMAL + "_z")
        )
        blockPosition = NBTUtil.readBlockPos(nbt.getCompound(NBT_BLOCK_POSITION))
        direction = Vector3d(
            nbt.getDouble(NBT_DIRECTION + "_x"),
            nbt.getDouble(NBT_DIRECTION + "_y"),
            nbt.getDouble(NBT_DIRECTION + "_z")
        )
        casterEntityId = if (nbt.contains(NBT_CASTER_ENTITY_ID)) {
            NBTUtil.loadUUID(nbt.get(NBT_CASTER_ENTITY_ID)!!)
        } else {
            null
        }
        entityId = if (nbt.contains(NBT_ENTITY_ID)) {
            NBTUtil.loadUUID(nbt.get(NBT_ENTITY_ID)!!)
        } else {
            null
        }
        deliveryEntityId = if (nbt.contains(NBT_DELIVERY_ENTITY_ID)) {
            NBTUtil.loadUUID(nbt.get(NBT_DELIVERY_ENTITY_ID)!!)
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
        nbt.putString(NBT_WORLD, world.dimension().location().toString())
        nbt.putDouble(NBT_POSITION + "_x", position.x)
        nbt.putDouble(NBT_POSITION + "_y", position.y)
        nbt.putDouble(NBT_POSITION + "_z", position.z)
        nbt.put(NBT_BLOCK_POSITION, NBTUtil.writeBlockPos(blockPosition))
        nbt.putDouble(NBT_DIRECTION + "_x", direction.x)
        nbt.putDouble(NBT_DIRECTION + "_y", direction.y)
        nbt.putDouble(NBT_DIRECTION + "_z", direction.z)
        nbt.putDouble(NBT_NORMAL + "_x", normal.x)
        nbt.putDouble(NBT_NORMAL + "_y", normal.y)
        nbt.putDouble(NBT_NORMAL + "_z", normal.z)
        if (casterEntityId != null) {
            nbt.put(NBT_CASTER_ENTITY_ID, NBTUtil.createUUID(casterEntityId))
        }
        if (entityId != null) {
            nbt.put(NBT_ENTITY_ID, NBTUtil.createUUID(entityId))
        }
        if (deliveryEntityId != null) {
            nbt.put(NBT_DELIVERY_ENTITY_ID, NBTUtil.createUUID(deliveryEntityId))
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

    fun copy(
        spell: Spell? = null,
        stageIndex: Int? = null,
        world: Level? = null,
        position: Vector3d? = null,
        blockPosition: BlockPos? = null,
        direction: Vector3d? = null,
        normal: Vector3d? = null,
        casterEntity: Entity? = null,
        entity: Entity? = null,
        deliveryEntity: Entity? = null
    ): DeliveryTransitionState {
        return DeliveryTransitionState(
            spell ?: this.spell,
            stageIndex ?: this.stageIndex,
            world ?: this.world,
            position ?: this.position,
            blockPosition ?: this.blockPosition,
            direction ?: this.direction,
            normal ?: this.normal,
            casterEntity ?: this.casterEntity,
            entity ?: this.entity,
            deliveryEntity ?: this.deliveryEntity
        )
    }

    companion object {
        // Constants used for NBT serialization / deserialization
        private const val NBT_SPELL = "spell"
        private const val NBT_STAGE_INDEX = "stage_index"
        private const val NBT_WORLD = "world"
        private const val NBT_POSITION = "position"
        private const val NBT_NORMAL = "normal"
        private const val NBT_BLOCK_POSITION = "block_position"
        private const val NBT_DIRECTION = "direction"
        private const val NBT_CASTER_ENTITY_ID = "caster_entity_id"
        private const val NBT_ENTITY_ID = "entity_id"
        private const val NBT_DELIVERY_ENTITY_ID = "delivery_entity_id"
    }
}