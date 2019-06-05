package com.DavidM1A2.afraidofthedark.client.particle;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.particle.Particle;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.HashMap;
import java.util.Map;

/**
 * An class of AOTD particle types since we don't have a registry system for them yet from forge
 */
public class AOTDParticleRegistry
{
    // Public client and server side enum that can be sent around in packets to notify the clients to
    // spawn particles in
    public enum ParticleTypes
    {
        ENARIA_BASIC_ATTACK_ID,
        ENARIA_SPELL_CAST_ID,
        ENARIA_SPELL_CAST_2_ID,
        ENARIA_TELEPORT_ID,
        SPELL_CAST_ID,
        SPELL_HIT_ID,
        SMOKE_SCREEN_ID
    }

    // A map of ID -> particle creator. This is used to instantiate the right particle for the id client side
    @SideOnly(Side.CLIENT)
    private static final Map<ParticleTypes, IParticleFactory> PARTICLE_REGISTRY = new HashMap<ParticleTypes, IParticleFactory>()
    {{
        put(ParticleTypes.ENARIA_BASIC_ATTACK_ID, (particleID, worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn, p_178902_15_) ->
                new ParticleEnariaBasicAttack(worldIn, xCoordIn, yCoordIn, zCoordIn));
        put(ParticleTypes.ENARIA_SPELL_CAST_ID, (particleID, worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn, p_178902_15_) ->
                new ParticleEnariaSpellCast(worldIn, xCoordIn, yCoordIn, zCoordIn));
        put(ParticleTypes.ENARIA_SPELL_CAST_2_ID, (particleID, worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn, p_178902_15_) ->
                new ParticleEnariaSpellCast2(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, zSpeedIn));
        put(ParticleTypes.ENARIA_TELEPORT_ID, (particleID, worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn, p_178902_15_) ->
                new ParticleEnariaTeleport(worldIn, xCoordIn, yCoordIn, zCoordIn));
        put(ParticleTypes.SPELL_CAST_ID, (particleID, worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn, p_178902_15_) ->
                new ParticleSpellCast(worldIn, xCoordIn, yCoordIn, zCoordIn));
        put(ParticleTypes.SPELL_HIT_ID, (particleID, worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn, p_178902_15_) ->
                new ParticleSpellHit(worldIn, xCoordIn, yCoordIn, zCoordIn));
        put(ParticleTypes.SMOKE_SCREEN_ID, ((particleID, worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn, p_178902_15_) ->
                new ParticleSmokeScreen(worldIn, xCoordIn, yCoordIn, zCoordIn)));
    }};

    /**
     * Called to create a given particle id in the world
     *
     * @param particleId The particle id to create
     * @param worldIn    The world the particle is being created in
     * @param xCoordIn   The x coordinate of the particle
     * @param yCoordIn   The y coordinate of the particle
     * @param zCoordIn   The z coordinate of the particle
     * @param xSpeedIn   The x speed of the particle
     * @param ySpeedIn   The y speed of the particle
     * @param zSpeedIn   The z speed of the particle
     */
    @SideOnly(Side.CLIENT)
    public static void spawnParticle(ParticleTypes particleId, World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn)
    {
        Particle particle = PARTICLE_REGISTRY.get(particleId).createParticle(0, worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);
        Minecraft.getMinecraft().effectRenderer.addEffect(particle);
    }
}
