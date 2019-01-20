package com.DavidM1A2.afraidofthedark.common.item;

import com.DavidM1A2.afraidofthedark.common.capabilities.player.research.IAOTDPlayerResearch;
import com.DavidM1A2.afraidofthedark.common.constants.ModCapabilities;
import com.DavidM1A2.afraidofthedark.common.constants.ModResearches;
import com.DavidM1A2.afraidofthedark.common.constants.ModToolMaterials;
import com.DavidM1A2.afraidofthedark.common.entity.enchantedSkeleton.EntityEnchantedSkeleton;
import com.DavidM1A2.afraidofthedark.common.item.core.AOTDItem;
import com.DavidM1A2.afraidofthedark.common.item.core.AOTDSword;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.common.util.EnumHelper;

/**
 * Class representing the blade of exhumation sword item
 */
public class ItemBladeOfExhumation extends AOTDSword
{
	/**
	 * Initializes the item with a name and material
	 */
	public ItemBladeOfExhumation()
	{
		super(ModToolMaterials.BLADE_OF_EXHUMATION, "blade_of_exhumation");
	}

	/**
	 * Called when the player left clicks on an entity
	 *
	 * @param stack The item stack used to click with
	 * @param player The player that clicked
	 * @param entity The entity that was clicked on
	 * @return True to cancel the interaction, false otherwise
	 */
	@Override
	public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity)
	{
		// If the sword is about to break cancel the interaction and don't let it break!
		if (stack.getItemDamage() == stack.getMaxDamage() - 1)
			return true;

		// If the player has researched the blade of exhumation research and the entity hit is an enchanted skeleton 1 shot kill it
		if (entity instanceof EntityEnchantedSkeleton && !entity.isDead)
		{
			IAOTDPlayerResearch playerResearch = player.getCapability(ModCapabilities.PLAYER_RESEARCH, null);
			// 1 shot kill the skeleton
			if (playerResearch.isResearched(ModResearches.BLADE_OF_EXHUMATION))
			{
				// 1 shot the skeleton
				entity.attackEntityFrom(DamageSource.causePlayerDamage(player), Float.MAX_VALUE);
			}
		}

		// If this hit sets the durability of the sword to 1 then play a break sound
		if (stack.getItemDamage() == stack.getMaxDamage() - 2)
			player.playSound(SoundEvents.BLOCK_METAL_BREAK, 0.8f, 0.8f + player.world.rand.nextFloat() * 0.4f);

		// Otherwise continue the hit interaction
		return super.onLeftClickEntity(stack, player, entity);
	}
}
