package com.DavidM1A2.afraidofthedark.common.item;

import com.DavidM1A2.afraidofthedark.common.constants.ModCapabilities;
import com.DavidM1A2.afraidofthedark.common.constants.ModDamageSources;
import com.DavidM1A2.afraidofthedark.common.constants.ModResearches;
import com.DavidM1A2.afraidofthedark.common.constants.ModToolMaterials;
import com.DavidM1A2.afraidofthedark.common.item.core.AOTDSword;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

/**
 * Class representing and astral silver sword that will do silver damage as well as regular damage
 */
public class ItemAstralSilverSword extends AOTDSword
{
	/**
	 * Constructor sets up the item's properties
	 */
	public ItemAstralSilverSword()
	{
		super(ModToolMaterials.ASTRAL_SILVER, "astral_silver_sword");
	}

	/**
	 * Called when the player left clicks an entity with the sword
	 *
	 * @param stack The item that was hit with
	 * @param player The player that did the hitting
	 * @param entity The entity that was hit
	 * @return True to cancel the interaction, false otherwise
	 */
	@Override
	public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity)
	{
		// If the player has researched astral silver then do silver damage
		if (player.getCapability(ModCapabilities.PLAYER_RESEARCH, null).isResearched(ModResearches.ASTRAL_SILVER))
		{
			entity.attackEntityFrom(ModDamageSources.getSilverDamage(player), this.getAttackDamage());
			return true;
		}

		// Otherwise do standard entity damage
		return super.onLeftClickEntity(stack, player, entity);
	}
}
