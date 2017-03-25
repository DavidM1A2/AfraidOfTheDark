/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.entities.Enaria.ghastly;

import com.DavidM1A2.AfraidOfTheDark.common.MCACommonLibrary.IMCAnimatedEntity;
import com.DavidM1A2.AfraidOfTheDark.common.MCACommonLibrary.animation.AnimationHandler;
import com.DavidM1A2.AfraidOfTheDark.common.initializeMod.ModCapabilities;
import com.DavidM1A2.AfraidOfTheDark.common.reference.AOTDDimensions;
import com.DavidM1A2.AfraidOfTheDark.common.reference.ResearchTypes;
import com.mojang.realmsclient.gui.ChatFormatting;

import net.minecraft.entity.EntityFlying;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class EntityGhastlyEnaria extends EntityFlying implements IMCAnimatedEntity
{
	protected AnimationHandler animHandler = new AnimationHandlerGhastlyEnaria(this);

	private static final double MOVE_SPEED = 0.02D;
	private static final double AGRO_RANGE = 300.0D;
	private static final double FOLLOW_RANGE = 300.0D;
	private static final double MAX_HEALTH = 9001.0D;
	private static final double ATTACK_DAMAGE = 900.0D;
	private static final double KNOCKBACK_RESISTANCE = 1.0D;
	private static final double PLAYER_CHECK_FREQUENCY = 10;
	private boolean benign;

	public EntityGhastlyEnaria(World worldIn)
	{
		super(worldIn);
		this.setSize(0.8F, 1.8F);
		this.setCustomNameTag("Ghastly Enaria");
		this.noClip = true;
		this.rotationYaw = this.rotationYawHead = this.rand.nextFloat() * 360;
		this.isImmuneToFire = true;

		this.moveHelper = new GhastlyEnariaMoveHelper(this);
		this.tasks.addTask(1, new GhastlyEnariaPlayerChase(this));
	}

	@Override
	public void onEntityUpdate()
	{
		super.onEntityUpdate();
		if (this.ticksExisted == 1)
		{
			EntityPlayer closest = this.world.getClosestPlayer(this.posX, this.posY, this.posZ, AOTDDimensions.getBlocksBetweenIslands() / 2, false);
			if (closest == null)
				this.setBenign(true);
			else
				this.setBenign(!closest.getCapability(ModCapabilities.PLAYER_DATA, null).isResearched(ResearchTypes.Enaria));
		}

		if (this.world.isRemote)
			if (this.isBenign())
				if (!this.getAnimationHandler().isAnimationActive("dance"))
					this.getAnimationHandler().activateAnimation("dance", 0);

		if (!this.world.isRemote)
			if (this.ticksExisted % PLAYER_CHECK_FREQUENCY == 0)
			{
				EntityPlayer entityPlayer = this.world.getClosestPlayerToEntity(this, 3);
				if (entityPlayer != null && !entityPlayer.isDead)
					AOTDDimensions.Nightmare.fromDimensionTo(entityPlayer.getCapability(ModCapabilities.PLAYER_DATA, null).getPlayerDimensionPreTeleport(), ((EntityPlayerMP) entityPlayer));
			}
	}

	// Apply entity attributes
	@Override
	protected void applyEntityAttributes()
	{
		if (this.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.MAX_HEALTH) == null)
		{
			this.getAttributeMap().registerAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(EntityGhastlyEnaria.MAX_HEALTH);
		}
		if (this.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.FOLLOW_RANGE) == null)
		{
			this.getAttributeMap().registerAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(EntityGhastlyEnaria.FOLLOW_RANGE);
		}
		if (this.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.KNOCKBACK_RESISTANCE) == null)
		{
			this.getAttributeMap().registerAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(EntityGhastlyEnaria.KNOCKBACK_RESISTANCE);
		}
		if (this.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.MOVEMENT_SPEED) == null)
		{
			this.getAttributeMap().registerAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(EntityGhastlyEnaria.MOVE_SPEED);
		}
		if (this.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.ATTACK_DAMAGE) == null)
		{
			this.getAttributeMap().registerAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(EntityGhastlyEnaria.ATTACK_DAMAGE);
		}
	}

	// Only take damage from silver weapons
	@Override
	public boolean attackEntityFrom(final DamageSource damageSource, float damage)
	{
		if (damageSource == DamageSource.OUT_OF_WORLD)
			return super.attackEntityFrom(damageSource, damage);
		return false;
	}

	@Override
	public String getCustomNameTag()
	{
		return ChatFormatting.RED + "" + ChatFormatting.BOLD + super.getDisplayName().getUnformattedComponentText();
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

	@Override
	public AnimationHandler getAnimationHandler()
	{
		return this.animHandler;
	}
}
