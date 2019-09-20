package com.davidm1a2.afraidofthedark.common.spell.component;

import com.davidm1a2.afraidofthedark.common.spell.Spell;
import com.davidm1a2.afraidofthedark.common.spell.SpellStage;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;

import java.util.Optional;
import java.util.UUID;

/**
 * Class representing a spell in the transition state. This object is immutable
 */
public class DeliveryTransitionState
{
    // Constants used for NBT serialization / deserialization
    private static final String NBT_SPELL = "spell";
    private static final String NBT_STAGE_INDEX = "stage_index";
    private static final String NBT_WORLD_ID = "world_id";
    private static final String NBT_POSITION = "position";
    private static final String NBT_BLOCK_POSITION = "block_position";
    private static final String NBT_DIRECTION = "direction";
    private static final String NBT_ENTITY_ID = "entity_id";
    private static final String NBT_DELIVERY_ENTITY_ID = "delivery_entity_id";

    private Spell spell;
    private int stageIndex;
    private WorldServer world;
    private Vec3d position;
    private BlockPos blockPos;
    private Vec3d direction;
    // The entity being delivered to, can be null
    private UUID entityId;
    // The entity delivering the spell, can be null
    private UUID deliveryEntityId;

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
    DeliveryTransitionState(Spell spell, int stageIndex, World world, Vec3d position, BlockPos blockPos, Vec3d direction, Entity entity, Entity deliveryEntity)
    {
        this.spell = spell;
        this.stageIndex = stageIndex;
        // This should only be created server side, so we can cast safely
        this.world = (WorldServer) world;
        this.position = position;
        this.blockPos = blockPos;
        this.direction = direction;
        this.entityId = Optional.ofNullable(entity).map(Entity::getPersistentID).orElse(null);
        this.deliveryEntityId = Optional.ofNullable(deliveryEntity).map(Entity::getPersistentID).orElse(null);
    }

    /**
     * Initializes the state from NBT
     *
     * @param nbt The NBT containing the delivery state
     */
    public DeliveryTransitionState(NBTTagCompound nbt)
    {
        this.spell = new Spell(nbt.getCompoundTag(NBT_SPELL));
        this.stageIndex = nbt.getInteger(NBT_STAGE_INDEX);
        this.world = DimensionManager.getWorld(nbt.getInteger(NBT_WORLD_ID));
        this.position = new Vec3d(nbt.getDouble(NBT_POSITION + "_x"), nbt.getDouble(NBT_POSITION + "_y"), nbt.getDouble(NBT_POSITION + "_z"));
        this.blockPos = NBTUtil.getPosFromTag(nbt.getCompoundTag(NBT_BLOCK_POSITION));
        this.direction = new Vec3d(nbt.getDouble(NBT_DIRECTION + "_x"), nbt.getDouble(NBT_DIRECTION + "_y"), nbt.getDouble(NBT_DIRECTION + "_z"));
        if (nbt.hasKey(NBT_ENTITY_ID))
        {
            this.entityId = NBTUtil.getUUIDFromTag(nbt.getCompoundTag(NBT_ENTITY_ID));
        }
        else
        {
            this.entityId = null;
        }
        if (nbt.hasKey(NBT_DELIVERY_ENTITY_ID))
        {
            this.deliveryEntityId = NBTUtil.getUUIDFromTag(nbt.getCompoundTag(NBT_DELIVERY_ENTITY_ID));
        }
        else
        {
            this.deliveryEntityId = null;
        }
    }

    /**
     * @return The transition state serialized as NBT
     */
    public NBTTagCompound writeToNbt()
    {
        NBTTagCompound nbt = new NBTTagCompound();

        nbt.setTag(NBT_SPELL, this.spell.serializeNBT());
        nbt.setInteger(NBT_STAGE_INDEX, this.stageIndex);
        nbt.setInteger(NBT_WORLD_ID, this.world.provider.getDimension());
        nbt.setDouble(NBT_POSITION + "_x", this.position.x);
        nbt.setDouble(NBT_POSITION + "_y", this.position.y);
        nbt.setDouble(NBT_POSITION + "_z", this.position.z);
        nbt.setTag(NBT_BLOCK_POSITION, NBTUtil.createPosTag(this.blockPos));
        nbt.setDouble(NBT_DIRECTION + "_x", this.direction.x);
        nbt.setDouble(NBT_DIRECTION + "_y", this.direction.y);
        nbt.setDouble(NBT_DIRECTION + "_z", this.direction.z);
        if (this.entityId != null)
        {
            nbt.setTag(NBT_ENTITY_ID, NBTUtil.createUUIDTag(this.entityId));
        }
        if (this.deliveryEntityId != null)
        {
            nbt.setTag(NBT_DELIVERY_ENTITY_ID, NBTUtil.createUUIDTag(this.deliveryEntityId));
        }

        return nbt;
    }

    /**
     * Utility method to get the current spell stage from the spell and index
     *
     * @return The current spell stage
     */
    public SpellStage getCurrentStage()
    {
        return this.getSpell().getStage(this.getStageIndex());
    }

    /**
     * @return The spell that is transitioning
     */
    public Spell getSpell()
    {
        return spell;
    }

    /**
     * @return The index of the spell stage that is transitioning
     */
    public int getStageIndex()
    {
        return stageIndex;
    }

    /**
     * @return The world that the transition is happening in
     */
    public World getWorld()
    {
        return world;
    }

    /**
     * @return The exact double vector position the transition occurred
     */
    public Vec3d getPosition()
    {
        return position;
    }

    /**
     * @return A rounded for of getPosition() that estimates the block the transition occurred in
     */
    public BlockPos getBlockPosition()
    {
        return blockPos;
    }

    /**
     * @return A normalized vector pointing in the direction the transition was in
     */
    public Vec3d getDirection()
    {
        return direction;
    }

    /**
     * @return The entity that this state was transitioning through
     */
    public Entity getEntity()
    {
        // If the entity is non null we get the entity in the world, otherwise we return null
        return Optional.ofNullable(entityId).map(world::getEntityFromUuid).orElse(null);
    }

    /**
     * @return The entity that performed the delivery and transition
     */
    public Entity getDeliveryEntity()
    {
        // If the entity is non null we get the entity in the world, otherwise we return null
        return Optional.ofNullable(deliveryEntityId).map(world::getEntityFromUuid).orElse(null);
    }
}
