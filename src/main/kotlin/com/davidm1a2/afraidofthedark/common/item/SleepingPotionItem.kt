package com.davidm1a2.afraidofthedark.common.item

import com.davidm1a2.afraidofthedark.common.constants.ModEffects
import com.davidm1a2.afraidofthedark.common.item.core.AOTDItem
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.item.UseAnim
import net.minecraft.world.level.Level

/**
 * Potion that gives you the drowsy potion effect
 *
 * @constructor sets the item name
 */
class SleepingPotionItem : AOTDItem("sleeping_potion", Properties()) {
    /**
     * It takes 32 ticks to drink the potion
     *
     * @param stack The item stack being drunk
     * @return The number of ticks to drink the potion
     */
    override fun getUseDuration(stack: ItemStack): Int {
        return 32
    }

    /**
     * It's a potion, so when the item is being used show the drink animation
     *
     * @param stack The item stack being drunk
     * @return DRINK since this is a potion
     */
    override fun getUseAnimation(stack: ItemStack): UseAnim {
        return UseAnim.DRINK
    }

    /**
     * Upon right clicking begin drinking the potion
     *
     * @param world  The world the player is in
     * @param player The player drinking the potion
     * @param hand   The hand the player is using to hold the potion
     * @return SUCCESS since the potion drinking began
     */
    override fun use(world: Level, player: Player, hand: InteractionHand): InteractionResultHolder<ItemStack> {
        player.startUsingItem(hand)
        return InteractionResultHolder.success(player.getItemInHand(hand))
    }

    /**
     * Called when the item is finished being drunk
     *
     * @param stack        The itemstack being drunk
     * @param worldIn      The world that the player is in
     * @param entityLiving The entity that drunk the potion
     * @return The itemstack that this item became
     */
    override fun finishUsingItem(stack: ItemStack, worldIn: Level, entityLiving:  LivingEntity): ItemStack {
        // Server side only processing
        if (!worldIn.isClientSide) {
            // This potion only effects players
            if (entityLiving is Player) {
                entityLiving.addEffect(MobEffectInstance(ModEffects.SLEEPING, 4800, 0, false, true))
                // If the player is not in creative mode reduce the bottle stack size by 1 and return the bottle
                if (!entityLiving.isCreative) {
                    stack.shrink(1)
                    // If the stack is empty return a glass bottle, otherwise add a glass bottle to the player's inventory
                    if (stack.isEmpty) {
                        return ItemStack(Items.GLASS_BOTTLE)
                    }
                    entityLiving.inventory.add(ItemStack(Items.GLASS_BOTTLE))
                }
            }
        }
        return super.finishUsingItem(stack, worldIn, entityLiving)
    }
}