/*
 * Author: David Slovikosky 
 * Mod: Afraid of the Dark 
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.armor;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.DavidM1A2.AfraidOfTheDark.initializeMod.ModItems;

public class StarMetalArmor extends AOTDArmor
{
	public StarMetalArmor(final ArmorMaterial armorMaterial, final int renderIndex, final int type)
	{
		super(armorMaterial, renderIndex, type);
		this.setUnlocalizedName((type == 0) ? "starMetalHelmet" : (type == 1) ? "starMetalChestplate" : (type == 2) ? "starMetalLeggings" : "starMetalBoots");
	}

	@Override
	//This is pretty self explanatory
	public String getArmorTexture(final ItemStack armor, final Entity entity, final int slot, final String type)
	{
		if (armor.getItem() == ModItems.starMetalLeggings)
		{
			return "afraidofthedark:textures/armor/starMetal_2.png";
		}
		else
		{
			return "afraidofthedark:textures/armor/starMetal_1.png";
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(final ItemStack stack, final EntityPlayer playerIn, final List tooltip, final boolean advanced)
	{
		tooltip.add("Magical armor will never break.");
	}

	/**
	 * Called to tick armor in the armor slot. Override to do something
	 */
	@Override
	public void onArmorTick(final World world, final EntityPlayer entityPlayer, final ItemStack itemStack)
	{
		if (!entityPlayer.isPotionActive(Potion.absorption))
		{
			entityPlayer.addPotionEffect(new PotionEffect(Potion.absorption.id, 1200, this.getNumberOfWornPieces(entityPlayer) - 1, false, false));
		}
	}

	@Override
	public ArmorProperties getProperties(final EntityLivingBase player, final ItemStack armor, final DamageSource source, final double damage, final int slot)
	{
		if ((source == DamageSource.drown) || (source == DamageSource.fall) || (source == DamageSource.inWall) || (source == DamageSource.outOfWorld) || (source == DamageSource.starve) || (source == DamageSource.lava))
		{
			return new ArmorProperties(0, .25, 0);
		}

		// Remove the ability of thorns to damage armor
		armor.setItemDamage(0);

		return new ArmorProperties(0, this.damageReduceAmount / 25D, armor.getMaxDamage());
	}

	@Override
	public int getArmorDisplay(final EntityPlayer player, final ItemStack armor, final int slot)
	{
		return this.getReductionBasedOffOfSlot(slot);
	}

	@Override
	public void damageArmor(final EntityLivingBase entity, final ItemStack stack, final DamageSource source, final int damage, final int slot)
	{
		return;
	}

	private int getReductionBasedOffOfSlot(final int slot)
	{
		return slot == 0 ? 3 : slot == 1 ? 6 : slot == 2 ? 8 : 3;
	}

	private int getNumberOfWornPieces(final EntityPlayer entityPlayer)
	{
		int number = 0;
		for (final ItemStack element : entityPlayer.inventory.armorInventory)
		{
			if ((element != null) && (element.getItem() instanceof StarMetalArmor))
			{
				number = number + 1;
			}
		}
		return number;
	}
}
