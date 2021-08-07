package com.davidm1a2.afraidofthedark.common.item

import com.davidm1a2.afraidofthedark.common.capabilities.getResearch
import com.davidm1a2.afraidofthedark.common.constants.ModEntities
import com.davidm1a2.afraidofthedark.common.constants.ModItems
import com.davidm1a2.afraidofthedark.common.constants.ModResearches
import com.davidm1a2.afraidofthedark.common.entity.enchantedSkeleton.EnchantedSkeletonEntity
import com.davidm1a2.afraidofthedark.common.item.core.AOTDItem
import net.minecraft.entity.item.ItemEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.potion.EffectInstance
import net.minecraft.potion.Effects

/**
 * Class representing the enchanted skeleton bone item
 *
 * @constructor sets up item properties
 */
class EnchantedSkeletonBoneItem : AOTDItem("enchanted_skeleton_bone", Properties()) {
    /**
     * Called when this item is on the ground as an entity, if enough bones are together they will combine into a new skeleton
     *
     * @param itemStack The item in the entity
     * @param entityItem The item on the ground
     * @return True to skip further processing or false otherwise
     */
    override fun onEntityItemUpdate(itemStack: ItemStack, entityItem: ItemEntity): Boolean {
        // To avoid server performance loss only check every "UPDATE TIME IN TICKS" ticks and ensure we're on server side
        if (!entityItem.level.isClientSide && entityItem.tickCount % UPDATE_TIME_IN_TICKS == 0) {
            // Get a list of items on the ground around this one
            val surroundingItems = entityItem.level.getEntitiesOfClass(
                ItemEntity::class.java,
                entityItem.boundingBox.inflate(COMBINE_RADIUS.toDouble())
            )

            // Keep a count of the number of bones on the ground
            var numberOfBones = 0
            // Keep a list of surrounding bone item stacks
            val surroundingBones = mutableListOf<ItemEntity>()
            // Iterate over surrounding item stacks to find ones that also have bones
            for (otherItem in surroundingItems) {
                // Test if the item has bones and is on the ground
                if (otherItem.item.item is EnchantedSkeletonBoneItem && otherItem.isOnGround) {
                    // Add the stack
                    surroundingBones.add(otherItem)
                    // Increment our bone count
                    numberOfBones = numberOfBones + otherItem.item.count
                }
            }

            // Test if we have enough bones to spawn a skeleton
            if (numberOfBones >= BONES_PER_SKELETON) {
                // Compute the number of skeletons to spawn and the number of bones that will remain after
                val numberOfSkeletonsToSpawn = numberOfBones / BONES_PER_SKELETON
                val bonesRemaining = numberOfBones % BONES_PER_SKELETON
                val world = entityItem.level

                // Iterate over the number of skeletons to spawn
                for (i in 0 until numberOfSkeletonsToSpawn) {
                    // Create the skeleton
                    val skeleton = EnchantedSkeletonEntity(ModEntities.ENCHANTED_SKELETON, world)
                    // Spawn the skeleton at the position of the itemstack
                    skeleton.moveTo(
                        entityItem.x,
                        entityItem.y + 0.01,
                        entityItem.z,
                        entityItem.yRot,
                        0.0f
                    )
                    // Give the skeleton 2 ticks of invisibility to ensure players can't see them without their spawning animation
                    skeleton.addEffect(EffectInstance(Effects.INVISIBILITY, 2))
                    // Spawn the skeleton
                    world.addFreshEntity(skeleton)
                }

                // Give all players in range of the summoned skeletons a research if possible
                world.getEntitiesOfClass(
                    PlayerEntity::class.java,
                    entityItem.boundingBox.inflate(RESEARCH_UNLOCK_RADIUS.toDouble())
                ).forEach {
                    val playerResearch = it.getResearch()
                    if (playerResearch.canResearch(ModResearches.ENCHANTED_SKELETON)) {
                        playerResearch.setResearch(ModResearches.ENCHANTED_SKELETON, true)
                        playerResearch.sync(it, true)
                    }
                }

                // If bones remain create a new entity item with that many bones left
                if (bonesRemaining > 0) {
                    // Create the left over item stack and spawn it in
                    val leftOver = ItemEntity(
                        world,
                        entityItem.x,
                        entityItem.y,
                        entityItem.z,
                        ItemStack(ModItems.ENCHANTED_SKELETON_BONE, bonesRemaining)
                    )
                    world.addFreshEntity(leftOver)
                }

                // Remove the bone item stacks
                surroundingBones.forEach { it.remove() }
            }
        }

        // Allow further processing
        return super.onEntityItemUpdate(itemStack, entityItem)
    }

    companion object {
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