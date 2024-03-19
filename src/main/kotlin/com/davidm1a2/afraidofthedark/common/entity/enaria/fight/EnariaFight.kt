package com.davidm1a2.afraidofthedark.common.entity.enaria.fight

import com.davidm1a2.afraidofthedark.AfraidOfTheDark
import com.davidm1a2.afraidofthedark.common.constants.ModBlocks
import com.davidm1a2.afraidofthedark.common.constants.ModParticles
import com.davidm1a2.afraidofthedark.common.constants.ModResearches
import com.davidm1a2.afraidofthedark.common.entity.enaria.EnariaEntity
import com.davidm1a2.afraidofthedark.common.entity.enaria.fight.events.EnariaFightEvent
import com.davidm1a2.afraidofthedark.common.entity.enaria.fight.events.EnariaFightEvents
import com.davidm1a2.afraidofthedark.common.event.custom.ManualResearchTriggerEvent
import com.davidm1a2.afraidofthedark.common.network.packets.animation.AnimationPacket
import com.davidm1a2.afraidofthedark.common.network.packets.other.ParticlePacket
import com.davidm1a2.afraidofthedark.common.network.packets.other.PlayEnariasFightMusicPacket
import com.davidm1a2.afraidofthedark.common.utility.toRotation
import net.minecraft.core.BlockPos
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.ListTag
import net.minecraft.nbt.NbtUtils
import net.minecraft.nbt.Tag
import net.minecraft.server.level.ServerPlayer
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.HorizontalDirectionalBlock
import net.minecraft.world.level.block.Rotation
import net.minecraft.world.phys.AABB
import net.minecraft.world.phys.Vec3
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.common.util.INBTSerializable
import java.util.*
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

class EnariaFight(
    val enaria: EnariaEntity,
    val position: BlockPos
) : INBTSerializable<CompoundTag> {
    // Everything based on enaria and the block pos MUST be lazily initialized to avoid querying the world before it's ready
    val rotation: Rotation by lazy {
        enaria.level.getBlockState(position).getValue(HorizontalDirectionalBlock.FACING).toRotation()
    }
    val centerPos: BlockPos by lazy {
        relativeToAbsolutePosition(0, 2, 38)
    }
    private val arenaBoundingBox: AABB by lazy {
        val arenaNegCorner = relativeToAbsolutePosition(-30, -2, -3)
        val arenaPosCorner = relativeToAbsolutePosition(30, 12, 79)
        AABB(arenaNegCorner, arenaPosCorner)
    }

    // Utility variables
    private val basicAttacks = EnariaBasicAttacks(this)
    private val nextEvents: Queue<EnariaFightEvents> = LinkedList()

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
            val player = enaria.level.getPlayerByUUID(it)
            if (player != null) {
                MinecraftForge.EVENT_BUS.post(ManualResearchTriggerEvent(player, ModResearches.ARCH_SORCERESS))

                // Tell the player to stop playing the enaria combat music
                AfraidOfTheDark.packetHandler.sendTo(PlayEnariasFightMusicPacket(), player as ServerPlayer)
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
                    // Pick a random event from our remaining events. If it's empty, refill it
                    if (nextEvents.isEmpty()) {
                        nextEvents.addAll(EnariaFightEvents.values().apply { shuffle() })
                    }
                    currentEvent = nextEvents.remove().build(this)
                    currentEvent!!.start()
                }
                // Case 3: Event is about to start, enaria is in position, show particles
                ticksUntilNextEvent < DELAY_BEFORE_EVENT_START -> {
                    // Every 5 ticks show particles
                    if (ticksUntilNextEvent % 5 == 0) {
                        // Make a ring of particles around enaria shooting upward and outward
                        val particleHeight = ((DELAY_BEFORE_EVENT_START - ticksUntilNextEvent.toDouble()) / DELAY_BEFORE_EVENT_START) * 3
                        val positions = List(30) { Vec3(centerPos.x + 0.5, centerPos.y + 1.5 + particleHeight, centerPos.z + 0.5) }
                        val speeds = List(positions.size) {
                            Vec3(
                                sin(Math.toRadians(360.0 / positions.size * it)) * 0.2,
                                0.0,
                                cos(Math.toRadians(360.0 / positions.size * it)) * 0.2
                            )
                        }

                        AfraidOfTheDark.packetHandler.sendToAllAround(
                            ParticlePacket.builder()
                                .particle(ModParticles.ENARIA_FIGHT_EVENT)
                                .positions(positions)
                                .speeds(speeds)
                                .build(), enaria, 100.0
                        )
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
                // Don't basic attack or move
                enaria.canMove = false
                return
            }
        }

        // If the current event doesn't allow basic attacks, don't attack or move
        if (currentEvent?.canBasicAttackDuringThis() == false) {
            enaria.canMove = false
            return
        }

        enaria.canMove = true

        // If we're ready for a basic attack, do it
        if (ticksUntilNextAutoAttack <= 0) {
            ticksUntilNextAutoAttack = TIME_BETWEEN_AUTO_ATTACKS
            basicAttacks.attack()
        }
    }

    private fun teleportEnariaToCenter() {
        val world = enaria.level

        // Clear the area in the center of the arena if a player has placed blocks there
        world.setBlockAndUpdate(centerPos, ModBlocks.ELDRITCH_STONE.defaultBlockState())
        for (x in -1..1) {
            for (y in 1..5) {
                for (z in -1..1) {
                    world.setBlockAndUpdate(centerPos.offset(x, y, z), Blocks.CAVE_AIR.defaultBlockState())
                }
            }
        }

        // Move Enaria to the arena center
        val enariaPos = centerPos.above()
        world.playSound(null, enaria.x, enaria.y, enaria.z, SoundEvents.ENDERMAN_TELEPORT, SoundSource.HOSTILE, 1.0f, 1.0f)
        enaria.teleportTo(enariaPos.x + 0.5, enariaPos.y.toDouble(), enariaPos.z + 0.5)
        world.playSound(null, enaria.x, enaria.y, enaria.z, SoundEvents.ENDERMAN_TELEPORT, SoundSource.HOSTILE, 1.0f, 1.0f)
    }

    private fun updatePlayersInFight() {
        // Get players in the arena
        val playersInArena = enaria.level.getEntitiesOfClass(Player::class.java, arenaBoundingBox)
        playersInArena.forEach {
            // If the player is new to the fight, tell them to play the fight music
            if (!playersInFight.contains(it.uuid)) {
                playersInFight.add(it.uuid)
                AfraidOfTheDark.packetHandler.sendTo(PlayEnariasFightMusicPacket(enaria), it as ServerPlayer)
                MinecraftForge.EVENT_BUS.post(ManualResearchTriggerEvent(it, ModResearches.INFERNO))
            }
        }

        playersInFight.removeIf {
            val player = enaria.level.getPlayerByUUID(it)
            if (player == null) {
                // Player has changed dimensions or logged out
                true
            } else {
                // if players are > 70 blocks from the center, they left the fight. For reference, the arena is roughly 60x82x14
                (player.position().distanceToSqr(Vec3.atCenterOf(centerPos)) > 70 * 70).also { isOutOfFight ->
                    if (isOutOfFight) {
                        // The player left the fight to stop playing the music
                        AfraidOfTheDark.packetHandler.sendTo(PlayEnariasFightMusicPacket(), player as ServerPlayer)
                    }
                }
            }
        }
    }

    private fun relativeToAbsolutePosition(relativeX: Int, relativeY: Int, relativeZ: Int): BlockPos {
        // Rotate, then translate
        return BlockPos(relativeX, relativeY, relativeZ).rotate(rotation).offset(position)
    }

    override fun serializeNBT(): CompoundTag {
        val nbt = CompoundTag()

        val playerListNbt = ListTag()
        playersInFight.forEach { playerListNbt.add(NbtUtils.createUUID(it)) }
        nbt.put(NBT_PLAYERS_IN_FIGHT, playerListNbt)

        nbt.putInt(NBT_TICKS_UNTIL_NEXT_EVENT, ticksUntilNextEvent)
        nbt.putInt(NBT_TICKS_UNTIL_NEXT_AUTO_ATTACK, ticksUntilNextAutoAttack)
        if (currentEvent != null) {
            nbt.putString(NBT_CURRENT_EVENT_NAME, currentEvent!!.type.name)
            nbt.put(NBT_CURRENT_EVENT_STATE, currentEvent!!.serializeNBT())
        }

        return nbt
    }

    override fun deserializeNBT(nbt: CompoundTag) {
        nbt.getList(NBT_PLAYERS_IN_FIGHT, Tag.TAG_INT_ARRAY.toInt()).forEach {
            val playerId = NbtUtils.loadUUID(it)
            playersInFight.add(playerId)
            // Tell all players in the fight to play the music
            enaria.level.getPlayerByUUID(playerId)?.let { player ->
                AfraidOfTheDark.packetHandler.sendTo(PlayEnariasFightMusicPacket(enaria), player as ServerPlayer)
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

        private const val MIN_TIME_BETWEEN_EVENTS = 20 * 5 // 5 sec
        private const val MAX_TIME_BETWEEN_EVENTS = 20 * 10 // 10 sec

        private const val TIME_BETWEEN_AUTO_ATTACKS = 20 * 3 // 3 sec

        private const val DELAY_BEFORE_EVENT_START = 20 * 2 // 2 sec
    }
}