package com.davidm1a2.afraidofthedark.common.item

import com.davidm1a2.afraidofthedark.common.capabilities.getResearch
import com.davidm1a2.afraidofthedark.common.constants.ModResearches
import com.davidm1a2.afraidofthedark.common.constants.ModToolMaterials
import com.davidm1a2.afraidofthedark.common.entity.enchantedSkeleton.EntityEnchantedSkeleton
import com.davidm1a2.afraidofthedark.common.item.core.AOTDSword
import net.minecraft.entity.Entity
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.SoundEvents
import net.minecraft.item.ItemStack
import net.minecraft.util.DamageSource

/**
 * Class representing the blade of exhumation sword item
 *
 * @constructor Initializes the item with a name and material
 */
class ItemBladeOfExhumation : AOTDSword("blade_of_exhumation", ModToolMaterials.BLADE_OF_EXHUMATION) {
    /**
     * Called when the player left clicks on an entity
     *
     * @param stack  The item stack used to click with
     * @param player The player that clicked
     * @param entity The entity that was clicked on
     * @return True to cancel the interaction, false otherwise
     */
    override fun onLeftClickEntity(stack: ItemStack, player: EntityPlayer, entity: Entity): Boolean {
        // If the sword is about to break cancel the interaction and don't let it break!
        if (stack.itemDamage == stack.maxDamage - 1) {
            return true
        }

        // If the player has researched the blade of exhumation research and the entity hit is an enchanted skeleton 1 shot kill it
        if (entity is EntityEnchantedSkeleton && !entity.isDead) {
            // 1 shot kill the skeleton
            if (player.getResearch().isResearched(ModResearches.BLADE_OF_EXHUMATION)) {
                // 1 shot the skeleton
                entity.attackEntityFrom(DamageSource.causePlayerDamage(player), Float.MAX_VALUE)
            }
        }

        // If this hit sets the durability of the sword to 1 then play a break sound
        if (stack.itemDamage == stack.maxDamage - 2) {
            player.playSound(SoundEvents.BLOCK_METAL_BREAK, 0.8f, 0.8f + player.world.rand.nextFloat() * 0.4f)
        }

        // Otherwise continue the hit interaction
        return super.onLeftClickEntity(stack, player, entity)
    }
}