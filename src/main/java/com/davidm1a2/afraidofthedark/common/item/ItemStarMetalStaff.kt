package com.davidm1a2.afraidofthedark.common.item

import com.davidm1a2.afraidofthedark.common.constants.ModCapabilities
import com.davidm1a2.afraidofthedark.common.constants.ModResearches
import com.davidm1a2.afraidofthedark.common.item.core.AOTDItemWithSharedCooldown
import com.davidm1a2.afraidofthedark.common.utility.NBTHelper
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityLiving
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraft.item.ItemStack
import net.minecraft.network.play.server.SPacketEntityVelocity
import net.minecraft.util.ActionResult
import net.minecraft.util.EnumActionResult
import net.minecraft.util.EnumHand
import net.minecraft.util.math.MathHelper
import net.minecraft.util.text.TextComponentTranslation
import net.minecraft.world.World

/**
 * Class representing a star metal staff that can do a (fizz e from LoL)
 *
 * @constructor sets the item's name
 */
class ItemStarMetalStaff : AOTDItemWithSharedCooldown("star_metal_staff")
{
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
    override fun onUpdate(stack: ItemStack, world: World, entity: Entity, itemSlot: Int, isSelected: Boolean)
    {
        // Server side processing only
        if (!world.isRemote)
        {
            // Check if the entity is a player
            if (entity is EntityPlayer)
            {
                // If the item isn't selected ensure the player isn't invincible
                if (!isSelected)
                {
                    // If a star metal staff is not selected make sure the player can take damage
                    if (!entity.capabilities.isCreativeMode && entity.capabilities.disableDamage && isInUse(stack))
                    {
                        entity.capabilities.disableDamage = false
                        setInUse(stack, false)
                    }
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
    override fun onItemRightClick(world: World, player: EntityPlayer, hand: EnumHand): ActionResult<ItemStack>
    {
        // Server side processing only
        if (!world.isRemote)
        {
            // Verify the player has the star metal research
            if (player.getCapability(ModCapabilities.PLAYER_RESEARCH, null)!!.isResearched(ModResearches.STAR_METAL))
            {
                // Get the item that the player was holding
                val heldItem = player.getHeldItem(hand)
                // If the item is not on cooldown fire it off
                if (!isOnCooldown(heldItem))
                {
                    // Make the player invincible
                    if (!player.capabilities.isCreativeMode)
                    {
                        player.capabilities.disableDamage = true
                    }
                    // Set the player's velocity to 0 with a 0.5 vertical velocity
                    (player as EntityPlayerMP).connection.sendPacket(SPacketEntityVelocity(player.getEntityId(), 0.0, 0.5, 0.0))

                    // Set the item on cooldown
                    setOnCooldown(heldItem, player)

                    // Set the player's active hand to the one that was right clicked with
                    player.setActiveHand(hand)

                    // Update the item NBT so that we know it's in use
                    setInUse(heldItem, true)

                    // We're good to go, return success
                    return ActionResult.newResult(EnumActionResult.SUCCESS, heldItem)
                }
                else
                {
                    // If the staff is on cooldown say that
                    player.sendMessage(TextComponentTranslation("aotd.star_metal_staff.on_cooldown", cooldownRemainingInSeconds(heldItem)))
                }
            }
            else
            {
                // If the player has the wrong research print an error
                player.sendMessage(TextComponentTranslation("aotd.dont_understand"))
            }
        }
        return super.onItemRightClick(world, player, hand)
    }

    /**
     * Called every tick the item is in use
     *
     * @param stack The itemstack that is in use
     * @param entityLivingBase The entity using the item
     * @param count The ticks left before the use is done
     */
    override fun onUsingTick(stack: ItemStack, entityLivingBase: EntityLivingBase, count: Int)
    {
        // Server side processing only
        // Make 'count' mutable...
        @Suppress("NAME_SHADOWING")
        var count = count
        if (!entityLivingBase.world.isRemote)
        {
            // Ensure the entity using the item is a player
            if (entityLivingBase is EntityPlayer)
            {
                // Figure out how many ticks the item has been in use
                count = getMaxItemUseDuration(stack) - count

                // On the first tick set the fall distance to 0 so the player doesn't take fall damage after using this item
                if (count == 1)
                {
                    entityLivingBase.fallDistance = 0.0f
                }

                // On the 5th tick and after freeze the entity's position
                if (count >= 5)
                {
                    (entityLivingBase as EntityPlayerMP).connection.sendPacket(SPacketEntityVelocity(entityLivingBase.getEntityId(), 0.0, 0.0, 0.0))
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
    override fun onItemUseFinish(stack: ItemStack, world: World, entityLiving: EntityLivingBase): ItemStack
    {
        // Server side processing only
        if (!entityLiving.world.isRemote)
        {
            // Update the item NBT so it's not in use
            setInUse(stack, false)

            // Only test players
            if (entityLiving is EntityPlayer)
            {
                // If the player is not creative let them take damage again
                if (!entityLiving.capabilities.isCreativeMode)
                {
                    entityLiving.capabilities.disableDamage = false
                }
                // Perform the knockback
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
    override fun onPlayerStoppedUsing(stack: ItemStack, world: World, entityLiving: EntityLivingBase, timeLeft: Int)
    {
        // Server side processing only
        if (!world.isRemote)
        {
            // Update the item NBT so it's not in use
            setInUse(stack, false)

            // Ensure the entity is a player
            if (entityLiving is EntityPlayer)
            {
                // If the player is not in creative let them take damage again
                if (!entityLiving.capabilities.isCreativeMode)
                {
                    entityLiving.capabilities.disableDamage = false
                }

                // If less than 5 ticks were left on the use still perform the knockback
                if (timeLeft < 5)
                {
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
    private fun performKnockback(world: World, entityPlayer: EntityPlayer)
    {
        // Grab all entities around the player
        val entityList = world.getEntitiesWithinAABBExcludingEntity(entityPlayer, entityPlayer.entityBoundingBox.grow(10.0))
        // Go over all nearby entities
        for (entity in entityList)
        {
            // If the entity is a player or anything living push it back
            if (entity is EntityPlayer || entity is EntityLiving)
            {
                // Compute the x,z force vector to push the entity in
                val motionX = entityPlayer.position.x - entity.position.x.toDouble()
                val motionZ = entityPlayer.position.z - entity.position.z.toDouble()
                // Compute the magnitude of the force
                val hypotenuse = MathHelper.sqrt(motionX * motionX + motionZ * motionZ).toDouble()
                // Push the entity away from the player
                entity.addVelocity(-motionX * KNOCKBACK_STRENGTH * 0.6 / hypotenuse, 0.1, -motionZ * KNOCKBACK_STRENGTH * 0.6 / hypotenuse)
            }
        }
    }

    /**
     * Returns the number of milliseconds required for this item to get off cooldown
     *
     * @param itemStack The itemstack to get the cooldown for
     * @return The number of milliseconds required to finish the cooldown
     */
    override fun getItemCooldownInMilliseconds(itemStack: ItemStack): Int
    {
        return 5000
    }

    /**
     * The maximum number of ticks the item may be "in use"
     *
     * @param stack The itemstack to test
     * @return The number of ticks the item may be in use for
     */
    override fun getMaxItemUseDuration(stack: ItemStack): Int
    {
        return MAX_TROLL_POLE_TIME_IN_TICKS
    }

    /**
     * Sets the 'in_use' nbt tag which is used to determine if the item was in use or not
     *
     * @param itemStack The itemstack to check
     * @param inUse True if the item is in use, false otherwise
     */
    fun setInUse(itemStack: ItemStack, inUse: Boolean)
    {
        NBTHelper.setBoolean(itemStack, NBT_IN_USE, inUse)
    }

    /**
     * True if the item has the 'in_use' nbt tag, false otherwise
     *
     * @param itemStack The itemstack to test
     */
    fun isInUse(itemStack: ItemStack): Boolean
    {
        return if (NBTHelper.hasTag(itemStack, NBT_IN_USE))
        {
            NBTHelper.getBoolean(itemStack, NBT_IN_USE)!!
        }
        else
        {
            false
        }
    }

    companion object
    {
        // The amount of knockback the staff has once dropping out of it
        private const val KNOCKBACK_STRENGTH = 6.0
        // The maximum number of ticks a player can be on the troll poll
        private const val MAX_TROLL_POLE_TIME_IN_TICKS = 60

        // NBT constants
        private const val NBT_IN_USE = "in_use"
    }
}