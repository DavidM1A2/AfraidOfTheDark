package com.DavidM1A2.AfraidOfTheDark.block;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.gui.IUpdatePlayerListBox;
import net.minecraftforge.fml.relauncher.Side;

import com.DavidM1A2.AfraidOfTheDark.initializeMod.ModBlocks;
import com.DavidM1A2.AfraidOfTheDark.playerData.LoadResearchData;
import com.DavidM1A2.AfraidOfTheDark.refrence.ResearchTypes;

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
		if (ticksExisted % TICKS_INBETWEEN_CHECKS == 0)
		{
			ticksExisted = 1;
			for (Object object : this.worldObj.getEntitiesWithinAABB(EntityPlayer.class, this.getRenderBoundingBox().expand(CHECK_RANGE, CHECK_RANGE, CHECK_RANGE)))
			{
				if (object instanceof EntityPlayer)
				{
					EntityPlayer entityPlayer = (EntityPlayer) object;
					if (!this.worldObj.isRemote)
					{
						if (LoadResearchData.canResearch(entityPlayer, ResearchTypes.VitaeI))
						{
							LoadResearchData.unlockResearchSynced(entityPlayer, ResearchTypes.VitaeI, Side.SERVER, true);
						}
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
