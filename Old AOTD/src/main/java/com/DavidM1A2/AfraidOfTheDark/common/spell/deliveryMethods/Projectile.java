package com.DavidM1A2.AfraidOfTheDark.common.spell.deliveryMethods;

import org.apache.commons.lang3.SerializationUtils;

import com.DavidM1A2.AfraidOfTheDark.AfraidOfTheDark;
import com.DavidM1A2.AfraidOfTheDark.common.entities.spell.EntitySpell;
import com.DavidM1A2.AfraidOfTheDark.common.entities.spell.AOE.EntityAOE;
import com.DavidM1A2.AfraidOfTheDark.common.entities.spell.myself.EntityMyself;
import com.DavidM1A2.AfraidOfTheDark.common.entities.spell.projectile.EntitySpellProjectile;
import com.DavidM1A2.AfraidOfTheDark.common.entities.spell.projectile.EntitySpellProjectileDive;
import com.DavidM1A2.AfraidOfTheDark.common.spell.Spell;
import com.DavidM1A2.AfraidOfTheDark.common.utility.LogHelper;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

public class Projectile extends DeliveryMethod
{
	@Override
	public double getCost()
	{
		return 5;
	}

	@Override
	public double getStageMultiplier()
	{
		return 1.1;
	}

	@Override
	public EntitySpell[] createSpellEntity(EntitySpell previous, int spellStageIndex)
	{
		Spell spellSource = previous.getSpellSource();
		if (previous instanceof EntitySpellProjectile)
		{
			return new EntitySpell[]
			{ new EntitySpellProjectileDive(previous.getSpellSource(), null, spellStageIndex, previous.posX, previous.posY, previous.posZ) };
		}
		else if (previous instanceof EntityMyself)
		{
			EntityLivingBase target = ((EntityMyself) previous).getTarget();
			return new EntitySpell[]
			{ new EntitySpellProjectile(spellSource, target, spellStageIndex, target.posX, target.posY + target.getEyeHeight(), target.posZ, target.getLookVec().x, target.getLookVec().y, target.getLookVec().z) };
		}
		else if (previous instanceof EntityAOE)
		{
			int numSpells = 5;
			EntitySpell[] spells = new EntitySpell[numSpells];
			for (int i = 0; i < numSpells; i++)
				spells[i] = new EntitySpellProjectile(spellSource, null, spellStageIndex, previous.posX, previous.posY, previous.posZ, previous.world.rand.nextDouble() - 0.5, previous.world.rand.nextDouble() - 0.5, previous.world.rand.nextDouble() - 0.5);
			return spells;
		}
		else
		{
			LogHelper.error("Previous spell entity was " + previous + ", and no case to handle it was found in " + this.getType().getNameFormatted());
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
			{ new EntitySpellProjectile(callbackClone, spellOwner, 0, spellOwner.posX, spellOwner.posY + 0.8d, spellOwner.posZ, spellOwner.getLookVec().x, spellOwner.getLookVec().y, spellOwner.getLookVec().z) };
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
		return DeliveryMethods.Projectile;
	}
}
