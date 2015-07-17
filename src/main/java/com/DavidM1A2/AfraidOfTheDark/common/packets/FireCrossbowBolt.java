/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.packets;

import com.DavidM1A2.AfraidOfTheDark.common.entities.Bolts.EntityIgneousBolt;
import com.DavidM1A2.AfraidOfTheDark.common.entities.Bolts.EntityIronBolt;
import com.DavidM1A2.AfraidOfTheDark.common.entities.Bolts.EntitySilverBolt;
import com.DavidM1A2.AfraidOfTheDark.common.entities.Bolts.EntityStarMetalBolt;
import com.DavidM1A2.AfraidOfTheDark.common.entities.Bolts.EntityWoodenBolt;
import com.DavidM1A2.AfraidOfTheDark.common.initializeMod.ModItems;
import com.DavidM1A2.AfraidOfTheDark.common.refrence.AOTDCrossbowBoltTypes;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class FireCrossbowBolt implements IMessage
{
	private AOTDCrossbowBoltTypes boltType;

	public FireCrossbowBolt()
	{
		boltType = AOTDCrossbowBoltTypes.Iron;
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
					case Iron:
						currentWorld.spawnEntityInWorld(new EntityIronBolt(currentWorld, entityPlayer));
						break;
					case Silver:
						currentWorld.spawnEntityInWorld(new EntitySilverBolt(currentWorld, entityPlayer));
						break;
					case Wooden:
						currentWorld.spawnEntityInWorld(new EntityWoodenBolt(currentWorld, entityPlayer));
						break;
					case Igneous:
						currentWorld.spawnEntityInWorld(new EntityIgneousBolt(currentWorld, entityPlayer));
						break;
					case StarMetal:
						currentWorld.spawnEntityInWorld(new EntityStarMetalBolt(currentWorld, entityPlayer));
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
					case Iron:
						if (entityPlayer.inventory.consumeInventoryItem(ModItems.ironBolt))
						{
							currentWorld.spawnEntityInWorld(new EntityIronBolt(currentWorld, entityPlayer));
							fired = true;
						}
						break;
					case Silver:
						if (entityPlayer.inventory.consumeInventoryItem(ModItems.silverBolt))
						{
							currentWorld.spawnEntityInWorld(new EntitySilverBolt(currentWorld, entityPlayer));
							fired = true;
						}
						break;
					case Wooden:
						if (entityPlayer.inventory.consumeInventoryItem(ModItems.woodenBolt))
						{
							currentWorld.spawnEntityInWorld(new EntityWoodenBolt(currentWorld, entityPlayer));
							fired = true;
						}
						break;
					case Igneous:
						if (entityPlayer.inventory.consumeInventoryItem(ModItems.igneousBolt))
						{
							currentWorld.spawnEntityInWorld(new EntityIgneousBolt(currentWorld, entityPlayer));
							fired = true;
						}
						break;
					case StarMetal:
						if (entityPlayer.inventory.consumeInventoryItem(ModItems.starMetalBolt))
						{
							currentWorld.spawnEntityInWorld(new EntityStarMetalBolt(currentWorld, entityPlayer));
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