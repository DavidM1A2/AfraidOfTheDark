package com.DavidM1A2.AfraidOfTheDark.common.spell.deliveryMethods;

import com.DavidM1A2.AfraidOfTheDark.common.entities.spell.EntitySpell;
import com.DavidM1A2.AfraidOfTheDark.common.entities.spell.projectile.EntitySpellProjectile;
import com.DavidM1A2.AfraidOfTheDark.common.spell.Spell;

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
		EntityPlayer spellOwner = previous.getSpellSource().getSpellOwner();
		return new EntitySpellProjectile(previous.getSpellSource(), spellStageIndex, previous.posX, previous.posY, previous.posZ, previous.getLookVec().xCoord, previous.getLookVec().yCoord, previous.getLookVec().zCoord, false);
	}

	@Override
	public EntitySpell createSpellEntity(Spell callback)
	{
		EntityPlayer spellOwner = callback.getSpellOwner();
		return new EntitySpellProjectile(callback, 0, spellOwner.posX, spellOwner.posY + 0.8d, spellOwner.posZ, spellOwner.getLookVec().xCoord, spellOwner.getLookVec().yCoord, spellOwner.getLookVec().zCoord, true);
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
}
