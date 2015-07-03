/*
 * Author: David Slovikosky 
 * Mod: Afraid of the Dark 
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.item;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.DavidM1A2.AfraidOfTheDark.common.entities.Werewolf.EntityWerewolf;
import com.DavidM1A2.AfraidOfTheDark.common.item.core.AOTDChargableSword;
import com.DavidM1A2.AfraidOfTheDark.common.playerData.HasStartedAOTD;
import com.DavidM1A2.AfraidOfTheDark.common.refrence.Constants;

public class ItemIgneousSword extends AOTDChargableSword
{
	private static final int HIT_RANGE = 5;

	public ItemIgneousSword()
	{
		super(Constants.AOTDToolMaterials.igneousTool, "igneousSword");
	}

	@Override
	public boolean onLeftClickEntity(final ItemStack stack, final EntityPlayer player, final Entity entity)
	{
		if (entity != null)
		{
			entity.setFire(5);

			if (entity instanceof EntityWerewolf)
			{
				if (HasStartedAOTD.get(player))
				{
					entity.attackEntityFrom(Constants.AOTDDamageSources.silverDamage, 10F);
				}
			}
		}

		return super.onLeftClickEntity(stack, player, entity);
	}

	// The journal shows who it is soulbound to
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(final ItemStack itemStack, final EntityPlayer entityPlayer, final List tooltip, final boolean p_77624_4_)
	{
		tooltip.add("Magical items will never break.");
		tooltip.add("Right click to use an AOE fire strike");
		tooltip.add("when charged to 100%");
	}

	@Override
	public int percentChargePerAttack()
	{
		return 5;
	}

	@Override
	public void performChargeAttack(ItemStack itemStack, World world, EntityPlayer entityPlayer)
	{
		List<Entity> surroundingEntities = world.getEntitiesWithinAABBExcludingEntity(entityPlayer, entityPlayer.getEntityBoundingBox().expand(HIT_RANGE, HIT_RANGE, HIT_RANGE));

		for (Entity entity : surroundingEntities)
		{
			if (entity instanceof EntityLivingBase)
			{
				entity.setFire(20);
			}
		}
	}
}
