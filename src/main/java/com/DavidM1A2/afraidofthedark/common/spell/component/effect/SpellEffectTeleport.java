package com.DavidM1A2.afraidofthedark.common.spell.component.effect;

import com.DavidM1A2.afraidofthedark.common.constants.ModSpellEffects;
import com.DavidM1A2.afraidofthedark.common.spell.component.deliveryMethod.base.DeliveryTransitionState;
import com.DavidM1A2.afraidofthedark.common.spell.component.effect.base.AOTDSpellEffect;
import com.DavidM1A2.afraidofthedark.common.spell.component.effect.base.SpellEffectEntry;
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
        EntityPlayer spellCaster = state.getSpell().getOwner(world);
        if (spellCaster != null)
        {
            // Create particles at the pre and post teleport position
            // Play sound at the pre and post teleport position
            this.createParticlesAt(1, 3, new Vec3d(spellCaster.posX, spellCaster.posY, spellCaster.posZ), spellCaster.dimension);
            world.playSound(null, spellCaster.posX, spellCaster.posY, spellCaster.posZ, SoundEvents.ENTITY_ENDERMEN_TELEPORT, SoundCategory.PLAYERS, 2.5F, 1.0F);
            ((EntityPlayerMP) spellCaster).connection.setPlayerLocation(state.getPosition().x, state.getPosition().y, state.getPosition().z, spellCaster.rotationYaw, spellCaster.rotationPitch);
            this.createParticlesAt(1, 3, new Vec3d(spellCaster.posX, spellCaster.posY, spellCaster.posZ), spellCaster.dimension);
            world.playSound(null, spellCaster.posX, spellCaster.posY, spellCaster.posZ, SoundEvents.ENTITY_ENDERMEN_TELEPORT, SoundCategory.PLAYERS, 2.5F, 1.0F);
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
