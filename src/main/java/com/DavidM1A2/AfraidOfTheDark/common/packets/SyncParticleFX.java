
package com.DavidM1A2.AfraidOfTheDark.common.packets;

import com.DavidM1A2.AfraidOfTheDark.common.refrence.AOTDParticleFXTypes;
import com.DavidM1A2.AfraidOfTheDark.common.utility.ParticleFXUtility;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
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
	public static class HandlerClient implements IMessageHandler<SyncParticleFX, IMessage>
	{
		@Override
		public IMessage onMessage(final SyncParticleFX message, final MessageContext ctx)
		{
			ParticleFXUtility.generateParticles(Minecraft.getMinecraft().thePlayer.worldObj, message.x, message.y, message.z, message.particle);

			return null;
		}
	}
}