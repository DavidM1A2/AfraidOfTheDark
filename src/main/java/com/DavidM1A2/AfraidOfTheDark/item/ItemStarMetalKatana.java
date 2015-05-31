package com.DavidM1A2.AfraidOfTheDark.item;

import java.util.List;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.DavidM1A2.AfraidOfTheDark.refrence.Refrence;
import com.DavidM1A2.AfraidOfTheDark.threads.Cooldown;
import com.DavidM1A2.AfraidOfTheDark.utility.LogHelper;

public class ItemStarMetalKatana extends AOTDSword implements IHasCooldown
{
	private static final int HITRANGE = 5;
	private Cooldown cooldown;
	
	public ItemStarMetalKatana()
	{
		super(Refrence.starMetalTool);
		this.setUnlocalizedName("starMetalKatana");
		cooldown = new Cooldown(this);
	}
	
	@Override
	public ItemStack onItemRightClick(final ItemStack itemStack, final World world, final EntityPlayer entityPlayer)
	{
		if (!cooldown.onCooldown())
		{
			if (!world.isRemote)
			{
				List entityList = world.getEntitiesWithinAABBExcludingEntity(entityPlayer, entityPlayer.getEntityBoundingBox().expand(HITRANGE, HITRANGE, HITRANGE));
				for(Object entityObject : entityList)
				{
					if (entityObject instanceof EntityPlayer || entityObject instanceof EntityLiving)
					{
						EntityLivingBase entity = (EntityLivingBase) entityObject;
						double knockbackStrength = EnchantmentHelper.getEnchantmentLevel(Enchantment.field_180313_o.effectId, itemStack) + 2;
						
						double motionX = entityPlayer.getPosition().getX() - entity.getPosition().getX();
						double motionZ = entityPlayer.getPosition().getZ() - entity.getPosition().getZ();
						
						double hypotenuse = MathHelper.sqrt_double(motionX * motionX + motionZ * motionZ);
						
						entity.addVelocity(-motionX * knockbackStrength * 0.6000000238418579D / hypotenuse, 0.1D, -motionZ * knockbackStrength * 0.6000000238418579D / hypotenuse);
					}
				}
			}

			cooldown = new Cooldown(this);
			cooldown.start();
		}	
		else 
		{
			if (world.isRemote)
			{
				entityPlayer.addChatMessage(new ChatComponentText(this.cooldown.onCooldown() ? ("Cooldown remaining: " + (this.cooldown.getSecondsRemaining() + 1) + " second" + ((this.cooldown.getSecondsRemaining() == 0) ? "." : "s.")) : "Ready to Use"));
			}
		}
		return super.onItemRightClick(itemStack, world, entityPlayer);	
	}
	
	/**
	 * allows items to add custom lines of information to the mouseover description
	 *
	 * @param tooltip All lines to display in the Item's tooltip. This is a List of Strings.
	 * @param advanced Whether the setting "Advanced tooltips" is enabled
	 */
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(final ItemStack stack, final EntityPlayer playerIn, final List tooltip, final boolean advanced)
	{
		tooltip.add(this.cooldown.onCooldown() ? ("Cooldown remaining: " + (this.cooldown.getSecondsRemaining() + 1) + " second" + ((this.cooldown.getSecondsRemaining() == 0) ? "." : "s.")) : "Ready to Use");
	}

	@Override
	public void cooldownCallback() 
	{
		
	}

	@Override
	public int getItemCooldownInMillis() 
	{
		return 10000;
	}
	
}
