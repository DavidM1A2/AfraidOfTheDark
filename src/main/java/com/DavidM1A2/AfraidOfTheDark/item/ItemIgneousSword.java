package com.DavidM1A2.AfraidOfTheDark.item;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.DavidM1A2.AfraidOfTheDark.refrence.Refrence;

public class ItemIgneousSword extends AOTDSword
{
	public ItemIgneousSword()
	{
		super(Refrence.igneousTool);
		this.setUnlocalizedName("igneousSword");
	}

	@Override
	public boolean onLeftClickEntity(final ItemStack stack, final EntityPlayer player, final Entity entity)
	{
		if (entity != null)
		{
			entity.setFire(5);
		}
		return super.onLeftClickEntity(stack, player, entity);
	}

	/**
	 * Called when a Block is right-clicked with this Item
	 *
	 * @param pos The block being right-clicked
	 * @param side The side being right-clicked
	 */
	@Override
	public boolean onItemUse(final ItemStack stack, final EntityPlayer playerIn, final World worldIn, BlockPos pos, final EnumFacing side, final float hitX, final float hitY, final float hitZ)
	{
		pos = pos.offset(side);

		if (!playerIn.func_175151_a(pos, side, stack))
		{
			return false;
		}
		else
		{
			if (worldIn.isAirBlock(pos))
			{
				worldIn.playSoundEffect(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, "fire.ignite", 1.0F, (Item.itemRand.nextFloat() * 0.4F) + 0.8F);
				worldIn.setBlockState(pos, Blocks.fire.getDefaultState());
			}
		}

		return super.onItemUse(stack, playerIn, worldIn, pos, side, hitX, hitY, hitZ);
	}

	/**
	 * Current implementations of this method in child classes do not use the entry argument beside ev. They just raise
	 * the damage on the stack.
	 *
	 * @param target The Entity being hit
	 * @param attacker the attacking entity
	 */
	@Override
	public boolean hitEntity(final ItemStack stack, final EntityLivingBase target, final EntityLivingBase attacker)
	{
		return true;
	}

	// The journal shows who it is soulbound to
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(final ItemStack itemStack, final EntityPlayer entityPlayer, final List information, final boolean p_77624_4_)
	{
		information.add("Magical items will never break.");
	}
}
