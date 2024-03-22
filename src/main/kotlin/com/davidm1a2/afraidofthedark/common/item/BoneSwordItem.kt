package com.davidm1a2.afraidofthedark.common.item

import com.davidm1a2.afraidofthedark.common.capabilities.getResearch
import com.davidm1a2.afraidofthedark.common.constants.ModResearches
import com.davidm1a2.afraidofthedark.common.constants.ModToolMaterials
import com.davidm1a2.afraidofthedark.common.entity.enchantedSkeleton.EnchantedSkeletonEntity
import com.davidm1a2.afraidofthedark.common.item.core.AOTDSwordItem
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.entity.player.Player
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.entity.Entity
import net.minecraft.world.item.ItemStack

/**
 * Class representing the blade of exhumation sword item
 *
 * @constructor Initializes the item with a name and material
 */
class BoneSwordItem : AOTDSwordItem(
    "bone_sword",
    ModToolMaterials.BONE,
    3,
    -2.4f,
    Properties()
) {
    /**
     * Called when the player left clicks on an entity
     *
     * @param stack  The item stack used to click with
     * @param player The player that clicked
     * @param entity The entity that was clicked on
     * @return True to cancel the interaction, false otherwise
     */
    override fun onLeftClickEntity(stack: ItemStack, player: Player, entity: Entity): Boolean {
        // If the sword is about to break cancel the interaction and don't let it break!
        if (stack.damageValue == stack.maxDamage - 1) {
            return true
        }

        // If the player has researched the blade of exhumation research and the entity hit is an enchanted skeleton 1 shot kill it
        if (entity is EnchantedSkeletonEntity && entity.isAlive) {
            // 1 shot kill the skeleton
            if (player.getResearch().isResearched(ModResearches.BONE_SWORD)) {
                // 1 shot the skeleton
                entity.hurt(DamageSource.playerAttack(player), Float.MAX_VALUE)
                stack.hurtAndBreak(1, player) {}
            }
        }

        // If this hit sets the durability of the sword to 1 then play a break sound
        if (stack.damageValue == stack.maxDamage - 2) {
            player.playSound(SoundEvents.METAL_BREAK, 0.8f, 0.8f + player.level.random.nextFloat() * 0.4f)
        }

        // Otherwise continue the hit interaction
        return super.onLeftClickEntity(stack, player, entity)
    }
}