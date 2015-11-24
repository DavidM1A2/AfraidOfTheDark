/*
 * Author: David Slovikosky
 * Mod: Afraid of the Dark
 * Ideas and Textures: Michael Albertson
 */
package com.DavidM1A2.AfraidOfTheDark.common.item;

import java.util.ArrayList;
import java.util.List;

import com.DavidM1A2.AfraidOfTheDark.common.entities.EnchantedSkeleton.EntityEnchantedSkeleton;
import com.DavidM1A2.AfraidOfTheDark.common.initializeMod.ModItems;
import com.DavidM1A2.AfraidOfTheDark.common.item.core.AOTDItem;
import com.DavidM1A2.AfraidOfTheDark.common.refrence.ResearchTypes;
import com.DavidM1A2.AfraidOfTheDark.common.savedData.AOTDPlayerData;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

public class ItemEnchantedSkeletonBone extends AOTDItem
{
	private static final int BONES_PER_SKELETON = 4;
	private static final int COMBINE_RADIUS = 4;
	private static final int UPDATE_TIME_IN_TICKS = 120;
	private static final int RESEARCH_UNLOCK_RADIUS = 10;

	public ItemEnchantedSkeletonBone()
	{
		super();
		this.setUnlocalizedName("enchantedSkeletonBone");
	}

	/**
	 * Called by the default implemetation of EntityItem's onUpdate method, allowing for cleaner control over the update of the item without having to
	 * write a subclass.
	 *
	 * @param entityItem
	 *            The entity Item
	 * @return Return true to skip any further update code.
	 */
	@Override
	public boolean onEntityItemUpdate(EntityItem entityItem)
	{
		if (entityItem.ticksExisted % UPDATE_TIME_IN_TICKS == 0)
		{
			if (!entityItem.worldObj.isRemote)
			{
				List surroundingEntities = entityItem.worldObj.getEntitiesWithinAABB(EntityItem.class, entityItem.getEntityBoundingBox().expand(COMBINE_RADIUS, COMBINE_RADIUS, COMBINE_RADIUS));

				int numberOfBones = 0;
				List<EntityItem> surroundingBones = new ArrayList<EntityItem>();
				for (Object object : surroundingEntities)
				{
					EntityItem current = (EntityItem) object;
					ItemStack currentStack = current.getDataWatcher().getWatchableObjectItemStack(10);
					if (currentStack.getItem() instanceof ItemEnchantedSkeletonBone)
					{
						if (current.onGround)
						{
							numberOfBones = numberOfBones + currentStack.stackSize;
							surroundingBones.add(current);
						}
					}
				}

				if (numberOfBones >= BONES_PER_SKELETON)
				{
					int numberOfSkeletonsToSpawn = numberOfBones / BONES_PER_SKELETON;
					int bonesRemaining = numberOfBones % BONES_PER_SKELETON;

					World world = entityItem.worldObj;
					for (int i = 0; i < numberOfSkeletonsToSpawn; i++)
					{
						EntityEnchantedSkeleton skeleton = new EntityEnchantedSkeleton(world);
						skeleton.setLocationAndAngles(entityItem.posX, entityItem.posY + 0.01, entityItem.posZ, entityItem.rotationYaw, 0.0F);
						skeleton.addPotionEffect(new PotionEffect(14, 2));
						world.spawnEntityInWorld(skeleton);

						for (Object object : world.getEntitiesWithinAABB(EntityPlayer.class, entityItem.getEntityBoundingBox().expand(RESEARCH_UNLOCK_RADIUS, RESEARCH_UNLOCK_RADIUS, RESEARCH_UNLOCK_RADIUS)))
						{
							if (object instanceof EntityPlayer)
							{
								EntityPlayer entityPlayer = (EntityPlayer) object;
								if (AOTDPlayerData.get(entityPlayer).canResearch(ResearchTypes.EnchantedSkeleton))
								{
									AOTDPlayerData.get(entityPlayer).unlockResearch(ResearchTypes.EnchantedSkeleton, true);
								}
							}
						}
					}

					if (bonesRemaining > 0)
					{
						EntityItem leftOver = new EntityItem(world, entityItem.posX, entityItem.posY, entityItem.posZ, new ItemStack(ModItems.enchantedSkeletonBone, bonesRemaining));
						world.spawnEntityInWorld(leftOver);
					}

					for (EntityItem items : surroundingBones)
					{
						items.setDead();
					}
				}
			}
		}

		return super.onEntityItemUpdate(entityItem);
	}
}
