package com.davidm1a2.afraidofthedark.common.item

import com.davidm1a2.afraidofthedark.common.capabilities.getResearch
import com.davidm1a2.afraidofthedark.common.constants.ModDamageSources.getSilverDamage
import com.davidm1a2.afraidofthedark.common.constants.ModResearches
import com.davidm1a2.afraidofthedark.common.constants.ModToolMaterials
import com.davidm1a2.afraidofthedark.common.item.core.AOTDSword
import net.minecraft.entity.Entity
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack

/**
 * Class representing and astral silver sword that will do silver damage as well as regular damage
 *
 * @constructor sets up the item's properties
 */
class ItemAstralSilverSword : AOTDSword("astral_silver_sword", ModToolMaterials.ASTRAL_SILVER)
{
    /**
     * Called when the player left clicks an entity with the sword
     *
     * @param stack  The item that was hit with
     * @param player The player that did the hitting
     * @param entity The entity that was hit
     * @return True to cancel the interaction, false otherwise
     */
    override fun onLeftClickEntity(stack: ItemStack, player: EntityPlayer, entity: Entity): Boolean
    {
        // If the player has researched astral silver then do silver damage
        if (player.getResearch().isResearched(ModResearches.ASTRAL_SILVER))
        {
            entity.attackEntityFrom(getSilverDamage(player), attackDamage)
        }
        else
        {
            return true
        }

        // Otherwise do standard entity damage
        return super.onLeftClickEntity(stack, player, entity)
    }
}