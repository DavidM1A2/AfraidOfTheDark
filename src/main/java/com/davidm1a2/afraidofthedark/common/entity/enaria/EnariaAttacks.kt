package com.davidm1a2.afraidofthedark.common.entity.enaria

import com.davidm1a2.afraidofthedark.AfraidOfTheDark
import com.davidm1a2.afraidofthedark.client.particle.AOTDParticleRegistry
import com.davidm1a2.afraidofthedark.common.entity.enchantedSkeleton.EntityEnchantedSkeleton
import com.davidm1a2.afraidofthedark.common.entity.splinterDrone.EntitySplinterDrone
import com.davidm1a2.afraidofthedark.common.entity.werewolf.EntityWerewolf
import com.davidm1a2.afraidofthedark.common.packets.otherPackets.SyncParticle
import net.minecraft.block.BlockAir
import net.minecraft.entity.SharedMonsterAttributes
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.SoundEvents
import net.minecraft.potion.Potion
import net.minecraft.potion.PotionEffect
import net.minecraft.util.EntityDamageSource
import net.minecraft.util.EnumFacing
import net.minecraft.util.SoundCategory
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3d
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint
import java.util.*
import kotlin.math.cos
import kotlin.math.sin

/**
 * Utility class containing a list of attacks enaria can use
 *
 * @property enaria Enaria entity reference
 * @property random Random object reference
 */
class EnariaAttacks(private val enaria: EntityEnaria, private val random: Random)
{
    // A list of possible potion effects enaria can apply
    private val possibleEffects: List<() -> PotionEffect> = listOf(
            // Slowness
            { PotionEffect(Potion.getPotionById(2)!!, 300, 1, false, true) },
            // Mining fatigue
            { PotionEffect(Potion.getPotionById(4)!!, 300, 2, false, true) },
            // Nausea
            { PotionEffect(Potion.getPotionById(9)!!, 350, 1, false, true) },
            // Blindness
            { PotionEffect(Potion.getPotionById(15)!!, 100, 0, false, true) },
            // Hunger
            { PotionEffect(Potion.getPotionById(17)!!, 100, 10, false, true) },
            // Weakness
            { PotionEffect(Potion.getPotionById(18)!!, 100, 4, false, true) },
            // Poison
            { PotionEffect(Potion.getPotionById(19)!!, 100, 3, false, true) },
            // Wither
            { PotionEffect(Potion.getPotionById(20)!!, 100, 2, false, true) }
    )
    // A set of random spell attacks enaria can perform
    private val possibleSpells: List<() -> Unit> = listOf(
            { spellAOEPotion() },
            { spellDarkness() },
            { spellShuffleInventory() },
            { spellSummonEnchantedSkeletons() },
            { spellSummonSplinterDrones() },
            { spellSummonWerewolves() }
    )

    /**
     * Makes enaria "basic attack" the player
     */
    fun performBasicAttack()
    {
        // Go over all nearby players
        for (entityPlayer in enaria.world.getEntitiesWithinAABB(EntityPlayer::class.java,
                enaria.entityBoundingBox.grow(BASIC_RANGE.toDouble())))
        {
            // If the player can be seen basic attack them
            if (enaria.canEntityBeSeen(entityPlayer))
            {
                // Attack for 6 hearts
                entityPlayer.attackEntityFrom(EntityDamageSource.causeMobDamage(enaria),
                        enaria.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).attributeValue.toFloat())
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
    private fun performBasicAttackParticleEffectTo(entityPlayer: EntityPlayer)
    {
        if (!entityPlayer.world.isRemote)
        {
            // Create a list of positions to create the particle fx at
            val positions: MutableList<Vec3d> = ArrayList()
            val speeds: MutableList<Vec3d> = ArrayList()
            // Create a trail of particle FX from enaria to the player
            for (i in 0 until NUMBER_OF_PARTICLES_PER_ATTACK)
            {
                // Compute the point along the ray from enaria -> player
                val x = enaria.posX + (entityPlayer.posX - enaria.posX) * i / NUMBER_OF_PARTICLES_PER_ATTACK
                val y = 1 + enaria.posY + (entityPlayer.posY - enaria.posY) * i / NUMBER_OF_PARTICLES_PER_ATTACK
                val z = enaria.posZ + (entityPlayer.posZ - enaria.posZ) * i / NUMBER_OF_PARTICLES_PER_ATTACK
                positions.add(Vec3d(x, y, z))
                speeds.add(Vec3d.ZERO)
            }
            // Send a packet with all particles at once to everyone in the area
            summonParticles(SyncParticle(AOTDParticleRegistry.ParticleTypes.ENARIA_BASIC_ATTACK_ID, positions, speeds))
        }
    }

    /**
     * Performs a random spell attack
     */
    fun performRandomSpell()
    {
        // Server side processing only
        if (!enaria.world.isRemote)
        {
            // Perform a random spell
            possibleSpells.random().invoke()
            // Create 50 spell cast particles around enaria that start low and move upwards
            var positions = List(60) { Vec3d(enaria.posX, enaria.posY, enaria.posZ) }
            var speeds = List(60) { Vec3d.ZERO }
            summonParticles(SyncParticle(AOTDParticleRegistry.ParticleTypes.ENARIA_SPELL_CAST_ID, positions, speeds))
            // Create 20 spell cast 2 particles around enaria that start high and move downwards
            positions = List(20) { Vec3d(enaria.posX, enaria.posY + 3, enaria.posZ) }
            // Make the 20 spell cast 2 particles move outwards in a circle
            speeds = List(positions.size)
            {
                Vec3d(sin(Math.toRadians(360.0 / positions.size * it)) * 0.3,
                        0.0,
                        cos(Math.toRadians(360.0 / positions.size * it)) * 0.3)
            }
            // Summon the particles in
            summonParticles(SyncParticle(AOTDParticleRegistry.ParticleTypes.ENARIA_SPELL_CAST_2_ID, positions, speeds))
        }
    }

    /**
     * Randomly teleports nearby the player
     */
    fun randomTeleport()
    {
        // Server side processing only
        if (!enaria.world.isRemote)
        {
            // Create 30 random positions around enaria for teleport particles
            val positions = (0..NUMBER_OF_PARTICLES_PER_TELEPORT).map()
            {
                Vec3d(enaria.position.x + random.nextDouble() * 4 - 2.0,
                        enaria.position.y + random.nextDouble() * 4 - 2.0 + 0.7,
                        enaria.position.z + random.nextDouble() * 4 - 2.0)
            }.toList()
            val speeds = List(positions.size) { Vec3d.ZERO }

            // Summon them in
            summonParticles(SyncParticle(AOTDParticleRegistry.ParticleTypes.ENARIA_TELEPORT_ID, positions, speeds))
            // Get all players in the allowed fight region and randomly teleport to one
            val entityPlayers = enaria.world.getEntitiesWithinAABB(EntityPlayer::class.java, enaria.allowedRegion)
            if (entityPlayers.isNotEmpty())
            {
                val entityPlayer = entityPlayers.random()
                // Teleport to the player and become invisible for 15 ticks
                enaria.setPosition(entityPlayer.posX, entityPlayer.posY, entityPlayer.posZ)
                enaria.addPotionEffect(PotionEffect(Potion.getPotionById(14)!!, 15, 0, false, false))
                // Play the enderman teleport sound
                enaria.world.playSound(null, enaria.posX, enaria.posY, enaria.posZ, SoundEvents.ENTITY_ENDERMEN_TELEPORT, SoundCategory.HOSTILE, 1.0f, 1.0f)
            }
        }
    }

    /**
     * Makes everything dark around the player
     */
    private fun spellDarkness()
    {
        // Go over all players in the fight arena
        for (entityPlayer in enaria.world.getEntitiesWithinAABB(EntityPlayer::class.java, enaria.allowedRegion))
        {
            // Play the wither hurt effect
            enaria.world.playSound(null, entityPlayer.posX, entityPlayer.posY, entityPlayer.posZ, SoundEvents.ENTITY_WITHER_HURT, SoundCategory.HOSTILE, 1.0f, 0.5f)
            // Remove night vision
            if (entityPlayer.isPotionActive(Potion.getPotionById(16)!!))
            {
                entityPlayer.removePotionEffect(Potion.getPotionById(16)!!)
            }
            // Add blindness
            entityPlayer.addPotionEffect(PotionEffect(Potion.getPotionById(15)!!, 260, 0, false, false))
        }
        // Go over all blocks within a +/- 5 radius that emit night and destroy them
        for (x in enaria.position.x - DARKNESS_RANGE until enaria.position.x + DARKNESS_RANGE)
        {
            for (y in enaria.position.y - DARKNESS_RANGE until enaria.position.y + DARKNESS_RANGE)
            {
                for (z in enaria.position.z - DARKNESS_RANGE until enaria.position.z + DARKNESS_RANGE)
                {
                    val blockPos = BlockPos(x, y, z)
                    // if the block emits light destroy it
                    if (enaria.world.getBlockState(blockPos).getLightValue(enaria.world, blockPos) > 0)
                    {
                        enaria.world.setBlockToAir(blockPos)
                    }
                }
            }
        }
    }

    /**
     * Shuffles the player's current item with a random one
     */
    private fun spellShuffleInventory()
    {
        // Randomly pick two slots and swap them
        for (entityPlayer in enaria.world.getEntitiesWithinAABB(EntityPlayer::class.java, enaria.allowedRegion))
        {
            // Don't affect creative mode players
            if (!entityPlayer.capabilities.isCreativeMode)
            {
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
                entityPlayer.inventoryContainer.detectAndSendChanges()
            }
        }
    }

    /**
     * Summons 4 werewolves around enaria
     */
    private fun spellSummonWerewolves()
    {
        // The number of werewolves spawned
        var numberOfWWsSpawned = 0
        // Go over all blocks around enaria
        for (facing in EnumFacing.HORIZONTALS)
        {
            // Grab the block next to enaria
            val current = enaria.position.offset(facing, 2).up()
            val block = enaria.world.getBlockState(current)
            // Ensure it's air so that a werewolf can spawn there
            if (block.block is BlockAir)
            {
                // Create a wererwolf and spawn it next to enaria
                val werewolf = EntityWerewolf(enaria.world)
                werewolf.setPosition(current.x.toDouble(), current.y.toDouble(), current.z.toDouble())
                // The werewolf can attack anyone, even players who haven't started AOTD
                werewolf.setCanAttackAnyone(true)
                // Spawn the werewolf
                enaria.world.spawnEntity(werewolf)
                numberOfWWsSpawned = numberOfWWsSpawned + 1
            }
        }
        // If no wolves were spawned enaria is trapped, teleport her out
        if (numberOfWWsSpawned == 0)
        {
            randomTeleport()
        }
    }

    /**
     * Summons 2 splinter drones around enaria
     */
    private fun spellSummonSplinterDrones()
    {
        // Count the number of splinter drones spawned so far
        var numberOfSplinterDronesSpawned = 0
        // Get a list of possible sides to spawn drones at, randomize the sides
        val possibleSides = EnumFacing.HORIZONTALS.toList().shuffled()
        // Grab random sides
        for (facing in possibleSides)
        {
            // Move outward 2 and up 2, attempt to spawn a drone there
            val current = enaria.position.offset(facing, 2).up(2)
            // Grab the block at that position
            val block = enaria.world.getBlockState(current)
            // If it's air, we can spawn the drone here
            if (block.block is BlockAir)
            {
                // Create the drone, set the position, and spawn it
                val splinterDrone = EntitySplinterDrone(enaria.world)
                splinterDrone.setPosition(current.x.toDouble(), current.y.toDouble(), current.z.toDouble())
                enaria.world.spawnEntity(splinterDrone)
                // If we've spawned the max number of drones break out
                numberOfSplinterDronesSpawned = numberOfSplinterDronesSpawned + 1
                if (numberOfSplinterDronesSpawned == MAX_SPLINTERS_SPAWNED)
                {
                    break
                }
            }
        }
        // If no drones were spawned enaria is stuck, so teleport
        if (numberOfSplinterDronesSpawned == 0)
        {
            randomTeleport()
        }
    }

    /**
     * Summons up to 8 enchanted skeletons around enaria
     */
    private fun spellSummonEnchantedSkeletons()
    {
        var numberOfSkeletonsSpawned = 0
        for (i in 0 until MAX_SKELETONS_SPAWNED)
        {
            val facing = EnumFacing.HORIZONTALS.random()
            // Move outward 2 and up 1, attempt to spawn a skeleton there
            val current = enaria.position.offset(facing, 2).up()
            // Grab the block at that position
            val block = enaria.world.getBlockState(current)
            // If it's air, we can spawn the skeleton here
            if (block.block is BlockAir)
            {
                // Create the skeleton, set the position, and spawn it
                val enchantedSkeleton = EntityEnchantedSkeleton(enaria.world)
                enchantedSkeleton.setPosition(current.x.toDouble(), current.y.toDouble(), current.z.toDouble())
                enaria.world.spawnEntity(enchantedSkeleton)
                numberOfSkeletonsSpawned = numberOfSkeletonsSpawned + 1
            }
        }
        // If no skeletons were spawned enaria is stuck, so teleport
        if (numberOfSkeletonsSpawned == 0)
        {
            randomTeleport()
        }
    }

    /**
     * Applies random negative potion effects to the players nearby
     */
    private fun spellAOEPotion()
    {
        // Go over all players
        for (entityPlayer in enaria.world.getEntitiesWithinAABB(EntityPlayer::class.java, enaria.allowedRegion))
        {
            // Only apply to non-creative mode
            if (!entityPlayer.capabilities.isCreativeMode)
            {
                // Create a list of effect indices
                val effectIndices = (0..possibleEffects.size).shuffled()
                // Apply 4 random bad potion effects
                for (i in 0..3)
                {
                    entityPlayer.addPotionEffect(possibleEffects[effectIndices[i]].invoke())
                }
            }
        }
        // Play the potion sound effect
        enaria.world.playSound(null,
                enaria.posX,
                enaria.posY,
                enaria.posZ,
                SoundEvents.ENTITY_SPLASH_POTION_BREAK,
                SoundCategory.HOSTILE,
                0.8f,
                0.4f / (random.nextFloat() * 0.4f + 0.8f))
    }

    /**
     * Utility method that sends enaria's animation packets to everyone nearby
     *
     * @param particlePacket The packet to send to everyone in the fight
     */
    private fun summonParticles(particlePacket: SyncParticle)
    {
        AfraidOfTheDark.INSTANCE.packetHandler.sendToAllAround(particlePacket, TargetPoint(enaria.dimension, enaria.posX, enaria.posY, enaria.posZ, 100.0))
    }

    companion object
    {
        // Constants used by enaria's attacks
        private const val DARKNESS_RANGE = 5
        private const val BASIC_RANGE = 20
        private const val NUMBER_OF_PARTICLES_PER_ATTACK = 30
        private const val NUMBER_OF_PARTICLES_PER_TELEPORT = 15
        private const val MAX_SPLINTERS_SPAWNED = 2
        private const val MAX_SKELETONS_SPAWNED = 8
    }
}