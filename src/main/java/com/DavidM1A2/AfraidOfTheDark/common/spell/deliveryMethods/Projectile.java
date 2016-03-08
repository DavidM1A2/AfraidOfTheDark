package com.DavidM1A2.AfraidOfTheDark.common.spell.deliveryMethods;

import com.DavidM1A2.AfraidOfTheDark.client.entities.spell.ModelSpellProjectile;
import com.DavidM1A2.AfraidOfTheDark.client.entities.spell.RenderSpell;
import com.DavidM1A2.AfraidOfTheDark.common.entities.spell.EntitySpell;
import com.DavidM1A2.AfraidOfTheDark.common.entities.spell.projectile.EntitySpellProjectile;
import com.DavidM1A2.AfraidOfTheDark.common.spell.Spell;

import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.player.EntityPlayer;

public class Projectile implements IDeliveryMethod
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
	public <T extends EntitySpell> RenderSpell<T> getDeliveryRenderer(RenderManager renderManager)
	{
		return new RenderSpell<T>(renderManager, new ModelSpellProjectile(), "afraidofthedark:textures/entity/spell/projectile.png");
	}

	@Override
	public Class<? extends EntitySpell> getDeliveryEntity()
	{
		return EntitySpellProjectile.class;
	}
}
