package com.DavidM1A2.afraidofthedark.common.spell.component.effect;

import com.DavidM1A2.afraidofthedark.common.constants.ModSpellEffects;
import com.DavidM1A2.afraidofthedark.common.spell.Spell;
import com.DavidM1A2.afraidofthedark.common.spell.component.EditableSpellComponentProperty;
import com.DavidM1A2.afraidofthedark.common.spell.component.effect.base.AOTDSpellEffect;
import com.DavidM1A2.afraidofthedark.common.spell.component.effect.base.SpellEffectEntry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

/**
 * Effect that heals a hit entity
 */
public class SpellEffectHeal extends AOTDSpellEffect
{
    // NBT constants for healing amount
    private static final String NBT_HEALING_AMOUNT = "healing_amount";

    // The default heal amount
    private static final int DEFAULT_HEAL_AMOUNT = 2;

    // The amount of healing this effect gives
    private int healAmount = DEFAULT_HEAL_AMOUNT;

    /**
     * Constructor adds the editable prop
     */
    public SpellEffectHeal()
    {
        super();
        this.addEditableProperty(new EditableSpellComponentProperty(
                "Heal Amount",
                "The amount of healing to do in half-hearts.",
                () -> Integer.toString(this.healAmount),
                newValue ->
                {
                    // Ensure the number is parsable
                    try
                    {
                        // Parse the heal amount
                        this.healAmount = Integer.parseInt(newValue);
                        // Ensure heal amount is valid
                        if (this.healAmount > 0)
                        {
                            return null;
                        }
                        else
                        {
                            this.healAmount = DEFAULT_HEAL_AMOUNT;
                            return "Heal amount must be larger than 0";
                        }
                    }
                    // If it's not valid return an error
                    catch (NumberFormatException e)
                    {
                        return newValue + " is not a valid integer!";
                    }
                }
        ));
    }

    /**
     * Gets the cost of the effect
     *
     * @return The cost of the effect
     */
    @Override
    public double getCost()
    {
        return healAmount * 2;
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
        if (entityHit instanceof EntityLivingBase && !(entityHit instanceof EntityArmorStand))
        {
            this.createParticlesAt(1, 3, new Vec3d(entityHit.posX, entityHit.posY, entityHit.posZ), entityHit.dimension);
            ((EntityLivingBase) entityHit).heal(this.healAmount);
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
        // No Effect on blocks
    }

    /**
     * Should get the SpellEffectEntry registry's type
     *
     * @return The registry entry that this component was built with, used for deserialization
     */
    @Override
    public SpellEffectEntry getEntryRegistryType()
    {
        return ModSpellEffects.HEAL;
    }

    /**
     * Serializes the spell component to NBT, override to add additional fields
     *
     * @return An NBT compound containing any required spell component info
     */
    @Override
    public NBTTagCompound serializeNBT()
    {
        NBTTagCompound nbt = super.serializeNBT();

        nbt.setInteger(NBT_HEALING_AMOUNT, this.healAmount);

        return nbt;
    }

    /**
     * Deserializes the state of this spell component from NBT
     *
     * @param nbt The NBT to deserialize from
     */
    @Override
    public void deserializeNBT(NBTTagCompound nbt)
    {
        super.deserializeNBT(nbt);
        this.healAmount = nbt.getInteger(NBT_HEALING_AMOUNT);
    }
}
