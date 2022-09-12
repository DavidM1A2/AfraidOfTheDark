package com.davidm1a2.afraidofthedark.common.item

import com.davidm1a2.afraidofthedark.common.constants.ModBlocks
import com.davidm1a2.afraidofthedark.common.constants.ModItems
import com.davidm1a2.afraidofthedark.common.constants.ModResearches
import com.davidm1a2.afraidofthedark.common.event.custom.ManualResearchTriggerEvent
import com.davidm1a2.afraidofthedark.common.item.core.AOTDItem
import net.minecraft.block.Blocks
import net.minecraft.enchantment.EnchantmentHelper
import net.minecraft.entity.EntityType
import net.minecraft.entity.item.ItemEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.particles.BlockParticleData
import net.minecraft.particles.ParticleTypes
import net.minecraft.util.SoundCategory
import net.minecraft.util.SoundEvents
import net.minecraft.util.math.AxisAlignedBB
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.common.util.Constants.BlockFlags

class StabilizingCatalystItem : AOTDItem("stabilizing_catalyst", Properties()) {
    override fun onEntityItemUpdate(stack: ItemStack, entity: ItemEntity): Boolean {
        val world = entity.level
        val blockPosition = entity.blockPosition()
        // Check every 10 ticks
        if (entity.tickCount % 10 == 0) {
            // If we're within an amorphous eldritch metal block
            val blockState = world.getBlockState(blockPosition)
            if (blockState.block == ModBlocks.AMORPHOUS_ELDRITCH_METAL) {
                // And there's an eldritch metal tool there
                val nearbyEldritchItems = world.getEntities(EntityType.ITEM, AxisAlignedBB(blockPosition)) { true }
                for (nearbyEldritchItem in nearbyEldritchItems) {
                    val nearbyEldritchItemStack = nearbyEldritchItem.item
                    val newItem = TRANSFORMATION_MAP[nearbyEldritchItemStack.item]
                    // And that eldritch tool can be mapped to another tool
                    if (newItem != null) {
                        if (world.isClientSide) {
                            for (i in 0..20) {
                                world.addParticle(
                                    BlockParticleData(ParticleTypes.BLOCK, blockState).setPos(blockPosition),
                                    entity.x + (random.nextFloat() - 0.5),
                                    entity.y + 1,
                                    entity.z + (random.nextFloat() - 0.5),
                                    0.0,
                                    0.0,
                                    0.0
                                )
                            }
                        }

                        world.playSound(null, entity.blockPosition(), SoundEvents.LAVA_EXTINGUISH, SoundCategory.BLOCKS, 0.5f, 2.6f + (random.nextFloat() - random.nextFloat()) * 0.8f)
                        world.playSound(null, entity.blockPosition(), SoundEvents.ANVIL_BREAK, SoundCategory.BLOCKS, 0.8f, 2.6f + (random.nextFloat() - random.nextFloat()) * 0.8f)

                        val newItemStack = ItemStack(newItem, nearbyEldritchItemStack.count)
                        EnchantmentHelper.setEnchantments(EnchantmentHelper.getEnchantments(nearbyEldritchItemStack), newItemStack)
                        val newItemEntity = ItemEntity(world, entity.x, entity.y, entity.z, newItemStack)
                        world.addFreshEntity(newItemEntity)

                        world.setBlock(blockPosition, Blocks.AIR.defaultBlockState(), BlockFlags.DEFAULT)
                        entity.item.shrink(1)
                        if (entity.item.count <= 0) {
                            entity.kill()
                        }
                        nearbyEldritchItem.kill()

                        world.getEntitiesOfClass(
                            PlayerEntity::class.java,
                            entity.boundingBox.inflate(RESEARCH_UNLOCK_RADIUS)
                        ).forEach {
                            MinecraftForge.EVENT_BUS.post(ManualResearchTriggerEvent(it, ModResearches.CATALYSIS))
                        }

                        return true
                    }
                }
            }
        }
        return super.onEntityItemUpdate(stack, entity)
    }

    companion object {
        private const val RESEARCH_UNLOCK_RADIUS = 10.0

        private val TRANSFORMATION_MAP = mapOf(
            ModItems.ELDRITCH_METAL_SWORD to Items.IRON_SWORD,
            ModItems.ELDRITCH_METAL_AXE to Items.IRON_AXE,
            ModItems.ELDRITCH_METAL_HOE to Items.IRON_HOE,
            ModItems.ELDRITCH_METAL_PICKAXE to Items.IRON_PICKAXE,
            ModItems.ELDRITCH_METAL_SHOVEL to Items.IRON_SHOVEL,
            ModItems.ELDRITCH_METAL_HELMET to Items.IRON_HELMET,
            ModItems.ELDRITCH_METAL_CHESTPLATE to Items.IRON_CHESTPLATE,
            ModItems.ELDRITCH_METAL_LEGGINGS to Items.IRON_LEGGINGS,
            ModItems.ELDRITCH_METAL_BOOTS to Items.IRON_BOOTS,
            ModItems.ELDRITCH_METAL_BOLT to ModItems.IRON_BOLT
        )
    }
}
