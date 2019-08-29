package com.DavidM1A2.afraidofthedark.common.spell.component.effect.base;

import com.DavidM1A2.afraidofthedark.AfraidOfTheDark;
import com.DavidM1A2.afraidofthedark.client.particle.AOTDParticleRegistry;
import com.DavidM1A2.afraidofthedark.common.packets.otherPackets.SyncParticle;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import org.apache.commons.lang3.RandomUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Base class for all AOTD effects
 */
public abstract class AOTDSpellEffect extends SpellEffect
{
    /**
     * Constructor just calls super currently
     */
    public AOTDSpellEffect()
    {
        super();
    }

    /**
     * Creates particles at the position. This is static so overridden effects can still use it
     *
     * @param min       The minimum number of particles to spawn
     * @param max       The maximum number of particles to spawn
     * @param pos       The position to spawn particles at
     * @param dimension The dimension to create particles in
     */
    public static void createParticlesAt(int min, int max, Vec3d pos, int dimension)
    {
        // Spawn particles
        List<Vec3d> positions = new ArrayList<>();
        for (int i = 0; i < RandomUtils.nextInt(min, max + 1); i++)
        {
            positions.add(pos);
        }
        // Send the particle packet
        AfraidOfTheDark.INSTANCE.getPacketHandler().sendToAllAround(
                new SyncParticle(AOTDParticleRegistry.ParticleTypes.SPELL_HIT_ID, positions, Collections.nCopies(positions.size(), Vec3d.ZERO)),
                new NetworkRegistry.TargetPoint(dimension, pos.x, pos.y, pos.z, 100));
    }
}
