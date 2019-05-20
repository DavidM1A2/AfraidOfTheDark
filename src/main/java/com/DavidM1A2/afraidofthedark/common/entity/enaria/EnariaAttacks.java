package com.DavidM1A2.afraidofthedark.common.entity.enaria;

import com.DavidM1A2.afraidofthedark.AfraidOfTheDark;
import com.DavidM1A2.afraidofthedark.client.particle.AOTDParticleRegistry;
import com.DavidM1A2.afraidofthedark.common.entity.enchantedSkeleton.EntityEnchantedSkeleton;
import com.DavidM1A2.afraidofthedark.common.entity.splinterDrone.EntitySplinterDrone;
import com.DavidM1A2.afraidofthedark.common.entity.werewolf.EntityWerewolf;
import com.DavidM1A2.afraidofthedark.common.packets.otherPackets.SyncParticle;
import com.google.common.collect.ImmutableList;
import net.minecraft.block.BlockAir;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import org.apache.commons.lang3.ArrayUtils;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Utility class containing a list of attacks enaria can use
 */
class EnariaAttacks
{
    // Constants used by enaria's attacks
    private static final int DARKNESS_RANGE = 5;
    private static final int BASIC_RANGE = 20;
    private static final int NUMBER_OF_PARTICLES_PER_ATTACK = 30;
    private static final int NUMBER_OF_PARTICLES_PER_TELEPORT = 15;
    private static final int MAX_SPLINTERS_SPAWNED = 2;
    private static final int MAX_SKELETONS_SPAWNED = 8;

    // Enaria entity reference
    private final EntityEnaria enaria;
    // Random object reference
    private final Random random;
    // A list of possible potion effects enaria can apply
    private final Supplier<PotionEffect>[] possibleEffects;
    // A set of random spell attacks enaria can perform
    private final List<Runnable> possibleSpells = ImmutableList.of(
            this::spellAOEPotion,
            this::spellDarkness,
            this::spellShuffleInventory,
            this::spellSummonEnchantedSkeletons,
            this::spellSummonSplinterDrones,
            this::spellSummonWerewolves);

    /**
     * Constructor initializes enaria, random obj, and possible pot effects
     *
     * @param enaria The enaria entity
     * @param random The random object to use
     */
    EnariaAttacks(EntityEnaria enaria, Random random)
    {
        this.enaria = enaria;
        this.random = random;
        this.possibleEffects = ArrayUtils.toArray(
                // Slowness
                () -> new PotionEffect(Potion.getPotionById(2), 300, 1, false, true),
                // Mining fatigue
                () -> new PotionEffect(Potion.getPotionById(4), 300, 2, false, true),
                // Nausea
                () -> new PotionEffect(Potion.getPotionById(9), 350, 1, false, true),
                // Blindness
                () -> new PotionEffect(Potion.getPotionById(15), 100, 0, false, true),
                // Hunger
                () -> new PotionEffect(Potion.getPotionById(17), 100, 10, false, true),
                // Weakness
                () -> new PotionEffect(Potion.getPotionById(18), 100, 4, false, true),
                // Poison
                () -> new PotionEffect(Potion.getPotionById(19), 100, 3, false, true),
                // Wither
                () -> new PotionEffect(Potion.getPotionById(20), 100, 2, false, true));
    }

    /**
     * Makes enaria "basic attack" the player
     */
    void performBasicAttack()
    {
        // Go over all nearby players
        for (EntityPlayer entityPlayer : this.enaria.world.getEntitiesWithinAABB(EntityPlayer.class, this.enaria.getEntityBoundingBox().grow(BASIC_RANGE)))
        {
            // If the player can be seen basic attack them
            if (this.enaria.canEntityBeSeen(entityPlayer))
            {
                // Attack for 6 hearts
                entityPlayer.attackEntityFrom(EntityDamageSource.causeMobDamage(this.enaria), (float) this.enaria.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue());
                // Show particle FX
                this.performBasicAttackParticleEffectTo(entityPlayer);
            }
        }
    }

    /**
     * Creates the particle effects when the player basic attacks
     *
     * @param entityPlayer The player being attacked
     */
    private void performBasicAttackParticleEffectTo(EntityPlayer entityPlayer)
    {
        if (!entityPlayer.world.isRemote)
        {
            // Create a list of positions to create the particle fx at
            List<Vec3d> positions = new ArrayList<>();
            List<Vec3d> speeds = new ArrayList<>();
            // Create a trail of particle FX from enaria to the player
            for (int i = 0; i < NUMBER_OF_PARTICLES_PER_ATTACK; i++)
            {
                // Compute the point along the ray from enaria -> player
                double x = this.enaria.posX + (entityPlayer.posX - this.enaria.posX) * i / NUMBER_OF_PARTICLES_PER_ATTACK;
                double y = 1 + this.enaria.posY + (entityPlayer.posY - this.enaria.posY) * i / NUMBER_OF_PARTICLES_PER_ATTACK;
                double z = this.enaria.posZ + (entityPlayer.posZ - this.enaria.posZ) * i / NUMBER_OF_PARTICLES_PER_ATTACK;
                positions.add(new Vec3d(x, y, z));
                speeds.add(Vec3d.ZERO);
            }

            // Send a packet with all particles at once to everyone in the area
            summonParticles(new SyncParticle(AOTDParticleRegistry.ParticleTypes.ENARIA_BASIC_ATTACK_ID, positions, speeds));
        }
    }

    /**
     * Randomly teleports nearby the player
     */
    void randomTeleport()
    {
        // Server side processing only
        if (!this.enaria.world.isRemote)
        {
            // Create 30 random positions around enaria for teleport particles
            List<Vec3d> positions = IntStream.range(0, NUMBER_OF_PARTICLES_PER_TELEPORT).boxed().map(ignored ->
                    new Vec3d(this.enaria.getPosition().getX() + random.nextDouble() * 4 - 2.0,
                            this.enaria.getPosition().getY() + random.nextDouble() * 4 - 2.0 + 0.7,
                            this.enaria.getPosition().getZ() + random.nextDouble() * 4 - 2.0)).collect(Collectors.toList());
            List<Vec3d> speeds = Collections.nCopies(positions.size(), Vec3d.ZERO);
            // Summon them in
            summonParticles(new SyncParticle(AOTDParticleRegistry.ParticleTypes.ENARIA_TELEPORT_ID, positions, speeds));

            // Get all players in the allowed fight region and randomly teleport to one
            List<EntityPlayer> entityPlayers = this.enaria.world.getEntitiesWithinAABB(EntityPlayer.class, this.enaria.getAllowedRegion());
            if (!entityPlayers.isEmpty())
            {
                EntityPlayer entityPlayer = entityPlayers.get(random.nextInt(entityPlayers.size()));
                // Teleport to the player and become invisible for 15 ticks
                this.enaria.setPosition(entityPlayer.posX, entityPlayer.posY, entityPlayer.posZ);
                this.enaria.addPotionEffect(new PotionEffect(Potion.getPotionById(14), 15, 0, false, false));
                // Play the enderman teleport sound
                this.enaria.world.playSound(null, this.enaria.posX, this.enaria.posY, this.enaria.posZ, SoundEvents.ENTITY_ENDERMEN_TELEPORT, SoundCategory.HOSTILE, 1.0F, 1.0F);
            }
        }
    }

    /**
     * Performs a random spell attack
     */
    void performRandomSpell()
    {
        // Server side processing only
        if (!this.enaria.world.isRemote)
        {
            // Perform a random spell
            this.possibleSpells.get(this.random.nextInt(this.possibleSpells.size())).run();
            // Create 50 spell cast particles around enaria that start low and move upwards
            List<Vec3d> positions = Collections.nCopies(60, new Vec3d(this.enaria.posX, this.enaria.posY, this.enaria.posZ));
            List<Vec3d> speeds = Collections.nCopies(60, Vec3d.ZERO);
            this.summonParticles(new SyncParticle(AOTDParticleRegistry.ParticleTypes.ENARIA_SPELL_CAST_ID, positions, speeds));
            // Create 20 spell cast 2 particles around enaria that start high and move downwards
            positions = Collections.nCopies(20, new Vec3d(this.enaria.posX, this.enaria.posY + 3, this.enaria.posZ));
            speeds = new ArrayList<>();
            // Make the 20 spell cast 2 particles move outwards in a circle
            for (int i = 0; i < positions.size(); i++)
            {
                speeds.add(new Vec3d(Math.sin(Math.toRadians(360d / positions.size() * i)) * 0.3, 0, Math.cos(Math.toRadians(360d / positions.size() * i)) * 0.3));
            }
            // Summon the particles in
            this.summonParticles(new SyncParticle(AOTDParticleRegistry.ParticleTypes.ENARIA_SPELL_CAST_2_ID, positions, speeds));
        }
    }

    /**
     * Makes everything dark around the player
     */
    private void spellDarkness()
    {
        // Go over all players in the fight arena
        for (EntityPlayer entityPlayer : this.enaria.world.getEntitiesWithinAABB(EntityPlayer.class, this.enaria.getAllowedRegion()))
        {
            // Play the wither hurt effect
            this.enaria.world.playSound(null, entityPlayer.posX, entityPlayer.posY, entityPlayer.posZ, SoundEvents.ENTITY_WITHER_HURT, SoundCategory.HOSTILE, 1.0f, 0.5f);

            // Remove night vision
            if (entityPlayer.isPotionActive(Potion.getPotionById(16)))
            {
                entityPlayer.removePotionEffect(Potion.getPotionById(16));
            }
            // Add blindness
            entityPlayer.addPotionEffect(new PotionEffect(Potion.getPotionById(15), 260, 0, false, false));
        }

        // Go over all blocks within a +/- 5 radius that emit night and destroy them
        for (int x = this.enaria.getPosition().getX() - DARKNESS_RANGE; x < this.enaria.getPosition().getX() + DARKNESS_RANGE; x++)
        {
            for (int y = this.enaria.getPosition().getY() - DARKNESS_RANGE; y < this.enaria.getPosition().getY() + DARKNESS_RANGE; y++)
            {
                for (int z = this.enaria.getPosition().getZ() - DARKNESS_RANGE; z < this.enaria.getPosition().getZ() + DARKNESS_RANGE; z++)
                {
                    BlockPos blockPos = new BlockPos(x, y, z);
                    // if the block emits light destroy it
                    if (this.enaria.world.getBlockState(blockPos).getLightValue(this.enaria.world, blockPos) > 0)
                    {
                        this.enaria.world.setBlockToAir(blockPos);
                    }
                }
            }
        }
    }

    /**
     * Shuffles the player's current item with a random one
     */
    private void spellShuffleInventory()
    {
        // Randomly pick two slots and swap them
        for (EntityPlayer entityPlayer : this.enaria.world.getEntitiesWithinAABB(EntityPlayer.class, this.enaria.getAllowedRegion()))
        {
            // Don't affect creative mode players
            if (!entityPlayer.capabilities.isCreativeMode)
            {
                // Pick a random inventory slot to swap with
                int toSwapPos = this.random.nextInt(36);
                // Grab the player's currently held item
                int currentItemPos = entityPlayer.inventory.currentItem;
                // Grab both itemstacks
                ItemStack current = entityPlayer.inventory.mainInventory.get(currentItemPos);
                ItemStack randomlyChosen = entityPlayer.inventory.mainInventory.get(toSwapPos);
                // Swap the stacks
                entityPlayer.inventory.mainInventory.set(toSwapPos, current);
                entityPlayer.inventory.mainInventory.set(currentItemPos, randomlyChosen);
                // Update the player's inventory
                entityPlayer.inventoryContainer.detectAndSendChanges();
            }
        }
    }

    /**
     * Summons 4 werewolves around enaria
     */
    private void spellSummonWerewolves()
    {
        // The number of werewolves spawned
        int numberOfWWsSpawned = 0;

        // Go over all blocks around enaria
        for (EnumFacing facing : EnumFacing.HORIZONTALS)
        {
            // Grab the block next to enaria
            BlockPos current = this.enaria.getPosition().offset(facing, 2).up();
            IBlockState block = this.enaria.world.getBlockState(current);
            // Ensure it's air so that a werewolf can spawn there
            if (block.getBlock() instanceof BlockAir)
            {
                // Create a wererwolf and spawn it next to enaria
                EntityWerewolf werewolf = new EntityWerewolf(this.enaria.world);
                werewolf.setPosition(current.getX(), current.getY(), current.getZ());
                // The werewolf can attack anyone, even players who haven't started AOTD
                werewolf.setCanAttackAnyone(true);
                // Spawn the werewolf
                this.enaria.world.spawnEntity(werewolf);
                numberOfWWsSpawned = numberOfWWsSpawned + 1;
            }
        }

        // If no wolves were spawned enaria is trapped, teleport her out
        if (numberOfWWsSpawned == 0)
        {
            this.randomTeleport();
        }
    }

    /**
     * Summons 2 splinter drones around enaria
     */
    private void spellSummonSplinterDrones()
    {
        // Count the number of splinter drones spawned so far
        int numberOfSplinterDronesSpawned = 0;

        // Get a list of possible sides to spawn drones at
        List<EnumFacing> possibleSides = Arrays.asList(EnumFacing.HORIZONTALS);
        // Shuffle the list of sides
        Collections.shuffle(possibleSides);
        // Grab random sides
        for (EnumFacing facing : possibleSides)
        {
            // Move outward 2 and up 2, attempt to spawn a drone there
            BlockPos current = this.enaria.getPosition().offset(facing, 2).up(2);
            // Grab the block at that position
            IBlockState block = this.enaria.world.getBlockState(current);
            // If it's air, we can spawn the drone here
            if (block.getBlock() instanceof BlockAir)
            {
                // Create the drone, set the position, and spawn it
                EntitySplinterDrone splinterDrone = new EntitySplinterDrone(this.enaria.world);
                splinterDrone.setPosition(current.getX(), current.getY(), current.getZ());
                this.enaria.world.spawnEntity(splinterDrone);

                // If we've spawned the max number of drones break out
                numberOfSplinterDronesSpawned = numberOfSplinterDronesSpawned + 1;
                if (numberOfSplinterDronesSpawned == MAX_SPLINTERS_SPAWNED)
                {
                    break;
                }
            }
        }

        // If no drones were spawned enaria is stuck, so teleport
        if (numberOfSplinterDronesSpawned == 0)
        {
            this.randomTeleport();
        }
    }

    /**
     * Summons up to 8 enchanted skeletons around enaria
     */
    private void spellSummonEnchantedSkeletons()
    {
        int numberOfSkeletonsSpawned = 0;
        for (int i = 0; i < MAX_SKELETONS_SPAWNED; i++)
        {
            EnumFacing facing = EnumFacing.HORIZONTALS[this.random.nextInt(EnumFacing.HORIZONTALS.length)];
            // Move outward 2 and up 1, attempt to spawn a skeleton there
            BlockPos current = this.enaria.getPosition().offset(facing, 2).up();
            // Grab the block at that position
            IBlockState block = this.enaria.world.getBlockState(current);
            // If it's air, we can spawn the skeleton here
            if (block.getBlock() instanceof BlockAir)
            {
                // Create the skeleton, set the position, and spawn it
                EntityEnchantedSkeleton enchantedSkeleton = new EntityEnchantedSkeleton(this.enaria.world);
                enchantedSkeleton.setPosition(current.getX(), current.getY(), current.getZ());
                this.enaria.world.spawnEntity(enchantedSkeleton);
                numberOfSkeletonsSpawned = numberOfSkeletonsSpawned + 1;
            }
        }

        // If no skeletons were spawned enaria is stuck, so teleport
        if (numberOfSkeletonsSpawned == 0)
        {
            this.randomTeleport();
        }
    }

    /**
     * Applies random negative potion effects to the players nearby
     */
    private void spellAOEPotion()
    {
        // Go over all players
        for (EntityPlayer entityPlayer : this.enaria.world.getEntitiesWithinAABB(EntityPlayer.class, this.enaria.getAllowedRegion()))
        {
            // Only apply to non-creative mode
            if (!entityPlayer.capabilities.isCreativeMode)
            {
                // Create a list of effect indices
                List<Integer> effectIndices = IntStream.range(0, this.possibleEffects.length).boxed().collect(Collectors.toList());
                // Shuffle it
                Collections.shuffle(effectIndices, this.random);

                // 4 random bad potion effects
                for (int i = 0; i < 4; i++)
                {
                    entityPlayer.addPotionEffect(this.possibleEffects[effectIndices.get(i)].get());
                }
            }
        }

        // Play the potion sound effect
        this.enaria.world.playSound(null, this.enaria.posX, this.enaria.posY, this.enaria.posZ, SoundEvents.ENTITY_SPLASH_POTION_BREAK, SoundCategory.HOSTILE, 0.8F, 0.4F / (this.random.nextFloat() * 0.4F + 0.8F));
    }

    /**
     * Utility method that sends enaria's animation packets to everyone nearby
     *
     * @param particlePacket The packet to send to everyone in the fight
     */
    private void summonParticles(SyncParticle particlePacket)
    {
        AfraidOfTheDark.INSTANCE.getPacketHandler().sendToAllAround(particlePacket, new NetworkRegistry.TargetPoint(this.enaria.dimension, this.enaria.posX, this.enaria.posY, this.enaria.posZ, 100));
    }
}
