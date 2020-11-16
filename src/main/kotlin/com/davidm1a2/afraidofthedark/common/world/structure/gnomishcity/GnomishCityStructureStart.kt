package com.davidm1a2.afraidofthedark.common.world.structure.gnomishcity

import com.davidm1a2.afraidofthedark.common.constants.ModLootTables
import com.davidm1a2.afraidofthedark.common.constants.ModSchematics
import com.davidm1a2.afraidofthedark.common.world.WorldHeightmap
import com.davidm1a2.afraidofthedark.common.world.structure.base.SchematicStructurePiece
import net.minecraft.util.EnumFacing
import net.minecraft.util.SharedSeedRandom
import net.minecraft.world.IWorld
import net.minecraft.world.biome.Biome
import net.minecraft.world.gen.IChunkGenerator
import net.minecraft.world.gen.feature.structure.StructureStart
import kotlin.math.min

class GnomishCityStructureStart : StructureStart {
    // Required for reflection
    constructor() : super()

    constructor(
        world: IWorld,
        chunkPosX: Int,
        chunkPosZ: Int,
        width: Int,
        length: Int,
        biome: Biome,
        random: SharedSeedRandom,
        seed: Long,
        chunkGenerator: IChunkGenerator<*>
    ) : super(
        chunkPosX,
        chunkPosZ,
        biome,
        random,
        seed
    ) {
        val cornerPosX = chunkPosX * 16 - width / 2
        val cornerPosY = 5
        val cornerPosZ = chunkPosZ * 16 - length / 2

        // Compute the stairs from surface to level 1, can be a random room
        val stairSurfaceTo1 = random.nextInt(9)

        val stairs2ToEnaria = listOf(1, 3, 5, 7)[random.nextInt(4)]

        // Compute the stairs from level 1 to level 2, can't be in the same spot as stairs from 'surface -> 1' or '2 -> enaria'
        var stairs1To2: Int
        do {
            stairs1To2 = random.nextInt(9)
        } while (stairSurfaceTo1 == stairs1To2 || stairs2ToEnaria == stairs1To2)
        val stairs1To2Facing = EnumFacing.Plane.HORIZONTAL.random(random)

        val rooms = ModSchematics.GNOMISH_CITY_ROOMS
            .flatMap { listOf(it, it) }
            .shuffled(random)
            .iterator()

        var amountOfStairRemoved = 0

        // floor 0 (bottom) or 1 (upper)
        for (floor in Floor.values()) {
            // Rooms in the x direction
            for (xIndex in 0 until ROOMS_PER_ROW) {
                // Rooms in the z direction
                for (zIndex in 0 until ROOMS_PER_ROW) {
                    // Grab the current room index
                    val currentRoom = xIndex + zIndex * 3

                    // The room will always occur at the same position
                    val roomPosX = cornerPosX + xIndex * 50
                    val roomPosY = cornerPosY + floor.ordinal * 15 + 15
                    val roomPosZ = cornerPosZ + zIndex * 50

                    when {
                        // If the room is the stair from surface to level 1 and the floor is upper (1) generate the stair room and stairwell
                        currentRoom == stairSurfaceTo1 && floor == Floor.UPPER -> {
                            var facing = EnumFacing.Plane.HORIZONTAL.random(random)
                            this.components.add(
                                SchematicStructurePiece(
                                    roomPosX,
                                    roomPosY,
                                    roomPosZ,
                                    random,
                                    ModSchematics.ROOM_STAIR_UP,
                                    ModLootTables.GNOMISH_CITY,
                                    facing
                                )
                            )

                            val stairwellX = roomPosX + 13
                            val stairwellY = roomPosY + 15
                            val stairwellZ = roomPosZ + 13
                            val stairwell = ModSchematics.STAIRWELL

                            val groundHeight = listOf(
                                WorldHeightmap.getHeight(stairwellX, stairwellZ, world, chunkGenerator),
                                WorldHeightmap.getHeight(stairwellX + stairwell.getWidth() - 1, stairwellZ, world, chunkGenerator),
                                WorldHeightmap.getHeight(stairwellX, stairwellZ + stairwell.getLength() - 1, world, chunkGenerator),
                                WorldHeightmap.getHeight(stairwellX + stairwell.getWidth() - 1, stairwellZ + stairwell.getLength() - 1, world, chunkGenerator),
                                WorldHeightmap.getHeight(stairwellX + stairwell.getWidth() / 2, stairwellZ + stairwell.getLength() / 2, world, chunkGenerator)
                            ).min()!!

                            var stairwellTop = stairwellY
                            while (stairwellTop < min(220, groundHeight)) {
                                this.components.add(
                                    SchematicStructurePiece(
                                        stairwellX,
                                        stairwellTop,
                                        stairwellZ,
                                        random,
                                        stairwell,
                                        facing = facing
                                    )
                                )
                                facing = facing.rotateY()
                                stairwellTop = stairwellTop + stairwell.getHeight() - 1
                            }

                            this.components.add(
                                GnomishCityStairwellClipperStructurePiece(
                                    stairwellX,
                                    groundHeight,
                                    stairwellTop,
                                    stairwellZ
                                )
                            )
                            amountOfStairRemoved = stairwellTop - groundHeight
                        }
                        // If the room is the stair from level 1 to 2 and the floor is upper (1) generate the stair room
                        currentRoom == stairs1To2 && floor == Floor.UPPER -> {
                            this.components.add(
                                SchematicStructurePiece(
                                    roomPosX,
                                    roomPosY,
                                    roomPosZ,
                                    random,
                                    ModSchematics.ROOM_STAIR_DOWN,
                                    ModLootTables.GNOMISH_CITY,
                                    stairs1To2Facing
                                )
                            )
                        }
                        // If the room is the stair from level 1 to 2 and the floor is lower (0) generate the stair room
                        currentRoom == stairs1To2 && floor == Floor.LOWER -> {
                            this.components.add(
                                SchematicStructurePiece(
                                    roomPosX,
                                    roomPosY,
                                    roomPosZ,
                                    random,
                                    ModSchematics.ROOM_STAIR_UP,
                                    ModLootTables.GNOMISH_CITY,
                                    stairs1To2Facing
                                )
                            )
                        }
                        // If the room is the stair from level 2 to enaria and the floor is lower (0) generate the stair room
                        currentRoom == stairs2ToEnaria && floor == Floor.LOWER -> {
                            val enariaFloorSettings = EnariaFloorSettings.values().find { it.roomId == currentRoom }
                                ?: throw IllegalArgumentException("Enaria floor settings can't be determined for room $currentRoom")

                            this.components.add(
                                SchematicStructurePiece(
                                    roomPosX,
                                    roomPosY,
                                    roomPosZ,
                                    random,
                                    ModSchematics.ROOM_STAIR_DOWN,
                                    ModLootTables.GNOMISH_CITY,
                                    enariaFloorSettings.facing
                                )
                            )

                            // Create the enaria lair too to fit under this room
                            this.components.add(
                                SchematicStructurePiece(
                                    cornerPosX + xIndex * 50 + enariaFloorSettings.xOffset,
                                    cornerPosY + floor.ordinal * 15,
                                    cornerPosZ + zIndex * 50 + enariaFloorSettings.zOffset,
                                    random,
                                    ModSchematics.ENARIA_LAIR,
                                    facing = enariaFloorSettings.facing
                                )
                            )
                        }
                        // Generate a non-stair room, pop it off of the queue so we don't get it again
                        else -> {
                            this.components.add(
                                SchematicStructurePiece(
                                    roomPosX,
                                    roomPosY,
                                    roomPosZ,
                                    random,
                                    rooms.next(),
                                    ModLootTables.GNOMISH_CITY
                                )
                            )
                        }
                    }
                }
            }
        }

        // Create tunnels for each floor
        for (floor in Floor.values()) {
            // One loop that iterates 3 times for the axis (x or z) that contains 3 tunnels
            for (i in 0 until ROOMS_PER_ROW) {
                // One loop that iterates 2 times for the axis (x or z) that contains 2 tunnels
                for (j in 0..1) {
                    // Create an east-west tunnel for these parameters
                    this.components.add(
                        SchematicStructurePiece(
                            cornerPosX + i * 50 + 13,
                            cornerPosY + floor.ordinal * 15 + 15 + 7,
                            cornerPosZ + j * 50 + 32,
                            random,
                            ModSchematics.CONNECTOR,
                            facing = EnumFacing.EAST
                        )
                    )

                    // Create a north-south tunnel for these parameters
                    this.components.add(
                        SchematicStructurePiece(
                            cornerPosX + j * 50 + 32,
                            cornerPosY + floor.ordinal * 15 + 15 + 7,
                            cornerPosZ + i * 50 + 13,
                            random,
                            ModSchematics.CONNECTOR,
                            facing = EnumFacing.NORTH
                        )
                    )
                }
            }
        }

        this.recalculateStructureSize(world)
        // Don't expand the bounding box past the upward stairs
        this.boundingBox.maxY = this.boundingBox.maxY - amountOfStairRemoved
    }

    private enum class Floor {
        LOWER,
        UPPER
    }

    private enum class EnariaFloorSettings(internal val roomId: Int, internal val facing: EnumFacing, internal val xOffset: Int, internal val zOffset: Int) {
        NORTH(7, EnumFacing.NORTH, -14, -73),
        SOUTH(1, EnumFacing.SOUTH, -14, 12),
        EAST(3, EnumFacing.EAST, 12, -14),
        WEST(5, EnumFacing.WEST, -73, -14)
    }

    companion object {
        private const val ROOMS_PER_ROW = 3
    }
}