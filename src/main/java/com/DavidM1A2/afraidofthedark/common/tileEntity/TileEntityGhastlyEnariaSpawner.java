package com.DavidM1A2.afraidofthedark.common.tileEntity;

import com.DavidM1A2.afraidofthedark.AfraidOfTheDark;
import com.DavidM1A2.afraidofthedark.common.constants.ModBlocks;
import com.DavidM1A2.afraidofthedark.common.entity.enaria.EntityGhastlyEnaria;
import com.DavidM1A2.afraidofthedark.common.tileEntity.core.AOTDTickingTileEntity;
import net.minecraft.util.math.AxisAlignedBB;

import java.util.List;

/**
 * Class that represents a ghastly enaria tile entity
 */
public class TileEntityGhastlyEnariaSpawner extends AOTDTickingTileEntity
{
    /**
     * Constructor just sets the block type
     */
    public TileEntityGhastlyEnariaSpawner()
    {
        super(ModBlocks.ENARIA_SPAWNER);
    }

    /**
     * Update gets called every tick
     */
    @Override
    public void update()
    {
        super.update();
        // Server side only processing
        if (!world.isRemote)
        {
            // Only check every 100 ticks
            if (this.ticksExisted % 100 == 0)
            {
                // Find all nearby enaria entities
                int distanceBetweenIslands = AfraidOfTheDark.INSTANCE.getConfigurationHandler().getBlocksBetweenIslands() / 2;
                List<EntityGhastlyEnaria> enariaEntities = this.world.getEntitiesWithinAABB(EntityGhastlyEnaria.class, new AxisAlignedBB(this.getPos(), this.getPos().up()).grow(distanceBetweenIslands, distanceBetweenIslands, distanceBetweenIslands));

                // True if enaria is alive, false otherwise
                boolean enariaAlive = enariaEntities.stream().map(enaria -> !enaria.isDead).findAny().isPresent();
                // If she's not alive, spawn her
                if (!enariaAlive)
                {
                    // Spawn her at ground level to start
                    EntityGhastlyEnaria enariaSpawn = new EntityGhastlyEnaria(this.world);
                    enariaSpawn.forceSpawn = true;
                    enariaSpawn.setPositionAndRotation(this.getPos().getX() + 0.5, this.getPos().getY() + 10.2, this.getPos().getZ() + 0.5, this.world.rand.nextFloat(), 0);
                    this.world.spawnEntity(enariaSpawn);
                }
            }
        }
    }
}
