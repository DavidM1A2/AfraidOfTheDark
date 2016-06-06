package com.DavidM1A2.AfraidOfTheDark.common.spell.deliveryMethods;

import org.apache.commons.lang3.SerializationUtils;

import com.DavidM1A2.AfraidOfTheDark.AfraidOfTheDark;
import com.DavidM1A2.AfraidOfTheDark.common.entities.spell.EntitySpell;
import com.DavidM1A2.AfraidOfTheDark.common.entities.spell.AOE.EntityAOE;
import com.DavidM1A2.AfraidOfTheDark.common.entities.spell.myself.EntityMyself;
import com.DavidM1A2.AfraidOfTheDark.common.entities.spell.projectile.EntitySpellProjectile;
import com.DavidM1A2.AfraidOfTheDark.common.spell.Spell;
import com.DavidM1A2.AfraidOfTheDark.common.utility.LogHelper;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;

public class AOE extends DeliveryMethod
{
	@Override
	public double getCost()
	{
		return 15;
	}

	@Override
	public double getStageMultiplier()
	{
		return 3;
	}

	@Override
	public EntitySpell[] createSpellEntity(EntitySpell previous, int spellStageIndex)
	{
		Spell spellSource = previous.getSpellSource();
		if (previous instanceof EntityAOE)
		{
			return new EntitySpell[]
			{ new EntityAOE(previous.worldObj, spellSource, spellStageIndex, ((EntityAOE) previous).getSize() + 5.0, new BlockPos(previous)) };
		}
		else if (previous instanceof EntitySpellProjectile)
		{
			return new EntitySpell[]
			{ new EntityAOE(previous.worldObj, spellSource, spellStageIndex, 5.0, new BlockPos(previous)) };
		}
		else if (previous instanceof EntityMyself)
		{
			EntityLivingBase target = ((EntityMyself) previous).getTarget();
			return new EntitySpell[]
			{ new EntityAOE(previous.worldObj, spellSource, spellStageIndex, 10.0, new BlockPos(target)) };
		}
		else
		{
			LogHelper.error("Previous spell entity was " + previous + ", and no case to handle it was found in " + this.getType().getName());
			return null;
		}
	}

	@Override
	public EntitySpell[] createSpellEntity(Spell callback)
	{
		EntityPlayer spellOwner = AfraidOfTheDark.proxy.getSpellOwner(callback);
		Spell callbackClone = SerializationUtils.<Spell> clone(callback);

		if (spellOwner != null)
		{
			return new EntitySpell[]
			{ new EntityAOE(spellOwner.worldObj, callbackClone, 0, 5, new BlockPos(spellOwner)) };
		}
		else
		{
			LogHelper.info("Attempted to create a spell on a player that is offline...");
			return null;
		}
	}

	@Override
	public void readFromNBT(NBTTagCompound compound)
	{
	}

	@Override
	public DeliveryMethods getType()
	{
		return DeliveryMethods.AOE;
	}
}
