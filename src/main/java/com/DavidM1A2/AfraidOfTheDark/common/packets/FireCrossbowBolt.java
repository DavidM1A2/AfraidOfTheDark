package com.DavidM1A2.AfraidOfTheDark.common.packets;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import com.DavidM1A2.AfraidOfTheDark.common.entities.Bolts.EntityIronBolt;
import com.DavidM1A2.AfraidOfTheDark.common.entities.Bolts.EntitySilverBolt;
import com.DavidM1A2.AfraidOfTheDark.common.entities.Bolts.EntityWoodenBolt;
import com.DavidM1A2.AfraidOfTheDark.common.initializeMod.ModItems;
import com.DavidM1A2.AfraidOfTheDark.common.refrence.AOTDCrossbowBoltTypes;

public class FireCrossbowBolt implements IMessage
{
	private AOTDCrossbowBoltTypes boltType;

	public FireCrossbowBolt()
	{
		boltType = AOTDCrossbowBoltTypes.iron;
	}

	public FireCrossbowBolt(AOTDCrossbowBoltTypes boltType)
	{
		this.boltType = boltType;
	}

	@Override
	public void fromBytes(final ByteBuf buf)
	{
		this.boltType = AOTDCrossbowBoltTypes.getTypeFromID(buf.readInt());
	}

	@Override
	public void toBytes(final ByteBuf buf)
	{
		buf.writeInt(AOTDCrossbowBoltTypes.getIDFromType(this.boltType));
	}

	// When we get a packet we set the current item that the player is holding to the data we rececived
	public static class HandlerServer implements IMessageHandler<FireCrossbowBolt, IMessage>
	{
		@Override
		public IMessage onMessage(final FireCrossbowBolt message, final MessageContext ctx)
		{
			EntityPlayer entityPlayer = ctx.getServerHandler().playerEntity;
			World currentWorld = entityPlayer.worldObj;
			if (entityPlayer.capabilities.isCreativeMode)
			{
				currentWorld.playSoundAtEntity(entityPlayer, "afraidofthedark:crossbowFire", 0.5F, ((currentWorld.rand.nextFloat() * 0.4F) + 0.8F));
				switch (message.boltType)
				{
					case iron:
						currentWorld.spawnEntityInWorld(new EntityIronBolt(currentWorld, entityPlayer));
						break;
					case silver:
						currentWorld.spawnEntityInWorld(new EntitySilverBolt(currentWorld, entityPlayer));
						break;
					case wooden:
						currentWorld.spawnEntityInWorld(new EntityWoodenBolt(currentWorld, entityPlayer));
						break;
					default:
						break;

				}
			}
			else
			{
				boolean fired = false;
				switch (message.boltType)
				{
					case iron:
						if (entityPlayer.inventory.consumeInventoryItem(ModItems.ironBolt))
						{
							currentWorld.spawnEntityInWorld(new EntityIronBolt(currentWorld, entityPlayer));
							fired = true;
						}
						break;
					case silver:
						if (entityPlayer.inventory.consumeInventoryItem(ModItems.silverBolt))
						{
							currentWorld.spawnEntityInWorld(new EntitySilverBolt(currentWorld, entityPlayer));
							fired = true;
						}
						break;
					case wooden:
						if (entityPlayer.inventory.consumeInventoryItem(ModItems.woodenBolt))
						{
							currentWorld.spawnEntityInWorld(new EntityWoodenBolt(currentWorld, entityPlayer));
							fired = true;
						}
						break;
					default:
						break;

				}
				if (fired)
				{
					currentWorld.playSoundAtEntity(entityPlayer, "afraidofthedark:crossbowFire", 0.5F, ((currentWorld.rand.nextFloat() * 0.4F) + 0.8F));
				}
			}

			return null;
		}
	}
}
