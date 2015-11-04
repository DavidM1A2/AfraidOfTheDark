/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */

package com.DavidM1A2.AfraidOfTheDark.common.packets;

import com.DavidM1A2.AfraidOfTheDark.common.packets.minersBasicMessageHandler.MessageHandler;
import com.DavidM1A2.AfraidOfTheDark.common.refrence.AOTDParticleFXTypes;
import com.DavidM1A2.AfraidOfTheDark.common.utility.ParticleFXUtility;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class SyncParticleFX implements IMessage
{
	private AOTDParticleFXTypes particle;
	private double x;
	private double y;
	private double z;

	public SyncParticleFX()
	{
		this.particle = AOTDParticleFXTypes.EnariaBasicAttack;
		this.x = 0;
		this.y = 0;
		this.z = 0;
	}

	public SyncParticleFX(final AOTDParticleFXTypes particle, final double x, final double y, final double z)
	{
		this.particle = particle;
		this.x = x;
		this.y = y;
		this.z = z;
	}

	@Override
	public void fromBytes(final ByteBuf buf)
	{
		this.particle = AOTDParticleFXTypes.values()[buf.readInt()];
		this.x = buf.readDouble();
		this.y = buf.readDouble();
		this.z = buf.readDouble();
	}

	@Override
	public void toBytes(final ByteBuf buf)
	{
		buf.writeInt(this.particle.getIndex());
		buf.writeDouble(this.x);
		buf.writeDouble(this.y);
		buf.writeDouble(this.z);
	}

	// when we receive a packet we set HasStartedAOTD
	public static class Handler extends MessageHandler.Client<SyncParticleFX>
	{
		@Override
		public IMessage handleClientMessage(final EntityPlayer entityPlayer, final SyncParticleFX msg, MessageContext ctx)
		{
			Minecraft.getMinecraft().addScheduledTask(new Runnable()
			{
				@Override
				public void run()
				{
					ParticleFXUtility.generateParticles(entityPlayer.worldObj, msg.x, msg.y, msg.z, msg.particle);
				}
			});

			return null;
		}
	}
}