package com.davidm1a2.afraidofthedark.common.item

import com.davidm1a2.afraidofthedark.common.capabilities.getResearch
import com.davidm1a2.afraidofthedark.common.constants.ModDamageSources.getSilverDamage
import com.davidm1a2.afraidofthedark.common.constants.ModResearches
import com.davidm1a2.afraidofthedark.common.constants.ModToolMaterials
import com.davidm1a2.afraidofthedark.common.item.core.AOTDChargeableSword
import com.davidm1a2.afraidofthedark.common.utility.NBTHelper
import net.minecraft.client.Minecraft
import net.minecraft.client.util.ITooltipFlag
import net.minecraft.enchantment.EnchantmentHelper
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityLiving
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.Enchantments
import net.minecraft.item.ItemStack
import net.minecraft.util.DamageSource
import net.minecraft.world.World
import kotlin.math.sqrt

/**
 * Star metal sword is a khopesh
 *
 * @constructor sets the sword properties
 */
class ItemStarMetalKhopesh : AOTDChargeableSword("star_metal_khopesh", ModToolMaterials.STAR_METAL)
{
    init
    {
        percentChargePerAttack = 35.0
    }

    /**
     * Called when you left click an entity with the sword
     *
     * @param stack The itemstack that the entity was clicked with
     * @param player The player that clicked the entity
     * @param entity The entity that was clicked
     * @return True to cancel the interaction, false otherwise
     */
    override fun onLeftClickEntity(stack: ItemStack, player: EntityPlayer, entity: Entity): Boolean
    {
        // If star metal is researched allow the sword to function
        if (player.getResearch().isResearched(ModResearches.STAR_METAL))
        {
            // Ensure the clicked entity was non-null
            entity.attackEntityFrom(getSilverDamage(player), attackDamage)
        }
        else
        {
            return true
        }

        return super.onLeftClickEntity(stack, player, entity)
    }

    /**
     * Adds tooltip information to the sword
     *
     * @param stack The itemstack to get information about
     * @param world The world that the item is in
     * @param tooltip The tooltip to return
     * @param flag True if advanced tooltips are on, false otherwise
     */
    override fun addInformation(stack: ItemStack, world: World?, tooltip: MutableList<String>, flag: ITooltipFlag)
    {
        val player = Minecraft.getMinecraft().player
        if (player != null && player.getResearch().isResearched(ModResearches.STAR_METAL))
        {
            tooltip.add("Magical items will never break.")
            tooltip.add("Right click to use an AOE knockback and")
            tooltip.add("damage attack when charged to 100%")
        }
        else
        {
            tooltip.add("I'm not sure how to use this.")
        }
    }

    /**
     * Called to knock back entities around the player
     *
     * @param itemStack    The itemstack that was charged
     * @param world        The world the charge attack happened in
     * @param entityPlayer The player who used the charge attack
     * @return True if the charge attack went off, false otherwise
     */
    override fun performChargeAttack(itemStack: ItemStack, world: World, entityPlayer: EntityPlayer): Boolean
    {
        val nearbyEntities = world.getEntitiesWithinAABBExcludingEntity(entityPlayer, entityPlayer.entityBoundingBox.grow(HIT_RANGE.toDouble()))
        // Iterate over all entities within 5 blocks of the player
        for (entity in nearbyEntities)
        {
            // Only knock back living entities
            if (entity is EntityPlayer || entity is EntityLiving)
            {
                // Compute the vector from player to entity
                val motionX = entityPlayer.position.x - entity.position.x.toDouble()
                val motionZ = entityPlayer.position.z - entity.position.z.toDouble()

                // Compute the magnitude of the distance vector
                val hypotenuse = sqrt(motionX * motionX + motionZ * motionZ)

                // Compute the strength we knock back with
                val knockbackStrength = EnchantmentHelper.getEnchantmentLevel(Enchantments.KNOCKBACK, itemStack) + 2.toDouble()

                // Compute the damage we hit with based on sharpness
                val sharpnessDamage = EnchantmentHelper.getEnchantmentLevel(Enchantments.SHARPNESS, itemStack)
                // Attack the entity with player damage and move them back
                entity.attackEntityFrom(DamageSource.causePlayerDamage(entityPlayer), attackDamage + 4.0f + sharpnessDamage * 1.5f)
                entity.addVelocity(-motionX * knockbackStrength * 0.6 / hypotenuse, 0.1, -motionZ * knockbackStrength * 0.6 / hypotenuse)
            }
        }

        // Spin the player
        resetSpin(itemStack)
        return true
    }

    /**
     * Used to spin the player when the khopesh is right clicked
     *
     * @param stack The itemstack of the sword that is spinning the player
     * @param world The world that the player is in
     * @param entity The entity that activated the sword to be spun
     * @param itemSlot The slot the khopesh is in
     * @param isSelected True if the sword is selected, false otherwise
     */
    override fun onUpdate(stack: ItemStack, world: World, entity: Entity, itemSlot: Int, isSelected: Boolean)
    {
        // Only spin the player if the stack is spinning the player
        if (shouldSpin(stack))
        {
            // Reduce the ticks remaining by one
            decrementSpinTicks(stack)
            // Spin the entity/player
            entity.rotationYaw = entity.rotationYaw + DEGREES_PER_TICK
        }
    }

    /**
     * Resets the number of ticks to spin left to TICKS_TO_SPIN
     *
     * @param itemStack The itemstack to set NBT data for
     */
    private fun resetSpin(itemStack: ItemStack)
    {
        NBTHelper.setInteger(itemStack, NBT_SPIN_TICKS_LEFT, TICKS_TO_SPIN)
    }

    /**
     * True if the khopesh should spin the player currently, false otherwise
     *
     * @param itemStack The itemstack to test spin for
     * @return True if the khopesh should spin the player, false otherwise
     */
    private fun shouldSpin(itemStack: ItemStack): Boolean
    {
        return NBTHelper.hasTag(itemStack, NBT_SPIN_TICKS_LEFT) && NBTHelper.getInteger(itemStack, NBT_SPIN_TICKS_LEFT)!! > 0
    }

    /**
     * Reduces the number of spin ticks by 1
     *
     * @param itemStack The itemstack to decrement the spin ticks for
     */
    private fun decrementSpinTicks(itemStack: ItemStack)
    {
        if (NBTHelper.hasTag(itemStack, NBT_SPIN_TICKS_LEFT))
        {
            NBTHelper.setInteger(itemStack, NBT_SPIN_TICKS_LEFT, NBTHelper.getInteger(itemStack, NBT_SPIN_TICKS_LEFT)!! - 1)
        }
    }

    companion object
    {
        // The AOE knockback range
        private const val HIT_RANGE = 5
        // NBT containing spin info
        private const val NBT_SPIN_TICKS_LEFT = "spin_ticks_left"
        // Number of ticks to spin
        private const val TICKS_TO_SPIN = 8
        // Number of degrees to spin per tick
        private const val DEGREES_PER_TICK = 360.0f / TICKS_TO_SPIN
    }
}