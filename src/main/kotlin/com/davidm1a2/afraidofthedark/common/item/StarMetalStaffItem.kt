package com.davidm1a2.afraidofthedark.common.item

import com.davidm1a2.afraidofthedark.common.capabilities.getResearch
import com.davidm1a2.afraidofthedark.common.constants.LocalizationConstants
import com.davidm1a2.afraidofthedark.common.constants.ModResearches
import com.davidm1a2.afraidofthedark.common.item.core.AOTDSharedCooldownItem
import com.davidm1a2.afraidofthedark.common.utility.NBTHelper
import net.minecraft.entity.Entity
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.MobEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.ServerPlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.network.play.server.SEntityVelocityPacket
import net.minecraft.util.ActionResult
import net.minecraft.util.ActionResultType
import net.minecraft.util.Hand
import net.minecraft.util.math.Vec3d
import net.minecraft.util.text.TranslationTextComponent
import net.minecraft.world.World

/**
 * Class representing a star metal staff that can do a (fizz e from LoL)
 */
class StarMetalStaffItem : AOTDSharedCooldownItem("star_metal_staff", Properties()) {
    /**
     * Called every tick the player has the item. Check if the player swapped off of the staff, if so make sure they're
     * no longer invincible
     *
     * @param stack The item being in the player's inventory
     * @param world The world the player is in
     * @param entity The entity that has the item
     * @param itemSlot The slot in the entities inventory that the item is in
     * @param isSelected True if the item is selected, false otherwise
     */
    override fun inventoryTick(stack: ItemStack, world: World, entity: Entity, itemSlot: Int, isSelected: Boolean) {
        // Check if the entity is a player
        if (entity is PlayerEntity) {
            // If the item isn't selected ensure the player isn't invincible
            if (!isSelected) {
                // If a star metal staff is not selected make sure the player can take damage
                if (!entity.isCreative && entity.abilities.disableDamage && isInUse(stack)) {
                    entity.abilities.disableDamage = false
                    setInUse(stack, false)
                }
            }
        }
    }

    /**
     * Called to fire the right click effect
     *
     * @param world  The world that the item is being held in
     * @param player The player that right clicked with the staff
     * @param hand   The hand that the staff is being held in
     * @return Success if the staff went off, pass if not
     */
    override fun onItemRightClick(world: World, player: PlayerEntity, hand: Hand): ActionResult<ItemStack> {
        // Get the item that the player was holding
        val heldItem = player.getHeldItem(hand)

        // Verify the player has the star metal research
        if (player.getResearch().isResearched(ModResearches.STAR_METAL)) {
            // If the item is not on cooldown fire it off
            if (!isOnCooldown(heldItem)) {
                // Make the player invincible
                if (!player.isCreative) {
                    player.abilities.disableDamage = true
                }

                // Set the player's velocity to 0 with a 0.5 vertical velocity
                if (!world.isRemote) {
                    (player as ServerPlayerEntity).connection.sendPacket(
                        SEntityVelocityPacket(player.getEntityId(), Vec3d(0.0, 0.5, 0.0))
                    )
                }

                // Set the item on cooldown
                setOnCooldown(heldItem, player)

                // Update the item NBT so that we know it's in use
                setInUse(heldItem, true)

                // Set the player's active hand to the one that was right clicked with
                player.activeHand = hand

                // We're good to go, return success
                return ActionResult.newResult(ActionResultType.SUCCESS, heldItem)
            } else {
                // If the staff is on cooldown say that
                if (!world.isRemote) {
                    player.sendMessage(TranslationTextComponent("message.afraidofthedark.star_metal_staff.on_cooldown", cooldownRemainingInSeconds(heldItem)))
                }
                return ActionResult.newResult(ActionResultType.FAIL, heldItem)
            }
        } else {
            // If the player has the wrong research print an error
            if (!world.isRemote) {
                player.sendMessage(TranslationTextComponent(LocalizationConstants.DONT_UNDERSTAND))
            }
        }
        return ActionResult.newResult(ActionResultType.PASS, heldItem)
    }

    /**
     * Called every tick the item is in use
     *
     * @param stack The itemstack that is in use
     * @param entityLivingBase The entity using the item
     * @param count The ticks left before the use is done
     */
    override fun onUsingTick(stack: ItemStack, entityLivingBase: LivingEntity, count: Int) {
        // Server side processing only
        // Make 'count' mutable...
        @Suppress("NAME_SHADOWING")
        var count = count
        if (!entityLivingBase.world.isRemote) {
            // Ensure the entity using the item is a player
            if (entityLivingBase is PlayerEntity) {
                // Figure out how many ticks the item has been in use
                count = getUseDuration(stack) - count

                // On the first tick set the fall distance to 0 so the player doesn't take fall damage after using this item
                if (count == 1) {
                    entityLivingBase.fallDistance = 0.0f
                }

                // On the 5th tick and after freeze the entity's position
                if (count >= 5) {
                    (entityLivingBase as ServerPlayerEntity).connection.sendPacket(
                        SEntityVelocityPacket(entityLivingBase.getEntityId(), Vec3d(0.0, 0.0, 0.0))
                    )
                }
            }
        }
    }

    /**
     * Called when the item has finished being used, performs the knockback
     *
     * @param stack The itemstack that was used
     * @param world The world the item was being used in
     * @param entityLiving The entity holding the item
     * @return The itemstack to return after the current stack was used
     */
    override fun onItemUseFinish(stack: ItemStack, world: World, entityLiving: LivingEntity): ItemStack {
        // Update the item NBT so it's not in use
        setInUse(stack, false)

        // Only test players
        if (entityLiving is PlayerEntity) {
            // If the player is not creative let them take damage again
            if (!entityLiving.isCreative) {
                entityLiving.abilities.disableDamage = false
            }
            // Perform the knockback
            if (!entityLiving.world.isRemote) {
                performKnockback(world, entityLiving)
            }
        }
        return super.onItemUseFinish(stack, world, entityLiving)
    }

    /**
     * Called when the player stops using the staff without actually letting it finish using
     *
     * @param stack The stack that was being used
     * @param world The world that the stack was being used in
     * @param entityLiving The entity that was using the staff
     * @param timeLeft The ticks left before the use would've been complete
     */
    override fun onPlayerStoppedUsing(stack: ItemStack, world: World, entityLiving: LivingEntity, timeLeft: Int) {
        // Update the item NBT so it's not in use
        setInUse(stack, false)

        // Ensure the entity is a player
        if (entityLiving is PlayerEntity) {
            // If the player is not in creative let them take damage again
            if (!entityLiving.isCreative) {
                entityLiving.abilities.disableDamage = false
            }

            // If less than 5 ticks were left on the use still perform the knockback
            if (timeLeft < 5) {
                // Server side processing only
                if (!world.isRemote) {
                    performKnockback(world, entityLiving)
                }
            }
        }
    }

    /**
     * Performs the knockback of the cloak after hitting the ground
     *
     * @param world The world the knockback is happening in
     * @param entityPlayer The player that caused the knockback
     */
    private fun performKnockback(world: World, entityPlayer: PlayerEntity) {
        // Grab all entities around the player
        val entityList =
            world.getEntitiesWithinAABBExcludingEntity(entityPlayer, entityPlayer.boundingBox.grow(10.0))

        // Go over all nearby entities
        for (entity in entityList) {
            // If the entity is a player or anything living push it back
            if (entity is PlayerEntity || entity is MobEntity) {
                val direction = entity.positionVector
                    .subtract(entityPlayer.positionVector)
                    .normalize()
                    .scale(KNOCKBACK_STRENGTH)

                // Push the entity away from the player
                entity.addVelocity(
                    direction.x,
                    direction.y,
                    direction.z
                )
            }
        }
    }

    /**
     * Returns the number of milliseconds required for this item to get off cooldown
     *
     * @param itemStack The itemstack to get the cooldown for
     * @return The number of milliseconds required to finish the cooldown
     */
    override fun getCooldownInMilliseconds(itemStack: ItemStack): Int {
        return 5000
    }

    /**
     * The maximum number of ticks the item may be "in use"
     *
     * @param stack The itemstack to test
     * @return The number of ticks the item may be in use for
     */
    override fun getUseDuration(stack: ItemStack): Int {
        return MAX_TROLL_POLE_TIME_IN_TICKS
    }

    override fun canContinueUsing(oldStack: ItemStack, newStack: ItemStack): Boolean {
        return newStack.item == this && isInUse(newStack)
    }

    /**
     * Sets the 'in_use' nbt tag which is used to determine if the item was in use or not
     *
     * @param itemStack The itemstack to check
     * @param inUse True if the item is in use, false otherwise
     */
    private fun setInUse(itemStack: ItemStack, inUse: Boolean) {
        NBTHelper.setBoolean(itemStack, NBT_IN_USE, inUse)
    }

    /**
     * True if the item has the 'in_use' nbt tag, false otherwise
     *
     * @param itemStack The itemstack to test
     */
    private fun isInUse(itemStack: ItemStack): Boolean {
        return if (NBTHelper.hasTag(itemStack, NBT_IN_USE)) {
            NBTHelper.getBoolean(itemStack, NBT_IN_USE)!!
        } else {
            false
        }
    }

    companion object {
        // The amount of knockback the staff has once dropping out of it
        private const val KNOCKBACK_STRENGTH = 4.0

        // The maximum number of ticks a player can be on the troll poll
        private const val MAX_TROLL_POLE_TIME_IN_TICKS = 60

        // NBT constants
        private const val NBT_IN_USE = "in_use"
    }
}