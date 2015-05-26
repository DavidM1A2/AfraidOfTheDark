package com.DavidM1A2.AfraidOfTheDark.armor;

import java.util.List;

import com.DavidM1A2.AfraidOfTheDark.initializeMod.ModItems;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.ISpecialArmor.ArmorProperties;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class StarMetalArmor extends AOTDArmor
{
	public StarMetalArmor(ArmorMaterial armorMaterial, int renderIndex, int type) 
	{
		super(armorMaterial, renderIndex, type);
		this.setUnlocalizedName((type == 0) ? "starMetalHelmet" : (type == 1) ? "starMetalChestplate" : (type == 2) ? "starMetalLeggings" : "starMetalBoots");
	}
	
	@Override
	//This is pretty self explanatory
	public String getArmorTexture(ItemStack armor, Entity entity, int slot, String type)
	{
		if (armor.getItem() == ModItems.starMetalLeggings)
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
	public void addInformation(ItemStack stack, EntityPlayer playerIn, List tooltip, boolean advanced)
	{
		tooltip.add("Magical armor will never break.");
	}
	
    /**
     * Called to tick armor in the armor slot. Override to do something
     */
    public void onArmorTick(World world, EntityPlayer entityPlayer, ItemStack itemStack)
    {
    	if (!entityPlayer.isPotionActive(Potion.absorption))
    	{
    		entityPlayer.addPotionEffect(new PotionEffect(Potion.absorption.id, 1200, MathHelper.floor_double(this.getNumberOfWornPieces(entityPlayer) * 1.5)));
    	}
    }

	@Override
	public ArmorProperties getProperties(EntityLivingBase player, ItemStack armor, DamageSource source, double damage, int slot) 
	{
		if (source == DamageSource.drown || source == DamageSource.fall || source == DamageSource.inWall || source == DamageSource.outOfWorld || source == DamageSource.starve || source == DamageSource.lava)
		{
			return new ArmorProperties(0, .25, 0);
		}

		// Remove the ability of thorns to damage armor
		armor.setItemDamage(0);

		return new ArmorProperties(0, this.damageReduceAmount / 25D, armor.getMaxDamage());
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
	
	private int getReductionBasedOffOfSlot(int slot)
	{
		return slot == 0 ? 3 : slot == 1 ? 6 : slot == 2 ? 8 : 3;
	}
	
	private int getNumberOfWornPieces(EntityPlayer entityPlayer)
	{
		int number = 0;
		for (int i = 0; i < entityPlayer.inventory.armorInventory.length; i++)
		{
			if (entityPlayer.inventory.armorInventory[i] != null && entityPlayer.inventory.armorInventory[i].getItem() instanceof StarMetalArmor)
			{
				number = number + 1;
			}
		}
		return number;
	}
}
