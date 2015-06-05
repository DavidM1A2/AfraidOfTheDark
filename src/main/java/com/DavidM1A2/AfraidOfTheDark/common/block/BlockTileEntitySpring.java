package com.DavidM1A2.AfraidOfTheDark.common.block;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.gui.IUpdatePlayerListBox;
import net.minecraft.util.AxisAlignedBB;
import net.minecraftforge.fml.relauncher.Side;

import com.DavidM1A2.AfraidOfTheDark.common.initializeMod.ModBlocks;
import com.DavidM1A2.AfraidOfTheDark.common.playerData.LoadResearchData;
import com.DavidM1A2.AfraidOfTheDark.common.refrence.ResearchTypes;

public class BlockTileEntitySpring extends AOTDTileEntity implements IUpdatePlayerListBox
{
	private int ticksExisted = 0;
	private static final int TICKS_INBETWEEN_CHECKS = 60;
	private static final int CHECK_RANGE = 3;

	public BlockTileEntitySpring()
	{
		super(ModBlocks.spring);
	}

	@Override
	public void update()
	{
		if (!this.worldObj.isRemote)
		{
			if (ticksExisted % TICKS_INBETWEEN_CHECKS == 0)
			{
				ticksExisted = 1;
				for (Object object : this.worldObj.getEntitiesWithinAABB(EntityPlayer.class, AxisAlignedBB.fromBounds(this.pos.getX(), this.pos.getY(), this.pos.getZ(), this.pos.getX() + 1, this.pos.getY() + 1, this.pos.getZ() + 1).expand(CHECK_RANGE, CHECK_RANGE, CHECK_RANGE)))
				{
					if (object instanceof EntityPlayer)
					{
						EntityPlayer entityPlayer = (EntityPlayer) object;
						if (LoadResearchData.canResearch(entityPlayer, ResearchTypes.VitaeI))
						{
							LoadResearchData.unlockResearchSynced(entityPlayer, ResearchTypes.VitaeI, Side.SERVER, true);
						}
						entityPlayer.addPotionEffect(new PotionEffect(Potion.regeneration.id, 100, 1, true, true));
					}
				}
			}
			else
			{
				ticksExisted = ticksExisted + 1;
			}
		}
	}
}
