/*
 * Author: David Slovikosky 
 * Mod: Afraid of the Dark 
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.item.armor;

import java.util.List;

import com.DavidM1A2.AfraidOfTheDark.common.entities.Werewolf.EntityWerewolf;
import com.DavidM1A2.AfraidOfTheDark.common.initializeMod.ModCapabilities;
import com.DavidM1A2.AfraidOfTheDark.common.initializeMod.ModItems;
import com.DavidM1A2.AfraidOfTheDark.common.reference.ResearchTypes;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class StarMetalArmor extends AOTDArmor
{
	public StarMetalArmor(final ArmorMaterial armorMaterial, final int renderIndex, final int type)
	{
		super(armorMaterial, renderIndex, type);
		this.setUnlocalizedName((type == 0) ? "starMetalHelmet" : (type == 1) ? "starMetalChestplate" : (type == 2) ? "starMetalLeggings" : "starMetalBoots");
	}

	@Override
	// This is pretty self explanatory
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
		if (entityPlayer.getCapability(ModCapabilities.PLAYER_DATA, null).isResearched(ResearchTypes.StarMetal))
		{
			if (!entityPlayer.isPotionActive(Potion.absorption))
			{
				entityPlayer.addPotionEffect(new PotionEffect(Potion.absorption.id, 1200, this.getNumberOfWornPieces(entityPlayer) - 1, false, false));
			}
		}
	}

	/*
	 * ArmorProperties(0, .24, 200); 0 = priority .24 = %age of damage reduced
	 * 200 is the max damage reduced
	 */
	@Override
	public ArmorProperties getProperties(final EntityLivingBase entityLivingBase, final ItemStack itemStack, final DamageSource source, final double damage, final int slot)
	{
		// Remove the ability of thorns to damage armor
		itemStack.setItemDamage(0);

		if ((source == DamageSource.drown) || (source == DamageSource.fall) || (source == DamageSource.inWall) || (source == DamageSource.outOfWorld) || (source == DamageSource.starve) || (source == DamageSource.lava))
		{
			return new ArmorProperties(0, .25, 0);
		}

		if (entityLivingBase instanceof EntityPlayer)
		{
			EntityPlayer entityPlayer = (EntityPlayer) entityLivingBase;
			if (entityPlayer.getCapability(ModCapabilities.PLAYER_DATA, null).isResearched(ResearchTypes.StarMetal))
			{
				if (source instanceof EntityDamageSource)
				{
					if (((EntityDamageSource) source).getEntity() instanceof EntityWerewolf)
					{
						return new ArmorProperties(0, .218, 200);
					}
				}
			}
			else
			{
				return new ArmorProperties(0, this.damageReduceAmount / 50, itemStack.getMaxDamage());
			}
		}
		return new ArmorProperties(0, this.damageReduceAmount / 25D, itemStack.getMaxDamage());
	}

	@Override
	public int getArmorDisplay(final EntityPlayer entityPlayer, final ItemStack itemStack, final int slot)
	{
		if (entityPlayer.getCapability(ModCapabilities.PLAYER_DATA, null).isResearched(ResearchTypes.StarMetal))
		{
			return this.getReductionBasedOffOfSlot(slot);
		}
		else
		{
			return this.getReductionBasedOffOfSlot(slot) / 2;
		}
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
