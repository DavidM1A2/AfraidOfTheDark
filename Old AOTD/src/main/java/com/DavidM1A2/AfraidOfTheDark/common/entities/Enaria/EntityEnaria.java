/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.entities.Enaria;

import com.DavidM1A2.AfraidOfTheDark.common.MCACommonLibrary.IMCAnimatedEntity;
import com.DavidM1A2.AfraidOfTheDark.common.MCACommonLibrary.animation.AnimationHandler;
import com.DavidM1A2.AfraidOfTheDark.common.entities.ICanTakeSilverDamage;
import com.DavidM1A2.AfraidOfTheDark.common.initializeMod.ModCapabilities;
import com.DavidM1A2.AfraidOfTheDark.common.reference.ResearchTypes;
import com.mojang.realmsclient.gui.ChatFormatting;

import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.BossInfo;
import net.minecraft.world.BossInfoServer;
import net.minecraft.world.World;

public class EntityEnaria extends EntityMob implements IMCAnimatedEntity, ICanTakeSilverDamage
{
	protected AnimationHandler animHandler = new AnimationHandlerEnaria(this);

	private static final double MOVE_SPEED = 0.6D;
	private static final double AGRO_RANGE = 16.0D;
	private static final double FOLLOW_RANGE = 32.0D;
	private static final double MAX_HEALTH = 400.0D;
	private static final double ATTACK_DAMAGE = 12.0D;
	private static final double KNOCKBACK_RESISTANCE = 0.5D;
	private static final double RESEARCH_UNLOCK_RANGE = 50D;
	public static final String IS_VALID = "isValid";
	public static final String LAST_HIT = "lastHit";
	private static final int MAX_DAMAGE_IN_1_HIT = 10;
	private final EnariaAttacks enariaAttacks;

	private final BossInfoServer bossInfo = (BossInfoServer) (new BossInfoServer(this.getDisplayName(), BossInfo.Color.PURPLE, BossInfo.Overlay.PROGRESS)).setDarkenSky(true);

	public EntityEnaria(World world)
	{
		super(world);
		this.setSize(0.8F, 1.8F);
		this.experienceValue = 50;
		this.isImmuneToFire = true;

		this.setCustomNameTag("Enaria");

		this.tasks.addTask(1, new EntityAISwimming(this));
		this.tasks.addTask(2, new EntityAIAttackEnaria(this));
		this.tasks.addTask(3, new EntityAIFollowTarget(this, 8.0D));
		this.tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 20.0F));

		this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, true));

		this.enariaAttacks = new EnariaAttacks(this, this.rand);
	}

	@Override
	public boolean isNonBoss()
	{
		return false;
	}

	@Override
	public void onLivingUpdate()
	{
		if (!this.world.isRemote)
		{
			this.bossInfo.setPercent(this.getHealth() / this.getMaxHealth());
			if (this.ticksExisted == 1)
				if (!this.getEntityData().getBoolean(IS_VALID))
					this.setDead();
		}
		super.onLivingUpdate();
	}

	// Apply entity attributes
	@Override
	protected void applyEntityAttributes()
	{
		super.applyEntityAttributes();
		this.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(EntityEnaria.MAX_HEALTH);
		this.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(EntityEnaria.FOLLOW_RANGE);
		this.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(EntityEnaria.KNOCKBACK_RESISTANCE);
		this.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(EntityEnaria.MOVE_SPEED);
		this.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(EntityEnaria.ATTACK_DAMAGE);
	}

	/**
	 * Add the given player to the list of players tracking this entity. For INSTANCE, a player may track a boss in order to view its associated boss
	 * bar.
	 */
	@Override
	public void addTrackingPlayer(EntityPlayerMP player)
	{
		super.addTrackingPlayer(player);
		this.bossInfo.addPlayer(player);
	}

	/**
	 * Removes the given player from the list of players tracking this entity. See {@link Entity#addTrackingPlayer} for more information on tracking.
	 */
	@Override
	public void removeTrackingPlayer(EntityPlayerMP player)
	{
		super.removeTrackingPlayer(player);
		this.bossInfo.removePlayer(player);
	}

	// Only take damage from silver weapons
	@Override
	public boolean attackEntityFrom(final DamageSource damageSource, float damage)
	{
		long timeBetweenHits = System.currentTimeMillis() - this.getEntityData().getLong(LAST_HIT);
		this.getEntityData().setLong(LAST_HIT, System.currentTimeMillis());

		// /kill the entity
		if (damage == Float.MAX_VALUE)
		{
			return super.attackEntityFrom(damageSource, damage);
		}
		// Cant nuke enaria
		else if (damage > MAX_DAMAGE_IN_1_HIT)
		{
			damage = MAX_DAMAGE_IN_1_HIT;
		}

		if (damageSource instanceof EntityDamageSource)
		{
			Entity source = ((EntityDamageSource) damageSource).getTrueSource();
			if (source instanceof EntityPlayer)
			{
				EntityPlayer entityPlayer = (EntityPlayer) source;
				if (!entityPlayer.getCapability(ModCapabilities.PLAYER_DATA, null).isResearched(ResearchTypes.Enaria.getPrevious()))
				{
					if (!entityPlayer.world.isRemote)
						entityPlayer.sendMessage(new TextComponentString("I can't understand who I'm fighting...."));
					return false;
				}
			}
			if (damageSource.damageType.equals("silverDamage"))
			{
				if (timeBetweenHits < 1000)
				{
					if (timeBetweenHits > 800)
					{
						damage = damage * 0.75f;
					}
					else if (timeBetweenHits > 500)
					{
						damage = damage * 0.35f;
					}
					else
					{
						damage = 1.0f;
						// 1/70 chance to teleport if taking silver damage
						if (this.rand.nextInt(40) == 0)
						{
							this.enariaAttacks.randomTeleport();
						}
					}
				}

				return super.attackEntityFrom(damageSource, damage);
			}
			else
			{
				// 1/60 chance to tele if taking non-silver, player damage
				if (this.rand.nextInt(60) == 0)
				{
					this.enariaAttacks.randomTeleport();
				}
			}
		}
		else
		{
			// 1/100 chance to tele if taking non-player damage
			if (this.rand.nextInt(100) == 0)
			{
				this.enariaAttacks.randomTeleport();
			}
		}

		return super.attackEntityFrom(DamageSource.GENERIC, 1);
	}

	@Override
	public void onDeath(DamageSource cause)
	{
		super.onDeath(cause);

		if (cause instanceof EntityDamageSource)
		{
			if (cause.getTrueSource() instanceof EntityPlayer)
			{
				for (EntityPlayer entityPlayer : this.world.getEntitiesWithinAABB(EntityPlayer.class, this.getEntityBoundingBox().expand(RESEARCH_UNLOCK_RANGE, RESEARCH_UNLOCK_RANGE, RESEARCH_UNLOCK_RANGE)))
				{
					if (!world.isRemote)
					{
						if (entityPlayer.getCapability(ModCapabilities.PLAYER_DATA, null).canResearch(ResearchTypes.Enaria))
						{
							entityPlayer.getCapability(ModCapabilities.PLAYER_DATA, null).unlockResearch(ResearchTypes.Enaria, true);
						}
					}
				}
			}
		}
	}

	@Override
	public String getCustomNameTag()
	{
		return ChatFormatting.RED + "" + ChatFormatting.BOLD + super.getDisplayName().getUnformattedComponentText();
	}

	@Override
	public ITextComponent getDisplayName()
	{
		return new TextComponentString(ChatFormatting.RED + "" + ChatFormatting.BOLD + super.getDisplayName().getUnformattedComponentText());
	}

	/**
	 * Moves the entity based on the specified heading.
	 */
	@Override
	public void moveRelative(float strafe, float up, float forward, float friction)
	{
		if (this.world.isRemote)
			if (this.motionX > 0.05 || this.motionZ > 0.05 || this.motionX < -0.05 || this.motionZ < -0.05)
				if (!this.animHandler.isAnimationActive("spell") && !animHandler.isAnimationActive("autoattack") && !animHandler.isAnimationActive("armthrow") && !animHandler.isAnimationActive("walk"))
					animHandler.activateAnimation("walk", 0);

		super.moveRelative(strafe, up, forward, friction);
	}

	@Override
	protected boolean canBeRidden(Entity entityIn)
	{
		return false;
	}

	/**
	 * Determines if an entity can be despawned, used on idle far away entities
	 */
	@Override
	protected boolean canDespawn()
	{
		return false;
	}

	@Override
	public AnimationHandler getAnimationHandler()
	{
		return animHandler;
	}

	public EnariaAttacks getEnariaAttacks()
	{
		return this.enariaAttacks;
	}
}
