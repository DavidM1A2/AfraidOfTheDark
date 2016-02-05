package com.DavidM1A2.AfraidOfTheDark.common.spell;

import java.util.Arrays;
import java.util.List;

import org.omg.CORBA.PRIVATE_MEMBER;

import com.DavidM1A2.AfraidOfTheDark.common.entities.spell.EntitySpell;
import com.DavidM1A2.AfraidOfTheDark.common.entities.spell.projectile.EntitySpellProjectile;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class SpellEntityCreator 
{
	public static void createAndSpawn(World world, EntitySpell previousEntitySpell, Spell callback, int spellStageIndex)
	{
		if (!world.isRemote)
			world.spawnEntityInWorld(SpellEntityCreator.create(previousEntitySpell, callback, spellStageIndex));
	}
	
	private static EntitySpell create(EntitySpell previousEntitySpell, Spell callback, int spellStageIndex)
	{
		EntitySpell toReturn = null;
		EntityPlayer spellOwner = callback.getSpellOwner();
		DeliveryMethod deliveryMethod = callback.getSpellStageByIndex(spellStageIndex).getKey();
		
		switch (deliveryMethod) 
		{
			case Projectile:
			{
				if (previousEntitySpell == null)
					toReturn = new EntitySpellProjectile(callback, spellStageIndex, spellOwner.posX, spellOwner.posY + 0.8d, spellOwner.posZ, spellOwner.getLookVec().xCoord, spellOwner.getLookVec().yCoord, spellOwner.getLookVec().zCoord, true);
				else
					toReturn = new EntitySpellProjectile(callback, spellStageIndex, previousEntitySpell.posX, previousEntitySpell.posY, previousEntitySpell.posZ, previousEntitySpell.getLookVec().xCoord, previousEntitySpell.getLookVec().yCoord, previousEntitySpell.getLookVec().zCoord, false);
				break;
			}
		}
		
		return toReturn;
	}
}
