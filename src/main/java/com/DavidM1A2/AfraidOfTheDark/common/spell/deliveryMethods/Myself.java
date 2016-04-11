package com.DavidM1A2.AfraidOfTheDark.common.spell.deliveryMethods;

import org.apache.commons.lang3.SerializationUtils;

import com.DavidM1A2.AfraidOfTheDark.AfraidOfTheDark;
import com.DavidM1A2.AfraidOfTheDark.common.entities.spell.EntitySpell;
import com.DavidM1A2.AfraidOfTheDark.common.entities.spell.myself.EntityMyself;
import com.DavidM1A2.AfraidOfTheDark.common.entities.spell.projectile.EntitySpellProjectile;
import com.DavidM1A2.AfraidOfTheDark.common.spell.Spell;
import com.DavidM1A2.AfraidOfTheDark.common.utility.LogHelper;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

public class Myself extends DeliveryMethod
{
	@Override
	public double getCost()
	{
		return 0;
	}

	@Override
	public double getStageMultiplier()
	{
		return 0;
	}

	@Override
	public EntitySpell createSpellEntity(EntitySpell previous, int spellStageIndex)
	{
		if (previous instanceof EntityMyself)
		{
			return new EntityMyself(previous.worldObj, previous.getSpellSource(), spellStageIndex, ((EntityMyself) previous).getTarget());
		}
		else if (previous instanceof EntitySpellProjectile)
		{
			EntityLivingBase targetHit = ((EntitySpellProjectile) previous).getTargetHit();
			return new EntityMyself(previous.worldObj, previous.getSpellSource(), spellStageIndex, targetHit);
		}
		else
		{
			LogHelper.error("Previous spell entity was " + previous + ", and no case to handle it was found in " + this.getType().getName());
			return null;
		}
	}

	@Override
	public EntitySpell createSpellEntity(Spell callback)
	{
		EntityPlayer spellOwner = AfraidOfTheDark.proxy.getSpellOwner(callback);
		Spell callbackClone = SerializationUtils.<Spell> clone(callback);

		if (spellOwner != null)
		{
			return new EntityMyself(spellOwner.worldObj, callbackClone, 0, spellOwner);
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
		return DeliveryMethods.Myself;
	}

}
