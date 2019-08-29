package com.DavidM1A2.afraidofthedark.common.event.register;

import com.DavidM1A2.afraidofthedark.common.constants.ModSpellDeliveryMethods;
import com.DavidM1A2.afraidofthedark.common.constants.ModSpellEffects;
import com.DavidM1A2.afraidofthedark.common.spell.component.DeliveryTransitionStateBuilder;
import com.DavidM1A2.afraidofthedark.common.spell.component.deliveryMethod.SpellDeliveryMethodAOE;
import com.DavidM1A2.afraidofthedark.common.spell.component.deliveryMethod.base.ISpellDeliveryEffectApplicator;
import com.DavidM1A2.afraidofthedark.common.spell.component.deliveryMethod.base.SpellDeliveryMethod;
import com.DavidM1A2.afraidofthedark.common.spell.component.effect.base.AOTDSpellEffect;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Registers any spell effect overrides
 */
public class SpellEffectOverrideRegister
{
    /**
     * Register all of our mod spell effect overrides
     */
    public static void initialize()
    {
        registerAoeFixes();
        registerAoeTeleportFix();
    }

    /**
     * Fixes the insane lag when using AOE+aoe effects, the effect is randomly placed in the AOE instead of at every possible spot
     */
    private static void registerAoeFixes()
    {
        ISpellDeliveryEffectApplicator customAOEApplicator = (state, effect) ->
        {
            SpellDeliveryMethod deliveryMethod = state.getCurrentStage().getDeliveryMethod();
            // Should always be true, we're overriding AOE's custom applicator
            if (deliveryMethod instanceof SpellDeliveryMethodAOE)
            {
                SpellDeliveryMethodAOE aoeDelivery = (SpellDeliveryMethodAOE) deliveryMethod;
                double radius = aoeDelivery.getRadius();
                if (aoeDelivery.shouldTargetEntities())
                {
                    // Fire default logic for entities, that's not a problem
                    return false;
                }
                else
                {
                    // Custom logic for block AOE

                    // Don't apply big AOE effects to every spot in the AOE, otherwise we lag hard. Pick sqrt(radius) random points inside the AOE
                    int numExplosions = MathHelper.clamp(MathHelper.ceil(MathHelper.sqrt(radius)), 1, Integer.MAX_VALUE);
                    ThreadLocalRandom random = ThreadLocalRandom.current();
                    BlockPos basePos = new BlockPos(state.getPosition());
                    DeliveryTransitionStateBuilder transitionBuilder = new DeliveryTransitionStateBuilder()
                            .withSpell(state.getSpell())
                            .withStageIndex(state.getStageIndex())
                            .withWorld(state.getWorld());
                    for (int i = 0; i < numExplosions; i++)
                    {
                        double randomX = random.nextDouble(radius * 2) - radius;
                        double randomY = random.nextDouble(radius * 2) - radius;
                        double randomZ = random.nextDouble(radius * 2) - radius;

                        // Grab the blockpos
                        BlockPos aoePos = basePos.add(randomX, randomY, randomZ);
                        // Test to see if the block is within the radius
                        if (aoePos.distanceSq(basePos) < radius * radius)
                        {
                            // Apply the effect at the position
                            effect.procEffect(transitionBuilder.withPosition(new Vec3d(aoePos.getX(), aoePos.getY(), aoePos.getZ())).build());
                        }
                        else
                        {
                            // Try again, selected point was not within the AOE zone
                            i--;
                        }
                    }
                }
            }
            return true;
        };
        ModSpellDeliveryMethods.AOE.addCustomEffectApplicator(ModSpellEffects.EXPLOSION, customAOEApplicator);
        ModSpellDeliveryMethods.AOE.addCustomEffectApplicator(ModSpellEffects.POTION_EFFECT, customAOEApplicator);
    }

    /**
     * Fixes the crazy teleport spam when using AOE+teleport causing a bunch of teleports
     */
    private static void registerAoeTeleportFix()
    {
        ModSpellDeliveryMethods.AOE.addCustomEffectApplicator(ModSpellEffects.TELEPORT, (state, effect) ->
        {
            World world = state.getWorld();
            EntityPlayer spellCaster = state.getSpell().getOwner(world);
            if (spellCaster != null)
            {
                // Get the radius
                double radius = ((SpellDeliveryMethodAOE) state.getCurrentStage().getDeliveryMethod()).getRadius();
                // The center point
                Vec3d center = state.getPosition();
                ThreadLocalRandom random = ThreadLocalRandom.current();
                // Pick a random spot in the AOE to teleport to, try 20 times to find an air space
                for (int i = 0; i < 20; i++)
                {
                    Vec3d teleportPos = center.addVector(
                            random.nextDouble(radius * 2) - radius,
                            random.nextDouble(radius * 2) - radius,
                            random.nextDouble(radius * 2) - radius);
                    BlockPos blockPos = new BlockPos(teleportPos);
                    if (world.getBlockState(blockPos).getBlock() == Blocks.AIR)
                    {
                        // Create particles at the pre and post teleport position
                        // Play sound at the pre and post teleport position
                        AOTDSpellEffect.createParticlesAt(1, 3, teleportPos, spellCaster.dimension);
                        world.playSound(null, teleportPos.x, teleportPos.y, teleportPos.z, SoundEvents.ENTITY_ENDERMEN_TELEPORT, SoundCategory.PLAYERS, 2.5F, 1.0F);
                        ((EntityPlayerMP) spellCaster).connection.setPlayerLocation(teleportPos.x, teleportPos.y, teleportPos.z, spellCaster.rotationYaw, spellCaster.rotationPitch);
                        AOTDSpellEffect.createParticlesAt(1, 3, teleportPos, spellCaster.dimension);
                        world.playSound(null, teleportPos.x, teleportPos.y, teleportPos.z, SoundEvents.ENTITY_ENDERMEN_TELEPORT, SoundCategory.PLAYERS, 2.5F, 1.0F);
                        break;
                    }
                }
            }
            return true;
        });
    }
}
