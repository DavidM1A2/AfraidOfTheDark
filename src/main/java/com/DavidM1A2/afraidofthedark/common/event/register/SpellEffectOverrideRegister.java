package com.DavidM1A2.afraidofthedark.common.event.register;

import com.DavidM1A2.afraidofthedark.common.constants.ModSpellDeliveryMethods;
import com.DavidM1A2.afraidofthedark.common.constants.ModSpellEffects;
import com.DavidM1A2.afraidofthedark.common.spell.component.deliveryMethod.SpellDeliveryMethodAOE;
import com.DavidM1A2.afraidofthedark.common.spell.component.deliveryMethod.base.DeliveryTransitionStateBuilder;
import com.DavidM1A2.afraidofthedark.common.spell.component.deliveryMethod.base.ISpellDeliveryEffectApplicator;
import com.DavidM1A2.afraidofthedark.common.spell.component.deliveryMethod.base.SpellDeliveryMethod;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

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
}
