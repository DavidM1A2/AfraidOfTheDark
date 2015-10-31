
package com.DavidM1A2.AfraidOfTheDark.common.playerData;

import net.minecraft.entity.Entity;
import net.minecraftforge.common.IExtendedEntityProperties;

public abstract class EntityData implements IExtendedEntityProperties
{
	private final Entity entity;

	public EntityData(Entity entity)
	{
		this.entity = entity;
	}

	public boolean isServerSide()
	{
		return this.entity != null && this.entity.worldObj != null && !this.entity.worldObj.isRemote;
	}
}
