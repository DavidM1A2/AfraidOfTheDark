package com.DavidM1A2.afraidofthedark.common.spell.component.effect;

import com.DavidM1A2.afraidofthedark.common.constants.ModSpellEffects;
import com.DavidM1A2.afraidofthedark.common.spell.Spell;
import com.DavidM1A2.afraidofthedark.common.spell.component.effect.base.AOTDSpellEffect;
import com.DavidM1A2.afraidofthedark.common.spell.component.effect.base.SpellEffectEntry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
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
     * Performs the effect against a given entity
     *
     * @param spell           The spell that caused the effect
     * @param spellStageIndex The spell stage that this effect is a part of
     * @param effectIndex     The effect slot that this effect is in
     * @param entityHit       The entity that the effect should be applied to
     */
    @Override
    public void performEffect(Spell spell, int spellStageIndex, int effectIndex, Entity entityHit)
    {
        EntityPlayer spellCaster = spell.getOwner(entityHit.world);
        if (spellCaster != null)
        {
            // Create particles at the pre and post teleport position
            this.createParticlesAt(1, 3, new Vec3d(spellCaster.posX, spellCaster.posY, spellCaster.posZ), spellCaster.dimension);
            this.createParticlesAt(1, 3, new Vec3d(entityHit.posX, entityHit.posY, entityHit.posZ), entityHit.dimension);
            // Play sound at the pre and post teleport position
            entityHit.world.playSound(null, spellCaster.posX, spellCaster.posY, spellCaster.posZ, SoundEvents.ENTITY_ENDERMEN_TELEPORT, SoundCategory.PLAYERS, 2.5F, 1.0F);
            entityHit.world.playSound(null, entityHit.posX, entityHit.posY, entityHit.posZ, SoundEvents.ENTITY_ENDERMEN_TELEPORT, SoundCategory.PLAYERS, 2.5F, 1.0F);
            ((EntityPlayerMP) spellCaster).connection.setPlayerLocation(entityHit.posX, entityHit.posY, entityHit.posZ, spellCaster.rotationYaw, spellCaster.rotationPitch);
        }
    }

    /**
     * Performs the effect at a given position in the world
     *
     * @param spell           The spell that caused the effect
     * @param spellStageIndex The spell stage that this effect is a part of
     * @param effectIndex     The effect slot that this effect is in
     * @param world           The world the effect is being fired in
     * @param position        The position the effect is being performed at
     */
    @Override
    public void performEffect(Spell spell, int spellStageIndex, int effectIndex, World world, BlockPos position)
    {
        EntityPlayer spellCaster = spell.getOwner(world);
        if (spellCaster != null)
        {
            // Create particles at the pre and post teleport position
            // Play sound at the pre and post teleport position
            this.createParticlesAt(1, 3, new Vec3d(spellCaster.posX, spellCaster.posY, spellCaster.posZ), spellCaster.dimension);
            world.playSound(null, spellCaster.posX, spellCaster.posY, spellCaster.posZ, SoundEvents.ENTITY_ENDERMEN_TELEPORT, SoundCategory.PLAYERS, 2.5F, 1.0F);
            ((EntityPlayerMP) spellCaster).connection.setPlayerLocation(position.getX(), position.getY(), position.getZ(), spellCaster.rotationYaw, spellCaster.rotationPitch);
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
