package com.DavidM1A2.AfraidOfTheDark.common.spell.deliveryMethods;

import java.io.Serializable;

import com.DavidM1A2.AfraidOfTheDark.client.entities.spell.RenderSpell;
import com.DavidM1A2.AfraidOfTheDark.common.entities.spell.EntitySpell;
import com.DavidM1A2.AfraidOfTheDark.common.spell.Spell;

import net.minecraft.client.renderer.entity.RenderManager;

public interface IDeliveryMethod extends Serializable
{
	/**
	 * 
	 * @return cost of the spell
	 */
	public abstract double getCost();

	/**
	 * 
	 * @param previous
	 *            The previous entity spell to this one
	 * @param spellStageIndex
	 *            The current index into the spell's array of stages
	 * @return An entity spell ready to be instantiated into the mc world
	 */
	public abstract EntitySpell createSpellEntity(EntitySpell previous, int spellStageIndex);

	/**
	 * 
	 * @param callback
	 *            The spell that's calling the projectile to be made. This means this projectile is the first delivery method
	 * @return An entity spell ready to be instantiated into the mc world
	 */
	public abstract EntitySpell createSpellEntity(Spell callback);

	/**
	 * 
	 * @param renderManager
	 *            The render manager given to us in proxy
	 * @return The spell renderer for this specific delivery method
	 */
	public abstract <T extends EntitySpell> RenderSpell<T> getDeliveryRenderer(RenderManager renderManager);

	/**
	 * 
	 * @return The class representing the entity for this delivery method
	 */
	public abstract Class<? extends EntitySpell> getDeliveryEntity();
}
