/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.entities.Enaria.ghastly;

import com.DavidM1A2.AfraidOfTheDark.common.utility.LogHelper;

import net.minecraft.entity.EntityFlying;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import net.minecraft.world.World;

public class EntityGhastlyEnaria extends EntityFlying //implements IMCAnimatedEntity
{
	//protected AnimationHandler animHandler = new AnimationHandlerGhastlyEnaria(this);

	private static final double MOVE_SPEED = 0.02D;
	private static final double AGRO_RANGE = 300.0D;
	private static final double FOLLOW_RANGE = 300.0D;
	private static final double MAX_HEALTH = 9001.0D;
	private static final double ATTACK_DAMAGE = 900.0D;
	private static final double KNOCKBACK_RESISTANCE = 1.0D;
	private boolean benign;

	public EntityGhastlyEnaria(World worldIn)
	{
		super(worldIn);
		this.setSize(0.8F, 1.8F);
		this.setCustomNameTag("Ghastly Enaria");
		this.noClip = true;

		this.moveHelper = new GhastlyEnariaMoveHelper(this);
		this.tasks.addTask(1, new GhastlyEnariaPlayerChase(this));
		//this.tasks.addTask(2, new EntityAIWatchClosest(this, EntityPlayer.class, 500, 1));
	}

	@Override
	public void onEntityUpdate()
	{
		LogHelper.info(this.rotationYaw);
		//if (this.isBenign())
		//{
		//}
		//			if (!worldObj.isRemote)
		//			{
		//				//AfraidOfTheDark.instance.getPacketHandler().sendToAllAround(new SyncAnimation("dance", this.getEntityId(), "dance"), this, 500);
		//			}
	}

	// Apply entity attributes
	@Override
	protected void applyEntityAttributes()
	{
		if (this.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.maxHealth) == null)
		{
			this.getAttributeMap().registerAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(EntityGhastlyEnaria.MAX_HEALTH);
		}
		if (this.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.followRange) == null)
		{
			this.getAttributeMap().registerAttribute(SharedMonsterAttributes.followRange).setBaseValue(EntityGhastlyEnaria.FOLLOW_RANGE);
		}
		if (this.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.knockbackResistance) == null)
		{
			this.getAttributeMap().registerAttribute(SharedMonsterAttributes.knockbackResistance).setBaseValue(EntityGhastlyEnaria.KNOCKBACK_RESISTANCE);
		}
		if (this.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.movementSpeed) == null)
		{
			this.getAttributeMap().registerAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(EntityGhastlyEnaria.MOVE_SPEED);
		}
		if (this.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.attackDamage) == null)
		{
			this.getAttributeMap().registerAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(EntityGhastlyEnaria.ATTACK_DAMAGE);
		}
	}

	// Only take damage from silver weapons
	@Override
	public boolean attackEntityFrom(final DamageSource damageSource, float damage)
	{
		if (damageSource == DamageSource.outOfWorld)
			return super.attackEntityFrom(damageSource, damage);
		return false;
	}

	@Override
	public IChatComponent getDisplayName()
	{
		return new ChatComponentText(EnumChatFormatting.RED + "" + EnumChatFormatting.BOLD + super.getDisplayName().getUnformattedTextForChat());
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound tagCompound)
	{
		super.writeEntityToNBT(tagCompound);
		tagCompound.setBoolean("benign", this.benign);
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound tagCompund)
	{
		super.readEntityFromNBT(tagCompund);
		this.benign = tagCompund.getBoolean("benign");
	}

	/**
	 * Determines if an entity can be despawned, used on idle far away entities
	 */
	@Override
	protected boolean canDespawn()
	{
		return false;
	}

	public boolean isBenign()
	{
		return this.benign;
	}

	public void setBenign(boolean benign)
	{
		this.benign = benign;
	}

	/*
	@Override
	public AnimationHandler getAnimationHandler()
	{
		return this.animHandler;
	}
	*/
}
