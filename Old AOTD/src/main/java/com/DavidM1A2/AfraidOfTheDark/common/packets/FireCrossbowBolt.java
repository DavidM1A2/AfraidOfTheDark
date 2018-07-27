/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.packets;

import com.DavidM1A2.AfraidOfTheDark.common.entities.bolts.EntityBolt;
import com.DavidM1A2.AfraidOfTheDark.common.initializeMod.ModSounds;
import com.DavidM1A2.AfraidOfTheDark.common.packets.minersBasicMessageHandler.MessageHandler;
import com.DavidM1A2.AfraidOfTheDark.common.reference.AOTDCrossbowBoltTypes;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
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
	public static class Handler extends MessageHandler.Server<FireCrossbowBolt>
	{
		@Override
		public IMessage handleServerMessage(final EntityPlayer entityPlayer, final FireCrossbowBolt msg, final MessageContext ctx)
		{
			entityPlayer.world.getMinecraftServer().addScheduledTask(new Runnable()
			{
				@Override
				public void run()
				{
					World world = entityPlayer.world;

					// Only fire a bolt if the player is in creative or has the right bolt item
					if (entityPlayer.capabilities.isCreativeMode || entityPlayer.inventory.clearMatchingItems(msg.boltType.getMyBoltItem(), -1, 1, null) == 1)
					{
						EntityBolt bolt = msg.boltType.createBolt(world, entityPlayer);
						// Push the bolt slightly forward so it does not collide with the player
						bolt.shoot(entityPlayer, entityPlayer.rotationPitch, entityPlayer.rotationYaw, 0f, 3f, 0f);
						bolt.posX = bolt.posX + bolt.motionX;
						bolt.posY = bolt.posY + bolt.motionY;
						bolt.posZ = bolt.posZ + bolt.motionZ;
						world.spawnEntity(bolt);
						world.playSound(entityPlayer, entityPlayer.posX, entityPlayer.posY, entityPlayer.posZ, ModSounds.crossbowFire, SoundCategory.MASTER, 0.5F, ((world.rand.nextFloat() * 0.4F) + 0.8F));
					}
				}
			});

			return null;
		}
	}
}