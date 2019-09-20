package com.davidm1a2.afraidofthedark.common.spell.component.effect;

import com.davidm1a2.afraidofthedark.common.constants.ModSpellEffects;
import com.davidm1a2.afraidofthedark.common.spell.component.DeliveryTransitionState;
import com.davidm1a2.afraidofthedark.common.spell.component.effect.base.AOTDSpellEffect;
import com.davidm1a2.afraidofthedark.common.spell.component.effect.base.SpellEffectEntry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

/**
 * Teleports the spell owner to the hit location
 */
public class SpellEffectTeleport extends AOTDSpellEffect
{
    /**
     * Gets the cost of the effect
     *
     * @return The cost of the effect
     */
    @Override
    public double getCost()
    {
        return 3;
    }

    /**
     * Performs the effect
     *
     * @param state The state that the spell is in
     */
    @Override
    public void procEffect(DeliveryTransitionState state)
    {
        World world = state.getWorld();
        EntityPlayer spellCaster = state.getSpell().getOwner();
        if (spellCaster != null)
        {
            Vec3d position = state.getPosition();
            // Create particles at the pre and post teleport position
            // Play sound at the pre and post teleport position
            createParticlesAt(1, 3, position, spellCaster.dimension);
            world.playSound(null, position.x, position.y, position.z, SoundEvents.ENTITY_ENDERMEN_TELEPORT, SoundCategory.PLAYERS, 2.5F, 1.0F);
            ((EntityPlayerMP) spellCaster).connection.setPlayerLocation(position.x, position.y, position.z, spellCaster.rotationYaw, spellCaster.rotationPitch);
            createParticlesAt(1, 3, position, spellCaster.dimension);
            world.playSound(null, position.x, position.y, position.z, SoundEvents.ENTITY_ENDERMEN_TELEPORT, SoundCategory.PLAYERS, 2.5F, 1.0F);
        }
    }

    /**
     * Should get the SpellEffectEntry registry's type
     *
     * @return The registry entry that this component was built with, used for deserialization
     */
    @Override
    public SpellEffectEntry getEntryRegistryType()
    {
        return ModSpellEffects.TELEPORT;
    }
}
