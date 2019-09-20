package com.davidm1a2.afraidofthedark.common.tileEntity;

import com.davidm1a2.afraidofthedark.common.capabilities.player.research.IAOTDPlayerResearch;
import com.davidm1a2.afraidofthedark.common.constants.*;
import com.davidm1a2.afraidofthedark.common.tileEntity.core.AOTDTickingTileEntity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.PotionTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.math.AxisAlignedBB;

/**
 * Tile entity for the dark forest block that makes players drowsy
 */
public class TileEntityDarkForest extends AOTDTickingTileEntity
{
    // The ticks inbetween checks for nearby players
    private static final int TICKS_INBETWEEN_CHECKS = 60;
    // The range that players get drowsy in blocks
    private static final int CHECK_RANGE = 14;

    /**
     * Constructor sets the block type of the tile entity
     */
    public TileEntityDarkForest()
    {
        super(ModBlocks.DARK_FOREST);
    }

    /**
     * Update gets called every tick
     */
    @Override
    public void update()
    {
        super.update();
        // Server side processing only
        if (!this.world.isRemote)
        {
            // If we've existed for a multiple of 60 ticks perform a check for nearby players
            if (this.ticksExisted % TICKS_INBETWEEN_CHECKS == 0)
            {
                // Grab all nearby players
                for (EntityPlayer entityPlayer : this.world.getEntitiesWithinAABB(EntityPlayer.class, new AxisAlignedBB(this.pos.getX(), this.pos.getY(), this.pos.getZ(), this.pos.getX() + 1, this.pos.getY() + 1, this.pos.getZ() + 1).grow(CHECK_RANGE)))
                {
                    // Grab their research
                    IAOTDPlayerResearch playerResearch = entityPlayer.getCapability(ModCapabilities.PLAYER_RESEARCH, null);
                    // If the player can research dark forest unlock it and sync that research
                    if (playerResearch.canResearch(ModResearches.DARK_FOREST))
                    {
                        playerResearch.setResearch(ModResearches.DARK_FOREST, true);
                        playerResearch.sync(entityPlayer, true);
                    }

                    // If the player has dark forest research unlocked add the sleeping potion effect and exchange water
                    // bottles with sleeping potion bottles.
                    if (playerResearch.isResearched(ModResearches.DARK_FOREST))
                    {
                        // 6 seconds of sleeping potion effect
                        entityPlayer.addPotionEffect(new PotionEffect(ModPotions.SLEEPING_POTION, 120, 0, true, false));
                        // Replace all water bottles with sleeping potions
                        for (int i = 0; i < entityPlayer.inventory.mainInventory.size(); i++)
                        {
                            // Grab the stack in the current slot
                            ItemStack itemStack = entityPlayer.inventory.getStackInSlot(i);
                            // If it's a potion with type water unlock the sleeping potion research and replace water bottles with sleeping potions
                            if (PotionUtils.getPotionFromItem(itemStack) == PotionTypes.WATER)
                            {
                                if (playerResearch.canResearch(ModResearches.SLEEPING_POTION))
                                {
                                    playerResearch.setResearch(ModResearches.SLEEPING_POTION, true);
                                    playerResearch.sync(entityPlayer, true);
                                }

                                entityPlayer.inventory.setInventorySlotContents(i, new ItemStack(ModItems.SLEEPING_POTION, itemStack.getCount()));
                            }
                        }
                    }
                }
            }
        }
    }
}
