package com.davidm1a2.afraidofthedark.common.item

import com.davidm1a2.afraidofthedark.common.capabilities.getResearch
import com.davidm1a2.afraidofthedark.common.constants.LocalizationConstants
import com.davidm1a2.afraidofthedark.common.constants.ModResearches
import com.davidm1a2.afraidofthedark.common.item.core.AOTDSharedCooldownItem
import com.davidm1a2.afraidofthedark.common.utility.NBTHelper
import com.davidm1a2.afraidofthedark.common.utility.sendMessage
import net.minecraft.entity.Entity
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.MobEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.ServerPlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.network.play.server.SEntityVelocityPacket
import net.minecraft.network.play.server.SPlayerPositionLookPacket
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.util.math.vector.Vector3d
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
            // If the item isn't selected or the active item isn't in the offhand ensure the player isn't invincible
            if (!isSelected && !(entity.usedItemHand == Hand.OFF_HAND && entity.useItem == stack)) {
                // If a star metal staff is not selected make sure the player can take damage
                if (!entity.isCreative && entity.abilities.invulnerable && isInUse(stack)) {
                    entity.abilities.invulnerable = false
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
    override fun use(world: World, player: PlayerEntity, hand: Hand): ActionResult<ItemStack> {
        // Get the item that the player was holding
        val heldItem = player.getItemInHand(hand)

        // Verify the player has the star metal research
        if (player.getResearch().isResearched(ModResearches.STAR_METAL)) {
            // If the item is not on cooldown fire it off
            if (!isOnCooldown(heldItem)) {
                // Make the player invincible
                if (!player.isCreative) {
                    player.abilities.invulnerable = true
                }

                // Set the player's velocity to 0 with a 0.5 vertical velocity
                if (!world.isClientSide) {
                    (player as ServerPlayerEntity).connection.send(
                        SEntityVelocityPacket(player.id, Vector3d(0.0, 0.5, 0.0))
                    )
                }

                // Set the item on cooldown
                setOnCooldown(heldItem, player)

                // Update the item NBT so that we know it's in use
                setInUse(heldItem, true)

                // Set the player's active hand to the one that was right clicked with
                player.startUsingItem(hand)

                // We're good to go, return success
                return ActionResult.success(heldItem)
            } else {
                // If the staff is on cooldown say that
                if (!world.isClientSide) {
                    player.sendMessage(
                        TranslationTextComponent("message.afraidofthedark.star_metal_staff.on_cooldown", cooldownRemainingInSeconds(heldItem))
                    )
                }
                return ActionResult.fail(heldItem)
            }
        } else {
            // If the player has the wrong research print an error
            if (!world.isClientSide) {
                player.sendMessage(TranslationTextComponent(LocalizationConstants.DONT_UNDERSTAND))
            }
        }
        return ActionResult.pass(heldItem)
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
        if (!entityLivingBase.level.isClientSide) {
            // Ensure the entity using the item is a player
            if (entityLivingBase is PlayerEntity) {
                // Figure out how many ticks the item has been in use
                count = getUseDuration(stack) - count

                // On the first tick set the fall distance to 0 so the player doesn't take fall damage after using this item
                if (count == 1) {
                    entityLivingBase.fallDistance = 0.0f
                }

                // On the 4th tick store the player's position
                if (count == 4) {
                    NBTHelper.setDouble(stack, NBT_X_POSITION, entityLivingBase.x)
                    NBTHelper.setDouble(stack, NBT_Y_POSITION, entityLivingBase.y)
                    NBTHelper.setDouble(stack, NBT_Z_POSITION, entityLivingBase.z)
                }

                // On the 5th tick and after freeze the entity's position
                if (count >= 5) {
                    val x = NBTHelper.getDouble(stack, NBT_X_POSITION)!!
                    val y = NBTHelper.getDouble(stack, NBT_Y_POSITION)!!
                    val z = NBTHelper.getDouble(stack, NBT_Z_POSITION)!!
                    (entityLivingBase as ServerPlayerEntity).connection.teleport(
                        x,
                        y,
                        z,
                        entityLivingBase.yRot,
                        entityLivingBase.xRot,
                        setOf(SPlayerPositionLookPacket.Flags.X_ROT, SPlayerPositionLookPacket.Flags.Y_ROT)
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
    override fun finishUsingItem(stack: ItemStack, world: World, entityLiving: LivingEntity): ItemStack {
        // Update the item NBT so it's not in use
        setInUse(stack, false)

        // Only test players
        if (entityLiving is PlayerEntity) {
            // If the player is not creative let them take damage again
            if (!entityLiving.isCreative) {
                entityLiving.abilities.invulnerable = false
            }
            // Perform the knockback
            if (!entityLiving.level.isClientSide) {
                performKnockback(world, entityLiving)
            }
        }
        return super.finishUsingItem(stack, world, entityLiving)
    }

    /**
     * Called when the player stops using the staff without actually letting it finish using
     *
     * @param stack The stack that was being used
     * @param world The world that the stack was being used in
     * @param entityLiving The entity that was using the staff
     * @param timeLeft The ticks left before the use would've been complete
     */
    override fun releaseUsing(stack: ItemStack, world: World, entityLiving: LivingEntity, timeLeft: Int) {
        // Update the item NBT so it's not in use
        setInUse(stack, false)

        // Ensure the entity is a player
        if (entityLiving is PlayerEntity) {
            // If the player is not in creative let them take damage again
            if (!entityLiving.isCreative) {
                entityLiving.abilities.invulnerable = false
            }

            // If less than 5 ticks were left on the use still perform the knockback
            if (timeLeft < 5) {
                // Server side processing only
                if (!world.isClientSide) {
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
        val entityList = world.getEntities(entityPlayer, entityPlayer.boundingBox.inflate(10.0))

        // Go over all nearby entities
        for (entity in entityList) {
            // If the entity is a player or anything living push it back
            if (entity is PlayerEntity || entity is MobEntity) {
                val direction = entity.position()
                    .subtract(entityPlayer.position())
                    .normalize()
                    .scale(KNOCKBACK_STRENGTH)

                // Push the entity away from the player
                entity.push(
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
        private const val NBT_X_POSITION = "x_position"
        private const val NBT_Y_POSITION = "y_position"
        private const val NBT_Z_POSITION = "z_position"
    }
}