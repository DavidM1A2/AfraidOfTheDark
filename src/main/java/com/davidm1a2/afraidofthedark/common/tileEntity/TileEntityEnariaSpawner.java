package com.davidm1a2.afraidofthedark.common.tileEntity;

import com.davidm1a2.afraidofthedark.common.constants.ModBlocks;
import com.davidm1a2.afraidofthedark.common.constants.ModCapabilities;
import com.davidm1a2.afraidofthedark.common.constants.ModResearches;
import com.davidm1a2.afraidofthedark.common.entity.enaria.EntityEnaria;
import com.davidm1a2.afraidofthedark.common.tileEntity.core.AOTDTickingTileEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.EnumDifficulty;

import java.util.UUID;

/**
 * Tile entity that spawns enaria in the gnomish city
 */
public class TileEntityEnariaSpawner extends AOTDTickingTileEntity
{
    // The NBT tag containing enaria's UUID
    private static final String NBT_ENARIA_ID = "enaria_id";
    // The number of ticks inbetween update checks
    private static final int TICKS_INBETWEEN_CHECKS = 40;
    // The enaria entity ID that this tile entity is observing
    private UUID enariaEntityID = null;
    // The bounding box that represents the check region to test for nearby players to spawn enaria
    private AxisAlignedBB playerCheckRegion;
    // The bounding box of the downstairs room enaria spawns in
    private AxisAlignedBB enariaArenaRegion;

    /**
     * Constructor just sets the enaria spawner block
     */
    public TileEntityEnariaSpawner()
    {
        super(ModBlocks.ENARIA_SPAWNER);
    }

    /**
     * Called when the tile entity is added to the world
     */
    @Override
    public void onLoad()
    {
        super.onLoad();
        this.playerCheckRegion = new AxisAlignedBB(this.pos.getX() - 11, this.pos.getY() - 2, this.pos.getZ() - 2, this.pos.getX() + 11, this.pos.getY() + 11, this.pos.getZ() + 20);
        this.enariaArenaRegion = new AxisAlignedBB(this.pos.getX() - 30, this.pos.getY() - 3, this.pos.getZ() - 3, this.pos.getX() + 30, this.pos.getY() + 12, this.pos.getZ() + 80);
    }

    /**
     * Called once per tick
     */
    @Override
    public void update()
    {
        super.update();
        // Server side processing only
        if (!this.world.isRemote)
        {
            // Only update every 40 ticks
            if (this.ticksExisted % TICKS_INBETWEEN_CHECKS == 0)
            {
                // Only spawn enaria in non-peaceful difficulty
                if (world.getDifficulty() != EnumDifficulty.PEACEFUL)
                {
                    // If the entity id is null we should spawn enaria
                    if (enariaEntityID == null)
                    {
                        // Go over all nearby players and if one of them can research the enaria research spawn enaria
                        for (EntityPlayer entityPlayer : this.world.getEntitiesWithinAABB(EntityPlayer.class, this.playerCheckRegion))
                        {
                            // If any player can spawn her spawn her
                            if (entityPlayer.getCapability(ModCapabilities.PLAYER_RESEARCH, null).canResearch(ModResearches.ENARIA))
                            {
                                this.summonEnaria();
                                break;
                            }
                        }
                    }
                    // If enaria is non-null check if she's still alive, if not reset the TE state
                    else
                    {
                        // Grab the entity from the world
                        Entity entity = this.world.getMinecraftServer().getEntityFromUuid(enariaEntityID);

                        // If the entity no longer exists clear the entityID
                        if (entity == null)
                        {
                            this.enariaEntityID = null;
                        }
                    }
                }
            }
        }
    }

    /**
     * Summons enaria into this dungeon
     */
    private void summonEnaria()
    {
        // Create a new enaria entity
        EntityEnaria enaria = new EntityEnaria(this.world, this.enariaArenaRegion);
        // Set enaria to spawn on her throne
        enaria.setPosition(this.pos.getX() + 0.5, this.pos.getY() + 7.0, this.pos.getZ() + 0.5);
        // Spawn her in
        this.world.spawnEntity(enaria);
        // Get her ID and store it
        this.enariaEntityID = enaria.getPersistentID();
    }

    /**
     * Writes the tile entity to the nbt tag compound
     *
     * @param compound The nbt tag compound to write to
     * @return The new nbt tag compound with any required changes made
     */
    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        compound = super.writeToNBT(compound);
        if (this.enariaEntityID != null)
        {
            compound.setTag(NBT_ENARIA_ID, NBTUtil.createUUIDTag(this.enariaEntityID));
        }
        return compound;
    }

    /**
     * Reads the tile entity in from the NBT tag compound
     *
     * @param compound The nbt compound to read data from
     */
    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);
        if (compound.hasKey(NBT_ENARIA_ID))
        {
            this.enariaEntityID = NBTUtil.getUUIDFromTag(compound.getCompoundTag(NBT_ENARIA_ID));
        }
        else
        {
            this.enariaEntityID = null;
        }
    }
}
