package com.davidm1a2.afraidofthedark.common.entity.enaria

import com.davidm1a2.afraidofthedark.AfraidOfTheDark
import com.davidm1a2.afraidofthedark.common.constants.ModEntities
import com.davidm1a2.afraidofthedark.common.constants.ModParticles
import com.davidm1a2.afraidofthedark.common.entity.enchantedSkeleton.EnchantedSkeletonEntity
import com.davidm1a2.afraidofthedark.common.entity.splinterDrone.SplinterDroneEntity
import com.davidm1a2.afraidofthedark.common.entity.werewolf.WerewolfEntity
import com.davidm1a2.afraidofthedark.common.network.packets.otherPackets.ParticlePacket
import net.minecraft.block.Blocks
import net.minecraft.entity.SharedMonsterAttributes
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.potion.EffectInstance
import net.minecraft.potion.Effects
import net.minecraft.util.Direction
import net.minecraft.util.EntityDamageSource
import net.minecraft.util.SoundCategory
import net.minecraft.util.SoundEvents
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3d
import net.minecraftforge.fml.network.PacketDistributor
import java.util.*
import kotlin.math.cos
import kotlin.math.sin

/**
 * Utility class containing a list of attacks enaria can use
 *
 * @property enaria Enaria entity reference
 * @property random Random object reference
 */
class EnariaAttackHelper(private val enaria: EnariaEntity, private val random: Random) {
    // A list of possible potion effects enaria can apply
    private val possibleEffects: List<() -> EffectInstance> = listOf(
        { EffectInstance(Effects.SLOWNESS, 300, 1, false, true) },
        { EffectInstance(Effects.MINING_FATIGUE, 300, 2, false, true) },
        { EffectInstance(Effects.NAUSEA, 350, 1, false, true) },
        { EffectInstance(Effects.BLINDNESS, 100, 0, false, true) },
        { EffectInstance(Effects.HUNGER, 100, 10, false, true) },
        { EffectInstance(Effects.WEAKNESS, 100, 4, false, true) },
        { EffectInstance(Effects.POISON, 100, 3, false, true) },
        { EffectInstance(Effects.WITHER, 100, 2, false, true) }
    )

    // A set of random spell attacks enaria can perform
    private val possibleSpells: List<() -> Unit> = listOf(
        this::spellAOEPotion,
        this::spellDarkness,
        this::spellShuffleInventory,
        this::spellSummonEnchantedSkeletons,
        this::spellSummonSplinterDrones,
        this::spellSummonWerewolves
    )

    /**
     * Makes enaria "basic attack" the player
     */
    fun performBasicAttack() {
        // Go over all nearby players
        for (entityPlayer in enaria.world.getEntitiesWithinAABB(
            PlayerEntity::class.java,
            enaria.boundingBox.grow(BASIC_RANGE.toDouble())
        )) {
            // If the player can be seen basic attack them
            if (enaria.canEntityBeSeen(entityPlayer)) {
                // Attack for 6 hearts
                entityPlayer.attackEntityFrom(
                    EntityDamageSource.causeMobDamage(enaria),
                    enaria.getAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).value.toFloat()
                )
                // Show particle FX
                performBasicAttackParticleEffectTo(entityPlayer)
            }
        }
    }

    /**
     * Creates the particle effects when the player basic attacks
     *
     * @param entityPlayer The player being attacked
     */
    private fun performBasicAttackParticleEffectTo(entityPlayer: PlayerEntity) {
        if (!entityPlayer.world.isRemote) {
            // Create a list of positions to create the particle fx at
            val positions = mutableListOf<Vec3d>()
            val speeds = mutableListOf<Vec3d>()

            // Create a trail of particle FX from enaria to the player
            for (i in 0 until NUMBER_OF_PARTICLES_PER_ATTACK) {
                // Compute the point along the ray from enaria -> player
                val x = enaria.posX + (entityPlayer.posX - enaria.posX) * i / NUMBER_OF_PARTICLES_PER_ATTACK
                val y = 1 + enaria.posY + (entityPlayer.posY - enaria.posY) * i / NUMBER_OF_PARTICLES_PER_ATTACK
                val z = enaria.posZ + (entityPlayer.posZ - enaria.posZ) * i / NUMBER_OF_PARTICLES_PER_ATTACK
                positions.add(Vec3d(x, y, z))
                speeds.add(Vec3d.ZERO)
            }

            // Send a packet with all particles at once to everyone in the area
            summonParticles(ParticlePacket(ModParticles.ENARIA_BASIC_ATTACK, positions, speeds))
        }
    }

    /**
     * Performs a random spell attack
     */
    fun performRandomSpell() {
        // Server side processing only
        if (!enaria.world.isRemote) {
            // Perform a random spell
            possibleSpells.random().invoke()

            // Create 50 spell cast particles around enaria that start low and move upwards
            var positions = List(50) { Vec3d(enaria.posX, enaria.posY, enaria.posZ) }
            var speeds = List(50) { Vec3d.ZERO }

            // Summon the particles in
            summonParticles(ParticlePacket(ModParticles.ENARIA_SPELL_CAST, positions, speeds))

            // Create 20 spell cast 2 particles around enaria that start high and move downwards
            positions = List(20) { Vec3d(enaria.posX, enaria.posY + 3, enaria.posZ) }

            // Make the 20 spell cast 2 particles move outwards in a circle
            speeds = List(positions.size) {
                Vec3d(
                    sin(Math.toRadians(360.0 / positions.size * it)) * 0.3,
                    0.0,
                    cos(Math.toRadians(360.0 / positions.size * it)) * 0.3
                )
            }

            // Summon the particles in
            summonParticles(ParticlePacket(ModParticles.ENARIA_SPELL_CAST_2, positions, speeds))
        }
    }

    /**
     * Randomly teleports nearby the player
     */
    fun randomTeleport() {
        // Server side processing only
        if (!enaria.world.isRemote) {
            // Create 30 random positions around enaria for teleport particles
            val positions = List(NUMBER_OF_PARTICLES_PER_TELEPORT)
            {
                Vec3d(
                    enaria.position.x + random.nextDouble() * 4 - 2.0,
                    enaria.position.y + random.nextDouble() * 4 - 2.0 + 0.7,
                    enaria.position.z + random.nextDouble() * 4 - 2.0
                )
            }
            val speeds = List(positions.size) { Vec3d.ZERO }

            // Summon them in
            summonParticles(ParticlePacket(ModParticles.ENARIA_TELEPORT, positions, speeds))

            // Get all players in the allowed fight region and randomly teleport to one
            val entityPlayers = enaria.world.getEntitiesWithinAABB(PlayerEntity::class.java, enaria.getAllowedRegion())
            if (entityPlayers.isNotEmpty()) {
                val entityPlayer = entityPlayers.random()
                // Teleport to the player and become invisible for 15 ticks
                enaria.setPosition(entityPlayer.posX, entityPlayer.posY, entityPlayer.posZ)
                enaria.addPotionEffect(EffectInstance(Effects.INVISIBILITY, 15, 0, false, false))
                // Play the enderman teleport sound
                enaria.world.playSound(
                    null,
                    enaria.posX,
                    enaria.posY,
                    enaria.posZ,
                    SoundEvents.ENTITY_ENDERMAN_TELEPORT,
                    SoundCategory.HOSTILE,
                    1.0f,
                    1.0f
                )
            }
        }
    }

    /**
     * Makes everything dark around the player
     */
    private fun spellDarkness() {
        // Go over all players in the fight arena
        for (entityPlayer in enaria.world.getEntitiesWithinAABB(PlayerEntity::class.java, enaria.getAllowedRegion())) {
            // Play the wither hurt effect
            enaria.world.playSound(
                null,
                entityPlayer.posX,
                entityPlayer.posY,
                entityPlayer.posZ,
                SoundEvents.ENTITY_WITHER_HURT,
                SoundCategory.HOSTILE,
                1.0f,
                0.5f
            )

            // Remove night vision
            if (entityPlayer.isPotionActive(Effects.NIGHT_VISION)) {
                entityPlayer.removePotionEffect(Effects.NIGHT_VISION)
            }

            // Add blindness
            entityPlayer.addPotionEffect(EffectInstance(Effects.NIGHT_VISION, 260, 0, false, false))
        }

        // Go over all blocks within a +/- 5 radius that emit night and destroy them
        for (x in enaria.position.x - DARKNESS_RANGE until enaria.position.x + DARKNESS_RANGE) {
            for (y in enaria.position.y - DARKNESS_RANGE until enaria.position.y + DARKNESS_RANGE) {
                for (z in enaria.position.z - DARKNESS_RANGE until enaria.position.z + DARKNESS_RANGE) {
                    val blockPos = BlockPos(x, y, z)
                    // if the block emits light destroy it
                    if (enaria.world.getBlockState(blockPos).getLightValue(enaria.world, blockPos) > 0) {
                        enaria.world.setBlockState(blockPos, Blocks.AIR.defaultState)
                    }
                }
            }
        }
    }

    /**
     * Shuffles the player's current item with a random one
     */
    private fun spellShuffleInventory() {
        // Randomly pick two slots and swap them
        for (entityPlayer in enaria.world.getEntitiesWithinAABB(PlayerEntity::class.java, enaria.getAllowedRegion())) {
            // Don't affect creative mode players
            if (!entityPlayer.isCreative) {
                // Pick a random inventory slot to swap with
                val toSwapPos = random.nextInt(36)

                // Grab the player's currently held item
                val currentItemPos = entityPlayer.inventory.currentItem

                // Grab both itemstacks
                val current = entityPlayer.inventory.mainInventory[currentItemPos]
                val randomlyChosen = entityPlayer.inventory.mainInventory[toSwapPos]

                // Swap the stacks
                entityPlayer.inventory.mainInventory[toSwapPos] = current
                entityPlayer.inventory.mainInventory[currentItemPos] = randomlyChosen

                // Update the player's inventory
                entityPlayer.container.detectAndSendChanges()
            }
        }
    }

    /**
     * Summons 4 werewolves around enaria
     */
    private fun spellSummonWerewolves() {
        // The number of werewolves spawned
        var numberOfWWsSpawned = 0
        // Go over all blocks around enaria
        for (facing in Direction.Plane.HORIZONTAL) {
            // Grab the block next to enaria
            val current = enaria.position.offset(facing, 2).up()
            val block = enaria.world.getBlockState(current)

            // Ensure it's air so that a werewolf can spawn there
            if (block.block.isAir(block, enaria.world, current)) {
                // Create a wererwolf and spawn it next to enaria
                val werewolf = WerewolfEntity(ModEntities.WEREWOLF, enaria.world)
                werewolf.setPosition(current.x.toDouble(), current.y.toDouble(), current.z.toDouble())

                // The werewolf can attack anyone, even players who haven't started AOTD
                werewolf.setCanAttackAnyone(true)

                // Spawn the werewolf
                enaria.world.addEntity(werewolf)
                numberOfWWsSpawned = numberOfWWsSpawned + 1
            }
        }

        // If no wolves were spawned enaria is trapped, teleport her out
        if (numberOfWWsSpawned == 0) {
            randomTeleport()
        }
    }

    /**
     * Summons 2 splinter drones around enaria
     */
    private fun spellSummonSplinterDrones() {
        // Count the number of splinter drones spawned so far
        var numberOfSplinterDronesSpawned = 0
        // Get a list of possible sides to spawn drones at, randomize the sides
        val possibleSides = Direction.Plane.HORIZONTAL.toList().shuffled()
        // Grab random sides
        for (facing in possibleSides) {
            // Move outward 2 and up 2, attempt to spawn a drone there
            val current = enaria.position.offset(facing, 2).up(2)
            // Grab the block at that position
            val block = enaria.world.getBlockState(current)
            // If it's air, we can spawn the drone here
            if (block.block.isAir(block, enaria.world, current)) {
                // Create the drone, set the position, and spawn it
                val splinterDrone = SplinterDroneEntity(ModEntities.SPLINTER_DRONE, enaria.world)
                splinterDrone.setPosition(current.x.toDouble(), current.y.toDouble(), current.z.toDouble())
                enaria.world.addEntity(splinterDrone)

                // If we've spawned the max number of drones break out
                numberOfSplinterDronesSpawned = numberOfSplinterDronesSpawned + 1
                if (numberOfSplinterDronesSpawned == MAX_SPLINTERS_SPAWNED) {
                    break
                }
            }
        }

        // If no drones were spawned enaria is stuck, so teleport
        if (numberOfSplinterDronesSpawned == 0) {
            randomTeleport()
        }
    }

    /**
     * Summons up to 8 enchanted skeletons around enaria
     */
    private fun spellSummonEnchantedSkeletons() {
        var numberOfSkeletonsSpawned = 0
        for (i in 0 until MAX_SKELETONS_SPAWNED) {
            val facing = Direction.Plane.HORIZONTAL.toList().random()
            // Move outward 2 and up 1, attempt to spawn a skeleton there
            val current = enaria.position.offset(facing, 2).up()
            // Grab the block at that position
            val block = enaria.world.getBlockState(current)
            // If it's air, we can spawn the skeleton here
            if (block.block.isAir(block, enaria.world, current)) {
                // Create the skeleton, set the position, and spawn it
                val enchantedSkeleton = EnchantedSkeletonEntity(ModEntities.ENCHANTED_SKELETON, enaria.world)
                enchantedSkeleton.setPosition(current.x.toDouble(), current.y.toDouble(), current.z.toDouble())
                enaria.world.addEntity(enchantedSkeleton)
                numberOfSkeletonsSpawned = numberOfSkeletonsSpawned + 1
            }
        }

        // If no skeletons were spawned enaria is stuck, so teleport
        if (numberOfSkeletonsSpawned == 0) {
            randomTeleport()
        }
    }

    /**
     * Applies random negative potion effects to the players nearby
     */
    private fun spellAOEPotion() {
        // Go over all players
        for (entityPlayer in enaria.world.getEntitiesWithinAABB(PlayerEntity::class.java, enaria.getAllowedRegion())) {
            // Only apply to non-creative mode
            if (!entityPlayer.isCreative) {
                // Create a list of effect indices
                val effectIndices = possibleEffects.indices.shuffled()
                // Apply 3 random bad potion effects
                for (i in 0..2) {
                    entityPlayer.addPotionEffect(possibleEffects[effectIndices[i]].invoke())
                }
            }
        }

        // Play the potion sound effect
        enaria.world.playSound(
            null,
            enaria.posX,
            enaria.posY,
            enaria.posZ,
            SoundEvents.ENTITY_SPLASH_POTION_BREAK,
            SoundCategory.HOSTILE,
            0.8f,
            0.4f / (random.nextFloat() * 0.4f + 0.8f)
        )
    }

    /**
     * Utility method that sends enaria's animation packets to everyone nearby
     *
     * @param particlePacket The packet to send to everyone in the fight
     */
    private fun summonParticles(particlePacket: ParticlePacket) {
        AfraidOfTheDark.packetHandler.sendToAllAround(
            particlePacket,
            PacketDistributor.TargetPoint(enaria.posX, enaria.posY, enaria.posZ, 100.0, enaria.dimension)
        )
    }

    companion object {
        // Constants used by enaria's attacks
        private const val DARKNESS_RANGE = 5
        private const val BASIC_RANGE = 20
        private const val NUMBER_OF_PARTICLES_PER_ATTACK = 30
        private const val NUMBER_OF_PARTICLES_PER_TELEPORT = 15
        private const val MAX_SPLINTERS_SPAWNED = 2
        private const val MAX_SKELETONS_SPAWNED = 8
    }
}