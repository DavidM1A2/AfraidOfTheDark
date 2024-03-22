package com.davidm1a2.afraidofthedark.common.item

import com.davidm1a2.afraidofthedark.common.constants.ModBlocks
import com.davidm1a2.afraidofthedark.common.constants.ModItems
import com.davidm1a2.afraidofthedark.common.constants.ModResearches
import com.davidm1a2.afraidofthedark.common.event.custom.ManualResearchTriggerEvent
import com.davidm1a2.afraidofthedark.common.item.core.AOTDItem
import net.minecraft.core.particles.BlockParticleOption
import net.minecraft.core.particles.ParticleTypes
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.item.ItemEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.enchantment.EnchantmentHelper
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.phys.AABB
import net.minecraftforge.common.MinecraftForge
import kotlin.random.Random

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
                val nearbyEldritchItems = world.getEntities(EntityType.ITEM, AABB(blockPosition)) { true }
                for (nearbyEldritchItem in nearbyEldritchItems) {
                    val nearbyEldritchItemStack = nearbyEldritchItem.item
                    val newItem = TRANSFORMATION_MAP[nearbyEldritchItemStack.item]
                    // And that eldritch tool can be mapped to another tool
                    if (newItem != null) {
                        if (world.isClientSide) {
                            for (i in 0..20) {
                                world.addParticle(
                                    BlockParticleOption(ParticleTypes.BLOCK, blockState).setPos(blockPosition),
                                    entity.x + (Random.nextFloat() - 0.5),
                                    entity.y + 1,
                                    entity.z + (Random.nextFloat() - 0.5),
                                    0.0,
                                    0.0,
                                    0.0
                                )
                            }
                        }

                        world.playSound(null, entity.blockPosition(), SoundEvents.LAVA_EXTINGUISH, SoundSource.BLOCKS, 0.5f, 2.6f + (Random.nextFloat() - Random.nextFloat()) * 0.8f)
                        world.playSound(null, entity.blockPosition(), SoundEvents.ANVIL_BREAK, SoundSource.BLOCKS, 0.8f, 2.6f + (Random.nextFloat() - Random.nextFloat()) * 0.8f)

                        val newItemStack = ItemStack(newItem, nearbyEldritchItemStack.count)
                        EnchantmentHelper.setEnchantments(EnchantmentHelper.getEnchantments(nearbyEldritchItemStack), newItemStack)
                        val newItemEntity = ItemEntity(world, entity.x, entity.y, entity.z, newItemStack)
                        world.addFreshEntity(newItemEntity)

                        world.setBlock(blockPosition, Blocks.AIR.defaultBlockState(), Block.UPDATE_ALL)
                        entity.item.shrink(1)
                        if (entity.item.count <= 0) {
                            entity.kill()
                        }
                        nearbyEldritchItem.kill()

                        world.getEntitiesOfClass(
                            Player::class.java,
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
            ModItems.ELDRITCH_METAL_SWORD to ModItems.AMORPHOUS_METAL_SWORD,
            ModItems.ELDRITCH_METAL_AXE to ModItems.AMORPHOUS_METAL_AXE,
            ModItems.ELDRITCH_METAL_HOE to ModItems.AMORPHOUS_METAL_HOE,
            ModItems.ELDRITCH_METAL_PICKAXE to ModItems.AMORPHOUS_METAL_PICKAXE,
            ModItems.ELDRITCH_METAL_SHOVEL to ModItems.AMORPHOUS_METAL_SHOVEL,
            ModItems.ELDRITCH_METAL_HELMET to ModItems.AMORPHOUS_METAL_HELMET,
            ModItems.ELDRITCH_METAL_CHESTPLATE to ModItems.AMORPHOUS_METAL_CHESTPLATE,
            ModItems.ELDRITCH_METAL_LEGGINGS to ModItems.AMORPHOUS_METAL_LEGGINGS,
            ModItems.ELDRITCH_METAL_BOOTS to ModItems.AMORPHOUS_METAL_BOOTS,
            ModItems.ELDRITCH_METAL_BOLT to ModItems.AMORPHOUS_METAL_BOLT
        )
    }
}
