package com.davidm1a2.afraidofthedark.common.item

import com.davidm1a2.afraidofthedark.common.constants.ModCapabilities
import com.davidm1a2.afraidofthedark.common.constants.ModItems
import com.davidm1a2.afraidofthedark.common.constants.ModResearches
import com.davidm1a2.afraidofthedark.common.entity.enchantedSkeleton.EntityEnchantedSkeleton
import com.davidm1a2.afraidofthedark.common.item.core.AOTDItem
import net.minecraft.entity.item.EntityItem
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.potion.Potion
import net.minecraft.potion.PotionEffect

/**
 * Class representing the enchanted skeleton bone item
 *
 * @constructor sets up item properties
 */
class ItemEnchantedSkeletonBone : AOTDItem("enchanted_skeleton_bone")
{
    /**
     * Called when this item is on the ground as an entity, if enough bones are together they will combine into a new skeleton
     *
     * @param entityItem The item on the ground
     * @return True to skip further processing or false otherwise
     */
    override fun onEntityItemUpdate(entityItem: EntityItem): Boolean
    {
        // To avoid server performance loss only check every "UPDATE TIME IN TICKS" ticks and ensure we're on server side
        if (!entityItem.world.isRemote && entityItem.ticksExisted % UPDATE_TIME_IN_TICKS == 0)
        {
            // Get a list of items on the ground around this one
            val surroundingItems = entityItem.world.getEntitiesWithinAABB(EntityItem::class.java, entityItem.entityBoundingBox.grow(COMBINE_RADIUS.toDouble()))

            // Keep a count of the number of bones on the ground
            var numberOfBones = 0
            // Keep a list of surrounding bone item stacks
            val surroundingBones: MutableList<EntityItem> = mutableListOf()
            // Iterate over surrounding item stacks to find ones that also have bones
            for (otherItem in surroundingItems)
            {
                // Test if the item has bones and is on the ground
                if (otherItem.item.item is ItemEnchantedSkeletonBone && otherItem.onGround)
                {
                    // Add the stack
                    surroundingBones.add(otherItem)
                    // Increment our bone count
                    numberOfBones = numberOfBones + otherItem.item.count
                }
            }

            // Test if we have enough bones to spawn a skeleton
            if (numberOfBones >= BONES_PER_SKELETON)
            {
                // Compute the number of skeletons to spawn and the number of bones that will remain after
                val numberOfSkeletonsToSpawn = numberOfBones / BONES_PER_SKELETON
                val bonesRemaining = numberOfBones % BONES_PER_SKELETON
                val world = entityItem.world

                // Iterate over the number of skeletons to spawn
                for (i in 0 until numberOfSkeletonsToSpawn)
                {
                    // Create the skeleton
                    val skeleton = EntityEnchantedSkeleton(world)
                    // Spawn the skeleton at the position of the itemstack
                    skeleton.setLocationAndAngles(entityItem.posX, entityItem.posY + 0.01, entityItem.posZ, entityItem.rotationYaw, 0.0f)
                    // Give the skeleton 2 ticks of invisibility to ensure players can't see them without their spawning animation
                    skeleton.addPotionEffect(PotionEffect(Potion.getPotionById(14)!!, 2))
                    // Spawn the skeleton
                    world.spawnEntity(skeleton)
                }

                // Give all players in range of the summoned skeletons a research if possible
                world.getEntitiesWithinAABB(EntityPlayer::class.java, entityItem.entityBoundingBox.grow(RESEARCH_UNLOCK_RADIUS.toDouble()))
                    .forEach()
                    {
                        val playerResearch = it.getCapability(ModCapabilities.PLAYER_RESEARCH, null)
                        if (playerResearch!!.canResearch(ModResearches.ENCHANTED_SKELETON))
                        {
                            playerResearch.setResearch(ModResearches.ENCHANTED_SKELETON, true)
                            playerResearch.sync(it, true)
                        }
                    }

                // If bones remain create a new entity item with that many bones left
                if (bonesRemaining > 0)
                {
                    // Create the left over item stack and spawn it in
                    val leftOver = EntityItem(world, entityItem.posX, entityItem.posY, entityItem.posZ, ItemStack(ModItems.ENCHANTED_SKELETON_BONE, bonesRemaining))
                    world.spawnEntity(leftOver)
                }

                // Remove the bone item stacks
                surroundingBones.forEach { it.setDead() }
            }
        }

        // Allow further processing
        return super.onEntityItemUpdate(entityItem)
    }

    companion object
    {
        // The number of bones required to combine into a skeleton
        private const val BONES_PER_SKELETON = 4
        // The distance bones can be apart to combine into a skeleton
        private const val COMBINE_RADIUS = 4
        // The amount of ticks between updates
        private const val UPDATE_TIME_IN_TICKS = 120
        // The radius at which players receive research when the skeleton spawns
        private const val RESEARCH_UNLOCK_RADIUS = 7
    }
}