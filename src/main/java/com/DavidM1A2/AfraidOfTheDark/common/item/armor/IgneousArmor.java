/*
 * Author: David Slovikosky 
 * Mod: Afraid of the Dark 
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.item.armor;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.DavidM1A2.AfraidOfTheDark.common.entities.POOPER123.EntityPOOPER123;
import com.DavidM1A2.AfraidOfTheDark.common.initializeMod.ModItems;

public class IgneousArmor extends AOTDArmor
{
	public IgneousArmor(final ArmorMaterial armorMaterial, final int renderIndex, final int type)
	{
		super(armorMaterial, renderIndex, type);
		this.setUnlocalizedName((type == 0) ? "igneousHelmet" : (type == 1) ? "igneousChestplate" : (type == 2) ? "igneousLeggings" : "igneousBoots");
	}

	@Override
	//This is pretty self explanatory
	public String getArmorTexture(final ItemStack armor, final Entity entity, final int slot, final String type)
	{
		if (armor.getItem() == ModItems.igneousLeggings)
		{
			return "afraidofthedark:textures/armor/igneous_2.png";
		}
		else
		{
			return "afraidofthedark:textures/armor/igneous_1.png";
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(final ItemStack stack, final EntityPlayer playerIn, final List tooltip, final boolean advanced)
	{
		tooltip.add("Magical armor will never break.");
	}

	/*
	 * ArmorProperties(0, .24, 200);
	 * 0 = priority
	 * .24 = %age of damage reduced
	 * 200 is the max damage reduced
	 */
	@Override
	public ArmorProperties getProperties(final EntityLivingBase entity, final ItemStack armor, final DamageSource source, final double damage, final int slot)
	{
		if (entity instanceof EntityPlayer)
		{
			final EntityPlayer entityPlayer = (EntityPlayer) entity;

			if (this.isWearingFullArmor(entityPlayer))
			{
				if (source.getEntity() != null)
				{
					source.getEntity().setFire(5);
				}
				if (entity.isBurning())
				{
					entity.extinguish();
				}
			}
		}

		if ((source == DamageSource.onFire) || (source == DamageSource.inFire))
		{
			return new ArmorProperties(0, .25, 25);
		}
		else if ((source == DamageSource.drown) || (source == DamageSource.fall) || (source == DamageSource.inWall) || (source == DamageSource.outOfWorld) || (source == DamageSource.starve))
		{
			return new ArmorProperties(0, .25, 0);
		}
		else if (source instanceof EntityDamageSource)
		{
			if (((EntityDamageSource) source).getEntity() instanceof EntityPOOPER123)
			{
				return new ArmorProperties(0, .21, 200);
			}
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

	private boolean isWearingFullArmor(final EntityPlayer entityPlayer)
	{
		if ((entityPlayer.inventory.armorInventory[0] != null) && (entityPlayer.inventory.armorInventory[1] != null) && (entityPlayer.inventory.armorInventory[2] != null) && (entityPlayer.inventory.armorInventory[3] != null))
		{
			return ((entityPlayer.inventory.armorInventory[0].getItem() instanceof IgneousArmor) && (entityPlayer.inventory.armorInventory[1].getItem() instanceof IgneousArmor) && (entityPlayer.inventory.armorInventory[2].getItem() instanceof IgneousArmor) && (entityPlayer.inventory.armorInventory[3]
					.getItem() instanceof IgneousArmor));
		}
		return false;
	}

	private int getReductionBasedOffOfSlot(final int slot)
	{
		return slot == 0 ? 3 : slot == 1 ? 6 : slot == 2 ? 8 : 3;
	}
}
