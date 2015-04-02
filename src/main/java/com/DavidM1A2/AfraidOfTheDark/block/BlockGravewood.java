/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.block;

import net.minecraft.block.BlockLog;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;

import com.DavidM1A2.AfraidOfTheDark.refrence.Refrence;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockGravewood extends BlockLog
{
	@SideOnly(Side.CLIENT)
	private IIcon[] icons;

	/*
	 * Define a gravewood block
	 */
	public BlockGravewood()
	{
		super();
		this.setBlockName("gravewood");
		this.setCreativeTab(Refrence.AFRAID_OF_THE_DARK);
	}

	// Side icon is different from the top and bottom icon here
	@Override
	@SideOnly(Side.CLIENT)
	protected IIcon getSideIcon(int p_150163_1_)
	{
		return this.icons[0];
	}

	@Override
	@SideOnly(Side.CLIENT)
	protected IIcon getTopIcon(int p_150161_1_)
	{
		return this.icons[1];
	}

	@Override
	public String getUnlocalizedName()
	{
		return String.format("tile.%s%s", Refrence.MOD_ID.toLowerCase() + ":", getUnwrappedUnlocalizedName(super.getUnlocalizedName()));
		// tile.modid:blockname.name
	}

	// Register block icons (one for the side and one for the top)
	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister iconRegister)
	{
		icons = new IIcon[2];
		icons[0] = iconRegister.registerIcon(String.format("%s_0", getUnwrappedUnlocalizedName(this.getUnlocalizedName())));
		icons[1] = iconRegister.registerIcon(String.format("%s_1", getUnwrappedUnlocalizedName(this.getUnlocalizedName())));
	}

	protected String getUnwrappedUnlocalizedName(String unlocalizedName)
	{
		return unlocalizedName.substring(unlocalizedName.indexOf(".") + 1);
	}

}
