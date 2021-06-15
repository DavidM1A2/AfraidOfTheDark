package com.davidm1a2.afraidofthedark.common.entity.enaria.fight

import com.davidm1a2.afraidofthedark.AfraidOfTheDark
import com.davidm1a2.afraidofthedark.common.capabilities.getResearch
import com.davidm1a2.afraidofthedark.common.constants.ModBlocks
import com.davidm1a2.afraidofthedark.common.constants.ModParticles
import com.davidm1a2.afraidofthedark.common.constants.ModResearches
import com.davidm1a2.afraidofthedark.common.entity.enaria.EnariaEntity
import com.davidm1a2.afraidofthedark.common.entity.enaria.fight.events.EnariaFightEvent
import com.davidm1a2.afraidofthedark.common.entity.enaria.fight.events.EnariaFightEvents
import com.davidm1a2.afraidofthedark.common.network.packets.animationPackets.AnimationPacket
import com.davidm1a2.afraidofthedark.common.network.packets.otherPackets.ParticlePacket
import com.davidm1a2.afraidofthedark.common.network.packets.otherPackets.PlayEnariasFightMusicPacket
import com.davidm1a2.afraidofthedark.common.utility.toRotation
import net.minecraft.block.Blocks
import net.minecraft.block.HorizontalFaceBlock
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.ServerPlayerEntity
import net.minecraft.nbt.CompoundNBT
import net.minecraft.nbt.ListNBT
import net.minecraft.nbt.NBTUtil
import net.minecraft.util.Rotation
import net.minecraft.util.SoundCategory
import net.minecraft.util.SoundEvents
import net.minecraft.util.math.AxisAlignedBB
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3d
import net.minecraftforge.common.util.Constants
import net.minecraftforge.common.util.INBTSerializable
import java.util.*
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

class EnariaFight(
    val enaria: EnariaEntity,
    val position: BlockPos
) : INBTSerializable<CompoundNBT> {
    // Everything based on enaria and the block pos MUST be lazily initialized to avoid querying the world before it's ready
    val rotation: Rotation by lazy {
        enaria.world.getBlockState(position)[HorizontalFaceBlock.HORIZONTAL_FACING].toRotation()
    }
    val centerPos: BlockPos by lazy {
        relativeToAbsolutePosition(0, 3, 38)
    }
    private val arenaBoundingBox: AxisAlignedBB by lazy {
        val arenaNegCorner = relativeToAbsolutePosition(-30, -2, -3)
        val arenaPosCorner = relativeToAbsolutePosition(30, 12, 79)
        AxisAlignedBB(arenaNegCorner, arenaPosCorner)
    }

    // Utility variables
    private val basicAttacks = EnariaBasicAttacks(this)

    // Variables which control the fight state. Save these to NBT

    // The UUIDs of players we know are fighting Enaria
    val playersInFight = mutableSetOf<UUID>()

    // Ticks until the next event should happen
    private var ticksUntilNextEvent: Int = Random.nextInt(MIN_TIME_BETWEEN_EVENTS, MAX_TIME_BETWEEN_EVENTS)

    // Ticks until the next auto attack
    private var ticksUntilNextAutoAttack: Int = TIME_BETWEEN_AUTO_ATTACKS

    // The current event that is happening
    private var currentEvent: EnariaFightEvent? = null

    fun tick(currentTick: Int) {
        // Every 1 second update the players fighting enaria
        if (currentTick % 20 == 0) {
            updatePlayersInFight()
        }

        // Tick events which change the entire arena
        tickEvents()

        // Tick basic attacks, think "auto attacks" but with spells
        tickBasicAttacks()
    }

    fun end() {
        // Each player in the fight gets the research
        updatePlayersInFight()
        playersInFight.forEach {
            val player = enaria.world.getPlayerByUuid(it)
            if (player != null) {
                val playerResearch = player.getResearch()
                if (playerResearch.canResearch(ModResearches.ENARIA)) {
                    playerResearch.setResearch(ModResearches.ENARIA, true)
                    playerResearch.sync(player, true)
                }

                // Tell the player to stop playing the enaria combat music
                AfraidOfTheDark.packetHandler.sendTo(PlayEnariasFightMusicPacket(), player as ServerPlayerEntity)
            }
        }
        playersInFight.clear()
        ticksUntilNextEvent = Random.nextInt(MIN_TIME_BETWEEN_EVENTS, MAX_TIME_BETWEEN_EVENTS)
        currentEvent?.forceStop()
        currentEvent = null
    }

    private fun tickEvents() {
        if (currentEvent == null) {
            ticksUntilNextEvent = ticksUntilNextEvent - 1
            when {
                // Case 1: Event is about to start: Teleport Enaria to the center of the arena and start the spell cast animation
                ticksUntilNextEvent == DELAY_BEFORE_EVENT_START -> {
                    // Move enaria to the center of the arena
                    teleportEnariaToCenter()

                    // Play the spell cast animation
                    AfraidOfTheDark.packetHandler.sendToAllAround(AnimationPacket(enaria, "spell", "spell"), enaria, 70.0)
                }
                // Case 2: Event is starting now
                ticksUntilNextEvent == 0 -> {
                    currentEvent = EnariaFightEvents.values().random().build(this)
                    currentEvent!!.start()
                }
                // Case 3: Event is about to start, enaria is in position, show particles
                ticksUntilNextEvent < DELAY_BEFORE_EVENT_START -> {
                    // Every 5 ticks show particles
                    if (ticksUntilNextEvent % 5 == 0) {
                        // Make a ring of particles around enaria shooting upward and outward
                        val particleHeight = ((DELAY_BEFORE_EVENT_START - ticksUntilNextEvent.toDouble()) / DELAY_BEFORE_EVENT_START) * 5
                        val positions = List(30) { Vec3d(centerPos.x + 0.5, centerPos.y + particleHeight, centerPos.z + 0.5) }
                        val speeds = List(positions.size) {
                            Vec3d(
                                sin(Math.toRadians(360.0 / positions.size * it)) * 0.2,
                                0.0,
                                cos(Math.toRadians(360.0 / positions.size * it)) * 0.2
                            )
                        }

                        AfraidOfTheDark.packetHandler.sendToAllAround(ParticlePacket(ModParticles.SPELL_CAST3, positions, speeds), enaria, 70.0)
                    }
                }
            }
        } else {
            if (currentEvent!!.isOver()) {
                ticksUntilNextEvent = Random.nextInt(MIN_TIME_BETWEEN_EVENTS, MAX_TIME_BETWEEN_EVENTS)
                currentEvent = null
            } else {
                currentEvent!!.tick()
            }
        }
    }

    private fun tickBasicAttacks() {
        ticksUntilNextAutoAttack = ticksUntilNextAutoAttack - 1

        // If no event is playing...
        if (currentEvent == null) {
            // And we're about to play an event
            if (ticksUntilNextEvent < DELAY_BEFORE_EVENT_START) {
                // Don't basic attack
                return
            }
        }

        // If the current event doesn't allow basic attacks, don't attack
        if (currentEvent?.canBasicAttackDuringThis() == false) {
            return
        }

        // If we're ready for a basic attack, do it
        if (ticksUntilNextAutoAttack <= 0) {
            ticksUntilNextAutoAttack = TIME_BETWEEN_AUTO_ATTACKS
            basicAttacks.attack()
        }
    }

    private fun teleportEnariaToCenter() {
        val world = enaria.world

        // Clear the area in the center of the arena if a player has placed blocks there
        world.setBlockState(centerPos, ModBlocks.GNOMISH_METAL_PLATE.defaultState)
        for (x in -1..1) {
            for (y in 1..5) {
                for (z in -1..1) {
                    world.setBlockState(centerPos.add(x, y, z), Blocks.CAVE_AIR.defaultState)
                }
            }
        }

        // Move Enaria to the arena center
        val enariaPos = centerPos.up()
        world.playSound(null, enaria.posX, enaria.posY, enaria.posZ, SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.HOSTILE, 1.0f, 1.0f)
        enaria.setPositionAndUpdate(enariaPos.x + 0.5, enariaPos.y.toDouble(), enariaPos.z + 0.5)
        world.playSound(null, enaria.posX, enaria.posY, enaria.posZ, SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.HOSTILE, 1.0f, 1.0f)
    }

    private fun updatePlayersInFight() {
        // Get players in the arena
        val playersInArena = enaria.world.getEntitiesWithinAABB(PlayerEntity::class.java, arenaBoundingBox)
        playersInArena.forEach {
            // If the player is new to the fight, tell them to play the fight music
            if (!playersInFight.contains(it.uniqueID)) {
                playersInFight.add(it.uniqueID)
                AfraidOfTheDark.packetHandler.sendTo(PlayEnariasFightMusicPacket(enaria), it as ServerPlayerEntity)
            }
        }

        playersInFight.removeIf {
            val player = enaria.world.getPlayerByUuid(it)
            if (player == null) {
                // Player has changed dimensions or logged out
                true
            } else {
                // if players are > 70 blocks from the center, they left the fight. For reference, the arena is roughly 60x82x14
                (player.position.distanceSq(centerPos) > 70 * 70).also { isOutOfFight ->
                    if (isOutOfFight) {
                        // The player left the fight to stop playing the music
                        AfraidOfTheDark.packetHandler.sendTo(PlayEnariasFightMusicPacket(), player as ServerPlayerEntity)
                    }
                }
            }
        }
    }

    private fun relativeToAbsolutePosition(relativeX: Int, relativeY: Int, relativeZ: Int): BlockPos {
        // Rotate, then translate
        return BlockPos(relativeX, relativeY, relativeZ).rotate(rotation).add(position)
    }

    override fun serializeNBT(): CompoundNBT {
        val nbt = CompoundNBT()

        val playerListNbt = ListNBT()
        playersInFight.forEach { playerListNbt.add(NBTUtil.writeUniqueId(it)) }
        nbt.put(NBT_PLAYERS_IN_FIGHT, playerListNbt)

        nbt.putInt(NBT_TICKS_UNTIL_NEXT_EVENT, ticksUntilNextEvent)
        nbt.putInt(NBT_TICKS_UNTIL_NEXT_AUTO_ATTACK, ticksUntilNextAutoAttack)
        if (currentEvent != null) {
            nbt.putString(NBT_CURRENT_EVENT_NAME, currentEvent!!.type.name)
            nbt.put(NBT_CURRENT_EVENT_STATE, currentEvent!!.serializeNBT())
        }

        return nbt
    }

    override fun deserializeNBT(nbt: CompoundNBT) {
        nbt.getList(NBT_PLAYERS_IN_FIGHT, Constants.NBT.TAG_COMPOUND).forEach {
            val playerId = NBTUtil.readUniqueId(it as CompoundNBT)
            playersInFight.add(playerId)
            // Tell all players in the fight to play the music
            enaria.world.getPlayerByUuid(playerId)?.let { player ->
                AfraidOfTheDark.packetHandler.sendTo(PlayEnariasFightMusicPacket(enaria), player as ServerPlayerEntity)
            }
        }

        ticksUntilNextEvent = nbt.getInt(NBT_TICKS_UNTIL_NEXT_EVENT)
        ticksUntilNextAutoAttack = nbt.getInt(NBT_TICKS_UNTIL_NEXT_AUTO_ATTACK)

        if (nbt.contains(NBT_CURRENT_EVENT_NAME)) {
            val type = EnariaFightEvents.valueOf(nbt.getString(NBT_CURRENT_EVENT_NAME))
            currentEvent = type.build(this)
            currentEvent!!.deserializeNBT(nbt.getCompound(NBT_CURRENT_EVENT_STATE))
        } else {
            currentEvent = null
        }
    }

    companion object {
        private const val NBT_PLAYERS_IN_FIGHT = "players_in_fight"
        private const val NBT_TICKS_UNTIL_NEXT_EVENT = "ticks_until_next_event"
        private const val NBT_TICKS_UNTIL_NEXT_AUTO_ATTACK = "ticks_until_next_auto_attack"
        private const val NBT_CURRENT_EVENT_NAME = "current_event_id"
        private const val NBT_CURRENT_EVENT_STATE = "current_event_state"

        private const val MIN_TIME_BETWEEN_EVENTS = 20 * 25 // 25 sec
        private const val MAX_TIME_BETWEEN_EVENTS = 20 * 35 // 35 sec

        private const val TIME_BETWEEN_AUTO_ATTACKS = 20 * 3 // 3 sec

        private const val DELAY_BEFORE_EVENT_START = 20 * 2 // 2 sec
    }
}