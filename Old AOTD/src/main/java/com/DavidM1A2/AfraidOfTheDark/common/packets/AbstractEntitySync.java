/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.packets;

import java.util.UUID;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public abstract class AbstractEntitySync implements IMessage
{
	// UUID for client to server
	protected UUID entityUUIDToUpdate;
	// int for server to client
	protected int entityIDToUpdate;

	public AbstractEntitySync()
	{
		this.entityUUIDToUpdate = null;
		this.entityIDToUpdate = 0;
	}

	public AbstractEntitySync(Entity entity)
	{
		this.entityUUIDToUpdate = entity.getPersistentID();
		this.entityIDToUpdate = entity.getEntityId();
	}

	@Override
	public void fromBytes(ByteBuf buf)
	{
		this.entityUUIDToUpdate = new UUID(buf.readLong(), buf.readLong());
		this.entityIDToUpdate = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		buf.writeLong(this.entityUUIDToUpdate.getMostSignificantBits());
		buf.writeLong(this.entityUUIDToUpdate.getLeastSignificantBits());
		buf.writeInt(this.entityIDToUpdate);
	}
}
