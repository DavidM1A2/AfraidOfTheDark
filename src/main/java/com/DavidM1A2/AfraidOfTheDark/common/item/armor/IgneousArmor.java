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
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class IgneousArmor extends AOTDArmor
{
	public IgneousArmor(final ArmorMaterial armorMaterial, final int renderIndex, final EntityEquipmentSlot equipmentSlot)
	{
		super(armorMaterial, renderIndex, equipmentSlot);
		this.setUnlocalizedName((equipmentSlot == EntityEquipmentSlot.HEAD) ? "igneousHelmet" : (equipmentSlot == EntityEquipmentSlot.CHEST) ? "igneousChestplate" : (equipmentSlot == EntityEquipmentSlot.LEGS) ? "igneousLeggings" : "igneousBoots");
	}

	@Override
	// This is pretty self explanatory
	public String getArmorTexture(final ItemStack armor, final Entity entity, final EntityEquipmentSlot equipmentSlot, final String type)
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
	 * ArmorProperties(0, .24, 200); 0 = priority .24 = %age of damage reduced
	 * 200 is the max damage reduced
	 */
	@Override
	public ArmorProperties getProperties(final EntityLivingBase entity, final ItemStack armor, final DamageSource source, final double damage, final int slot)
	{
		// Remove the ability of thorns to damage armor
		armor.setItemDamage(0);
		if (entity instanceof EntityPlayer)
		{
			final EntityPlayer entityPlayer = (EntityPlayer) entity;

			if (entityPlayer.getCapability(ModCapabilities.PLAYER_DATA, null).isResearched(ResearchTypes.Igneous))
			{
				if (this.isWearingFullArmor(entityPlayer))
				{
					if (source.getEntity() != null)
					{
						source.getEntity().setFire(5);

						double knockbackStrength = 1.0;
						double motionX = entityPlayer.getPosition().getX() - source.getEntity().getPosition().getX();
						double motionZ = entityPlayer.getPosition().getZ() - source.getEntity().getPosition().getZ();
						double hypotenuse = MathHelper.sqrt_double(motionX * motionX + motionZ * motionZ);
						source.getEntity().addVelocity(-motionX * knockbackStrength * 0.6000000238418579D / hypotenuse, 0.1D, -motionZ * knockbackStrength * 0.6000000238418579D / hypotenuse);
					}
					if (entity.isBurning())
					{
						entity.extinguish();
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
					if (((EntityDamageSource) source).getEntity() instanceof EntityWerewolf)
					{
						return new ArmorProperties(0, .21, 200);
					}
				}
			}
			else
			{
				return new ArmorProperties(0, this.damageReduceAmount / 50D, armor.getMaxDamage());
			}
		}

		// Default armor calculation
		return new ArmorProperties(0, this.damageReduceAmount / 25D, armor.getMaxDamage());
	}

	@Override
	public int getArmorDisplay(final EntityPlayer entityPlayer, final ItemStack itemStack, final int slot)
	{
		if (entityPlayer.getCapability(ModCapabilities.PLAYER_DATA, null).isResearched(ResearchTypes.Igneous))
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

	private boolean isWearingFullArmor(final EntityPlayer entityPlayer)
	{
		if ((entityPlayer.inventory.armorInventory.get(0) != null) && (entityPlayer.inventory.armorInventory.get(1) != null) && (entityPlayer.inventory.armorInventory.get(2) != null) && (entityPlayer.inventory.armorInventory.get(3) != null))
		{
			return ((entityPlayer.inventory.armorInventory.get(0).getItem() instanceof IgneousArmor) && (entityPlayer.inventory.armorInventory.get(1).getItem() instanceof IgneousArmor) && (entityPlayer.inventory.armorInventory.get(2).getItem() instanceof IgneousArmor)
					&& (entityPlayer.inventory.armorInventory.get(3).getItem() instanceof IgneousArmor));
		}
		return false;
	}

	private int getReductionBasedOffOfSlot(final int slot)
	{
		return slot == 0 ? 3 : slot == 1 ? 6 : slot == 2 ? 8 : 3;
	}
}
