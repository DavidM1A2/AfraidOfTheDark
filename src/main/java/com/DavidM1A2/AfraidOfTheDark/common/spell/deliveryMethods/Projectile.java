package com.DavidM1A2.AfraidOfTheDark.common.spell.deliveryMethods;

import org.apache.commons.lang3.SerializationUtils;

import com.DavidM1A2.AfraidOfTheDark.AfraidOfTheDark;
import com.DavidM1A2.AfraidOfTheDark.common.entities.spell.EntitySpell;
import com.DavidM1A2.AfraidOfTheDark.common.entities.spell.projectile.EntitySpellProjectile;
import com.DavidM1A2.AfraidOfTheDark.common.spell.Spell;
import com.DavidM1A2.AfraidOfTheDark.common.utility.LogHelper;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

public class Projectile extends DeliveryMethod
{
	@Override
	public double getCost()
	{
		return 0;
	}

	@Override
	public EntitySpell createSpellEntity(EntitySpell previous, int spellStageIndex)
	{
		Spell spellSource = previous.getSpellSource();
		return new EntitySpellProjectile(previous.getSpellSource(), spellStageIndex, previous.posX, previous.posY, previous.posZ, 0, 0, 0, false);
	}

	@Override
	public EntitySpell createSpellEntity(Spell callback)
	{
		EntityPlayer spellOwner = AfraidOfTheDark.proxy.getSpellOwner(callback);
		Spell callbackClone = SerializationUtils.<Spell> clone(callback);
		if (spellOwner != null)
			return new EntitySpellProjectile(callbackClone, 0, spellOwner.posX, spellOwner.posY + 0.8d, spellOwner.posZ, spellOwner.getLookVec().xCoord, spellOwner.getLookVec().yCoord, spellOwner.getLookVec().zCoord, true);
		else
		{
			LogHelper.info("Attempted to create a spell on a player that is offline...");
			return null;
		}
	}

	@Override
	public void writeToNBT(NBTTagCompound compound)
	{
		super.writeToNBT(compound);
		compound.setInteger("id", DeliveryMethods.Projectile.getID());
	}

	@Override
	public void readFromNBT(NBTTagCompound compound)
	{

	}

	@Override
	public DeliveryMethods getType()
	{
		return DeliveryMethods.Projectile;
	}
}
