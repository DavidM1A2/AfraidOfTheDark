package com.DavidM1A2.afraidofthedark.common.item;

import com.DavidM1A2.afraidofthedark.common.constants.ModCapabilities;
import com.DavidM1A2.afraidofthedark.common.constants.ModItems;
import com.DavidM1A2.afraidofthedark.common.entity.enchantedSkeleton.EntityEnchantedSkeleton;
import com.DavidM1A2.afraidofthedark.common.item.core.AOTDItem;
import com.google.common.collect.Lists;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

import java.util.List;

/**
 * Class representing the enchanted skeleton bone item
 */
public class ItemEnchnatedSkeletonBone extends AOTDItem
{
	// The number of bones required to combine into a skeleton
	private static final int BONES_PER_SKELETON = 4;
	// The distance bones can be apart to combine into a skeleton
	private static final int COMBINE_RADIUS = 4;
	// The amount of ticks between updates
	private static final int UPDATE_TIME_IN_TICKS = 120;
	// The radius at which players receive research when the skeleton spawns
	private static final int RESEARCH_UNLOCK_RADIUS = 10;

	/**
	 * Constructor sets up item properties
	 */
	public ItemEnchnatedSkeletonBone()
	{
		super("enchanted_skeleton_bone");
	}

	/**
	 * Called when this item is on the ground as an entity, if enough bones are together they will combine into a new skeleton
	 *
	 * @param entityItem The item on the ground
	 * @return True to skip further processing or false otherwise
	 */
	@Override
	public boolean onEntityItemUpdate(EntityItem entityItem)
	{
		// To avoid server performance loss only check every "UPDATE TIME IN TICKS" ticks and ensure we're on server side
		if (!entityItem.world.isRemote && entityItem.ticksExisted % UPDATE_TIME_IN_TICKS == 0)
		{
			// Get a list of items on the ground around this one
			List<EntityItem> surroundingItems = entityItem.world.getEntitiesWithinAABB(EntityItem.class, entityItem.getEntityBoundingBox().expand(COMBINE_RADIUS, COMBINE_RADIUS, COMBINE_RADIUS));

			// Keep a count of the number of bones on the ground
			int numberOfBones = 0;
			// Keep a list of surrounding bone item stacks
			List<EntityItem> surroundingBones = Lists.newLinkedList();
			// Iterate over surrounding item stacks to find ones that also have bones
			for (EntityItem otherItem : surroundingItems)
			{
				// Test if the item has bones and is on the ground
				if (otherItem.getItem().getItem() instanceof ItemEnchnatedSkeletonBone && otherItem.onGround)
				{
					// Add the stack
					surroundingBones.add(otherItem);
					// Increment our bone count
					numberOfBones = numberOfBones + otherItem.getItem().getCount();
				}
			}

			// Test if we have enough bones to spawn a skeleton
			if (numberOfBones >= BONES_PER_SKELETON)
			{
				// Compute the number of skeletons to spawn and the number of bones that will remain after
				int numberOfSkeletonsToSpawn = numberOfBones / BONES_PER_SKELETON;
				int bonesRemaining = numberOfBones % BONES_PER_SKELETON;

				World world = entityItem.world;
				// Iterate over the number of skeletons to spawn
				for (int i = 0; i < numberOfSkeletonsToSpawn; i++)
				{
					// Create the skeleton
					EntityEnchantedSkeleton skeleton = new EntityEnchantedSkeleton(world);
					// Spawn the skeleton at the position of the itemstack
					skeleton.setLocationAndAngles(entityItem.posX, entityItem.posY + 0.01, entityItem.posZ, entityItem.rotationYaw, 0.0F);
					// Give the skeleton 2 ticks of invisibility to ensure players can't see them without their spawning animation
					skeleton.addPotionEffect(new PotionEffect(Potion.getPotionById(14), 2));
					// Spawn the skeleton
					world.spawnEntity(skeleton);
				}

				// Give all players in range of the summoned skeletons a research if possible
				world.getEntitiesWithinAABB(EntityPlayer.class, entityItem.getEntityBoundingBox().expand(RESEARCH_UNLOCK_RADIUS, RESEARCH_UNLOCK_RADIUS, RESEARCH_UNLOCK_RADIUS)).forEach(entityPlayer ->
				{
					/*
					if (entityPlayer.getCapability(ModCapabilities.PLAYER_DATA, null).canResearch(ResearchTypes.EnchantedSkeleton))
					{
						entityPlayer.getCapability(ModCapabilities.PLAYER_DATA, null).setResearch(ResearchTypes.EnchantedSkeleton, true);
					}
					*/
				});

				// If bones remain create a new entity item with that many bones left
				if (bonesRemaining > 0)
				{
					// Create the left over item stack and spawn it in
					EntityItem leftOver = new EntityItem(world, entityItem.posX, entityItem.posY, entityItem.posZ, new ItemStack(ModItems.ENCHANTED_SKELETON_BONE, bonesRemaining));
					world.spawnEntity(leftOver);
				}

				// Remove the bone item stacks
				surroundingBones.forEach(Entity::setDead);
			}
		}

		// Allow further processing
		return super.onEntityItemUpdate(entityItem);
	}
}
