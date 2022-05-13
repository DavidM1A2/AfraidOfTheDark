package com.davidm1a2.afraidofthedark.common.item

import com.davidm1a2.afraidofthedark.common.capabilities.getResearch
import com.davidm1a2.afraidofthedark.common.constants.ModDamageSources.getSilverDamage
import com.davidm1a2.afraidofthedark.common.constants.ModResearches
import com.davidm1a2.afraidofthedark.common.constants.ModToolMaterials
import com.davidm1a2.afraidofthedark.common.item.core.AOTDSwordItem
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack

/**
 * Class representing and astral silver sword that will do silver damage as well as regular damage
 *
 * @constructor sets up the item's properties
 */
class AstralSilverSwordItem : AOTDSwordItem("astral_silver_sword", ModToolMaterials.ASTRAL_SILVER, 3, -1.2f, Properties()) {
    /**
     * Called when the player left clicks an entity with the sword
     *
     * @param stack  The item that was hit with
     * @param enemy The player that did the hitting
     * @param hittingEntity The entity that was hit
     * @return True to cancel the interaction, false otherwise
     */
    override fun hurtEnemy(stack: ItemStack, enemy: LivingEntity, hittingEntity: LivingEntity): Boolean {
        // If the player has researched astral silver then do silver damage
        if (hittingEntity is PlayerEntity) {
            if (hittingEntity.getResearch().isResearched(ModResearches.SILVER_SLAYER)) {
                enemy.hurt(getSilverDamage(hittingEntity), damage)
            } else {
                return true
            }
        }

        // Otherwise do standard entity damage
        return super.hurtEnemy(stack, enemy, hittingEntity)
    }
}