package com.davidm1a2.afraidofthedark.common.tileEntity

import com.davidm1a2.afraidofthedark.common.capabilities.getResearch
import com.davidm1a2.afraidofthedark.common.constants.ModBlocks
import com.davidm1a2.afraidofthedark.common.constants.ModResearches
import com.davidm1a2.afraidofthedark.common.entity.enaria.EntityEnaria
import com.davidm1a2.afraidofthedark.common.tileEntity.core.AOTDTickingTileEntity
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.nbt.NBTUtil
import net.minecraft.util.math.AxisAlignedBB
import net.minecraft.world.EnumDifficulty
import java.util.*

/**
 * Tile entity that spawns enaria in the gnomish city
 *
 * @constructor just sets the enaria spawner block
 * @property enariaEntityID The enaria entity ID that this tile entity is observing
 * @property playerCheckRegion The bounding box that represents the check region to test for nearby players to spawn enaria
 * @property enariaArenaRegion The bounding box of the downstairs room enaria spawns in
 */
class TileEntityEnariaSpawner : AOTDTickingTileEntity(ModBlocks.ENARIA_SPAWNER) {
    private var enariaEntityID: UUID? = null
    private lateinit var playerCheckRegion: AxisAlignedBB
    private lateinit var enariaArenaRegion: AxisAlignedBB

    /**
     * Called when the tile entity is added to the world
     */
    override fun onLoad() {
        super.onLoad()
        playerCheckRegion = AxisAlignedBB(
            (pos.x - 11).toDouble(),
            (pos.y - 2).toDouble(),
            (pos.z - 2).toDouble(),
            (pos.x + 11).toDouble(),
            (pos.y + 11).toDouble(),
            (pos.z + 20).toDouble()
        )
        enariaArenaRegion = AxisAlignedBB(
            (pos.x - 30).toDouble(),
            (pos.y - 3).toDouble(),
            (pos.z - 3).toDouble(),
            (pos.x + 30).toDouble(),
            (pos.y + 12).toDouble(),
            (pos.z + 80).toDouble()
        )
    }

    /**
     * Called once per tick
     */
    override fun update() {
        super.update()
        // Server side processing only
        if (!world.isRemote) {
            // Only update every 40 ticks
            if (ticksExisted % TICKS_INBETWEEN_CHECKS == 0L) {
                // Only spawn enaria in non-peaceful difficulty
                if (world.difficulty != EnumDifficulty.PEACEFUL) {
                    // If the entity id is null we should spawn enaria
                    if (enariaEntityID == null) {
                        // Go over all nearby players and if one of them can research the enaria research spawn enaria
                        for (entityPlayer in world.getEntitiesWithinAABB(EntityPlayer::class.java, playerCheckRegion)) {
                            // If any player can spawn her spawn her
                            if (entityPlayer.getResearch().canResearch(ModResearches.ENARIA)) {
                                summonEnaria()
                                break
                            }
                        }
                    } else {
                        // Grab the entity from the world
                        val entity = world.minecraftServer!!.getEntityFromUuid(enariaEntityID!!)
                        // If the entity no longer exists clear the entityID
                        if (entity == null) {
                            enariaEntityID = null
                        }
                    }
                }
            }
        }
    }

    /**
     * Summons enaria into this dungeon
     */
    private fun summonEnaria() {
        // Create a new enaria entity
        val enaria = EntityEnaria(world, enariaArenaRegion)
        // Set enaria to spawn on her throne
        enaria.setPosition(pos.x + 0.5, pos.y + 7.0, pos.z + 0.5)
        // Spawn her in
        world.spawnEntity(enaria)
        // Get her ID and store it
        enariaEntityID = enaria.persistentID
    }

    /**
     * Writes the tile entity to the nbt tag compound
     *
     * @param compound The nbt tag compound to write to
     * @return The new nbt tag compound with any required changes made
     */
    override fun writeToNBT(compound: NBTTagCompound): NBTTagCompound {
        super.writeToNBT(compound)
        if (enariaEntityID != null) {
            compound.setTag(NBT_ENARIA_ID, NBTUtil.createUUIDTag(enariaEntityID!!))
        }
        return compound
    }

    /**
     * Reads the tile entity in from the NBT tag compound
     *
     * @param compound The nbt compound to read data from
     */
    override fun readFromNBT(compound: NBTTagCompound) {
        super.readFromNBT(compound)
        enariaEntityID = if (compound.hasKey(NBT_ENARIA_ID)) {
            NBTUtil.getUUIDFromTag(compound.getCompoundTag(NBT_ENARIA_ID))
        } else {
            null
        }
    }

    companion object {
        // The NBT tag containing enaria's UUID
        private const val NBT_ENARIA_ID = "enaria_id"
        // The number of ticks inbetween update checks
        private const val TICKS_INBETWEEN_CHECKS = 40
    }
}