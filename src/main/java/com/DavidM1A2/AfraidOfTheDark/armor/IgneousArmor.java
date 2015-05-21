package com.DavidM1A2.AfraidOfTheDark.armor;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraftforge.common.ISpecialArmor;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.DavidM1A2.AfraidOfTheDark.initializeMod.ModItems;
import com.DavidM1A2.AfraidOfTheDark.refrence.Refrence;

public class IgneousArmor extends AOTDArmor implements ISpecialArmor
{
	public IgneousArmor(ArmorMaterial armorMaterial, int renderIndex, int type)
	{
		super(armorMaterial, renderIndex, type);
		this.setCreativeTab(Refrence.AFRAID_OF_THE_DARK);
		this.setUnlocalizedName((type == 0) ? "igneousHelmet" : (type == 1) ? "igneousChestplate" : (type == 2) ? "igneousLeggings" : "igneousBoots");
	}

	@Override
	//This is pretty self explanatory
	public String getArmorTexture(ItemStack armor, Entity entity, int slot, String type)
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

	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, EntityPlayer playerIn, List tooltip, boolean advanced)
	{
		tooltip.add("Magical armor will never break.");
	}

	@Override
	public ArmorProperties getProperties(EntityLivingBase entity, ItemStack armor, DamageSource source, double damage, int slot)
	{
		if (entity instanceof EntityPlayer)
		{
			EntityPlayer entityPlayer = (EntityPlayer) entity;

			if (isWearingFullArmor(entityPlayer))
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

		if (source == DamageSource.onFire || source == DamageSource.inFire)
		{
			return new ArmorProperties(0, .25, 25);
		}
		if (source == DamageSource.drown || source == DamageSource.fall || source == DamageSource.inWall || source == DamageSource.outOfWorld || source == DamageSource.starve)
		{
			return new ArmorProperties(0, .25, 0);
		}
		return new ArmorProperties(0, .25, (MathHelper.floor_double(damage * .5) + 5));
	}

	@Override
	public int getArmorDisplay(EntityPlayer player, ItemStack armor, int slot)
	{
		return getReductionBasedOffOfSlot(slot);
	}

	@Override
	public void damageArmor(EntityLivingBase entity, ItemStack stack, DamageSource source, int damage, int slot)
	{
		return;
	}

	private boolean isWearingFullArmor(EntityPlayer entityPlayer)
	{
		if (entityPlayer.inventory.armorInventory[0] != null && entityPlayer.inventory.armorInventory[1] != null && entityPlayer.inventory.armorInventory[2] != null && entityPlayer.inventory.armorInventory[3] != null)
		{
			return (entityPlayer.inventory.armorInventory[0].getItem() instanceof IgneousArmor && entityPlayer.inventory.armorInventory[1].getItem() instanceof IgneousArmor && entityPlayer.inventory.armorInventory[2].getItem() instanceof IgneousArmor && entityPlayer.inventory.armorInventory[3]
					.getItem() instanceof IgneousArmor);
		}
		return false;
	}

	private int getReductionBasedOffOfSlot(int slot)
	{
		return slot == 0 ? 3 : slot == 1 ? 6 : slot == 2 ? 8 : 3;
	}
}
