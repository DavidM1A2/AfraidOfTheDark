package com.DavidM1A2.afraidofthedark.common.packets;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

import java.util.UUID;

/**
 * Class used to encapsulate sending data about non-player entities
 */
public abstract class EntitySyncBase implements IMessage
{
	// UUID for client to server
	protected UUID entityUUID;
	// Int ID for server to client
	protected int entityID;

	/**
	 * Required default constructor for all packets
	 */
	public EntitySyncBase()
	{
		this.entityUUID = null;
		this.entityID = 0;
	}

	/**
	 * Primary constructor used to initializes this packet with the entity to sync
	 *
	 * @param entity The entity to sync
	 */
	public EntitySyncBase(Entity entity)
	{
		this.entityUUID = entity.getPersistentID();
		this.entityID = entity.getEntityId();
	}

	/**
	 * Deserializes the UUID and ID
	 *
	 * @param buf The buffer to read from
	 */
	@Override
	public void fromBytes(ByteBuf buf)
	{
		this.entityUUID = new UUID(buf.readLong(), buf.readLong());
		this.entityID = buf.readInt();
	}

	/**
	 * Serializes the UUID and ID
	 *
	 * @param buf The buffer to write to
	 */
	@Override
	public void toBytes(ByteBuf buf)
	{
		buf.writeLong(this.entityUUID.getMostSignificantBits());
		buf.writeLong(this.entityUUID.getLeastSignificantBits());
		buf.writeInt(this.entityID);
	}
}
